package com.test.vttest;

import java.util.Random;

import com.test.vttest.physics_engine.VectorComponents;
import com.test.vttest.physics_engine.VesselPhysics;
import com.test.vttest.physics_engine.ocean.Ocean;
import com.test.vttest.physics_engine.wind.Wind;
import com.test.vttest.vessel.Vessel;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class VTTestThread extends Thread {
	// Constantes para mensajes
	public static final String WIND_SPEED = "wind_speed";
	public static final String WIND_DIRECTION = "wind_direction";
	public static final String OCEAN_SPEED = "ocean_speed";
	public static final String OCEAN_DIRECTION = "ocean_direction";
	public static final String WIND_ON_SAIL_FORCE_MODULE = "wind_on_sail_force_module";
	public static final String WIND_ON_SAIL_FORCE_DIRECTION = "wind_on_sail_force_direction";
	public static final String OCEAN_ON_HULL_FORCE_MODULE = "ocean_on_hull_force_module";
	public static final String OCEAN_ON_HULL_FORCE_DIRECTION = "ocean_on_hull_force_direction";
	public static final String OCEAN_ON_KEEL_FORCE_MODULE = "ocean_on_keel_force_module";
	public static final String OCEAN_ON_KEEL_FORCE_DIRECTION = "ocean_on_keel_force_direction";
	public static final String FORCE_ON_VESSEL_MODULE = "force_on_vessel_module";
	public static final String FORCE_ON_VESSEL_DIRECTION = "force_on_vessel_direction";
	public static final String SAIL_ANGLE = "sail_angle";
	public static final String VESSEL_DIRECTION = "vessel_direction";
	public static final String VESSEL_ADVANCE_SPEED = "vessel_advance_speed";
	public static final String VESSEL_ROTATION_SPEED = "vessel_rotation_speed";
	public static final String DELAY = "delay";
	
	// Constantes
	private static final float TURN_SPEED = 0.1f;
	private static final float WEATHER_UPDATE_TIME = 5000;
	
	// Control de tiempo
	private static final long TIME_PER_FRAME = 125; // Tiempo en milisegundos de cada frame -> 8fps
	
	private Handler handler;
	private Wind wind;
	private Ocean ocean;
	private Vessel vessel;
	public boolean running;
	public long current_time;
	public long last_time;
	public long weather_update_delay;
	
	public VTTestThread(Handler _handler)
	{
		handler = _handler;
		running = false;
	}
	
	public void run() 
	{
		Random alea = new Random(System.currentTimeMillis());
		wind = new Wind(Wind.CENTER_SPEED + (alea.nextInt()%2)*alea.nextFloat()*Wind.MAX_DIFFERENCE, 2*(float) Math.PI*(alea.nextFloat() - 0.5f));
		ocean = new Ocean(Ocean.CENTER_SPEED + (alea.nextInt()%2)*alea.nextFloat()*Ocean.MAX_DIFFERENCE, 2*(float) Math.PI*(alea.nextFloat() - 0.5f));
		vessel = new Vessel();
		
		current_time = System.currentTimeMillis();
		last_time = System.currentTimeMillis();
		weather_update_delay = 0;
		
		while (running)
		{	
			current_time = System.currentTimeMillis();
			
			long delay_ms = current_time - last_time;
			float delay_s = (float) delay_ms/1000.0f;		
			weather_update_delay += delay_ms;
			last_time = System.currentTimeMillis();
			
			if (weather_update_delay >= WEATHER_UPDATE_TIME)
			{		
				// Actualizamos el viento
				wind.update();
				// Actualizamos el océano
				ocean.update();
				weather_update_delay -= WEATHER_UPDATE_TIME;
			}
			
			Bundle b = new Bundle();
			b.putFloat(WIND_SPEED, wind.getSpeedKmh());
			b.putFloat(WIND_DIRECTION, wind.getDirection().getAngle());
			b.putFloat(OCEAN_SPEED, ocean.getSpeedKmh());
			b.putFloat(OCEAN_DIRECTION, ocean.getDirection().getAngle());
			b.putFloat(SAIL_ANGLE, vessel.getSail().getSailAngleValue());
			b.putFloat(VESSEL_DIRECTION, vessel.getVesselDirection().getAngle());
			
			VectorComponents vcw = VesselPhysics.computeWindForceOnSail(wind, vessel);
			b.putFloat(WIND_ON_SAIL_FORCE_MODULE, vcw.module);
			b.putFloat(WIND_ON_SAIL_FORCE_DIRECTION, vcw.direction);
			
			VectorComponents vch = VesselPhysics.computeAdvanceDragForce(ocean, vessel);
			b.putFloat(OCEAN_ON_HULL_FORCE_MODULE, vch.module);
			b.putFloat(OCEAN_ON_HULL_FORCE_DIRECTION, vch.direction);
			
			VectorComponents vck = VesselPhysics.computeOceanForceOnKeel(ocean, vessel);
			b.putFloat(OCEAN_ON_KEEL_FORCE_MODULE, vck.module);
			b.putFloat(OCEAN_ON_KEEL_FORCE_DIRECTION, vck.direction);
			
			//VectorComponents vcv = VesselPhysics.computeForceOnVessel(wind, ocean, vessel);
			VectorComponents vcv = VesselPhysics.computeForceOnVessel(vcw, vch, vck);
			b.putFloat(FORCE_ON_VESSEL_MODULE, vcv.module);
			b.putFloat(FORCE_ON_VESSEL_DIRECTION, vcv.direction);
			
			VectorComponents acc = VesselPhysics.computeAdvanceAcceleration(vcv, vessel);
			
			VesselPhysics.computeVesselAdvanceSpeed(vessel, acc, delay_s);
			b.putFloat(VESSEL_ADVANCE_SPEED, vessel.getAdvanceSpeed());
			
			VesselPhysics.computeVesselRotationSpeed(vessel, acc, VesselPhysics.computeRotationDragForce(ocean, vessel), delay_s);
			b.putFloat(VESSEL_ROTATION_SPEED, vessel.getRotationSpeed());			

			vessel.setVesselDirection(vessel.getVesselDirection().getAngle() + vessel.getRotationSpeed()*delay_s);			
			vessel.getSail().setSailAngle(vessel.getSail().getSailAngleValue() + vessel.getRotationSpeed()*delay_s);
			b.putFloat(VESSEL_DIRECTION, vessel.getVesselDirection().getAngle());
			
			b.putFloat(DELAY, delay_s);
			
			// Enviamos la información a la actividad para que la muestre
			Message msg = handler.obtainMessage();		
			msg.setData(b);
			handler.sendMessage(msg);
			
			if (delay_ms < TIME_PER_FRAME) {
				try {
					Thread.sleep(TIME_PER_FRAME - delay_ms);
				} catch(Exception e) {
				
				}
			}
		}
	}
	
	public void turnSailLeft()
	{
		synchronized(vessel)
		{
			//vessel.setSailAngle(vessel.getSailAngle() - TURN_SPEED);
			vessel.getSail().getSailAngle().dec(TURN_SPEED);
		}
	}
	
	public void turnSailRight()
	{
		synchronized(vessel)
		{
			//vessel.setSailAngle(vessel.getSailAngle() + TURN_SPEED);
			vessel.getSail().getSailAngle().inc(TURN_SPEED);
		}
	}
}
