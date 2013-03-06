package com.test.vttest.physics_engine;

import com.test.vttest.physics_engine.maths.Angle;
import com.test.vttest.physics_engine.ocean.Ocean;
import com.test.vttest.physics_engine.wind.Wind;
import com.test.vttest.vessel.Sail;
import com.test.vttest.vessel.Vessel;

public class VesselPhysics {
	public static final float ANG_180 = 3.14159f;	// Ajustamos PI a float (en Math es double)
	public static final float ANG_90 = ANG_180/2.0f;	// Ajustamos PI/2 a float (en Math es double)
	
	// Resistencia del casco al avance
	public static final float HULL_RESISTANCE_TO_ADVANCE = 10.0f;	// [N/Kg]

	// Checkea el caso de que los cuadrantes del viento y el barco sean el mismo o los opuestos
	private static float checkSameCuadrantCase(Wind _wind, Sail _sail)
	{
		// En el mismo cuadrante
		if (_wind.getDirection().getCuadrant() == _sail.getSailAngle().getCuadrant())
		{
			if (_wind.getDirection().getAngle() > _sail.getSailAngleValue())
			{
				return _sail.getSailAngle().getNormalAv();
			}
			else if (_wind.getDirection().getAngle() < _sail.getSailAngleValue())
			{
				return _sail.getSailAngle().getNormalRe();
			}
		}
		// En cuadrantes opuestos
		else
		{
			Angle angle_to_compare = new Angle(_sail.getSailAngleValue() + Angle.A180);
			if (_wind.getDirection().getAngle() > angle_to_compare.getAngle())
			{
				return _sail.getSailAngle().getNormalRe();
			}
			else if (_wind.getDirection().getAngle() < angle_to_compare.getAngle())
			{
				return _sail.getSailAngle().getNormalAv();
			}
		}
		return 0.0f;
	}
	
	// Calcula la fuerza ejercida por el viento en la vela
	public static VectorComponents computeWindForceOnSail(Wind _wind, Vessel _vessel) 
	{
		// Fuerza del viento sobre la vela
		//float force = 0.0f;	
		// Superficie efectiva de la vela
		//float effective_sail_surface = Math.abs(Vessel.MAX_SAIL_AREA*Math.cos(_sail_angle-_wind.getDirection()));
		
		// Fv-vl = Pv*Sevl
		//force = _wind.computeWindPressure()*effective_sail_surface;
		//force = _wind.computeWindPressure()*Vessel.MAX_SAIL_AREA;
		
		VectorComponents vc = new VectorComponents();
		vc.module = _wind.computeWindPressure()*Vessel.MAX_SAIL_AREA;
		// Para la dirección de la fuerza se toma como referencia de coordenadas el velero
		
		// NormalAv
		if ((_wind.getDirection().getCuadrant() == 1 && _vessel.getSail().getSailAngle().getCuadrant() == 2) ||
			(_wind.getDirection().getCuadrant() == 2 && _vessel.getSail().getSailAngle().getCuadrant() == 3) ||
			(_wind.getDirection().getCuadrant() == 3 && _vessel.getSail().getSailAngle().getCuadrant() == 4) ||
			(_wind.getDirection().getCuadrant() == 4 && _vessel.getSail().getSailAngle().getCuadrant() == 1))
		{
			vc.direction = _vessel.getSail().getSailAngle().getNormalRe();
		}
		// NormalAv
		else if (	(_wind.getDirection().getCuadrant() == 2 && _vessel.getSail().getSailAngle().getCuadrant() == 1) ||
					(_wind.getDirection().getCuadrant() == 3 && _vessel.getSail().getSailAngle().getCuadrant() == 2) ||
					(_wind.getDirection().getCuadrant() == 4 && _vessel.getSail().getSailAngle().getCuadrant() == 3) ||
					(_wind.getDirection().getCuadrant() == 1 && _vessel.getSail().getSailAngle().getCuadrant() == 4))
		{
			vc.direction = _vessel.getSail().getSailAngle().getNormalAv();
		}
		else
		{
			vc.direction = checkSameCuadrantCase(_wind, _vessel.getSail());
		}
		return vc;
	}
	
	// Calcula la fuerza ejercida por la corriente marina en la quilla
	public static VectorComponents computeOceanForceOnKeel(Ocean _ocean, Vessel _vessel)
	{
		//float force = 0.0f;
		//force = _ocean.computeOceanPressure()*Vessel.MAX_KEEL_AREA;
		
		VectorComponents vc = new VectorComponents();
		vc.module = _ocean.computeOceanPressure()*Vessel.MAX_KEEL_AREA/**Math.abs((float) Math.sin(_ocean.getDirection() - _vessel.getVesselDirection()))*/;
		// Para la dirección de la fuerza se toma como referencia de coordenadas el velero
		vc.direction = _ocean.getDirection().getAngle() /*- _vessel.getVesselDirection()*/;
		return vc;
	}
	
