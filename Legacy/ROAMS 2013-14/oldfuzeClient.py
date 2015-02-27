from socket import *
import threading
import RPi.GPIO as GPIO
import time
global id
id = '4'
global setting
setting = 0
global gps
gps = 'AA000000000000'
global elev
elev = '00000'


class serverCommands(threading.Thread):
#Class responsible for communicating with the magServer
#Sends GPS Data
#Communicates fuse data

   def __init__(self):
      threading.Thread.__init__(self)

   def run(self):
      global id, setting, gps, elev
      host = "192.168.1.1"
      port = 4445
      size = 1024
      ephemeris = ''

      #Initialize socket using default address family and default socket type
      s = socket(AF_INET, SOCK_STREAM)
      id = '4'
      setting = 0

      while True:
         try:
            s.connect((host,port))
            print 'command connection established'
            break
         except:
            pass
      GPIO.setup(7, GPIO.OUT)
      GPIO.output(7, True)

      while True:
         data = s.recv(size)
         if data:
            if data[0:3] == '111':
               ephemeris = data
               print 'ephemeris received'
               GPIO.setup(11, GPIO.OUT)
               GPIO.output(11, True)
               GPIO.setup(15, GPIO.OUT)
               #turn on green light
            else:
               listSize = (len(data) - 13) / 25
               print listSize
               for i in range(0, listSize):
                  idNum = (i * 25) + 6
                  if data[idNum] == id:
                     print id
                     print data[idNum]
                     set = data[idNum + 2]
                     print "Received " + set
                     GPIO.setup(12, GPIO.OUT)
                     GPIO.setup(16, GPIO.OUT)
                     GPIO.setup(18, GPIO.OUT)
                     GPIO.setup(22, GPIO.OUT)
                     GPIO.output(12, False)
                     GPIO.output(16, False)
                     GPIO.output(18, False)
                     GPIO.output(22, False)
                     if set == '0':
                        setting = 0
                        print 'impact'
                        GPIO.output(12, True)
                     elif set == '1':
                        setting = 1
                        print 'delay'
                        GPIO.output(16, True)
                     elif set == '2':
                        setting = 2
                        print 'near ground'
                        GPIO.output(18, True)
                     elif set == '3':
                        setting = 3
                        print 'proximity'
                        GPIO.output(22, True)
                     else:
                        print 'error'

                     gpsDataLower = idNum + 4
                     gpsDataUpper = idNum + 18
                     gpsData = data[gpsDataLower: gpsDataUpper]
                     if len(gpsData) == 14:
                        if gpsData != 'AA000000000000':
                           gps = gpsData

                     elevDataLower = idNum + 19
                     elevDataUpper = idNum + 24
                     elevData = data[elevDataLower: elevDataUpper]
                     if len(elevData) == 5:
                        if elevData != '00000':
                           elev = elevData
                           GPIO.output(15, True)

class serverPings(threading.Thread):

   def __init__(self):
      threading.Thread.__init__(self)


   def run(self):
      global id, setting
      host = "192.168.1.1"
      port = 4446
      size = 1024
      ephemeris = ''
      s = socket(AF_INET, SOCK_STREAM)
      id = '4'
      setting = 0

      while True:
         try:
            s.connect((host,port))
            print 'ping connection established'
            break
         except:
            pass
      s.send('iam ' + id + ',' + str(setting) + ',' + gps + ',' + elev)
      print 'info sent'

      while True:
         data = s.recv(size)
         if data[0:13] == id + ' acknowledge':
           s.send(id + ' here,' + str(setting) + ',' + gps + ',' + elev)
           #print 'acknowledgement sent'
         elif data[1:13] == ' acknowledge':
           pass
         else:
           print data
      

class fuzeClient:

   def main(self):
            commandThread = serverCommands()
            pingThread = serverPings()
            commandThread.start()
            pingThread.start()


if __name__=="__main__":
   client = fuzeClient()
   client.main()
