package com.test.vtcomponentviews.vt_timebased_button;

import java.util.Timer;
import java.util.TimerTask;

public abstract class CustomTrigger {
	private Timer timer;
	private TimerTask task;
	private long delay;
	private boolean repeat;
	
	public CustomTrigger() {
		timer = new Timer();
		task = null;
		delay = 0;
		repeat = false;
		stop();
	}
	
	public CustomTrigger(long _delay) {
		timer = new Timer();
		task = null;
		delay = _delay;
		repeat = false;
		stop();
	}
	
	public void start() {
		if (task != null) task.cancel();
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				triggeredFunction();
				if (repeat) {
					start();
				} else {
					stop();
				}
			}			
		};
		
		timer.schedule(task, delay);
		
	}
	
	public void stop() {
		timer.cancel();
		if (task != null) task.cancel();
	}
	
	public void destroy() {
		if (task != null) task.cancel();
	}
	
	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	public void repeat() {
		repeat = true;
	}
	
	public void noRepeat() {
		repeat = false;
	}
	
	/*
	 * Function that was triggered when the timer reaches zero
	 */
	public abstract void triggeredFunction();
}
