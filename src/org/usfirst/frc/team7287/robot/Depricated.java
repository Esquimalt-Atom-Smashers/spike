import edu.wpi.first.wpilibj.DriverStation;

public class Depricated {
//	@Override
//	public void autonomousInit() {
//		timeFactor = 1;
//		timeFix = 0;
//		timer.reset();
//		timer.start();
//		autoState = 0;
//	}
//	
//	
//	@Override
//	public void autonomousPeriodic() { // none of this is used, I wouldn't try it if I were you
//		
//		switchAndScaleSides = DriverStation.getInstance().getGameSpecificMessage();
//		this.closeSwitchSide = String.valueOf(switchAndScaleSides.charAt(0));
//		double turnTime = 0.5;
//		switch (autoState) {
//				case 0:
//					if (startLeft.get() || startRight.get()) {
//						//Robot is not in Middle
//						autoState = 4;
//						break;
//					}
//					if (closeSwitchSide.equals("L")) {
//						autoState = 2;
//					}
//					else {
//						autoState = 1;
//					}
//					break;
//				case 1:
//					//Drives straight into switch to deliver cube(middle right)
//					drive.forward(1.0);
//					if (timer.get() >= 1) {
//						timer.reset();
//						autoState = 10 ;
//					}
//					break;
//				case 2: //first part of s
//					double rightSideSpeed = 1;
//					drive.tankDrive(rightSideSpeed*0.45, rightSideSpeed);
//					if (timer.get() >= 0.8) {
//						timer.reset();
//						autoState = 3 ;
////						autoState = 10 ;
//					}
//					break;
//				case 3: // drive forward to align with left side of switch
//					double leftSideSpeed = 1;
//					drive.tankDrive(leftSideSpeed,  leftSideSpeed*0.55);
//					if (timer.get() >= 0.86) {
//						timer.reset();
//						autoState = 11 ;
//					}
//					break;
//				case 4: //Left and Right Autonomous
//					drive.forward(0.5);
//					if (timer.get() <= 2.0) {
//						break;
//					}
//					
//					if (closeSwitchSide.equals("R") && startRight.get() && timer.get() > 2.0 && timer.get() < 3.0) {
//						drive.tankDrive(0.2, 0.6);
//					} else if (closeSwitchSide.equals("L") && startLeft.get() && timer.get() > 2.0 && timer.get() < 3.0) {
//						drive.tankDrive(0.6, 0.2);
//					} else {
//						timer.reset();
//						autoState = 10;
//					}
//					break;
//				case 10: // Hard Stop
//					drive.stop();
//					break;
//					
//				case 11: // Soft Stop
//					break;
//				}
//				
//	}
//	
//	private void calibrate() {
//		double turnSpeed = 0.46;
//		double linearSpeed = 0.5;
//		if (timer.get() > timeFactor + timeFix - 1.0 && timer.get() < timeFactor + timeFix) {
//			drive.forward(linearSpeed);
//		} else if (timer.get() > timeFactor + timeFix && timer.get() < timeFactor + timeFix + 0.5) {
//			drive.turn("left",turnSpeed);
//		} else if (timer.get()> timeFactor + timeFix + 0.5 && timer.get() < timeFactor + timeFix + 1.0) {
//			drive.stop();
//		} else {
//			drive.stop();
//			timeFix++;
//			timeFactor++;
//		}
//	}

}
