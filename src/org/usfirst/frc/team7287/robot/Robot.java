
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

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;


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
	Timer timer;
	int timeFactor;
	int timeFix;
	private Drive drive;	
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
		spike = new DifferentialDrive(new Spark(0), new Spark(1));
		stick = new Joystick(0);
		gantryController = new Joystick(1);
		timer = new Timer();
		drive = new Drive(spike, false);
		clawHeightSensor = new ClawHeightSensor(0);
		clawMotor = new TalonSRX(0);
		verticalMotor = new TalonSRX(1);
		startRight = new DigitalInput (0);
		startLeft = new DigitalInput(1);
		topLimit = new DigitalInput (2);
		bottomLimit = new DigitalInput(3);
	}
	

	@Override
	public void autonomousInit() {
		timeFactor = 1;
		timeFix = 0;
		timer.reset();
		timer.start();
		autoState = 0;
	}
	
	
	@Override
	public void autonomousPeriodic() {
		switchAndScaleSides = DriverStation.getInstance().getGameSpecificMessage();
		this.closeSwitchSide = String.valueOf(switchAndScaleSides.charAt(0));
		double turnTime = 0.5;
		switch (autoState) {
				case 0:
					if (startLeft.get() || startRight.get()) {
						//Robot is not in Middle
						autoState = 4;
						break;
					}
					if (closeSwitchSide.equals("L")) {
						autoState = 2;
					}
					else {
						autoState = 1;
					}
					break;
				case 1:
					//Drives straight into switch to deliver cube(middle right)
					drive.forward(1.0);
					if (timer.get() >= 1) {
						timer.reset();
						autoState = 10 ;
					}
					break;
				case 2: //first part of s
					double rightSideSpeed = 1;
					drive.tankDrive(rightSideSpeed*0.45, rightSideSpeed);
					if (timer.get() >= 0.8) {
						timer.reset();
						autoState = 3 ;
//						autoState = 10 ;
					}
					break;
				case 3: // drive forward to align with left side of switch
					double leftSideSpeed = 1;
					drive.tankDrive(leftSideSpeed,  leftSideSpeed*0.55);
					if (timer.get() >= 0.86) {
						timer.reset();
						autoState = 11 ;
					}
					break;
				case 4: //Left and Right Autonomous
					drive.forward(0.5);
					if (timer.get() <= 2.0) {
						break;
					}
					
					if (closeSwitchSide.equals("R") && startRight.get() && timer.get() > 2.0 && timer.get() < 3.0) {
						drive.tankDrive(0.2, 0.6);
					} else if (closeSwitchSide.equals("L") && startLeft.get() && timer.get() > 2.0 && timer.get() < 3.0) {
						drive.tankDrive(0.6, 0.2);
					} else {
						timer.reset();
						autoState = 10;
					}
					break;
				case 10: // Hard Stop
					drive.stop();
					break;
					
				case 11: // Soft Stop
					break;
				}
				
	}

	
	@Override
	public void teleopPeriodic() {
		System.out.println("Switch is: Left: " + startLeft.get() + " Right: " + startRight.get());
		double teleopSpeed = 1;
		if (gantryController.getRawAxis(1) != 0) {
			grab(gantryController.getRawAxis(1));
		}
		else {
			grab(0);
		}

		if (gantryController.getRawAxis(3) != 0){
			
			upDown(gantryController.getRawAxis(3));
		}
		else {
			upDown(0);
		}
		
		spike.arcadeDrive(stick.getY()*teleopSpeed, stick.getRawAxis(2)*teleopSpeed);
	}
	
	
	private void upDown(double move) {	
		move = -move; //invert move directions
		
		if (move > 0) {
			move = move * 0.9;
		}
//		if (move< 0 && topLimit.get() || move>0 && bottomLimit.get()) {
			if (move< 0 && topLimit.get()) {
			move = 0;
			}
			verticalMotor.set(ControlMode.PercentOutput, move);
		}	
	
	private void grab(double speed) {
		clawMotor.set(ControlMode.PercentOutput, speed * 0.7);
	}
	
	
	private void drop() {
		clawMotor.set(ControlMode.PercentOutput, 0);
	}
	

	private void calibrate() {
		double turnSpeed = 0.46;
		double linearSpeed = 0.5;
		if (timer.get() > timeFactor + timeFix - 1.0 && timer.get() < timeFactor + timeFix) {
			drive.forward(linearSpeed);
		} else if (timer.get() > timeFactor + timeFix && timer.get() < timeFactor + timeFix + 0.5) {
			drive.turn("left",turnSpeed);
		} else if (timer.get()> timeFactor + timeFix + 0.5 && timer.get() < timeFactor + timeFix + 1.0) {
			drive.stop();
		} else {
			drive.stop();
			timeFix++;
			timeFactor++;
		}
	}
	
	
}// end of Robot Class

