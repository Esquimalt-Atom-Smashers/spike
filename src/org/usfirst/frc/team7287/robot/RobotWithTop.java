
package org.usfirst.frc.team7287.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.usfirst.frc.team7287.robot.Drive;
import org.usfirst.frc.team7287.robot.ClawHeightSensor;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.cscore.*;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;

import edu.wpi.first.wpilibj.CameraServer;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;



/**
* The Robot class is the master control class for Spike the robot.
*
* @author  Esquimalt Robotics
* @since   2018-02-01 
*/
public class Robot extends IterativeRobot {
	private DifferentialDrive spike;
	private Joystick stick;
	private Joystick gantryController;
	ClawHeightSensor clawHeightSensor;
	TalonSRX clawMotor;
	TalonSRX verticalMotor;
	DigitalInput topLimit;
	DigitalInput bottomLimit;
	String switchAndScaleSides;
	String closeSwitchSide;
	int autoState;
	DigitalInput startRight;
	DigitalInput startLeft;
	
	
	
	
	@Override
	public void robotInit() {
		spike = new DifferentialDrive(new Spark(0), new Spark(1)); // the base and wheels
		stick = new Joystick(0); // controls spike
		gantryController = new Joystick(1); // controls arms
		clawHeightSensor = new ClawHeightSensor(0);
		clawMotor = new TalonSRX(0);
		verticalMotor = new TalonSRX(1);
		startRight = new DigitalInput (0);
		startLeft = new DigitalInput(1);
		topLimit = new DigitalInput (2);
		bottomLimit = new DigitalInput(3);
		
		//ultrasonic = new AnalogInput(0);
		
		
	}
	
	
	@Override
	public void teleopPeriodic() {
		double teleopSpeed = 0.6;
		if (gantryController.getRawAxis(1) != 0) {
			grab(gantryController.getRawAxis(1));
		}
		else {
			grab(0);
		}
		if (camera == null)
		{
			System.exit(0);
		}
		if (gantryController.getRawAxis(3) != 0){
			
			upDown(gantryController.getRawAxis(3));
		}
		else {
			upDown(0);
		}
		
		spike.arcadeDrive(stick.getY()*teleopSpeed, stick.getRawAxis(2)*teleopSpeed);
		//System.out.println(ultrasonic.getValue());
	}
	
	
	private void upDown(double move) {	// move gantry up or down
		move *= -1; //invert move directions
		
		if (move > 0) {
			move *= 0.9; // make sure it doesn't go too fast
		}
		if (move < 0 && topLimit.get() || move > 0 && bottomLimit.get()) { // stop motor burnout 
			move = 0;
			System.out.println("Limit switch pressed");
		}
		verticalMotor.set(ControlMode.PercentOutput, move);
	}	
	
	private void grab(double speed) {
		clawMotor.set(ControlMode.PercentOutput, speed * 0.7);
	}
	
	
	private void drop() {
		clawMotor.set(ControlMode.PercentOutput, 0);
	}
	
	
	
}// end of Robot Class

