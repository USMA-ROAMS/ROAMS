package magServer;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class RGBStatusLight {
	// create gpio controller
    final GpioController gpio = GpioFactory.getInstance();

    final GpioPinDigitalOutput greenPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, "green", PinState.HIGH);
    final GpioPinDigitalOutput bluePin  = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09, "blue",  PinState.HIGH);
    final GpioPinDigitalOutput redPin   = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "red",   PinState.HIGH);
    
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
    	changeLight("100");
    }
    
    public void changeLight(String color){
    	switch (color) {
    	case "100": 
    		redPin.low();
    		greenPin.high();
    		bluePin.high();
    		break;
    	case "010":
    		redPin.high();
    		greenPin.low();
    		bluePin.high();
    		break;
    	case "001":
    		redPin.high();
    		greenPin.high();
    		bluePin.low();
    		break;
    	case "110":
    		redPin.low();
    		greenPin.low();
    		bluePin.high();
    		break;
    	case "011":
    		redPin.high();
			greenPin.low();
			bluePin.low();
			break;
    	case "101":
    		redPin.low();
    		greenPin.high();
    		bluePin.low();
    		break;
    	case "111":
    		redPin.low();
    		greenPin.low();
    		bluePin.low();
    		break;
    	}
    }
}
