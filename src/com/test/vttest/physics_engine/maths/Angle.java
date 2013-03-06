package com.test.vttest.physics_engine.maths;

/* Adaptación del sistema de ángulos para atan en android
 * 
 * 
 * 				|-PI/2
 * 		 -PI	|
 * 			--------- 0
 * 		  PI	|
 * 				|PI/2
 */

public class Angle {	
	public static final float A180	= 3.14159f;
	public static final float A360 	= 2.0f*A180;
	public static final float A90	= A180/2.0f; 
	
	private float angle;
	
	public Angle()
	{
		angle = 0.0f;
	}
	
	public Angle(float _a)
	{
		setAngle(_a);
	}
	
	public void inc(float _inc)
	{
		setAngle(angle + _inc);
	}
	
	public void dec(float _dec)
	{
		setAngle(angle - _dec);
	}
	
	public void setAngle(float _a) 
	{
		// Si es mayor que PI, se pasa a la parte negativa
		if (_a > A180) 			angle = _a - A360; 
		// Si es menor que -PI, se pasa a la parte positiva
		else if (_a < -A180)	angle = _a + A360;
		// Si no, estamos en zona neutra :D
		else					angle = _a;
	}
	
	public float getAngle() 
	{
		return angle;
	}
	
	// Otras funciones interesantes
	// Devuelve el ángulo normal en avance
	public float getNormalAv()
	{
		Angle a = new Angle(angle + A90);
		return a.getAngle();
	}
	
	// Devuelve el ángulo normal en reverso
	public float getNormalRe()
	{
		Angle a = new Angle(angle - A90);
		return a.getAngle();
	}
	
	// Devuelve el cuadrante en el que se encuentra el ángulo
	/* 
	 * 
	 * 				|-PI/2
	 * 		 -PI III| IV
	 * 			--------- 0
	 * 		  PI II	| I
	 * 				|PI/2
	 */
	public int getCuadrant() {
		if (angle > 0.0f && angle <= A90)
		{
			return 1;
		} 
		else if (angle > A90 && angle < A180)
		{
			return 2;
		}
		else if (angle > -A180 && angle < -A90)
		{
			return 3;
		}
		else if (angle > -A90 && angle <= 0.0f)
		{
			return 4;
		}
		else
		{
			return 0;
		}
	}
}
