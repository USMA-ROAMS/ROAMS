from socket import *
import threading
import time

class DroneSwarm:
    numDrones = 20
    drones = []
    
    def main(self):
        for i in range(self.numDrones):
            print "Making a new Drone"
            t = threading.Thread(target = self.droneClient)
            self.drones.append(t)
            print "Starting drone"
            t.start()

    def droneClient(self):
        self.mess = "iam 0,0,AA000000000000,00000\n"
        sock = socket(AF_INET, SOCK_STREAM)
        sock.connect(('127.0.0.1',4445))

        reader = readerThread(sock)
        reader.start()
        
        while 1:
            sock.send(self.mess)
            time.sleep(1)
        
        sock.send("closeme\n")
        sock.close()        

class readerThread(threading.Thread):
    def __init__(self, clntsock):
        self.clntsock = clntsock
        threading.Thread.__init__(self)
    def run(self):
        while 1:
            data = self.clntsock.recv(1024)
            if not data: break
            elif data == "killthread":
                self.clntsock.send("closeme\n")
            print "\n" + str(data)
if __name__ == "__main__":
    swarm = DroneSwarm()
    swarm.main()

    
            
        
