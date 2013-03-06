package com.test.vttest.physics_engine.maths;

public class Coords2D {
	private float x, y;
	
	public Coords2D()
	{
		x = y = 0.0f;
	}
	
	public Coords2D(float _x, float _y)
	{
		X(_x);
		Y(_y);
	}
	
	public void X(float _x)
	{
		x = _x;
	}
	
	public void Y(float _y) 
	{
		y = _y;
	}
	
	public float X()
	{
		return x;
	}
	
	public float Y()
	{
		return y;
	}
}
