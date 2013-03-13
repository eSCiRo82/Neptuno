package com.test.vtopenglengine.texture;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
/*
 * NOTAS
 * - OpenGL ES sólo permite trabajar con triángulos
 */
// OpenGL versión 1.0
import javax.microedition.khronos.opengles.GL10;
// OpenGL versión 1.1
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Square3DTexture {
	private FloatBuffer mFVertexBuffer;
	private ByteBuffer mColorBuffer;
	private ByteBuffer mIndexBuffer;
	private FloatBuffer mTextureBuffer;
	// Array de texturas
	private int[] textures;
	// Vértices del cuadrado cuando está mirando a la derecha
	float vertices_right[] = { 	
									 1.0f,  1.0f, 	0.0f, 	//  *---*
									 1.0f, -1.0f,	0.0f,  	//    / 
									 -1.0f, -1.0f,  0.0f,//  *---*
									 -1.0f,  1.0f, 	0.0f
								};
	// Vértices del cuadrado cuando está mirando a la izquierda
	float vertices_left[] = { 	
					 			-1.0f,  1.0f, 	0.0f, 	//  *---*
					 			-1.0f, -1.0f,	0.0f,  	//    / 
					 			1.0f, -1.0f,  0.0f,//  *---*
					 			1.0f,  1.0f, 	0.0f
							};
	
	byte maxColor = (byte) 0xFF;
	
	// Cada color tiene 4 componentes R-G-B-A
	byte colors[] = {
						maxColor, maxColor, 	   0, maxColor,
						0,		  maxColor,	maxColor, maxColor,
						0,			     0,        0, maxColor,
						maxColor,        0, maxColor, maxColor
					};
	
	// 0 *---* 3
	//     / 
	// 1 *---* 2
	// Éste es el mapeo de los vértices para definir el orden en que se dibujan usando 
	// GL_TRIANGLE_FAN
	byte indices[] = {0,1,2,3};
	
	float texture_coords[] = 	{
									0.0f, 0.0f,
									0.0f, 1.0f,
									1.0f, 1.0f,
									1.0f, 0.0f
								};
	public Square3DTexture() {
		textures = new int[1];
		
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(4*vertices_right.length);
		vbb.order(ByteOrder.nativeOrder());
		
		// Creamos los buffers de cada uno de los elementos
		// Buffer de vértices
		mFVertexBuffer = vbb.asFloatBuffer();
		mFVertexBuffer.put(vertices_right);
		mFVertexBuffer.position(0);
		
		// Buffer de colores
		mColorBuffer = ByteBuffer.allocateDirect(colors.length);
		mColorBuffer.put(colors);
		mColorBuffer.position(0);
		
		// Buffer de índices
		mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
		mIndexBuffer.order(ByteOrder.nativeOrder());
		mIndexBuffer.put(indices);
		mIndexBuffer.position(0);
		
		ByteBuffer tbb = ByteBuffer.allocateDirect(4*texture_coords.length);
		tbb.order(ByteOrder.nativeOrder());
		
		mTextureBuffer = tbb.asFloatBuffer();
		mTextureBuffer.put(texture_coords);
		mTextureBuffer.position(0);
	}
	
	public void draw(GL10 _gl)
	{
		// Triángulos formados tomando los vértices en orden de las agujas del relog
		_gl.glFrontFace(GL10.GL_CCW);
		
		// Le dice a OpenGL que se le van a dar dos vectores: el de vértices y el de datos
		_gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);				
		_gl.glVertexPointer(3,	// Número de elementos por vértice (x,y,z)  
							GL10.GL_FLOAT, // Datos en punto flotante
							0, // Stride, "paso": número de bytes que se pueden saltar en caso de necesidad
							mFVertexBuffer); // Vértices
		// Habilita las texturas 2D (las únicas admitidas en OpenGL ES)
		_gl.glEnable(GL10.GL_TEXTURE_2D);
		// Habilita el Blending (define cómo el color fuente es mezclado con el color destino siguiendo
		// alguna ecuación
		_gl.glEnable(GL10.GL_BLEND);
		
		// Esta es la forma más común de mezcla, donde el color fuente y el destino coinciden
		_gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		// Indicamos a OpenGL que use un array de coordenadas de textura
		_gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// Mapeamos las coordenadas de la textura al hardware
		_gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
		// Indica qué textura vamos a usar
		_gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);		
				
	
		// Esta línea ahora toma la textura y los ajusta a los vértices de la forma
		//_gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		_gl.glDrawElements(GL10.GL_TRIANGLE_FAN, 4, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
		
		_gl.glDisable(GL10.GL_VERTEX_ARRAY);
		//_gl.glDisable(GL10.GL_COLOR_ARRAY);
		_gl.glDisable(GL10.GL_TEXTURE_COORD_ARRAY);
		_gl.glDisable(GL10.GL_BLEND);
		_gl.glDisable(GL10.GL_TEXTURE_2D);		
		_gl.glFinish();
	}
	
	// Esta función se encarga de crear una textura OpenGL
	public int createTexture(GL10 _gl, Context _context, int _res)
	{
		// Cargamos la imagen
		Bitmap image = BitmapFactory.decodeResource(_context.getResources(), _res);
		// Asigna un identificador único a la textura para que después pueda ser usado por ejemplo 
		// para borrarlo con glDeleteTextures
		_gl.glGenTextures(1, textures, 0);
		// Asigna el tipo de textura a la textura, en este caso 2D
		_gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		// La imagen es creada internamente en el formato nativo en el que está basada
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, image, 0);
		// Especificamos algunos parámetros necesarios que indican cómo debe manejar OpenGL
		// la textura bajo ciertas circunstancias, como cuando se debe encoger o agrandar
		// para ajustarla a algún polígono
		// Establece cómo se debe comportar al minimizar (de forma linear)
		_gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		// Establece cómo se debe comportar al maximizar (de forma linear)
		_gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		// Reciclamos la imagen
		image.recycle();		
		return _res;
	}
}
