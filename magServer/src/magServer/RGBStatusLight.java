package magServer;

import java.util.Random;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;

public class RGBStatusLight {
	// create gpio controller
    GpioController gpio;

    GpioPinDigitalOutput greenPin;
    GpioPinDigitalOutput bluePin;
    GpioPinDigitalOutput redPin; 
    
    /*
     * Red     = 100
     * Green   = 010
     * Blue    = 001
     * yellow  = 110
     * cyan    = 011
     * magenta = 101
     * white   = 111
     */

    public void init(){
    	Gpio.wiringPiSetup();
    	gpio = GpioFactory.getInstance();
    	greenPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "green", PinState.HIGH);
    	bluePin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "blue",  PinState.HIGH);
    	redPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "red",   PinState.HIGH);
    	changeLight("100");
    }
    
    public void changeLight(String color){

    	System.out.println("Changing LED Color to... ");
    	System.out.println(color);
    	switch (color) {
    	case "000":
    		System.out.println("...off");
    		this.redPin.high();
    		this.greenPin.high();
    		this.bluePin.high();
    		break;
    	case "100": 
    		System.out.println("...red");
    		this.redPin.low();
    		this.greenPin.high();
    		this.bluePin.high();
    		break;
    	case "010":
    		System.out.println("...green");
    		this.redPin.high();
    		this.greenPin.low();
    		this.bluePin.high();
    		break;
    	case "001":
    		System.out.println("...blue");
    		this.redPin.high();
    		this.greenPin.high();
    		this.bluePin.low();
    		break;
    	case "110":
    		System.out.println("...yellow");
    		this.redPin.low();
    		this.greenPin.low();
    		this.bluePin.high();
    		break;
    	case "011":
    		System.out.println("...cyan");
    		this.redPin.high();
    		this.greenPin.low();
    		this.bluePin.low();
			break;
    	case "101":
    		System.out.println("...magenta");
    		this.redPin.low();
    		this.greenPin.high();
    		this.bluePin.low();
    		break;
    	case "111":
    		System.out.println("...white");
    		this.redPin.low();
    		this.greenPin.low();
    		this.bluePin.low();
    		break;
    	}
  
    }
    
    public void danceParty(){
    	int i = 0;
    	while(i<100){
    		int rand = randInt(0,7);
    		switch(rand){
    		case 0:
    			changeLight("000");
    			break;
    		case 1:
    			changeLight("100");
    			break;
    		case 2:
    			changeLight("010");
    			break;
    		case 3:
    			changeLight("001");
    			break;
    		case 4:
    			changeLight("101");
    			break;
    		case 5:
    			changeLight("011");
    			break;
    		case 6:
    			changeLight("110");
    			break;
    		case 7:
    			changeLight("111");
    			break;
    		}
    		try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
    		i++;
    	}
    }
    
    public void blinkRed(){
    	int i = 0;
    	while(i<5){
    		changeLight("100");
    		changeLight("000");
    		i++;
    	}
    	changeLight("010");
    }
    
    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
