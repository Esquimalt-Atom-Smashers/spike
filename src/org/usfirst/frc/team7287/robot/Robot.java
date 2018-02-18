
package org.usfirst.frc.team7287.robot;

import edu.wpi.first.wpilibj.IterativeRobot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.usfirst.frc.team7287.robot.Drive;


public class Robot extends IterativeRobot {
	private DifferentialDrive spike;
	private Joystick stick;
	Timer timer;
	double turnSpeed = 0.46;
	double linearSpeed = 0.5;
	int timeFactor;
	int timeFix;
	private Drive drive;
	
	@Override
	public void robotInit() {
		spike = new DifferentialDrive(new Spark(0), new Spark(1));
		stick = new Joystick(0);
		timer = new Timer();
		drive = new Drive(spike, false);
	}
	
	@Override
	public void autonomousInit() {
		timeFactor = 1;
		timeFix = 0;
		timer.reset();
		timer.start();
	}
	
	
	@Override
	public void autonomousPeriodic() {
		initialCubeDrop();
	}
	
	private void initialCubeDrop() {
		double driveTime = 2.5;
		double superSpeed = 1.0;
		double fallTime = 0.25;
		if (timer.get() < driveTime * 0.1) {
			drive.forward(superSpeed);
		} else if(timer.get() < driveTime * fallTime && timer.get() > driveTime * 0.1) {
			drive.stop(); 
		} else if(timer.get() > driveTime * fallTime && timer.get() < driveTime * 1.0) {
			drive.forward(0.5);
		} else {
			drive.stop();
		}
	}
	
	private void calibrate() {
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
	
	@Override
	public void teleopPeriodic() {
		spike.arcadeDrive(-stick.getY()*(0.4*stick.getRawAxis(3)+0.6), stick.getRawAxis(4)*(0.4*stick.getRawAxis(3)+0.6));
	}
}
