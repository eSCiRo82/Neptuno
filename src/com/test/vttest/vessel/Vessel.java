package com.test.vttest.vessel;

import com.test.vttest.physics_engine.VesselPhysics;
import com.test.vttest.physics_engine.maths.Angle;

public class Vessel {
	// Constantes del velero
	// Vela
	public static final float MAX_SAIL_ANGLE = 1.57f;	// Ángulo en radianes
	public static final float MIN_SAIL_ANGLE = -1.57f;	// Ángulo en radianes
	
	// NOTA: Configurar el velero con distintas velas, cascos y quillas puede suponer aliciente
	// Área de la vela
	public static float MAX_SAIL_AREA = 500.0f;	// Área de la vela [m^2]	
	// Peso del barco
	public static float VESSEL_WEIGHT = 800000.0f; // [Kg]	
	// Área de la quilla
	public static float MAX_KEEL_AREA = 50.0f;	// Área de la quilla [m^2]
	
	public static float MAX_VESSEL_ADVANCE_SPEED = 12.0f; 	// Máxima velocidad que puede alcanzar el velero [m/s]
	public static float MIN_VESSEL_ADVANCE_SPEED = -2.0f;	// El barco puede ir hacia atrás si no configura bien sus velas [m/s]
	public static float MAX_VESSEL_ROTATION_SPEED = 0.1f;
	public static float MIN_VESSEL_ROTATION_SPEED = -0.1f;
	
		
	// Estado del velero
	private VesselStates state;
	
	// Variables del velero	
	//private float sail_angle;		// Ángulo de la vela respecto de la horizontal del barco
	private Angle vessel_direction;	// Ángulo de la dirección del barco
									// Es el mismo ángulo que el de la quilla del barco
	
	// Componentes del velero
	private Sail sail;
	// Velocidades del velero
	private float advance_speed;	// Velocidad de avance del velero	[m/s]
	private float rotation_speed;	// Velocidad de giro del velero		[rad/s]
	
	// Carga del velero
	private Cargo cargo = null;	// Carga que transporta el barco
			
	public Vessel() 
	{
		init();	// Inicializamos el velero
	}
	
	public void init() 
	{
		//sail_angle = 0.0f;			// Tomamos siempre como 0 la horizontal de la pantalla
		vessel_direction = new Angle((float) Math.PI/2.0f);	// Perpendicular a la vela al inicio
		state = VesselStates.ANCHORAGE;	// El velero inicialmente está anclado
		cargo = new Cargo();	// Carga del barco
		sail = new Sail(); // Vela del barco
		advance_speed = 6.0f;
		rotation_speed = 0.0f;
	}
	
	// SETTERS //
	/*public void setSailAngle(float _sail_angle) 
	{
		sail_angle = _sail_angle;
		if (_sail_angle > MAX_SAIL_ANGLE)
		{
			sail_angle = MAX_SAIL_ANGLE;
		}
		else if (_sail_angle < MIN_SAIL_ANGLE)
		{
			sail_angle = MIN_SAIL_ANGLE;
		}
	}*/
	
	public void setAdvanceSpeed(float _as)
	{
		if (_as >= MIN_VESSEL_ADVANCE_SPEED && _as <= MAX_VESSEL_ADVANCE_SPEED) {
			advance_speed = _as;
		}
	}
	
	public void setRotationSpeed(float _rs) 
	{
		if (_rs >= MIN_VESSEL_ROTATION_SPEED && _rs <= MAX_VESSEL_ROTATION_SPEED)
		{
			rotation_speed = _rs;
		}
	}
	
	public void setVesselDirection(float _vessel_direction) 
	{
		vessel_direction.setAngle(_vessel_direction);
	}
	
	// TODO Hacer un updateVessel
	
	// GETTERS //
	// Devuelve el ángulo de la vela
	/*public float getSailAngle() 
	{
		return sail_angle;
	}
	
	public float getSailNormalAv() {
		return sail_angle - VesselPhysics.ANG_90;
	}
	
	public float getSailNormalRe() {
		return sail_angle + VesselPhysics.ANG_90;
	}*/
	
	// Devuelve la dirección del barco
	public Angle getVesselDirection() 
	{
		return vessel_direction;
	}
	
	public float getTotalWeight() 
	{
		return cargo.weight + VESSEL_WEIGHT;
	}
	
	public float getAdvanceSpeed()
	{
		return advance_speed;	// hay que calcularla
	}
	
	public float getAdvanceSpeedKmh()
	{
		return advance_speed*3600.0f/1000.0f;
	}
	
	public float getRotationSpeed()
	{
		return rotation_speed;
	}
		
	// Devuelve la carga que transporta el velero
	public Cargo getCargo()
	{
		return cargo;	
	}
	
	public Sail getSail()
	{
		return sail;
	}
}