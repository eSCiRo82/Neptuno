package com.test.vttest.graphics_engine.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

// Esta clase se encarga de poner en pantalla una imagen
// con la dirección actual seguida según el ángulo dado
public class DirectionWheelView extends ImageView {
	private int length;
	private float angle;
	private Paint paint;
	
	public DirectionWheelView(Context _context, AttributeSet _attrs) {
		super(_context, _attrs);
		
		angle = 0.0f;
		paint = new Paint();
		paint.setColor(0xFFFFFFFF);
		paint.setStrokeWidth(3.0f);
		paint.setStyle(Paint.Style.STROKE);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int h = getHeight();
		int w = getWidth();
		
		if (h > w) {
			length = w/2;
		} else {
			length = h/2;
		}
		
		float xc = (float) w/2.0f;
		float yc = (float) h/2.0f;
		float xe = xc + ((float) ((double) length*Math.cos(angle)));
		float ye = yc + ((float) ((double) length*Math.sin(angle)));
		
		canvas.drawArc(new RectF(xc - length, yc - length, xc + length, yc + length), 0, 360, false, paint);
		canvas.drawLine(xc, yc, xe, ye, paint);	
	}

	public void setAngle(float _angle) {
		angle = _angle;
		this.invalidate();
	}	
}
