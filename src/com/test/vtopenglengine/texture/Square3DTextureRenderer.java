package com.test.vtopenglengine.texture;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.test.vtopenglengine.R;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
//import android.view.SurfaceView;

public class Square3DTextureRenderer implements Renderer {
	
	private boolean mTranslucentBackground;
	private float mTransY;
	//private float mAngle;
	private Square3DTexture mSquare;
	private Context context;

	public Square3DTextureRenderer(boolean _useTranslucentBackground, Context _context)
	{
		mTranslucentBackground = _useTranslucentBackground;
		mSquare = new Square3DTexture();
		context = _context;
	}
	
	// Se llama cada vez que el sistema refresca la imagen (varias veces por segundo)
	@Override
	public void onDrawFrame(GL10 _gl) {
		// Se limpia la pantalla: 
		// GL_COLOR_BUFFER_BIT mantiene los datos RGBA en el buffer
		// GL_DEPTH_BUFFER_BIT asegura que los objetos más cercanos se oscurecen apropiadamente respecto a los lejanos 
		_gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		_gl.glMatrixMode(GL10.GL_MODELVIEW);
		_gl.glLoadIdentity();
		// Traslada el objeto trazando un seno en Y
		_gl.glTranslatef(0.0f, /*(float) Math.sin(mTransY)*/ 0.0f, -3.0f/* + (float) Math.cos(mTransY)*/);
		
		// Llamamos a la rutina de dibujo del cuadrado
		mSquare.draw(_gl);
		
		mTransY += 0.05f;
	}

	// Se llama cuando la superficie cambia su tamaño
	@Override
	public void onSurfaceChanged(GL10 _gl, int _width, int _height) {
		// Especifica las dimensiones y el lugar donde se sitúa la ventana de OpenGL
		_gl.glViewport(0, 0, _width, _height);
		
		float ratio = (float) _width/_height;
		// En este modo es el que permite proyectar una escena 3D a la pantalla en 2D
		_gl.glMatrixMode(GL10.GL_PROJECTION);
		// Resetea la matriz a sus valores iniciales
		_gl.glLoadIdentity();
		// Define el volumen de espacio que se está observando actualmente
		// Lo que caiga fuera de este espacio se considera invisible
		// Estos objetos no son tenidos en cuenta, con lo que se ahorran recursos
		_gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
	}

	// Es llamada cuando se crea la superficie
	@Override
	public void onSurfaceCreated(GL10 _gl, EGLConfig _config) {
		// Permite desabilitar el dithering, que permite que pantalas con una paleta limitada
		// de colores luzca mejor, a costa del rendimiento
		_gl.glDisable(GL10.GL_DITHER);
		
		// Permite establecer distintas propiedades de corrección, como por ejemplo favorecer
		// la velocidad frente a la calidad.
		_gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		/*if (mTranslucentBackground)
		{
			_gl.glClearColor(0, 0, 0, 0);
		}
		else
		{
			_gl.glClearColor(1, 1, 1, 1);
		}*/
		
		_gl.glClearColor(1, 1, 1, 1);
		// Esta propiedad permite desechar aquellos triángulos que están fuera de nuestra visió
		// Si deshabilitamos esta propiedad tb se pintan los que están en la cara de atrás si 
		// cambiamos CW por CCW y viceversa
		//_gl.glEnable(GL10.GL_CULL_FACE);
		// Hace que se suavicen los polígonos y los colores
		_gl.glShadeModel(GL10.GL_SMOOTH);
		// Habilita el z-buffering
		_gl.glEnable(GL10.GL_DEPTH_TEST);
		
		mSquare.createTexture(_gl, context, R.drawable.tree);
	}
}
