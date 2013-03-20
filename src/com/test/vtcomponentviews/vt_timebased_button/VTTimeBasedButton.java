package com.test.vtcomponentviews.vt_timebased_button;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;

public class VTTimeBasedButton extends ImageView {
	// Colores, para probar antes de tener imágenes
	private static final int DISABLED_COLOR = 0xFF555555;
	private static final int ENABLED_COLOR = 0xFFFF0000;
	private static final int LINE_COLOR = 0xFFFFFF00;
	// Tamaño stándar de la vista
	public static final float MIN_SIZE = 53;
	private float LINE_WIDTH;
	
	private long disabled_time;	// Éste es el tiempo de recarga del botón
	private long remaining_time; // Tiempo restante
	
	// Recursos BITMAPS
	private Bitmap enabled_bitmap_res;	// Imagen para cuando el botón está habilitado 
	private Bitmap disabled_bitmap_res;	// Imagen para cuando el botón está deshabilitado
	// Recursos de color
	private int disabled_color_res;	// Colores del botón cuando no se ha definido un bitmap
	private int enabled_color_res;
	private int line_color_res;	// La línea sí será un color
	
	private boolean reloading;	// Indica que estamos en tiempo de espera o de recarga
	private VTTimedBasedButtonListener listener;	// Escucha para realizar la acción del botón
	
	private float width, height;	// Dimensiones de la vista
	
	private TimerThread timer;	// Temporizador
	
	// Variables de dibujo
	private RectF button_oval, time_oval;
	private Paint paint;
	private float end_angle;
	private float SIZE;
	
	
	// Este hilo es un temporizador para el tiempo de espera
	private class TimerThread extends Thread
	{
		public TimerThread()
		{
			remaining_time = disabled_time;
		}
		
		public void run()
		{
			long last = System.currentTimeMillis();
			long current = System.currentTimeMillis();
			long delay;
			
			while (remaining_time > 0)
			{
				current = System.currentTimeMillis();
				delay = current - last;
				// Actualizamos el tiempo
				remaining_time -= delay;				
				//update();	// Actualizamos el tiempo
				last = current;								
			}
			
			remaining_time = 0;	// Nos quedamos con que es cero siempre
			reloading = false; // Salimos del tiempo de recarga
			//invalidate();
		}
	}

