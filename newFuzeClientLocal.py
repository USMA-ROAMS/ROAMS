from socket import *
import threading
import time
global id, setting, gps, elev

setting = 0
gps = 'AA000000000000'
elev = '00000'

class serverCommands(threading.Thread):
#Class responsible for communicating with the magServer
#Sends GPS Data
#Communicates fuse data

   def __init__(self):
      threading.Thread.__init__(self)

   def run(self):
      global id, setting, gps, elev
      host = "127.0.0.1"
      port = 4445
      size = 1024
      ephemeris = ''

      #Initialize socket using default address family and default socket type
      s = socket(AF_INET, SOCK_STREAM)
      setting = 0

      while True:
         try:
            lightOn(15)
            s.connect((host,port))
            print 'command connection established'
            lightOn(14) # Super blinky lights indicate ESTABLISHED CONNECTION
            break
         except:
            pass

      while True:
         print "I'm listening"
         data = s.recv(size)
         if data:
            if data[0:3] == '111':
               ephemeris = data
               print 'ephemeris received'
               #GPIO.setup(11, GPIO.OUT)
               #GPIO.output(11, True)
               #GPIO.setup(15, GPIO.OUT)
               #turn on green light
            elif data[0] == '0':
              print 'Received ID: ' + data[0]
              id = data[1:3]
            elif data[0] == '?':
              if data[1:3] == id:
                s.send('!' + id)
            elif data[0] == '1':
               set = data[3]
               print "Received " + set
               if set == '0':
                  setting = 0
                  print 'impact'
                  lightOn(45) # Red light indicates IMPACT
               elif set == '1':
                  setting = 1
                  print 'delay'
                  lightOn(46) # Blue light indicates DELAY
               elif set == '2':
                  setting = 2
                  print 'near ground'
                  lightOn(47) # Yellow light indicates NEAR GROUND
               elif set == '3':
                  setting = 3
                  print 'proximity'
                  lightOn(48) # Green light indicates PROXIMITY
               else:
                  print 'Unknown fuze setting'

               gpsData = data[4:18]
               if len(gpsData) == 14:
                  if gpsData != 'AA000000000000':
                     gps = gpsData

               elevData = data[18:23]
               if len(elevData) == 5:
                  if elevData != '00000':
                     elev = elevData
                     #GPIO.output(15, True)
               else:
                  print 'Unrecognized Message Type'
# class serverPings(threading.Thread):

   # def __init__(self):
      # threading.Thread.__init__(self)


   # def run(self):
      # global id, setting
      # host = "192.168.1.1"
      # port = 4446
      # size = 1024
      # ephemeris = ''
      # s = socket(AF_INET, SOCK_STREAM)
      # id = '4'
      # setting = 0

      # while True:
         # try:
            # s.connect((host,port))
            # print 'ping connection established'
            # break
         # except:
            # pass
      # s.send('iam ' + id + ',' + str(setting) + ',' + gps + ',' + elev)
      # print 'info sent'

      # while True:
         # data = s.recv(size)
         # if data[0:13] == id + ' acknowledge':
           # s.send(id + ' here,' + str(setting) + ',' + gps + ',' + elev)
           # #print 'acknowledgement sent'
         # elif data[1:13] == ' acknowledge':
           # pass
         # else:
           # print data
      
class fuzeClient:

   def main(self):
            commandThread = serverCommands()
            #pingThread = serverPings()
            commandThread.start()
            #pingThread.start()

# gpio code
def lightOn(pin):
   fileName = "/sys/class/gpio/gpio" + str(pin) + "/value"
   inFile = open(fileName, "w+")
   inFile.write("1")
   inFile.close()

def lightOff(pin):
   fileName = "/sys/class/gpio/gpio" + str(pin) + "/value"
   inFile = open(fileName, "w+")
   inFile.write("0")
   inFile.close()

if __name__=="__main__":
   client = fuzeClient()
   client.main()
