package com.test.vttest.graphics_engine.views;

import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.test.vttest.physics_engine.VectorComponents;
import com.test.vttest.physics_engine.maths.Angle;

public class ForcesView extends ImageView {
	private static final float MAX_FORCE = 150000.0f;
	private static float NEWTON_PER_PIXEL = 120.0f;
	
	private static float TILE_SIZE;	
	
	private VectorComponents force_on_vessel;
	private VectorComponents wind_on_sail_force;
	private VectorComponents ocean_on_hull_force;
	private VectorComponents ocean_on_keel_force;
	
	private float sail_angle = 0.0f;
	private float mid_sail_size = 25.0f;
	private float vessel_direction = (float) Math.PI/2.0f;
	private float mid_vessel_size = 50.0f;
	
	
	// Punto de referencia del grid
	private float xg_ref, yg_ref;
	
	private int h, w;
	private float xc, yc;
	private Paint paint;
	private float vessel_speed;
	private float delay;
	private boolean init;
	private Random alea;
	
	public ForcesView(Context _context, AttributeSet _attrs) {
		super(_context, _attrs);
		
		alea = new Random(System.currentTimeMillis());
		
		force_on_vessel = new VectorComponents();
		wind_on_sail_force = new VectorComponents();
		ocean_on_hull_force = new VectorComponents();
		ocean_on_keel_force = new VectorComponents();	 
		
		vessel_speed = 0.0f;
		
		paint = new Paint();
		paint.setStrokeWidth(1.0f);
		paint.setStyle(Paint.Style.STROKE);
		xg_ref = getWidth()/2.0f;
		yg_ref = getHeight()/2.0f;
		init = false;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (!init) {
			h = getHeight();
			w = getWidth();
			
			if (h > w) {
				NEWTON_PER_PIXEL = MAX_FORCE/h;
				TILE_SIZE = h/10.0f;
			} else {
				NEWTON_PER_PIXEL = MAX_FORCE/w;
				TILE_SIZE = w/10.0f;
			}
			
			NEWTON_PER_PIXEL *= 2.0f;		
			
			xg_ref = xc = (float) w/2.0f;
			yg_ref = yc = (float) h/2.0f;
			init = true;
		}
			
		drawGrid(canvas, delay);
		drawVessel(canvas);
		drawSail(canvas);		
		
		if (force_on_vessel != null) {
			drawVector(canvas, force_on_vessel, 0xFFFFCC00);
		}
		
		if (wind_on_sail_force != null) {
			drawVector(canvas, wind_on_sail_force, 0xFFFF00FF);
		}
		
		if (ocean_on_hull_force != null) {
			drawVector(canvas, ocean_on_hull_force, 0xFF00FF00);
		}
		
		if (ocean_on_keel_force != null) {
			drawVector(canvas, ocean_on_keel_force, 0xFFFFFF00);
		}		
	}

	private void drawGrid(Canvas canvas, float _delay)
	{
		Angle ang = new Angle(vessel_direction + Angle.A180);
		float speed = vessel_speed*_delay;
		xg_ref += (speed*(float) Math.cos(ang.getAngle()));
		yg_ref += (speed*(float) Math.sin(ang.getAngle()));
		
		if (xg_ref < 0.0f) {
			xg_ref = w;
		} 
		else if (xg_ref > w)
		{
			xg_ref = 0.0f;
		}
		
		if (yg_ref < 0.0f) {
			yg_ref = h;
		} 
		else if (yg_ref > h)
		{
			yg_ref = 0.0f;
		}
		/*if (xg_ref > TILE_SIZE)
		{
			xg_ref -= TILE_SIZE;
		} else if (xg_ref < -TILE_SIZE)
		{
			xg_ref += TILE_SIZE;
		}
		
		if (yg_ref > TILE_SIZE)
		{
			yg_ref -= TILE_SIZE;
		} else if (yg_ref < -TILE_SIZE)
		{
			yg_ref += TILE_SIZE;
		}*/
		
		float xi = xg_ref - TILE_SIZE;
		float yi = yg_ref - TILE_SIZE;
		
		paint.setColor(0xFFFFFFFF);
		paint.setStrokeWidth(1.0f);
		paint.setStyle(Paint.Style.FILL);
		
		/*while (yi < h)
		{
			float yf = yi + TILE_SIZE;
			float xiaux = xi;
			while (xiaux < w)
			{
				float xf = xiaux + TILE_SIZE;
				int color = 0xFF000000;
				color += (alea.nextInt(128) + 128);				
				paint.setColor(color);
				canvas.drawRect(xiaux, yi, xf, yf, paint);
				xiaux = xf;
			}
			yi = yf;
		}*/
		
		RectF ref = new RectF(xg_ref - 15.0f, yg_ref - 15.0f, xg_ref + 15.0f, yg_ref + 15.0f);
		canvas.drawArc(ref, 0.0f, 360.0f, false, paint);
		
		paint.setStyle(Paint.Style.STROKE);
				
		paint.setStrokeWidth(5.0f);
		paint.setColor(0xFF0000FF);
		
		float xe = xc + 100.0f*(float) Math.cos(ang.getAngle());
		float ye = xc + 100.0f*(float) Math.sin(ang.getAngle());
		canvas.drawLine(xc, yc, xe, ye, paint);
	}
	
