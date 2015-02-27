import socket
import threading
import time

tabHost = '192.168.0.1'
tabPort = 4444
piHost = '192.168.1.1'
piPort = 4445
pingPort = 4446
roundThreads = []
global piServer, tabServer, tabString, pingServer
piStrings = []
global piThreads
global mortars
global lock
lock = threading.Lock()
piThreads = 0
global ephemeris
global hasEph
hasEph = False

class socketThread(threading.Thread):

   def __init__(self, parent, clntSock):
      global piThreads
      threading.Thread.__init__(self)
      self.clntSock = clntSock
      self.parent = parent
      if self.parent.serverIP == '192.168.1.1':
         self.clntNum = piThreads
         piStrings.append('new round client')
         piThreads += 1

   def run(self):
      global tabString, piServer, ephemeris, hasEph
      while 1:
         message = self.clntSock.recv(1024)
         if message:
            if message == 'close':
               break
            if self.parent.serverIP == '192.168.0.1':
               if message[0:3] == '111':
                  ephemeris = message
                  hasEph = True
                  piServer.sendToAll(ephemeris)
                  print 'sending ephemeris to rounds'
               else:
                  tabString = message #interpret tabString and send to certain rounds
                  piServer.sendToAll(message)
                  print 'sending to all rounds: ' + message
                #Do something with information from tablet
            if self.parent.serverIP == '192.168.1.1':
               piStrings[self.clntNum] = message
               for strings in piStrings:
                  tabServer.sendToAll("Received from server: " + message)
               #Do something with information from round
      self.clntSock.close()
      print 'socket closed'


class updateList(threading.Thread):				##List of all mortars in magazine
   def __init__(self, parent, clntSock):
      threading.Thread.__init__(self)
      self.parent = parent
      self.clntSock = clntSock
      self.id = ''
      self.setting = ''
      self.gps = ''
      self.elev = ''
      
   def run(self):
      global mortars, lock
      self.clntSock.setblocking(0)
      waitForIAM = True
      while waitForIAM:
         #time.sleep(0.5)
         ack = True
         try:
            message = self.clntSock.recv(1024)	
         except:
            pass
         try:
            if message[0:3] == 'iam':
               fuze = self.update(message[4:])
               break
         except:
            pass
      self.ping(fuze)



   def ping(self, fuze):							##Scrubs current line-up of mortars
     global mortars,lock
     ack = True
     while 1: 
      time.sleep(0.5)
      self.sendToRound(self.id + ' acknowledge')
      print 'send ack to: ' + self.id
      for x in range(0,900000):
         if x == 899999:
            ack = False
         try:													##Checks to make sure rounds have correct data
             message = self.clntSock.recv(1024)			
             if message:
                print message
                if message[0:6] == self.id + ' here':		##Round Present
                   ack = True
                   if message[7:8] == self.setting:
                      if message[9:23] == self.gps:
                         if message[24:] == self.elev:
                            pass
                   else:									##Corrects it if not
                      self.setting = message[7:8]
                      self.gps = message[9:23]
                      self.elev = message[24:]
                      for item in mortars:
                         if item == fuze:
                            with lock:						##Connects to fuze to make changes
                               print 'lock acquired'
                               item[1] = self.setting
                               item[2] = self.gps
                               item[3] = self.elev
                               print 'lock released'
                            break
                      print 'data change'
                      self.sendToTab()
                   break
                else:										#Bad Round
                    print 'this round is defunct or fake'
                    for item in mortars:
                       if item == fuze:
                          lock.acquire()
                          mortars.remove(item)
                          lock.release()
                          break
                    self.sendToTab()
                    self.clntSock.close()
                    self.thread.exit()
                    #self.parent.killThread(self)
             else:
                if x == 899999:
                   print 'outer else'				##Testing purposes
                   ack = False
         except:
            if x == 899999:
               print 'pass statement'				##Testing purposes
               ack = False
      if ack == False:
         print 'remove mortar'
         for item in mortars:
            if item == fuze:
               lock.acquire()
               mortars.remove(item)
               lock.release()
               break
         self.sendToTab()
         print 'no ack recieved'
         self.clntSock.close()
         return

   def update(self, message):			##updates what the mortar is listed as
      global mortars, lock
      id = message[0:1]
      setting = message[2:3]
      gps = message [4:18]
      elev = message [19:]
      fuze = [id,setting,gps,elev]
      self.id = id
      self.setting = setting
      self.gps = gps
      self.elev = elev
      mortars.append(fuze)
      self.sendToTab()
      return fuze

   def sendToRound(self,message):		##Self explanitory
      self.clntSock.send(message)		##Can't find clntSock.send


   def sendToTab(self):				
      global mortars
      string = '<list>'
      for item in mortars:
        lock.acquire()
        string += item[0] + ',' + item[1] + ',' + item[2] + ','+ item[3] + ';'
        lock.release()
      string += '</list>\n'
      print string
      tabServer.sendToAll(string)