	// Calcula la fuerza de resistencia del mar sobre el casco del velero
	public static VectorComponents computeAdvanceDragForce(Ocean _ocean, Vessel _vessel) 
	{
		VectorComponents vc = new VectorComponents();
		//vc.module = HULL_RESISTANCE_TO_ADVANCE*_vessel.getTotalWeight();
		// Foc = 0.2*da*v*v*A;
		vc.module = 0.206f*_vessel.getAdvanceSpeed()*_vessel.getAdvanceSpeed()*40.0f; // Establecer área frontal del casco
		// Para la dirección de la fuerza se toma como referencia de coordenadas el velero
		vc.direction = -_vessel.getVesselDirection().getAngle() /*+ ANG_180- _vessel.getVesselDirection()*/;
		return vc;
	}
	
	// Calcula la fuerza de resistencia del mar sobre el casco del velero al girar
		public static VectorComponents computeRotationDragForce(Ocean _ocean, Vessel _vessel) 
		{
			VectorComponents vc = new VectorComponents();
			//vc.module = HULL_RESISTANCE_TO_ADVANCE*_vessel.getTotalWeight();
			// Foc = 0.2*da*v*v*A;
			vc.module = 0.206f*_vessel.getRotationSpeed()*_vessel.getRotationSpeed()*200.0f; // Establecer área lateral del casco
			// Para la dirección de la fuerza se toma como referencia de coordenadas el velero
			if (_vessel.getRotationSpeed() > 0)
				vc.direction = _vessel.getVesselDirection().getNormalRe() /*+ ANG_180- _vessel.getVesselDirection()*/;
			else 
				vc.direction = _vessel.getVesselDirection().getNormalAv();
			return vc;
		}
	
	public static VectorComponents computeForceOnVessel(VectorComponents _fws, VectorComponents _foh, VectorComponents _fok)
	{
		VectorComponents Ft = new VectorComponents(_fws.x_comp() + _foh.x_comp() + _fok.x_comp(), _fws.y_comp() + _foh.y_comp() + _fok.y_comp()); 
		return Ft;
	}
	
	// Calcula la fuerza total aplicada sobre el velero
	public static VectorComponents computeForceOnVessel(Wind _wind, Ocean _ocean, Vessel _vessel)
	{
		VectorComponents Ft;
		VectorComponents Fws = computeWindForceOnSail(_wind, _vessel);
		VectorComponents Foh = computeAdvanceDragForce(_ocean, _vessel);
		VectorComponents Fok = computeOceanForceOnKeel(_ocean, _vessel);
		
		/*
		float xch = Foh.x_comp();
		float ych = Foh.y_comp();
		float xcw = Fws.x_comp();
		float ycw = Fws.y_comp();
		float xck = Fok.x_comp();
		float yck = Fok.y_comp();
		*/
		Ft = new VectorComponents(Fws.x_comp() + Foh.x_comp() + Fok.x_comp(), Fws.y_comp() + Foh.y_comp() + Fok.y_comp()); 
		
		return Ft;
	}
	
	public static VectorComponents computeAdvanceAcceleration(VectorComponents _force, Vessel _vessel)
	{
		VectorComponents acc = new VectorComponents();
		acc.module = _force.module/Vessel.VESSEL_WEIGHT;
		acc.direction = _force.direction - _vessel.getVesselDirection().getAngle();
		return acc;
	}
	
	// Para hacer el cálculo de la velocidad de avance y de rotación se debe tomar como eje de coordenadas
	// de referencia el eje del barco
	public static void computeVesselAdvanceSpeed(Vessel _vessel, VectorComponents _acc, float _delay)
	{	
		_vessel.setAdvanceSpeed(_vessel.getAdvanceSpeed() + _acc.x_comp()*_delay);		
	}
	
	public static void computeVesselRotationSpeed(Vessel _vessel, VectorComponents _racc, VectorComponents _rdf, float _delay) 
	{
		float acc = _rdf.module/Vessel.VESSEL_WEIGHT;
		float accy  =_racc.y_comp();
		if (_rdf.direction - _vessel.getVesselDirection().getAngle() < 0) {
			acc = -acc;
		}
		
		_vessel.setRotationSpeed(_vessel.getRotationSpeed() + (_racc.y_comp() + acc)*_delay);
	}
}