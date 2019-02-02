
package org.usfirst.frc.team7287.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.usfirst.frc.team7287.robot.Drive;
import edu.wpi.cscore.*;

import edu.wpi.first.wpilibj.CameraServer;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import sensors.DistanceSensor;
import java.util.LinkedList;
import Vision.*;

/**
* The Robot class is the master control class for Spike the robot.
*
* @author  Esquimalt Robotics
* @since   2018-02-01 
*/
public class Robot extends IterativeRobot implements Functions {
	private DifferentialDrive spike;
	private Joystick stick;
	UsbCamera camera;
	private DistanceSensor sonicFront;
  	private DistanceSensor sonicBack;
	
	private Thread cameraThread;
	private CvSink imageGetter;
	private ObjectFinder ballFinder;
	
	private static final int CAMERA_WIDTH = 160;
	private static final int CAMERA_HEIGHT = 480;
	
	
	
	@Override
	public void robotInit() {
		spike = new DifferentialDrive(new Spark(0), new Spark(1)); // the base and wheels
		stick = new Joystick(0); // controls spike
		ballFinder = new ObjectFinder(ObjectFinder.Objects.BALL);
		
		sonicFront = new DistanceSensor(0);
		sonicBack = new DistanceSensor(1);
		
		
		// this whole section is for the camera. You can access it on the FRC
		// dashboard by selecting "USB Camera 0" in the bottom left.
		// this is copied from the docs
		camera = CameraServer.getInstance().startAutomaticCapture();
        cameraThread = new Thread(() -> {
            UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
            camera.setResolution(640, 480);
            
            CvSink cvSink = CameraServer.getInstance().getVideo();
            CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
            
            Mat source = new Mat();
            Mat output = new Mat();
            
            while(!Thread.interrupted()) {
                cvSink.grabFrame(source);
                Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
                outputStream.putFrame(output);
            }
        });
        
        cameraThread.start();
        imageGetter = CameraServer.getInstance().getVideo();
	}
	
	
	@Override
	public void teleopPeriodic() {
		Mat image = new Mat();
		imageGetter.grabFrame(image);
		PixelGroup[] groups = ballFinder.rankAndSortGroups(ballFinder.findObject(image));
		if (groups.length > 0)
		{
			double mapVal = map(groups[0].getX() + groups[0].getWidth()/2, 0, CAMERA_WIDTH, -1, 1);
			mapVal = (Math.abs(mapVal) > 0.55)? Math.signum(mapVal) * 0.55:mapVal;
			System.out.println(mapVal);
			spike.arcadeDrive(0, mapVal);
		}
		//spike.arcadeDrive(0, ballFinder.findObject(image).size()/3);
		//System.out.println(ballFinder.findObject(image).size());
		
		//System.out.println(ultrasonic.getValue());
		//System.out.println("front(0) : " + sonicFront.getDistance());
		
	}
	
	
}// end of Robot Class

;