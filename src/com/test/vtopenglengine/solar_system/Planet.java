package com.test.vtopenglengine.solar_system;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Planet {
	private float x,y,z;
	private FloatBuffer m_VertexData;
	private FloatBuffer m_NormalData;
	private FloatBuffer m_ColorData;
	
	private float m_Scale;
	private float m_Squash;
	private float m_Radius;
	
	private int m_Stacks; 	// Paralelas al plano X-Z (Paralelos)
							// La primera y la última serán los polos
	private int	m_Slices;	// Paralelos al plano X-Y (Meridianos)
	
	public Planet(int _stacks, int _slices, float _radius, float _squash) {
		m_Stacks = _stacks; // Cantidad de paralelos y meridianos
		m_Slices = _slices; // Más paralelos y meridianos suponen más definición al planeta
		m_Radius = _radius; // Radio de la esfera. Es una forma de preescalar el planeta para
							// no tener que usar glScalef cada vez que queramos dibujarla (*)
		m_Squash = _squash; // Es el nivel de "achatamiento" de los polos de un planeta
							// 1 es perfectamente esférica
		
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
		
		init(_stacks, _slices, _radius, _squash, "dummy");
	}
	
	private void init(int _stacks, int _slices, float _radius, float _squash, String _texture_file) {
		float vertex_data[];
		float color_data[];
		float normal_data[];
		float color_increment = 0.0f;
		
		float blue = 0;
		float red = 1.0f;
		//int num_vertices = 0;
		int v_index = 0;
		int c_index = 0;
		int n_index = 0;
		
		m_Scale = _radius; // (*) El radio es nuestra escala
		m_Squash = _squash;
		
		color_increment = 1.0f/(float) _stacks;
		
		m_Stacks = _stacks;
		
		m_Slices = _slices;
		
		vertex_data = new float[3*((m_Slices*2+2)*m_Stacks)]; 	// Se necesitan dos triángulos por cara por ello m_Slices*2. 
																// El +2 es porque los dos primeros vértices son también los últimos
																// y deben estar duplicados
		color_data = new float[4*((m_Slices*2+2)*m_Stacks)];
		
		normal_data = new float[(3*(m_Slices*2+2)*m_Stacks)];
		
		int phiIdx, thetaIdx;
		// Optimización
		// float phi_1 = (float) -Math.PI/2.0f;
		// float phi_0 = (float) -Math.PI/2.0f;
		
		// Latitud, desde el polo sur (-PI/2) al polo norte (PI/2) -> PHI
		for (phiIdx = 0; phiIdx < m_Stacks; phiIdx++)
		{
			// Calcula los límites de latitud de una línea específica
			// -0.5f asegura que los valores están siempre por debajo de PI/2
			
			// Optimización -> calcular phi_1 fuera, hacer phi_0 = phi_1 y sólo calcular el siguiente phi_1;
			// phi_0 = phi_1;
			// phi_1 = (float) Math.PI * ((float) (phiIdx + 1)*(1.0f/(float) m_Stacks) - 0.5f);
			float phi_0 = (float) Math.PI * (((float) phiIdx/(float) m_Stacks) - 0.5f);
			float phi_1 = (float) Math.PI * (((float) (phiIdx+1)/(float) m_Stacks) - 0.5f);
			// Precalculamos ciertos parámetros constantes durante esta parte de la generación de la esfera
			float cos_phi_0 = (float) Math.cos(phi_0);
			float sin_phi_0 = (float) Math.sin(phi_0);
			float cos_phi_1 = (float) Math.cos(phi_1);
			float sin_phi_1 = (float) Math.sin(phi_1);
			
			float cos_theta, sin_theta;
			
			// Longitud, recorremos todo el contorno de la esfera (0-2PI)
			for (thetaIdx = 0; thetaIdx < m_Slices; thetaIdx++)
			{
				float theta = (float) (-2.0f*(float) Math.PI*((float) thetaIdx) / (float) (m_Slices-1));
				cos_theta = (float) Math.cos(theta);
				sin_theta = (float) Math.sin(theta);
				
				// Calculamos los puntos sobre el Paralelo que corresponden a la 
				// intersección con cada Meridiano
				// Ecuación paramétrica de una esfera
				//			0 <= Theta <= 2PI			0 <= Theta <= 2PI
				//			0 <= Phi <= PI				-PI/2 <= Phi <= PI/2
				//			x horizontal				x horizontal
				//			y profundidad				y vertical
				//			z vertical					z profundidad
				// x 	= 	x0 + r*cos(t)*sin(p)	=	x0 + r*cos(t)*cos(p)
				// y	=	y0 + r*sin(t)*sin(p)	=	y0 + r*sin(p)
				// z	=	z0 + r*cos(p)			=	z0 + r*cos(p)*sin(t)
				vertex_data[v_index] = m_Scale*cos_phi_0*cos_theta;
				// Multiplicamos por m_Squash para producir el achatamiento en Y
				vertex_data[v_index+1] = m_Scale*sin_phi_0*m_Squash;
				vertex_data[v_index+2] = m_Scale*cos_phi_0*sin_theta;
				
				vertex_data[v_index+3] = m_Scale*cos_phi_1*cos_theta;
				vertex_data[v_index+4] = m_Scale*sin_phi_1*m_Squash;
				vertex_data[v_index+5] = m_Scale*cos_phi_1*sin_theta;
				
				// Establecemos los colores de cada vértice
				color_data[c_index] = (float) red;
				color_data[c_index+1] = (float) 0.0f;
				color_data[c_index+2] = (float) blue;
				color_data[c_index+4] = (float) red;
				color_data[c_index+5] = (float) 0.0f;
				color_data[c_index+6] = (float) blue;
				color_data[c_index+3] = (float) 1.0f;
				color_data[c_index+7] = (float) 1.0f;
				
				// Vectores normales a la cara para indicar a la luz
				// cómo actuar sobre la cara
				// OBVIO: Las normales a los vértices son las mismas que los vértices pero sin escalar
				// ya que todas estas normales parten del centro de las esfera
				// Optimización: ¿Por qué no calcular primero las normales y luego los vértices simplemente
				// multiplicándolos por la escala?
				normal_data[n_index] = cos_phi_0*cos_theta;
				normal_data[n_index+1] = sin_phi_0;
				normal_data[n_index+2] = cos_phi_0*sin_theta;
				
				normal_data[n_index+3] = cos_phi_1*cos_theta;
				normal_data[n_index+4] = sin_phi_1;
				normal_data[n_index+5] = cos_phi_1*sin_theta;
				
				v_index += 6; // 2*3 Pasamos al siguiente conjunto de vértices
				n_index += 6; // 2*3
				c_index += 8; // 2*4 Pasamos al siguiente conjunto de colores
			}
			
			blue += color_increment;
			red  -= color_increment;
			
			// Esto son dos triángulos degenerados en un punto para que sirvan de unión con el
			// paralelo actual
			vertex_data[v_index] = vertex_data[v_index + 3] = vertex_data[v_index - 3];
			vertex_data[v_index + 1] = vertex_data[v_index + 3] = vertex_data[v_index - 2];
			vertex_data[v_index + 2] = vertex_data[v_index + 5] = vertex_data[v_index - 1];		
		
		}
		// Finalmente convertimos los arrays de vértices, normales y colores en arrays que OpenGL pueda entender
		m_VertexData = SolarSystemRenderer.makeFloatBuffer(vertex_data);
		m_ColorData = SolarSystemRenderer.makeFloatBuffer(color_data);
		m_NormalData = SolarSystemRenderer.makeFloatBuffer(normal_data);		
	}
		
	// Función para dibujar la esfera
	public void draw(GL10 _gl)
	{
		// En el sentido de las agujas del reloj
		_gl.glFrontFace(GL10.GL_CW);
		_gl.glMatrixMode(GL10.GL_MODELVIEW);
		_gl.glEnable(GL10.GL_CULL_FACE);
		_gl.glCullFace(GL10.GL_BACK);
		
		// Añadimos los vértices, normales y colores a la escena
		_gl.glNormalPointer(GL10.GL_FLOAT, 0, m_NormalData);
		_gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		
		_gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_VertexData);
		_gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		_gl.glColorPointer(4, GL10.GL_FLOAT, 0, m_ColorData);
		_gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		_gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, (m_Slices+1)*2*(m_Stacks-1) + 2);
	}
	
	public void setPosition(float _x, float _y, float _z)
	{
		x = _x;
		y = _y;
		z = _z;
	}
	
	public float X()
	{
		return x;
	}
	
	public float Y()
	{
		return y;		
	}
	
	public float Z()
	{
		return z;
	}
}
