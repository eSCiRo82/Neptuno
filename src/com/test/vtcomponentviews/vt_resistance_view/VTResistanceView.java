package com.test.vtcomponentviews.vt_resistance_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 *  Algunos ejemplitos:
 *  GradientDrawable, LinearGradient
 * 	RadialGradient gradient = new RadialGradient(200, 200, 200, 0xFFFFFFFF,
            0xFF000000, android.graphics.Shader.TileMode.CLAMP);
    Paint p = new Paint();
    p.setDither(true);
    p.setShader(gradient);
    
    linearGradientShader = new LinearGradient(
    0, 0, w, h, 
    shaderColor1, shaderColor0, Shader.TileMode.MIRROR);
  
	MyPaint.setShader(linearGradientShader);
	canvas.drawRect(0, 0, w, h, MyPaint);
 */
public class VTResistanceView extends ImageView {
	public static final int SECTIONS = 8;
	// Arco de cada una de las 8 secciones
	public static final float SECTION_ARC_ANGLE = 37;//*(float) Math.PI/180;
	// Separación entre secciones
	public static final float SECTION_INTERVAL_ARC_ANGLE = 8;//*(float) Math.PI/180;
	// Tamaño stándar de la vista
	public static final float MIN_SIZE = 53;
	public static final float STANDAR_LINE_WIDTH = 8.0f;
	public float LINE_WIDTH;
	public float SIZE;
	
	private int front_ini_color;	// Color de inicio de la barra principal para el degradado
	private int front_end_color;	// Color de fin de la barra principal para el degradado
	
	private int back_ini_color;		// Color de inicio del fondo para el degradado
	private int back_end_color; 	// Color de fin del fondo para el degradado
	
	private int front_line_width;	// Ancho de la línea
	private int back_line_width;	
	
	private Bitmap cover_bitmap; 	// todavía no sé muy bien para qué, pero ahí están	
	private boolean has_cover_bitmap;
	
	private float radius;
	private float width, height;
	
	//private int sections;	// Divide la barra en secciones
	
	private Paint paint;
	private RectF oval, back_oval;
	
	public VTResistanceView(Context _context, AttributeSet _attrs) {
		super(_context, _attrs);
		paint = new Paint();
		paint.setColor(0xFFFF0000);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(8);
		width = MIN_SIZE;
		height = MIN_SIZE;
		radius = width/2 - 4;
		oval = new RectF(4,4,radius*2,radius*2);
		back_oval = new RectF(0,0,width, height);
		LINE_WIDTH = 8;
		SIZE = MIN_SIZE;
		
		//if (_attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "android:layout_width").compareTo("wrap_content") == 0)
		//{
		/*if (getLayoutParams().width == LayoutParams.WRAP_CONTENT) {
			width = 83;
		}
		
		if (getLayoutParams().height == LayoutParams.WRAP_CONTENT)
		{
			height = 83;
		}
		
		radius = width/2 - 10;		
		oval.set(0,0,width,height);*/
	}	
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		if(getLayoutParams().width==LayoutParams.WRAP_CONTENT)
	    {
	        width = MIN_SIZE;
	    }
	    else if((getLayoutParams().width==LayoutParams.MATCH_PARENT)||(getLayoutParams().width==LayoutParams.FILL_PARENT))
	    {
	        width = MeasureSpec.getSize(widthMeasureSpec);
	    }
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
		
		// Por si algún cuco decide dar distinto valor a las dimensiones	
		if (width > height) {
			SIZE = height;
		} else {
			SIZE = width;
		}
		
		radius = SIZE/2 - 2;
		float mid = SIZE/2;
		
		LINE_WIDTH = STANDAR_LINE_WIDTH*SIZE/MIN_SIZE;
		paint.setStrokeWidth(LINE_WIDTH);
		
		//oval.set(4,4,radius*2,radius*2);
		//oval.set((width-SIZE+LINE_WIDTH)/2,(height-SIZE+LINE_WIDTH)/2,(width+SIZE-LINE_WIDTH)/2,(height+SIZE-LINE_WIDTH)/2);
		oval.set(LINE_WIDTH/2.0f, LINE_WIDTH/2.0f, SIZE - LINE_WIDTH/2.0f, SIZE - LINE_WIDTH/2.0f);
		
										// WTF!!								// WTF
		setMeasuredDimension((int) width /*| MeasureSpec.EXACTLY*/, (int) height /*| MeasureSpec.EXACTLY*/);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);		
		
		float start_angle = SECTION_INTERVAL_ARC_ANGLE/2.0f;
		/*canvas.drawARGB(255,0,0,255);
		paint.setColor(0x80808000);
		canvas.drawArc(oval, start_angle, 360.0f, false, paint);*/
		for (int i = 0; i < SECTIONS; i++)
		{
			paint.setColor(0xFFFF0000);
			canvas.drawArc(oval, start_angle, SECTION_ARC_ANGLE, false, paint);
			
			start_angle += 45.0f;
		}
	}
	
	/*@Override
	protected void onSizeChanged(int _w, int _h, int _oldw, int _oldh) {
		if (_w > 0 && _h > 0) {
			width = _w;
			height = _h;
			radius = _w/2 - 10;		
			oval.set(4,4,radius*2,radius*2);
		}
		super.onSizeChanged(_w, _h, _oldw, _oldh);
		
	}*/
}
