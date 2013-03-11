package com.test.vtopenglengine.solar_system;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

public class SolarSystemRenderer implements Renderer {
	public final static int SS_SUNLIGHT = GL10.GL_LIGHT0;
	
	private Planet mMercury;
	private Planet mVenus;
	private Planet mEarth;
	private Planet mMars;	
	private Planet mJupiter;
	private Planet mSaturn;
	private Planet mUranus;
	private Planet mNeptune;
	private Planet mPluto;
	private Planet mSun;
	private float mTransY;
	private float mAngleMercury;
	private float mAngleVenus;
	private float mAngleEarth;
	private float mAngleMars;
	private float mAngleJupiter;
	private float mAngleSaturn;
	private float mAngleUranus;
	private float mAngleNeptune;
	private float mAnglePluto;

	public SolarSystemRenderer() {
		mMercury = new Planet(20, 20, 0.056f, 1.0f);
		mMercury.setPosition(0.0f, 0.0f, -1.93f);
		
		mVenus = new Planet(20, 20, 0.136f, 1.0f);
		mVenus.setPosition(0.0f, 0.0f, -3.62f);
		
		mEarth = new Planet(20, 20, 0.144f, 1.0f);
		mEarth.setPosition(0.0f, 0.0f, -5.0f);
		
		mMars = new Planet(20, 20, 0.080f, 1.0f);
		mMars.setPosition(0.0f, 0.0f, -7.62f);
		
		mJupiter = new Planet(20, 20, 1.6f, 1.0f);
		mJupiter.setPosition(0.0f, 0.0f, -26.01f);
		
		mSaturn = new Planet(20, 20, 1.36f, 1.0f);
		mSaturn.setPosition(0.0f, 0.0f, -47.77f);
		
		mUranus = new Planet(20, 20, 0.6f, 1.0f);
		mUranus.setPosition(0.0f, 0.0f, -95.95f);
		
		mNeptune = new Planet(20, 20, 0.56f, 1.0f);
		mNeptune.setPosition(0.0f, 0.0f, -150.54f);
		
		mPluto = new Planet(20, 20, 0.032f, 1.0f);
		mPluto.setPosition(0.0f, 0.0f, -194.64f);
		
		mSun = new Planet(20, 20, 3.0f, 1.0f);
		mSun.setPosition(0.0f, 0.0f, 0.0f);
		
		mAngleMercury = 0.0f;
		mAngleVenus = 0.0f;
		mAngleEarth = 0.0f;
		mAngleMars = 0.0f;
		mAngleJupiter = 0.0f;
		mAngleSaturn = 0.0f;
		mAngleUranus = 0.0f;
		mAngleNeptune = 0.0f;
		mAnglePluto = 0.0f;
	}
	
