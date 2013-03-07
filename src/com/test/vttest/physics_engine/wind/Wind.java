package com.test.vttest.physics_engine.wind;

import java.util.Random;

import com.test.vttest.physics_engine.maths.Angle;

public class Wind 
{
	public static final float MAX_WIND_SPEED = 30.0f; // Velocidad en m/s 	-> 108 km/h
	public static final float MIN_WIND_SPEED = 1.0f; // Velocidad en m/s	-> 3.6 km/h
	// Velocidad media
	public static final float CENTER_SPEED = 15.5f;
	// M�xima diferencia de velocidad
	public static final float MAX_DIFFERENCE = 14.5f;
	// La variaci�n aleatoria de velocidad puede desviarse del centro entre [-0.5,0.5]
	// El s�mbolo es negativo pues queremos que de una prioridad inversa seg�n el l�mite
	// al que se aproxime
	public static final float MAX_SPEED_DEVIATION = -0.5f;
	
	// NOTA: estas constantes podr�an ser usadas para establecer la dificultad del juego :)
	public static float SPEED_DEVIATION = 0.0f;	// Desviaci�n de la distribuci�n normal												
	public static final float SPEED_VARIATION = 0.25f;	// Variaci�n de la distribuci�n normal
	
	public static final float DIRECTION_DEVIATION = 0.0f;
	public static final float DIRECTION_VARIATION = 0.05f;
	
	private Random alea = null;
	
	private Angle direction;	// Direcci�n del viento	[0-2PI] [rad]
	private float speed;	// [m/s]
	
	public Wind()
	{
		alea = new Random(System.currentTimeMillis());
		speed = MIN_WIND_SPEED;
		direction = new Angle();
	}
	
	public Wind(float _init_speed, float _init_direction)
	{
		alea = new Random(System.currentTimeMillis());
		speed = _init_speed;
		direction = new Angle(_init_direction);
	}
		
	// Actualiza el viento en un valor aleatorio entre el m�ximo y m�nimo establecidos
	public void update() 
	{
		float variation = windSpeedVariation();
		speed += variation;
		if (speed < MIN_WIND_SPEED) 
		{
			speed = MIN_WIND_SPEED;
		} 
		else if (speed > MAX_WIND_SPEED) 
		{
			speed = MAX_WIND_SPEED;
		}
		
		SPEED_DEVIATION = MAX_SPEED_DEVIATION*(speed - CENTER_SPEED)/MAX_DIFFERENCE;
		
		direction.inc(windDirectionVariation());
	}
	
	// Realiza una selecci�n de un valor aleatorio con distribuci�n normal donde 
	// se tienen una variaci�n y una desviaci�n distintas de 1 y 0 respectivamente
	// Variaci�n de la velocidad del viento
	private float windSpeedVariation() 
	{
		return (((float) alea.nextGaussian()) * SPEED_VARIATION + SPEED_DEVIATION);
	}
	
	// Variaci�n de la direcci�n del viento
	private float windDirectionVariation() 
	{
		return (((float) alea.nextGaussian()) * DIRECTION_VARIATION + DIRECTION_DEVIATION);
	}	
		
	// Calcula la presi�n del viendo en funci�n de la velocidad
	// Pv = (Dv*V^2)/2 ~ 0.6*V^2
	public float computeWindPressure() 
	{
		return (0.6f*speed*speed);
	}
	
	// GETTERS //
	// Devuelve la velocidad actual del viento
	public float getSpeed() 
	{
		return speed;
	}
	
	// Devuelve la velocidad en Km/h
	public float getSpeedKmh()
	{
		return speed*3600.0f/1000.0f;
	}
	
	// Devuelve la direcci�n actual del viento
	public Angle getDirection() 
	{
		return direction;
	}
}