class serverThread(threading.Thread):

   def __init__(self, serverIP, serverPort):
      threading.Thread.__init__(self)
      self.threadCount = 0		##Number of threads			Currently Never Updates
      self.myThreads = []		##Array of threads
      self.serverIP = serverIP	
      self.serverPort = serverPort
      self.sendLock = threading.Lock()


   def run(self):
      global hasEph, ephemeris, pingPort, mortars, piThreads, lock
      serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  ##Address and Socket type.  Predefined constants
      serverSocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1) ##
      serverSocket.bind((self.serverIP, self.serverPort)) ##bind new socket to main thread
      serverSocket.listen(20)
      while 1:
         connectionSocket, addr = serverSocket.accept() ##reconnects sockets
         if self.serverPort == pingPort:								##If pingthread, update list of mortars
           pingServer = updateList(self, connectionSocket)
           self.myThreads.append(pingServer)							##Adds all mortars to list of threads
           pingServer.start()
         else:
            newClient = socketThread(self, connectionSocket)
            self.myThreads.append(newClient)							##Adds new thread to myThreads
            newClient.start()
            if self.serverPort == piPort:								##If PiThread:
               lock.acquire()												##Print info from each mortar in html(?)
               print mortars
               string = '<list>'
               for item in mortars:										
                  string += item[0] + ',' + item[1] + ',' + item[2] + ',' + item[3] + ';'
               string += '</list>\n'
               try:
                  print 'Sent to tab: ' + string
                  tabServer.sendToAll(string)
               except:
                 pass
               lock.release()
         print 'new client on ' + self.serverIP
         if hasEph and self.serverIP == '192.168.1.1' and self.serverPort != pingPort:
            newClient.clntSock.send(ephemeris)					##If PiHost, send GPS Ephemeris
            print 'ephemeris sent to new client'
         for thread in self.myThreads:					##scrubs myThreads
            if not thread.isAlive():	
               thread.join()						##Should be killThread(thread)
               self.myThreads.remove(thread)
               if self.serverIP == '192.168.1.1':
                  piThreads -= 1

   def sendToAll(self, message):
      for thread in self.myThreads:
         if thread.isAlive():
            self.sendLock.acquire()
            thread.clntSock.send(message)
            self.sendLock.release()

#   def sendToRound(self, message, ip):
#      for thread in self.myThreads:
#         if ip == 
#            self.sendLock.acquire()
#            thread.clntSock.send(message)
#            self.sendLock.release()

   def killThread(self,thread):			##Removes thread from myThreads
      global piThreads
      thread.join()
      self.myThreads.remove(thread)
      if self.serverIP == '192.168.1.1':
         piThreads -= 1
      

if __name__ == "__main__":
   global piServer, tabServer
   global mortars
   mortars = []  ## Array of strings representing IDs
   piServer = serverThread(piHost, piPort)  ## Socket communicating with rounds
   tabServer = serverThread(tabHost, tabPort)  ## Socket communicating with tablet.
                                               ## Receives settings from tablet. Calls piServer to send those settings to rounds.
   pingServer = serverThread(piHost, pingPort)  ## Socket pinging rounds.
                                                ## Live updates tablet with current rounds and their settings
   piServer.start()
   tabServer.start()
   pingServer.start()