	@Override
	public void onDrawFrame(GL10 _gl) {
		float[] paleYellow = {1.0f, 1.0f, 0.3f, 1.0f};
		float[] black = {0.0f, 0.0f, 0.0f, 0.0f};
		float[] mercury = {0.0f, 0.8f, 0.8f, 1.0f};
		float[] venus 	= {1.0f, 0.8f, 0.4f, 1.0f};
		float[] mars	= {0.75f, 0.0f, 0.0f, 1.0f};
		float[] jupiter = {1.0f, 0.75f, 0.5f, 1.0f};
		float[] saturn	= {0.8f, 0.65f, 0.0f, 1.0f};
		float[] uranus 	= {0.0f, 1.0f, 1.0f, 1.0f};
		float[] neptune	= {0.0f, 0.0f, 0.6f, 1.0f};
		float[] pluto	= {0.75f, 0.75f, 0.75f, 1.0f};
		
		float[] earth	= {0.0f, 0.0f, 1.0f, 1.0f};
		
		_gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		_gl.glMatrixMode(GL10.GL_MODELVIEW);
		_gl.glLoadIdentity();
		
		// glPushMatrix / glPopMatrix permiten aislar las transformaciones hechas de una parte del código a otra
		_gl.glPushMatrix();
		{
			// Esta traslación es de la "cámara" o punto de vista
			_gl.glTranslatef(0.0f, 0.0f/*(float) Math.sin(mTransY)*/, -20.0f /*+ (float) Math.cos(mTransY)*/);
			
			// Realizamos los ajustes sobre el planeta
			/*_gl.glPushMatrix(); // Asegura que ninguna transformación sobre mPlanet afecta a mSun
			{
				_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, makeFloatBuffer(mercury));
				_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, makeFloatBuffer(black));
				// Rotamos alrededor del eje Y
				_gl.glRotatef(mAngleMercury, 0.0f, 1.0f, 0.0f);
				_gl.glRotatef(7.0f, 1.0f, 0.0f, 0.0f);
				// Rotamos alrededor del eje Z
				//_gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);
				executePlanet(mMercury, _gl);
			}
			_gl.glPopMatrix();*/
			
			// Realizamos los ajustes sobre el planeta
			/*_gl.glPushMatrix(); // Asegura que ninguna transformación sobre mPlanet afecta a mSun
			{
				_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, makeFloatBuffer(venus));
				_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, makeFloatBuffer(black));
				// Rotamos alrededor del eje Y
				_gl.glRotatef(mAngleVenus, 0.0f, 1.0f, 0.0f);
				_gl.glRotatef(3.4f, 1.0f, 0.0f, 0.0f);
				// Rotamos alrededor del eje Z
				//_gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);
				executePlanet(mVenus, _gl);
			}
			_gl.glPopMatrix();
			
			// Realizamos los ajustes sobre el planeta
			_gl.glPushMatrix(); // Asegura que ninguna transformación sobre mPlanet afecta a mSun
			{
				_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, makeFloatBuffer(earth));
				_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, makeFloatBuffer(black));
				// Rotamos alrededor del eje Y
				_gl.glRotatef(mAngleEarth, 0.0f, 1.0f, 0.0f);
				// Rotamos alrededor del eje Z
				//_gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);
				executePlanet(mEarth, _gl);
			}
			_gl.glPopMatrix();
			
			// Realizamos los ajustes sobre el planeta
			_gl.glPushMatrix(); // Asegura que ninguna transformación sobre mPlanet afecta a mSun
			{
				_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, makeFloatBuffer(mars));
				_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, makeFloatBuffer(black));
				// Rotamos alrededor del eje Y
				_gl.glRotatef(mAngleMars, 0.0f, 1.0f, 0.0f);
				_gl.glRotatef(1.85f, 1.0f, 0.0f, 0.0f);
				
				// Rotamos alrededor del eje Z
				//_gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);
				executePlanet(mMars, _gl);
			}
			_gl.glPopMatrix();
						
			// Realizamos los ajustes sobre mJupiter
			_gl.glPushMatrix(); // Asegura que ninguna transformación sobre mPlanet afecta a mSun
			{
				_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, makeFloatBuffer(jupiter));
				_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, makeFloatBuffer(black));
				// Rotamos alrededor del eje Y
				_gl.glRotatef(mAngleJupiter, 0.0f, 1.0f, 0.0f);
				_gl.glRotatef(1.31f, 1.0f, 0.0f, 0.0f);
				
				// Rotamos alrededor del eje Z
				//_gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);
				executePlanet(mJupiter, _gl);
			}
			_gl.glPopMatrix();
			
			// Realizamos los ajustes sobre el planeta
			_gl.glPushMatrix(); // Asegura que ninguna transformación sobre mPlanet afecta a mSun
			{
				_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, makeFloatBuffer(saturn));
				_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, makeFloatBuffer(black));
				// Rotamos alrededor del eje Y
				_gl.glRotatef(mAngleSaturn, 0.0f, 1.0f, 0.0f);
				_gl.glRotatef(2.49f, 1.0f, 0.0f, 0.0f);
				
				// Rotamos alrededor del eje Z
				//_gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);
				executePlanet(mSaturn, _gl);
			}
			_gl.glPopMatrix();
			
			// Realizamos los ajustes sobre el planeta
			_gl.glPushMatrix(); // Asegura que ninguna transformación sobre mPlanet afecta a mSun
			{
				_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, makeFloatBuffer(uranus));
				_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, makeFloatBuffer(black));
				// Rotamos alrededor del eje Y
				_gl.glRotatef(mAngleUranus, 0.0f, 1.0f, 0.0f);
				_gl.glRotatef(0.77f, 1.0f, 0.0f, 0.0f);
				
				// Rotamos alrededor del eje Z
				//_gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);
				executePlanet(mUranus, _gl);
			}
			_gl.glPopMatrix();
			
			// Realizamos los ajustes sobre el planeta
			_gl.glPushMatrix(); // Asegura que ninguna transformación sobre mPlanet afecta a mSun
			{
				_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, makeFloatBuffer(neptune));
				_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, makeFloatBuffer(black));
				// Rotamos alrededor del eje Y
				_gl.glRotatef(mAngleNeptune, 0.0f, 1.0f, 0.0f);
				_gl.glRotatef(1.77f, 1.0f, 0.0f, 0.0f);
				
				// Rotamos alrededor del eje Z
				//_gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);
				executePlanet(mNeptune, _gl);
			}
			_gl.glPopMatrix();
			
			// Realizamos los ajustes sobre el planeta
			_gl.glPushMatrix(); // Asegura que ninguna transformación sobre mPlanet afecta a mSun
			{
				_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, makeFloatBuffer(pluto));
				_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, makeFloatBuffer(black));
				// Rotamos alrededor del eje Y
				_gl.glRotatef(mAnglePluto, 0.0f, 1.0f, 0.0f);
				_gl.glRotatef(17.15f, 1.0f, 0.0f, 0.0f);
				
				// Rotamos alrededor del eje Z
				//_gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);
				executePlanet(mPluto, _gl);
			}
			_gl.glPopMatrix();*/
			
			doPlanet(_gl, mMercury, mercury, 7f, mAngleMercury);
			doPlanet(_gl, mVenus, venus, 3.4f, mAngleVenus);
			doPlanet(_gl, mEarth, earth, 0f, mAngleEarth);
			doPlanet(_gl, mMars, mars, 1.85f, mAngleMars);
			doPlanet(_gl, mJupiter, jupiter, 1.31f, mAngleJupiter);
			doPlanet(_gl, mSaturn, saturn, 2.49f, mAngleSaturn);
			doPlanet(_gl, mUranus, uranus, 0.77f, mAngleUranus);
			doPlanet(_gl, mNeptune, neptune, 1.77f, mAngleNeptune);
			doPlanet(_gl, mPluto, pluto, 17.15f, mAnglePluto);
			//_gl.glPushMatrix();
			// Modificamos el material...
			_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_EMISSION, makeFloatBuffer(paleYellow));
			_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, makeFloatBuffer(black));
			
