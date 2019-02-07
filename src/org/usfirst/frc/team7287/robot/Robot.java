

package org.usfirst.frc.team7287.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.cscore.*;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;




/**
* The Robot class is the master control class for Spike the robot.
*
* @author  Esquimalt Robotics
* @since   2018-02-01 
*/

// This is the 2018 robot. It had claws that moved in and out to grab boxes and the claws
// were attached to a gantry to move them up and down as well. If you want to build this,
// make sure to put the limit switches in for the gantry. Before we had that, the motor
// would start smoking if you drove it too hard.
// This is very bare-bones. There was more code, but it didn't really do anything so I
// took it all out. It still works though, if you were to actually put it onto Spike. RIP
// -Lucas Henry, programming head 2019

public class Robot extends IterativeRobot {
	private DifferentialDrive spike;
	private Joystick stick;
	private Joystick gantryController;
	TalonSRX clawMotor;
	TalonSRX verticalMotor;
	DigitalInput topLimit;
	DigitalInput bottomLimit;
	
	
	
	
	@Override
	public void robotInit() {
		spike = new DifferentialDrive(new Spark(0), new Spark(1)); // the base and wheels
		stick = new Joystick(0); // controls spike
		gantryController = new Joystick(1); // controls arms
		clawMotor = new TalonSRX(0); // talonSRX is name of motor driver
		verticalMotor = new TalonSRX(1); // "
		topLimit = new DigitalInput(0); // limit switch pressed by gantry moving up
		bottomLimit = new DigitalInput(1); // same on bottom
	}
	
	
	@Override
	public void teleopPeriodic() {
		grab(gantryController.getRawAxis(1)); // claws
		upDown(gantryController.getRawAxis(3)); // gantry up and down
		spike.arcadeDrive(stick.getY(), stick.getRawAxis(2)); // move around
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
	
}// end of Robot Class

