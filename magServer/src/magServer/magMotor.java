package magServer;

import com.pi4j.io.gpio.PinState;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

public class magMotor {
	int softPwmPin=1;
	int speedPercent=30;
	GpioPinDigitalInput hallEffect;
	GpioPinDigitalOutput direction;
	
	
	public void init(){
		Gpio.wiringPiSetup();
		SoftPwm.softPwmCreate(softPwmPin,0,100);
		GpioController gpio = GpioFactory.getInstance();
		this.hallEffect = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, "hallEffect",PinPullResistance.PULL_DOWN);
		this.direction = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00);
	}
	
	public void rotatePhysicalMagazine(String dir, String rots){
		int rotCounter = 0;
		if (dir.equals("0")){
			while(rotCounter < Integer.parseInt(rots)){
				this.direction.high();
				SoftPwm.softPwmWrite(softPwmPin, speedPercent);
				PinState lastHallState = this.hallEffect.getState();
				while(this.hallEffect.getState()==lastHallState){}
				lastHallState = this.hallEffect.getState();
				while(this.hallEffect.getState()==lastHallState){}
				lastHallState=this.hallEffect.getState();
				SoftPwm.softPwmWrite(softPwmPin, 0);
				rotCounter += 1;
			}
		}
		else if(dir.equals("1")){
			while(rotCounter < Integer.parseInt(rots)){
				this.direction.low();
				SoftPwm.softPwmWrite(softPwmPin, speedPercent);
				PinState lastHallState = this.hallEffect.getState();
				while(this.hallEffect.getState()==lastHallState){}
				lastHallState = this.hallEffect.getState();
				while(this.hallEffect.getState()==lastHallState){}
				lastHallState=this.hallEffect.getState();
				SoftPwm.softPwmWrite(softPwmPin, 0);
				rotCounter += 1;
			}
		}
		else{System.out.println("Wat...");}
	}
}