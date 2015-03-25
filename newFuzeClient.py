from socket import *
import threading
import time
global idNum, setting, gps, elev

setting = 0
gps = 'AA000000000000'
elev = '00000'

class serverCommands(threading.Thread):
# Class responsible for communicating with the magServer
# Sends GPS Data
# Communicates fuse data

    def __init__(self):
        threading.Thread.__init__(self)

    def run(self):
        global idNum, setting, gps, elev
        host = "192.168.42.1"
        port = 4445
        size = 1024
        ephemeris = ""

        # Initialize socket using default address family and default socket type
        s = socket(AF_INET, SOCK_STREAM)
        setting = 0

        while True:
            try:
                s.connect((host,port))
                print "Command Connection Established"
                lightOn(14) # Super blinky lights indicate ESTABLISHED CONNECTION
                break
            except:
                pass

        while True:
            print "--- I'm listening ---"
            data = s.recv(size)
            if data:
                # update ephemeris
                if data[0:3] == '111':
                    ephemeris = data
                    print "Ephemeris received: " + str(ephemeris)

                # receive an assigned ID
                elif data[0] == '0':
                    idNum = data[1:3]
                    print 'Received ID: ' + idNum

                # is asked "are you here?"
                elif data[0] == '?':
                    if data[1:3] == idNum:
                        s.send('!' + idNum + "\r\n")
                        print "Server just called me..."

                # change fuze setting
                elif data[0] == '1':
                    lightOff(45)   # clear all fuze lights
                    lightOff(46)
                    lightOff(47)
                    lightOff(48)
                    fuzeSet = data[3]
                    print "Change fuze setting: " + fuzeSet
                    if fuzeSet == '1':
                        setting = 1
                        print 'impact'
                        lightOn(45) # Red light indicates IMPACT
                    elif fuzeSet == '2':
                        setting = 2
                        print 'proximity'
                        lightOn(48) # Green light indicates PROXIMITY
                    elif fuzeSet == '3':
                        setting = 3
                        print 'delay'
                        lightOn(46) # Blue light indicates DELAY
                    elif fuzeSet == '4':
                        setting = 4
                        print 'near ground'
                        lightOn(47) # Yellow light indicates NEAR GROUND
                    else:
                        print 'Unknown fuze setting'

                # wrong message type
                else:
                    print 'Unrecognized Message Type'

                # send an acknowledgement message to server if receiving data
                s.send('!' + idNum + "\r\n")
                print "Send an acknowledgement"

##                gpsData = data[4:18]
##                if len(gpsData) == 14:
##                    if gpsData != 'AA000000000000':
##                        gps = gpsData
##                        
##                elevData = data[18:23]
##                if len(elevData) == 5:
##                    if elevData != '00000':
##                        elev = elevData
##            
                
            else:
                print '## No data is sent ##'
      
class fuzeClient:

    def main(self):
        commandThread = serverCommands()
        commandThread.start()

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
