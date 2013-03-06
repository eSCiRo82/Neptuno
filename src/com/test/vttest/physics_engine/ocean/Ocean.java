package com.test.vttest.physics_engine.ocean;
import java.util.Random;

import com.test.vttest.physics_engine.maths.Angle;

public class Ocean {
	public static final float MAX_OCEAN_SPEED = 5.0f; // Velocidad en m/s 	-> 18 km/h
	public static final float MIN_OCEAN_SPEED = 0.1f; // Velocidad en m/s	-> 0.36 km/h
	
	// Velocidad media
	public static final float CENTER_SPEED = 2.55f;
	// Máxima diferencia de velocidad
	public static final float MAX_DIFFERENCE = 2.45f;
	// La variación aleatoria de velocidad puede desviarse del centro entre [-0.5,0.5]
	// El símbolo es negativo pues queremos que de una prioridad inversa según el límite
	// al que se aproxime
	public static final float MAX_SPEED_DEVIATION = -0.5f;
	
	// NOTA: estas constantes podrían ser usadas para establecer la dificultad del juego :)
	public static float SPEED_DEVIATION = 0.0f;	// Desviación de la distribución normal												
	public static final float SPEED_VARIATION = 0.005f;	// Variación de la distribución normal
	
	public static final float DIRECTION_DEVIATION = 0.0f;
	public static final float DIRECTION_VARIATION = 0.002f;
	
	private Random alea = null;
	
	private float speed;		// [m/s]
	private Angle direction;	// Dirección de la corriente marina [0-2PI] [rad]
	
	public Ocean() 
	{
		alea = new Random(System.currentTimeMillis());
		speed = (MIN_OCEAN_SPEED + MAX_OCEAN_SPEED)/2.0f;
		direction = new Angle();
	}
	
	public Ocean(float _init_speed, float _init_direction)
	{
		alea = new Random(System.currentTimeMillis());
		speed = _init_speed;
		direction = new Angle(_init_direction);
	}
	
	// Actualiza el viento en un valor aleatorio entre el máximo y mínimo establecidos
	public void update() 
	{
		float variation = oceanSpeedVariation();
		
		speed += variation;
		if (speed < MIN_OCEAN_SPEED) 
		{
			speed = MIN_OCEAN_SPEED;
		} 
		else if (speed > MAX_OCEAN_SPEED) 
		{
			speed = MAX_OCEAN_SPEED;
		}
		
		SPEED_DEVIATION = MAX_SPEED_DEVIATION*(speed - CENTER_SPEED)/MAX_DIFFERENCE;
		
		direction.inc(oceanDirectionVariation());
	}
	
	// Realiza una selección de un valor aleatorio con distribución normal donde 
	// se tienen una variación y una desviación distintas de 1 y 0 respectivamente
	private float oceanSpeedVariation() 
	{
		return (((float) alea.nextGaussian()) * SPEED_VARIATION + SPEED_DEVIATION);
	}
	
	// Variación de la dirección de la corriente marina
	private float oceanDirectionVariation() 
	{
		return (((float) alea.nextGaussian()) * DIRECTION_VARIATION + DIRECTION_DEVIATION);
	}
	
	// Calcula la presión del océano
	// Po = (Cl*Do*Vo^2)/2 ~ 0.31*Vo^2
	public float computeOceanPressure() 
	{
		return (0.31f*speed*speed);
	}
	
	// GETTERS //
	// Devuelve la velocidad actual
	public float getSpeed() 
	{
		return speed;
	}
	
	// Devuelve la velocidad en Km/h
	public float getSpeedKmh()
	{
		return speed*3600.0f/1000.0f;
	}
	
	// Devuelve la dirección actual de la corriente marina
	public Angle getDirection() 
	{
		return direction;
	}
}