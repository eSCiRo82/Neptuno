package com.test.vtopenglengine.terrain;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.test.vtopenglengine.obj_file_parser.BoatHullModel;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

public class TerrainRenderer implements Renderer {

	private boolean mTranslucentBackground;
	private float mTransY;
	private float mAngle;
	private TerrainModel mTerrain;
	private float[] color = {1.0f, 1.0f, 0.0f, 1.0f};
	private float[] ambient = {1.0f, 1.0f, 1.0f, 1.0f};
	private float[] black = {0.0f, 0.0f, 0.0f, 0.0f};
	private float[] sunPos = {100.0f, 100.0f, 100.0f, 1.0f};
	

	public TerrainRenderer(Context _context, boolean _useTranslucentBackground)
	{
		mTranslucentBackground = _useTranslucentBackground;
		mTerrain = new TerrainModel();
		mTransY = 0.0f;
		mAngle = 0.0f;
	}
	
	// Se llama cada vez que el sistema refresca la imagen (varias veces por segundo)
	@Override
	public void onDrawFrame(GL10 _gl) {
		// Se limpia la pantalla: 
		// GL_COLOR_BUFFER_BIT mantiene los datos RGBA en el buffer
		// GL_DEPTH_BUFFER_BIT asegura que los objetos m�s cercanos se oscurecen apropiadamente respecto a los lejanos 
		_gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		_gl.glMatrixMode(GL10.GL_MODELVIEW);
		_gl.glLoadIdentity();
		_gl.glPushMatrix();_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, makeFloatBuffer(ambient));
		mTerrain.draw(_gl);
		_gl.glPopMatrix();
	}

	// Se llama cuando la superficie cambia su tama�o
	@Override
	public void onSurfaceChanged(GL10 _gl, int _width, int _height) {
		// Especifica las dimensiones y el lugar donde se sit�a la ventana de OpenGL
		_gl.glViewport(0, 0, _width, _height);
		
		float ratio;// = (float)_width/(float)_height;
		float zNear = 0.1f;
		float zFar  = 1000;
		// FOV de 30� pasado a radianes (57.3 ~= 180/PI)
		float fieldOfView = 60.0f/57.3f;
		float size;
		
		_gl.glEnable(GL10.GL_NORMALIZE);
		
		ratio = (float)_width/(float)_height;
		
		// En este modo es el que permite proyectar una escena 3D a la pantalla en 2D
		_gl.glMatrixMode(GL10.GL_PROJECTION);
		
		// El FOV se divide entre dos porque si tomamos como origen de coordenadas el
		// centro de la pantalla, nos moveremos de -size a +size.
		// Por eso el campo de visi�n se divide entre dos, para un campo de visi�n
		// de G grados, nos moveremos en -G/2 a +G/2.
		// Al multiplicarlo por zNear estamos estableciendo una escala
		size = zNear * (float) (Math.tan((double) (fieldOfView/2.0f)));
		
		// Establecemos los l�mites y dividimos bottom y top por ratio para que el 
		// cuadrado sea realmente un cuadrado pues ratio es la relaci�n de aspecto
		// entre el ancho y el alto de la pantalla
		_gl.glFrustumf(-size, size, -size/ratio, size/ratio, zNear, zFar);
		
		_gl.glMatrixMode(GL10.GL_MODELVIEW);
	}

	// Es llamada cuando se crea la superficie
	@Override
	public void onSurfaceCreated(GL10 _gl, EGLConfig _config) {
		// Permite desabilitar el dithering, que permite que pantallas con una paleta limitada
		// de colores luzca mejor, a costa del rendimiento
		_gl.glDisable(GL10.GL_DITHER);
		
		//_gl.glEnable(GL10.GL_BLEND);
		//_gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		// Permite establecer distintas propiedades de correcci�n, como por ejemplo favorecer
		// la velocidad frente a la calidad.
		_gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		if (mTranslucentBackground)
		{
			_gl.glClearColor(0, 0, 0, 0);
		}
		else
		{
			_gl.glClearColor(1, 1, 1, 1);
		}
		//_gl.glClearColor(1, 1, 1, 1);
		// Esta propiedad permite desechar aquellos tri�ngulos que est�n fuera de nuestra visi�n
		_gl.glEnable(GL10.GL_CULL_FACE);
		// Hace que se suavicen los pol�gonos y los colores
		_gl.glShadeModel(GL10.GL_SMOOTH);
		// Habilita el z-buffering
		_gl.glEnable(GL10.GL_DEPTH_TEST);
	}
	
	// Funci�n auxiliar para convertir los arrays en bufferes que entienda OpenGL
		public static FloatBuffer makeFloatBuffer(float[] _arr) {
			ByteBuffer bb = ByteBuffer.allocateDirect(_arr.length * 4);
			bb.order(ByteOrder.nativeOrder());
			FloatBuffer fb = bb.asFloatBuffer();
			fb.put(_arr);
			fb.position(0);
			return fb;
		}
}