	public VTTimeBasedButton(Context _context, AttributeSet _attrs) {
		super(_context, _attrs);
		listener = null;
		disabled_time = 30000;
		remaining_time = 0;
		reloading = false;
		
		button_oval = new RectF(0.0f,0.0f,0.0f,0.0f);
		time_oval = new RectF(0.0f,0.0f,0.0f,0.0f);
		
		paint = new Paint();
		width = MIN_SIZE;
		height = MIN_SIZE;
		SIZE = MIN_SIZE;
		LINE_WIDTH = 2;
		
		timer = new TimerThread();
		
		enabled_bitmap_res = null;
		disabled_bitmap_res = null;
		
		disabled_color_res = DISABLED_COLOR;
		enabled_color_res = ENABLED_COLOR;
		line_color_res = LINE_COLOR;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				return true;
			case MotionEvent.ACTION_MOVE:
				return true;
			case MotionEvent.ACTION_UP:
				if (!reloading)
				{
					float x = event.getX();
					float y = event.getY();
					
					// Cuando pulsamos sobre el botón
					if (x > 0 && x < SIZE && y > 0 && y < SIZE)
					{
						// Ponemos el tiempo de espera (también se pone en el thread, bueno...)
						remaining_time = disabled_time;
						// Activamos el modo de espera
						reloading = true;
						// Creamos un nuevo temporizador
						timer = new TimerThread();
						// Inicializamos el temporizador
						timer.start();
						invalidate();
						// Lanzamos la acción, si hay acción
						if (listener != null) listener.onClick();
					}
				}
				return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// En wrap_content establecemos un tamaño mínimo
		if(getLayoutParams().width==LayoutParams.WRAP_CONTENT)
	    {
	        width = MIN_SIZE;
	    }
		// Si ajustamos al padre, pues que lo calcule él :D
	    else if((getLayoutParams().width==LayoutParams.MATCH_PARENT)||(getLayoutParams().width==LayoutParams.FILL_PARENT))
	    {
	    	width = MeasureSpec.getSize(widthMeasureSpec);
	    }
		// En cualquier otro caso tomamos el tamaño definido por el usuario en el xml
	    else
	    	width = getLayoutParams().width;
		
		if(getLayoutParams().height==LayoutParams.WRAP_CONTENT)
	    {
			height = MIN_SIZE;
	    }
	    else if((getLayoutParams().height==LayoutParams.MATCH_PARENT)||(getLayoutParams().height==LayoutParams.FILL_PARENT))
	    {
	    	height = MeasureSpec.getSize(heightMeasureSpec);
	    }
	    else
	    	height = getLayoutParams().height;

		// Por si algún cuco decide dar distinto valor a las dimensiones :D
		if (width > height) {
			SIZE = height;
		} else {
			SIZE = width;
		}
		// Ajustamos el tamaño de la línea
		LINE_WIDTH = LINE_WIDTH*SIZE/MIN_SIZE;
		// Definimos las dimensiones el rectángulo con el tamaño del tiempo
		time_oval.set((width-SIZE+LINE_WIDTH)/2,(height-SIZE+LINE_WIDTH)/2,(width+SIZE-LINE_WIDTH)/2,(height+SIZE-LINE_WIDTH)/2);
		// Definimos las dimensiones del botón
		button_oval.set(0.0f, 0.0f, SIZE, SIZE);
			
										 //  WTF!!								 //  WTF
		setMeasuredDimension((int) width /*| MeasureSpec.EXACTLY*/, (int) height /*| MeasureSpec.EXACTLY*/);
	}
	
	@Override
	protected void onDraw(Canvas canvas) 
	{
		// Sincronizamos con el timer
		//synchronized(timer)
		//{
			// Si no se está en tiempo de espera, se pinta el botón activo
			if (!reloading)
			{
				if (enabled_bitmap_res == null) {
					paint.setColor(enabled_color_res);
					paint.setStyle(Paint.Style.FILL);			
					canvas.drawArc(button_oval, 0, 360.0f, false, paint);
				} else {
					canvas.drawBitmap(enabled_bitmap_res, button_oval.left, button_oval.top, paint);
				}
			}
			// Si estamos en tiempo de espera
			else
			{
				// Primero dibujamos el botón inactivo
				if (disabled_bitmap_res == null) 
				{
					paint.setColor(disabled_color_res);
					paint.setStyle(Paint.Style.FILL);			
					canvas.drawArc(button_oval, 0, 360.0f, false, paint);
				} else {
					canvas.drawBitmap(disabled_bitmap_res, button_oval.left, button_oval.top, paint);
				}
				// Después dibujamos el tiempo que queda				
				paint.setColor(line_color_res);
				paint.setStyle(Paint.Style.STROKE);
				paint.setStrokeWidth(LINE_WIDTH);				
				end_angle = 360.0f*(1-(float) remaining_time/(float) disabled_time);				
				canvas.drawArc(time_oval, 0, end_angle, false, paint);
				this.invalidate();
			}
		//}		
	}
	
	// SETTERS
	public void setDisabledTime(long _dt)
	{
		disabled_time = _dt;
	}
	
	public void setEnabledBitmap(Bitmap _ebres)
	{
		enabled_bitmap_res = _ebres;
	}
	
	public void setDisabledBitmap(Bitmap _dbres)
	{
		disabled_bitmap_res = _dbres;
	}
	
	public void setEnabledColor(int _color)
	{
		enabled_color_res = _color;
	}
	
	public void setDisabledColor(int _color)
	{
		disabled_color_res = _color;
	}
	
	public void setLineColor(int _color)
	{
		line_color_res = _color;
	}
	
	public void setVTTimedBasedButtonListener(VTTimedBasedButtonListener _listener)
	{
		listener = _listener;
	}
}
