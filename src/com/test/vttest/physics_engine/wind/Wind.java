package com.test.vttest.physics_engine.wind;

import java.util.Random;

import com.test.vttest.physics_engine.maths.Angle;

public class Wind 
{
	public static final float MAX_WIND_SPEED = 30.0f; // Velocidad en m/s 	-> 108 km/h
	public static final float MIN_WIND_SPEED = 1.0f; // Velocidad en m/s	-> 3.6 km/h
	// Velocidad media
	public static final float CENTER_SPEED = 15.5f;
	// Máxima diferencia de velocidad
	public static final float MAX_DIFFERENCE = 14.5f;
	// La variación aleatoria de velocidad puede desviarse del centro entre [-0.5,0.5]
	// El símbolo es negativo pues queremos que de una prioridad inversa según el límite
	// al que se aproxime
	public static final float MAX_SPEED_DEVIATION = -0.5f;
	
	// NOTA: estas constantes podrían ser usadas para establecer la dificultad del juego :)
	public static float SPEED_DEVIATION = 0.0f;	// Desviación de la distribución normal												
	public static final float SPEED_VARIATION = 0.25f;	// Variación de la distribución normal
	
	public static final float DIRECTION_DEVIATION = 0.0f;
	public static final float DIRECTION_VARIATION = 0.05f;
	
	private Random alea = null;
	
	private Angle direction;	// Dirección del viento	[0-2PI] [rad]
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
		
	// Actualiza el viento en un valor aleatorio entre el máximo y mínimo establecidos
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
	
	// Realiza una selección de un valor aleatorio con distribución normal donde 
	// se tienen una variación y una desviación distintas de 1 y 0 respectivamente
	// Variación de la velocidad del viento
	private float windSpeedVariation() 
	{
		return (((float) alea.nextGaussian()) * SPEED_VARIATION + SPEED_DEVIATION);
	}
	
	// Variación de la dirección del viento
	private float windDirectionVariation() 
	{
		return (((float) alea.nextGaussian()) * DIRECTION_VARIATION + DIRECTION_DEVIATION);
	}	
		
	// Calcula la presión del viendo en función de la velocidad
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
	
	// Devuelve la dirección actual del viento
	public Angle getDirection() 
	{
		return direction;
	}
}