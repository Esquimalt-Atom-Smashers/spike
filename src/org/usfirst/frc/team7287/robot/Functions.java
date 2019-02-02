package org.usfirst.frc.team7287.robot;

public interface Functions 
{
	default double map(double val, double lbound1, double ubound1, double lbound2, double ubound2)
	{
		double multiplier = (ubound2 - lbound2)/(ubound1 - lbound1);
		return (multiplier * val + lbound2 - lbound1);
	}
}