			// ... y lo aplicamos al sol
			executePlanet(mSun, _gl);		
						
			// Volvemos a modificar el material para los objetos que vengan después
			_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_EMISSION, makeFloatBuffer(black));
	
			// Le dice a OpenGL que se le van a dar dos vectores: el de vértices y el de datos
			_gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			_gl.glEnableClientState(GL10.GL_COLOR_ARRAY);		
		}
		_gl.glPopMatrix();
				
		// Llamamos a la rutina de dibujo del cuadrado
		//mPlanet.draw(_gl);
		
		mTransY += 0.05f;
		mAngleEarth += 0.5f;
		mAngleJupiter += 0.1;
		
		mAngleMercury   += 2.07f;
		mAngleVenus 	+= 0.81f;
		mAngleEarth 	+= 0.5f;
		mAngleMars 		+= 0.26f;
		mAngleJupiter 	+= 0.04;
		mAngleSaturn 	+= 0.017f;
		mAngleUranus 	+= 0.006f;
		mAngleNeptune 	+= 0.003f;
		mAnglePluto 	+= 0.002f;
	}

	@Override
	public void onSurfaceChanged(GL10 _gl, int _width, int _height) {
		// Especifica las dimensiones y el lugar donde se sitúa la ventana de OpenGL
		_gl.glViewport(0, 0, _width, _height);
		
		float ratio;// = (float)_width/(float)_height;
		float zNear = 0.05f;
		float zFar  = 1000;
		// FOV de 30º pasado a radianes (57.3 ~= 180/PI)
		float fieldOfView = 30.0f/57.3f;
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
		
		_gl.glClearColor(1f, 1f, 1f, 1f);
		
		// Esta propiedad permite desechar aquellos triángulos que están fuera de nuestra visión
		_gl.glEnable(GL10.GL_CULL_FACE);
		// Hace que se suavicen los polígonos y los colores
		_gl.glShadeModel(GL10.GL_SMOOTH);
		// Habilita el z-buffering
		_gl.glEnable(GL10.GL_DEPTH_TEST);
		// Si pongo a falso estra propiedad falla y pone lineas negras y cosas raras :O
		// Necesita estar a true si se llama a glClearDepth (pasa en algunas GPUs)
		_gl.glDepthMask(true);
		initLighting(_gl);
		
	}

	// Función auxiliar para convertir los arrays en bufferes que entienda OpenGL
	public static FloatBuffer makeFloatBuffer(float[] _arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(_arr.length * 4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(_arr);
		fb.position(0);
		return fb;
	}

	private void doPlanet(GL10 _gl, Planet _mPlanet, float[] _diffuse, float _xangle, float _yangle) 
	{
		float[] black = {0.0f, 0.0f, 0.0f, 0.0f};
		_gl.glPushMatrix(); // Asegura que ninguna transformación sobre mPlanet afecta a mSun
		{
			_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, makeFloatBuffer(_diffuse));
			_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, makeFloatBuffer(black));
			// Rotamos alrededor del eje Y
			_gl.glRotatef(_yangle, 0.0f, 1.0f, 0.0f);
			_gl.glRotatef(_xangle, 1.0f, 0.0f, 0.0f);
			
			// Rotamos alrededor del eje Z
			//_gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);
			executePlanet(_mPlanet, _gl);
		}
		_gl.glPopMatrix();
	}
	
	private void initLighting(GL10 _gl)
	{
		// El color de las luces está en formato RGBA
		float[] green	= {0.0f, 1.0f, 0.0f, 1.0f};
		float[] red 	= {1.0f, 0.0f, 0.0f, 1.0f};
		float[] blue 	= {0.0f, 0.0f, 1.0f, 1.0f};
		float[] yellow	= {0.5f, 0.5f, 0.0f, 1.0f};
		float[] white	= {1.0f, 1.0f, 1.0f, 1.0f};
		// Posición de la luz en el espacio
		// x, y, z
		float[] sunPos = {0.0f, 0.0f, 0.0f, 1.0f};
		
		// Establecemos la posición y la luz 
		_gl.glLightfv(SS_SUNLIGHT, GL10.GL_POSITION, makeFloatBuffer(sunPos));
		// Es la luz que podría representar el sol o un flash. Es reflejada por el objeto en todas direcciones
		_gl.glLightfv(SS_SUNLIGHT, GL10.GL_DIFFUSE, makeFloatBuffer(white));
		// La luz SPECULAR es aquella que refleja un objeto y es más direccional que la DIFFUSE
		_gl.glLightfv(SS_SUNLIGHT, GL10.GL_SPECULAR, makeFloatBuffer(yellow));
		// Luz ambiente
		//_gl.glLightfv(SS_SUNLIGHT, GL10.GL_AMBIENT, makeFloatBuffer(blue));
	
		// Aplicamos un material
		// En OpenGL ES siempre debe ser GL_FRONT_AND_BACK
		_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, makeFloatBuffer(green));
		// Aplicamos un material SPECULAR para que actúe la luz del mismo tipo
		_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, makeFloatBuffer(red));
		// Material para la luz de ambiente
		//_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, makeFloatBuffer(blue));
		// Simula un material que emite luz
		//_gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_EMISSION, makeFloatBuffer(yellow));
		
		_gl.glLightf(SS_SUNLIGHT, GL10.GL_LINEAR_ATTENUATION, 0.025f);
		
		// Indicamos al objeto cuál es su brillo
		// Los valores van de 0 a 128 y cuanto mayor sea, menos refleja la luz. Este valor
		// afecta a la luz SPECULAR en mayor medida ya que es más direccional que la DIFFUSE
		_gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 2.0f);
		// FLAT significa que una cara es una unidad sólida de color mientras SMOOTH
		// indica que los colores se difuminarán de una cara a otra y a lo largo de la cara
		_gl.glShadeModel(GL10.GL_SMOOTH);
		// Le indicamos a OpenGL que queremos usar luces
		_gl.glEnable(GL10.GL_LIGHTING);
		// Le indicamos a OpenGL la luz que debe habilitar (hasta 8 distintas en OpenGL ES para Android)
		_gl.glEnable(SS_SUNLIGHT);
		_gl.glLoadIdentity();
	}
	
	// Esta función aplica los cambios en gl al planeta y además lo pone en su posición respecto al "ojo"
	private void executePlanet(Planet m_planet, GL10 _gl)
	{
		// Aquí, con push y pop nos aseguramos que los cambios sólo se aplican a m_planet
		// de forma exclusiva
		
		_gl.glPushMatrix();
		_gl.glTranslatef(m_planet.X(), m_planet.Y(), m_planet.Z());
		m_planet.draw(_gl);
		_gl.glPopMatrix();
	}
}
