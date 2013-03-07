package com.test.vtopenglengine.example_1;

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

public class Square3D {
	private FloatBuffer mFVertexBuffer;
	private ByteBuffer mColorBuffer;
	private ByteBuffer mIndexBuffer;
	
	public Square3D() {
		// Vértices del cuadrado
		float vertices[] = { 	
										-1.0f, -1.0f, //  *---*
										1.0f, -1.0f,  //    / 
										-1.0f, 1.0f,  //  *---*
										1.0f, 1.0f
									};
		
		byte maxColor = (byte) 0xFF;
		
		// Cada color tiene 4 componentes R-G-B-A
		byte colors[] = {
							maxColor, maxColor, 	   0, maxColor,
							0,		  maxColor,	maxColor, maxColor,
							0,			     0,        0, maxColor,
							maxColor,        0, maxColor, maxColor
						};
		
		// 0 *---* 1
		//     / 
		// 3 *---* 2
		// Éste es el mapeo de los vértices para definir los triángulos
		byte indices[] = {
							0, 3, 1,
							0, 2, 3
						 };
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(4*vertices.length);
		vbb.order(ByteOrder.nativeOrder());
		
		// Creamos los buffers de cada uno de los elementos
		// Buffer de vértices
		mFVertexBuffer = vbb.asFloatBuffer();
		mFVertexBuffer.put(vertices);
		mFVertexBuffer.position(0);
		
		// Buffer de colores
		mColorBuffer = ByteBuffer.allocateDirect(colors.length);
		mColorBuffer.put(colors);
		mColorBuffer.position(0);
		
		// Buffer de índices
		mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
		mIndexBuffer.put(indices);
		mIndexBuffer.position(0);
	}
	
	public void draw(GL10 gl)
	{
		// Triángulos formados tomando los vértices en orden de las agujas del relog
		gl.glFrontFace(GL11.GL_CW);
							
		gl.glVertexPointer(	2,	// Número de elementos por vértice (x,y)  
							GL11.GL_FLOAT, // Datos en punto flotante
							0, // Stride, "paso": número de bytes que se pueden saltar en caso de necesidad
							mFVertexBuffer); // Vértices
		gl.glColorPointer(	4, // Número de elementos por vértice (r,g,b,a)
							GL11.GL_UNSIGNED_BYTE, // Son bytes sin signo
							0, // Stride
							mColorBuffer); // Colores
		gl.glDrawElements(	GL11.GL_TRIANGLES, // Forma de la geometría a usar para formar las figuras
							6, // Número de elementos a dibujar (2 triángulos con 3 vértices = 6)
							GL11.GL_UNSIGNED_BYTE, 
							mIndexBuffer);
		// Triángulos formados tomando los vértices en orden contrario a las agujas del reloj
		gl.glFrontFace(GL11.GL_CCW);
	}
}