	private void drawVector(Canvas canvas, VectorComponents _vector, int _color)
	{
		paint.setColor(_color);
		paint.setStrokeWidth(3.0f);
		//float l = _vector.module/NEWTON_PER_PIXEL;
				
		float xe = xc + _vector.x_comp()/NEWTON_PER_PIXEL;
		float ye = yc + _vector.y_comp()/NEWTON_PER_PIXEL;
		
		canvas.drawLine(xc, yc, xe, ye, paint);
		canvas.drawLine(xc, yc, xe, yc, paint);
		canvas.drawLine(xc, yc, xc, ye, paint);
	}
	
	private void drawSail(Canvas canvas)
	{	
		paint.setStrokeWidth(6.0f);
		paint.setColor(0xFFFFFFFF);
		float xcomp = mid_sail_size*(float) Math.cos(sail_angle);
		float ycomp = mid_sail_size*(float) Math.sin(sail_angle);
		float x1 = xc - xcomp;
		float y1 = yc - ycomp;
		float x2 = xc + xcomp;
		float y2 = yc + ycomp;
		canvas.drawLine(x1, y1, x2, y2, paint);
	}
	
	private void drawVessel(Canvas canvas)
	{	
		paint.setStrokeWidth(25.0f);
		paint.setColor(0xFFFF0000);
		float xcomp = mid_vessel_size*(float) Math.cos(vessel_direction);
		float ycomp = mid_vessel_size*(float) Math.sin(vessel_direction);
		float x1 = xc - xcomp;
		float y1 = yc - ycomp;
		float x2 = xc + xcomp;
		float y2 = yc + ycomp;
		canvas.drawLine(x1, y1, x2, y2, paint);
		paint.setStrokeWidth(10.0f);
		paint.setColor(0xFFFFFFFF);
		canvas.drawLine(xc, yc, x2, y2, paint);
		//canvas.drawLine(xc, yc, x2, y2, paint);
	}
	
	public void setForceOnVessel(VectorComponents _v)
	{
		if (force_on_vessel != null) { 
			force_on_vessel.module = _v.module;
			force_on_vessel.direction = _v.direction;
			//this.invalidate();
		}
	}
	
	public void setWindOnSailForce(VectorComponents _v)
	{
		if (wind_on_sail_force != null) {
			wind_on_sail_force.module = _v.module;
			wind_on_sail_force.direction = _v.direction;
			//this.invalidate();
		}
	}
	
	public void setOceanOnHullForce(VectorComponents _v)
	{
		if (ocean_on_hull_force != null) {
			ocean_on_hull_force.module = _v.module;
			ocean_on_hull_force.direction = _v.direction;
			//this.invalidate();
		}
	}
	
	public void setOceanOnKeelForce(VectorComponents _v)
	{
		if (ocean_on_keel_force != null) {
			ocean_on_keel_force.module = _v.module;
			ocean_on_keel_force.direction = _v.direction;
			//this.invalidate();
		}
	}
	
	public void setSailAngle(float _sail_angle)
	{
		sail_angle = _sail_angle;
		//this.invalidate();
	}
	
	public void setVesselDirection(float _vessel_direction)
	{
		vessel_direction = _vessel_direction;
		//this.invalidate();
	}
	
	public void setVesselSpeed(float _vessel_speed)
	{
		vessel_speed = _vessel_speed;
		//this.invalidate();
	}
	
	public void setDelay(float _delay)
	{
		delay = _delay;
		//this.invalidate();
	}
}
