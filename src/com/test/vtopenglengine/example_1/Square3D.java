package com.test.vtopenglengine.example_1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
/*
 * NOTAS
 * - OpenGL ES s�lo permite trabajar con tri�ngulos
 */
// OpenGL versi�n 1.0
import javax.microedition.khronos.opengles.GL10;
// OpenGL versi�n 1.1
import javax.microedition.khronos.opengles.GL11;

public class Square3D {
	private FloatBuffer mFVertexBuffer;
	private ByteBuffer mColorBuffer;
	private ByteBuffer mIndexBuffer;
	
	public Square3D() {
		// V�rtices del cuadrado
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
		// �ste es el mapeo de los v�rtices para definir los tri�ngulos
		byte indices[] = {
							0, 3, 1,
							0, 2, 3
						 };
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(4*vertices.length);
		vbb.order(ByteOrder.nativeOrder());
		
		// Creamos los buffers de cada uno de los elementos
		// Buffer de v�rtices
		mFVertexBuffer = vbb.asFloatBuffer();
		mFVertexBuffer.put(vertices);
		mFVertexBuffer.position(0);
		
		// Buffer de colores
		mColorBuffer = ByteBuffer.allocateDirect(colors.length);
		mColorBuffer.put(colors);
		mColorBuffer.position(0);
		
		// Buffer de �ndices
		mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
		mIndexBuffer.put(indices);
		mIndexBuffer.position(0);
	}
	
	public void draw(GL10 gl)
	{
		// Tri�ngulos formados tomando los v�rtices en orden de las agujas del relog
		gl.glFrontFace(GL11.GL_CW);
							
		gl.glVertexPointer(	2,	// N�mero de elementos por v�rtice (x,y)  
							GL11.GL_FLOAT, // Datos en punto flotante
							0, // Stride, "paso": n�mero de bytes que se pueden saltar en caso de necesidad
							mFVertexBuffer); // V�rtices
		gl.glColorPointer(	4, // N�mero de elementos por v�rtice (r,g,b,a)
							GL11.GL_UNSIGNED_BYTE, // Son bytes sin signo
							0, // Stride
							mColorBuffer); // Colores
		gl.glDrawElements(	GL11.GL_TRIANGLES, // Forma de la geometr�a a usar para formar las figuras
							6, // N�mero de elementos a dibujar (2 tri�ngulos con 3 v�rtices = 6)
							GL11.GL_UNSIGNED_BYTE, 
							mIndexBuffer);
		// Tri�ngulos formados tomando los v�rtices en orden contrario a las agujas del reloj
		gl.glFrontFace(GL11.GL_CCW);
	}
}
