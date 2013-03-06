package com.test.vttest.physics_engine;

public class VectorComponents 
{	
	public float module, direction;
	
	public VectorComponents() {
		module = 0.0f;
		direction = 0.0f;
	}
	
	public VectorComponents(float _x, float _y)
	{
		computeModule(_x, _y);
		computeDirection(_x, _y);
	}
	
	public float x_comp() {
		return module*(float) Math.cos(direction);
	}
	
	public float y_comp() {
		return module*(float) Math.sin(direction);
	}
	
	public void computeModule(float _x, float _y)
	{
		module = (float) Math.sqrt(_x*_x + _y*_y);
	}
	
	public void computeDirection(float _x, float _y)
	{
		direction = (float) Math.atan2(_y, _x);
	}
}