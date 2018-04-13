
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
//	DigitalInput bottomLimit;
//	DigitalInput topLimit;
	String switchAndScaleSides = DriverStation.getInstance().getGameSpecificMessage();
	String closeSwitchSide;
	int autoState;
	DigitalInput startingPosition; //0 is middle, 1 is not
	
	
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
//		bottomLimit = new DigitalInput(0);
//		topLimit = new DigitalInput (1);
		startingPosition = new DigitalInput (0);
		this.closeSwitchSide = String.valueOf(this.switchAndScaleSides.charAt(0));
		System.out.println("Our side of each is: " + switchAndScaleSides);
		System.out.println("Our side of the close switch is: " + closeSwitchSide);
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
		double turnTime = 0.5;
		double direction = -1;
		boolean reverse = false;
		double reverseValue = 1;
		switch (autoState) {
				case 0:
					if (startingPosition.get() == true) {
						//Robot is not in Middle
						autoState = 1;
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
					if (timer.get() >= 0.8) {
						timer.reset();
						autoState = 10 ;
					}
					break;
				case 2: //first part of s
					double rightSideSpeed = 0.75;
					drive.tankDrive(rightSideSpeed*0.4, rightSideSpeed);
					if (timer.get() >= 1) {
						timer.reset();
						autoState = 3 ;
					}
					break;
				case 3: // drive forward to align with left side of switch
					double leftSideSpeed = 0.75;
					drive.tankDrive(leftSideSpeed,  leftSideSpeed*0.7);
					if (timer.get() >= 1.50) {
						timer.reset();
						autoState = 10 ;
					}
					break;
				
				case 10: // Stop
					drive.stop();
					break;
				case 20:	//2nd cube
					//From switch to turn point
					drive.reverse(reverseValue * 0.50);
					if (timer.get() >= 0.50) {
						timer.reset();
						if (reverse) {
							autoState = 10;
							break;
						}
						autoState = 21;
					}
					break;
				case 21: //turn
					if(closeSwitchSide.equals("L")) {
						drive.turn("right",reverseValue * 1);
						if (timer.get() >= 0.45) {
							timer.reset();
							if (reverse) {
								autoState = autoState - 1;
								break;
							}
							autoState = 10;
							break;
							}
					}
						if(closeSwitchSide.equals("R")) {
							drive.turn("left",reverseValue * 1);
							if (timer.get() >= 0.45) {
								timer.reset();
								if (reverse) {
									autoState = autoState - 1;
									break;
								}
								autoState = 10;
								break;
							}
						}
				case 22://forward to centre
					drive.forward(reverseValue * 1);
					if (timer.get() >= 0.4) {
						timer.reset();
						if (reverse) {
							autoState = autoState - 1;
							break;
						}
						autoState = 10;
						break;
					}
				case 23://opposite turn from before
					if(closeSwitchSide.equals("R")) {
						drive.turn("right",reverseValue * 1);
						if (timer.get() >= 0.45) {
							timer.reset();
							if (reverse) {
								autoState = autoState - 1;
								break;
							}
							autoState = 10;
							break;
						}
					}
						if(closeSwitchSide.equals("L")) {
							drive.turn("left",reverseValue * 1);
							if (timer.get() >= 0.45) {
								timer.reset();
								if (reverse) {
									autoState = autoState - 1;
									break;
								}
								autoState = 10;
								break;
							}
						}	
					case 24:
						drive.forward(reverseValue * 1);
						if (timer.get() >= 0.3) {
							timer.reset();
							if (reverse) {
								autoState = autoState - 1;
								break;
							}
							autoState = 10;
							break;
						}
					case 25:
						grab(0.5);
						if (timer.get() >= 0.45) {
							timer.reset();
							reverse = true;
							reverseValue = -1;
							autoState = autoState - 1;
							break;
						}
			}
	}
	

	
	@Override
	public void teleopPeriodic() {
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
			move = move * 0.5;
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

