package com.test.vtopenglengine.example_2;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
//import android.view.SurfaceView;

public class Cube3DRenderer implements Renderer {
	
	private boolean mTranslucentBackground;
	private float mTransY;
	private float mAngle;
	private Cube3D mCube;

	public Cube3DRenderer(boolean _useTranslucentBackground)
	{
		mTranslucentBackground = _useTranslucentBackground;
		mCube = new Cube3D();
		mTransY = 0.0f;
		mAngle = 0.0f;
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
		// El orden de aplicación de las transformaciones sigue la norma "de la última a la primera"
		// Todas las transformaciones se hacen entorno al origen
		// Para realizar una buena transformación el orden más normal sería: escalar, rotar y trasladar
		// es decir, el orden de los comandos sería : glTranslatef, glRotatef, glScalef
		// Traslada el objeto trazando un seno en Y
		_gl.glTranslatef(0.0f, (float) Math.sin(mTransY), -7.0f /*+ (float) Math.cos(mTransY)*/);
		// Rotamos alrededor del eje Y
		_gl.glRotatef(mAngle, 0.0f, 1.0f, 0.0f);
		// Rotamos alrededor del eje Z
		_gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);		
		// Estas dos líneas son equivalentes a:
		// _gl.glRotatef(mAngle, 0.0f, 1.0f, 1.0f);
		// 				 Ángulo, ejeX, ejeY, ejeZ
		// siempre y cuando se gire el mismo ángulo en todos los planos
		
		// Le dice a OpenGL que se le van a dar dos vectores: el de vértices y el de datos
		_gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		_gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		// Llamamos a la rutina de dibujo del cuadrado
		mCube.draw(_gl);
		
		mTransY += 0.05f;
		mAngle += 0.4f;
	}

	// Se llama cuando la superficie cambia su tamaño
	@Override
	public void onSurfaceChanged(GL10 _gl, int _width, int _height) {
		// Especifica las dimensiones y el lugar donde se sitúa la ventana de OpenGL
		_gl.glViewport(0, 0, _width, _height);
		
		float ratio;// = (float)_width/(float)_height;
		float zNear = 0.1f;
		float zFar  = 1000;
		// FOV de 30º pasado a radianes (57.3 ~= 180/PI)
		float fieldOfView = 60.0f/57.3f;
		float size;
		
		_gl.glEnable(GL10.GL_NORMALIZE);
		
		ratio = (float)_width/(float)_height;
		
		// En este modo es el que permite proyectar una escena 3D a la pantalla en 2D
		_gl.glMatrixMode(GL10.GL_PROJECTION);
		
		// El FOV se divide entre dos porque si tomamos como origen de coordenadas el
		// centro de la pantalla, nos moveremos de -size a +size.
		// Por eso el campo de visión se divide entre dos, para un campo de visión
		// de G grados, nos moveremos en -G/2 a +G/2.
		// Al multiplicarlo por zNear estamos estableciendo una escala
		size = zNear * (float) (Math.tan((double) (fieldOfView/2.0f)));
		
		// Establecemos los límites y dividimos bottom y top por ratio para que el 
		// cuadrado sea realmente un cuadrado pues ratio es la relación de aspecto
		// entre el ancho y el alto de la pantalla
		_gl.glFrustumf(-size, size, -size/ratio, size/ratio, zNear, zFar);
		
		_gl.glMatrixMode(GL10.GL_MODELVIEW);
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
		if (mTranslucentBackground)
		{
			_gl.glClearColor(0, 0, 0, 0);
		}
		else
		{
			_gl.glClearColor(1, 1, 1, 1);
		}
		
		// Esta propiedad permite desechar aquellos triángulos que están fuera de nuestra visión
		_gl.glEnable(GL10.GL_CULL_FACE);
		// Hace que se suavicen los polígonos y los colores
		_gl.glShadeModel(GL10.GL_SMOOTH);
		// Habilita el z-buffering
		_gl.glEnable(GL10.GL_DEPTH_TEST);
	}
}
