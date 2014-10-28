from socket import *
import threading
import time
import errno

class Tablet(threading.Thread):
    numTablets = 1
    tablets = []
    
    def __init__(self):
        self.mess = "01,0,AA000000000000,00000\n"
        self.sock = socket(AF_INET, SOCK_STREAM)
        self.sock.connect(('127.0.0.1',4446))

        L = threading.Thread(target = Tablet.Listen(self))
        L.start()

        T = threading.Thread(target = Tablet.Talk(self))
        T.start()
        
        self.sock.send("closeme\n")
        self.sock.close()        

    def Listen(self):
        while(1):
            data = self.sock.recv(1024)
            if data:
                print('Received: ', data)

    def Talk(self):
        while(1):
            self.sock.send(self.mess)
            time.sleep(1)
            print "data sent!"
            
if __name__ == "__main__":
    tablet = Tablet()

    
            
        
