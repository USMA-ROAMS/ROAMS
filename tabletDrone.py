from socket import *
import threading
import time

class Tablet:
    numTablets = 1
    tablets = []
    
    def main(self):
        
        print "Making a new Drone"
        t = threading.Thread(target = self.tab)
        self.tablets.append(t)
        print "Starting drone"
        t.start()

    def tab(self):
        self.mess = "<line>01,0,AA000000000000,00000</line>"
        sock = socket(AF_INET, SOCK_STREAM)
        sock.connect(('127.0.0.1',4446))

        reader = readerThread(sock)
        reader.start()
        
        
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
    tablet = Tablet()
    tablet.main()

    
            
        
