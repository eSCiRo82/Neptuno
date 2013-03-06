package com.test.vttest;

import java.text.DecimalFormat;

import com.example.vttest.R;
import com.test.vttest.graphics_engine.views.DirectionWheelView;
import com.test.vttest.graphics_engine.views.ForcesView;
import com.test.vttest.physics_engine.VectorComponents;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class VTTestMainActivity extends Activity {
	private VectorComponents force_on_vessel;
	private VectorComponents wind_on_sail_force;
	private VectorComponents ocean_on_hull_force;
	private VectorComponents ocean_on_keel_force;
	private Handler handler;
	private VTTestThread thread;
	
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
		
		setContentView(R.layout.activity_vttest_main);
		
		force_on_vessel = new VectorComponents();
		wind_on_sail_force = new VectorComponents();
		ocean_on_hull_force = new VectorComponents();
		ocean_on_keel_force = new VectorComponents();
		
		final Button left_sail_button = (Button) this.findViewById(R.id.id_sail_left);
		left_sail_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				thread.turnSailLeft();
			}
		});
		
		final Button right_sail_button = (Button) this.findViewById(R.id.id_sail_right);
		right_sail_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				thread.turnSailRight();
			}
		});
		
		final TextView wind_speed = (TextView) this.findViewById(R.id.id_wind_speed);
		final TextView wind_direction = (TextView) this.findViewById(R.id.id_wind_direction);
		final TextView ocean_speed = (TextView) this.findViewById(R.id.id_ocean_speed);
		final TextView ocean_direction = (TextView) this.findViewById(R.id.id_ocean_direction);
		
		final TextView wind_on_sail_force_module = (TextView) this.findViewById(R.id.id_wind_on_sail_force_module);
		final TextView wind_on_sail_force_direction = (TextView) this.findViewById(R.id.id_wind_on_sail_force_direction);
		final TextView ocean_on_hull_force_module = (TextView) this.findViewById(R.id.id_ocean_on_hull_force_module);
		final TextView ocean_on_hull_force_direction = (TextView) this.findViewById(R.id.id_ocean_on_hull_force_direction);
		final TextView ocean_on_keel_force_module = (TextView) this.findViewById(R.id.id_ocean_on_keel_force_module);
		final TextView ocean_on_keel_force_direction = (TextView) this.findViewById(R.id.id_ocean_on_keel_force_direction);
		final TextView force_on_vessel_module = (TextView) this.findViewById(R.id.id_force_on_vessel_module);
		final TextView force_on_vessel_direction = (TextView) this.findViewById(R.id.id_force_on_vessel_direction);
		final TextView vessel_advance_speed = (TextView) this.findViewById(R.id.id_vessel_advance_speed);
		final TextView vessel_rotation_speed = (TextView) this.findViewById(R.id.id_vessel_rotation_speed);
		
		final DirectionWheelView wind_wheel = (DirectionWheelView) this.findViewById(R.id.id_wind_wheel);
		final DirectionWheelView ocean_wheel = (DirectionWheelView) this.findViewById(R.id.id_ocean_wheel);
		final ForcesView forces = (ForcesView) this.findViewById(R.id.id_forces);
		
		final DecimalFormat df = new DecimalFormat("0.000");
		final Resources res = getResources();
		
		wind_speed.setText(res.getString(R.string.parameter_base, res.getString(R.string.wind_speed), df.format(0.0f), res.getString(R.string.speed_units)));
		wind_direction.setText(res.getString(R.string.parameter_base, res.getString(R.string.wind_direction), df.format(0.0f), res.getString(R.string.direction_units)));
		ocean_speed.setText(res.getString(R.string.parameter_base, res.getString(R.string.ocean_speed), df.format(0.0f), res.getString(R.string.speed_units)));
		ocean_direction.setText(res.getString(R.string.parameter_base, res.getString(R.string.ocean_direction), df.format(0.0f), res.getString(R.string.direction_units)));
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.getData().containsKey(VTTestThread.WIND_SPEED)) {
					wind_speed.setText(res.getString(	R.string.parameter_base, 
														res.getString(R.string.wind_speed), 
														df.format(msg.getData().getFloat(VTTestThread.WIND_SPEED)), 
														res.getString(R.string.speed_units)));					
				}
				
				if (msg.getData().containsKey(VTTestThread.WIND_DIRECTION)) {
					wind_direction.setText(res.getString(	R.string.parameter_base, 
															res.getString(R.string.wind_direction), 
															df.format(msg.getData().getFloat(VTTestThread.WIND_DIRECTION)), 
															res.getString(R.string.direction_units)));
					wind_wheel.setAngle(msg.getData().getFloat(VTTestThread.WIND_DIRECTION));
				}

				if (msg.getData().containsKey(VTTestThread.OCEAN_SPEED)) {
					ocean_speed.setText(res.getString(	R.string.parameter_base, 
														res.getString(R.string.ocean_speed), 
														df.format(msg.getData().getFloat(VTTestThread.OCEAN_SPEED)), 
														res.getString(R.string.speed_units)));
				}

				if (msg.getData().containsKey(VTTestThread.OCEAN_DIRECTION)) {
					ocean_direction.setText(res.getString(	R.string.parameter_base, 
															res.getString(R.string.ocean_direction), 
															df.format(msg.getData().getFloat(VTTestThread.OCEAN_DIRECTION)), 
															res.getString(R.string.direction_units)));
					ocean_wheel.setAngle(msg.getData().getFloat(VTTestThread.OCEAN_DIRECTION));
				}
				
				if (	msg.getData().containsKey(VTTestThread.WIND_ON_SAIL_FORCE_MODULE) &&
						msg.getData().containsKey(VTTestThread.WIND_ON_SAIL_FORCE_DIRECTION)) {
					wind_on_sail_force.module = msg.getData().getFloat(VTTestThread.WIND_ON_SAIL_FORCE_MODULE);
					wind_on_sail_force.direction = msg.getData().getFloat(VTTestThread.WIND_ON_SAIL_FORCE_DIRECTION);
					forces.setWindOnSailForce(wind_on_sail_force);
					wind_on_sail_force_module.setText(	res.getString(	R.string.parameter_base, 
													res.getString(R.string.wind_on_sail_force_module), 
													df.format(wind_on_sail_force.module), 
													res.getString(R.string.force_units)));					
					wind_on_sail_force_direction.setText(	res.getString(	R.string.parameter_base, 
															res.getString(R.string.wind_on_sail_force_direction), 
															df.format(wind_on_sail_force.direction), 
															res.getString(R.string.direction_units)));
				}
				
				if (	msg.getData().containsKey(VTTestThread.OCEAN_ON_HULL_FORCE_MODULE) &&
						msg.getData().containsKey(VTTestThread.OCEAN_ON_HULL_FORCE_DIRECTION)) {
					ocean_on_hull_force.module = msg.getData().getFloat(VTTestThread.OCEAN_ON_HULL_FORCE_MODULE);
					ocean_on_hull_force.direction = msg.getData().getFloat(VTTestThread.OCEAN_ON_HULL_FORCE_DIRECTION);
					forces.setOceanOnHullForce(ocean_on_hull_force);
					ocean_on_hull_force_module.setText(	res.getString(	R.string.parameter_base, 
														res.getString(R.string.ocean_on_hull_force_module), 
														df.format(ocean_on_hull_force.module), 
														res.getString(R.string.force_units)));
					ocean_on_hull_force_direction.setText(	res.getString(R.string.parameter_base, 
															res.getString(R.string.ocean_on_hull_force_direction), 
															df.format(ocean_on_hull_force.direction), 
															res.getString(R.string.direction_units)));
				}
				
				if (	msg.getData().containsKey(VTTestThread.OCEAN_ON_KEEL_FORCE_MODULE) &&
						msg.getData().containsKey(VTTestThread.OCEAN_ON_KEEL_FORCE_DIRECTION)) {
					ocean_on_keel_force.module = msg.getData().getFloat(VTTestThread.OCEAN_ON_KEEL_FORCE_MODULE);
					ocean_on_keel_force.direction = msg.getData().getFloat(VTTestThread.OCEAN_ON_KEEL_FORCE_DIRECTION);
					forces.setOceanOnKeelForce(ocean_on_keel_force);
					ocean_on_keel_force_module.setText(	res.getString(	R.string.parameter_base, 
														res.getString(R.string.ocean_on_keel_force_module), 
														df.format(msg.getData().getFloat(VTTestThread.OCEAN_ON_KEEL_FORCE_MODULE)), 
														res.getString(R.string.force_units)));
					ocean_on_keel_force_direction.setText(	res.getString(R.string.parameter_base, 
															res.getString(R.string.ocean_on_keel_force_direction), 
															df.format(ocean_on_keel_force.direction), 
															res.getString(R.string.direction_units)));
				}
				
				if (	msg.getData().containsKey(VTTestThread.FORCE_ON_VESSEL_MODULE) &&
						msg.getData().containsKey(VTTestThread.FORCE_ON_VESSEL_DIRECTION)) {
					force_on_vessel.module = msg.getData().getFloat(VTTestThread.FORCE_ON_VESSEL_MODULE);
					force_on_vessel.direction = msg.getData().getFloat(VTTestThread.FORCE_ON_VESSEL_DIRECTION);
					forces.setForceOnVessel(force_on_vessel);
					force_on_vessel_module.setText(	res.getString(	R.string.parameter_base, 
														res.getString(R.string.force_on_vessel_module), 
														df.format(force_on_vessel.module), 
														res.getString(R.string.force_units)));
					force_on_vessel_direction.setText(	res.getString(R.string.parameter_base, 
															res.getString(R.string.force_on_vessel_direction), 
															df.format(force_on_vessel.direction), 
															res.getString(R.string.direction_units)));
				}
				
				if (	msg.getData().containsKey(VTTestThread.SAIL_ANGLE)) {
					forces.setSailAngle(msg.getData().getFloat(VTTestThread.SAIL_ANGLE));
				}
				
				if (	msg.getData().containsKey(VTTestThread.VESSEL_DIRECTION)) {
					forces.setVesselDirection(msg.getData().getFloat(VTTestThread.VESSEL_DIRECTION));
				}
				
				if (	msg.getData().containsKey(VTTestThread.VESSEL_ADVANCE_SPEED)) {
					vessel_advance_speed.setText(	res.getString(	R.string.parameter_base, 
											res.getString(R.string.vessel_advance_speed), 
											df.format(msg.getData().getFloat(VTTestThread.VESSEL_ADVANCE_SPEED)), 
											res.getString(R.string.speed_units_ms)));
					forces.setVesselSpeed(msg.getData().getFloat(VTTestThread.VESSEL_ADVANCE_SPEED));
				}
				
				if (	msg.getData().containsKey(VTTestThread.VESSEL_ROTATION_SPEED)) {
					vessel_rotation_speed.setText(	res.getString(	R.string.parameter_base, 
													res.getString(R.string.vessel_rotation_speed), 
													df.format(msg.getData().getFloat(VTTestThread.VESSEL_ROTATION_SPEED)), 
													res.getString(R.string.speed_units_ms)));
				}
				
				if (msg.getData().containsKey(VTTestThread.VESSEL_DIRECTION)) {
					forces.setVesselDirection(msg.getData().getFloat(VTTestThread.VESSEL_DIRECTION));
				}
				
				if (msg.getData().containsKey(VTTestThread.DELAY)) {
					forces.setDelay(msg.getData().getFloat(VTTestThread.DELAY));
				}
				
				forces.invalidate();
			}
		};
		
		thread = new VTTestThread(handler);
		thread.running = true;
		thread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_vttest_main, menu);
		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			thread.running = false;
			this.finish();
			//thread.stop();
		}
		return super.onKeyUp(keyCode, event);
	}
	
	
}
