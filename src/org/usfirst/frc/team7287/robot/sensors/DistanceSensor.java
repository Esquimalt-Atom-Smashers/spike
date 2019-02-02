package sensors;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DistanceSensor {
	private static AnalogInput sensor;
	private static final double VOLTS_TO_DIST = 1.0;
	
	public DistanceSensor(int AnalogPin) {
		if (AnalogPin >= 0 && AnalogPin <= 9) {
			sensor = new AnalogInput(AnalogPin);
		} else {
			System.out.println("Invalid pin!");
			sensor = null;
		}
	}
	
	public static double getVoltage() {
		return sensor.getVoltage();
	}
	
	public static double getDistance() {
		return sensor.getVoltage() * VOLTS_TO_DIST;
	}
	
	public static void UpdateDashboard() {
		SmartDashboard.putNumber("Distance (volts)", getVoltage());
		SmartDashboard.putNumber("Distance (real)", getDistance());
	}
}
