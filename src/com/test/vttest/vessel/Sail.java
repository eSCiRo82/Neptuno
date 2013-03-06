package com.test.vttest.vessel;

import com.test.vttest.physics_engine.maths.Angle;

// Vela del barco
public class Sail {
	public static final float MAX_SAIL_ANGLE = Angle.A90;	// Ángulo en radianes
	public static final float MIN_SAIL_ANGLE = -Angle.A90;	// Ángulo en radianes
	
	private Angle sail_angle;
	
	public Sail()
	{
		sail_angle = new Angle();
	}
	
	// SETTERS //
	public void setSailAngle(float _sail_angle) 
	{
		if (_sail_angle > MAX_SAIL_ANGLE)
		{
			sail_angle.setAngle(MAX_SAIL_ANGLE);
		}
		else if (_sail_angle < MIN_SAIL_ANGLE)
		{
			sail_angle.setAngle(MIN_SAIL_ANGLE);
		}
		else
		{
			sail_angle.setAngle(_sail_angle);			
		}
	}
	
	public float getSailAngleValue()
	{
		return sail_angle.getAngle();
	}
	
	public Angle getSailAngle()
	{
		return sail_angle;
	}
}
