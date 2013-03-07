package com.test.vtopenglengine.example_2;

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

public class Cube3D {
	private FloatBuffer mFVertexBuffer;
	private ByteBuffer mTfan1;
	private ByteBuffer mTfan2;
	private ByteBuffer mColorBuffer;
	
	public Cube3D() {
		// Vértices del cubo
		
			/**       4*-----*5 
			  		  /|    /|
			 		 /7*---/-*6
			 	   0*-/---*1/ 
			        |/    |/ 
			       3*-----*2			  
			 */
		float vertices[] = { 		//    X       Y       Z
										-1.0f, 	 1.0f, 	 1.0f,
										 1.0f, 	 1.0f, 	 1.0f,   
										 1.0f,  -1.0f, 	 1.0f,  
										-1.0f,  -1.0f,   1.0f,
										
										-1.0f,   1.0f,  -1,0f,
										 1.0f,   1.0f,  -1,0f,
										 1.0f,  -1.0f,  -1,0f,
										-1.0f,  -1.0f,  -1,0f,
									};
		
		byte maxColor = (byte) 0xFF;
		
		// Cada color tiene 4 componentes R-G-B-A
		byte colors[] = {
							maxColor, maxColor, 	   0, maxColor,
							0,		  maxColor,	maxColor, maxColor,
							0,			     0,        0, maxColor,
							maxColor,        0, maxColor, maxColor,
							
							maxColor, maxColor, 	   0, maxColor,
							0,		  maxColor,	maxColor, maxColor,
							0,			     0,        0, maxColor,
							maxColor,        0, maxColor, maxColor
						};
		
		// 0 *---* 1
		//     / 
		// 3 *---* 2
		// Éste es el mapeo de los vértices para definir los triángulos
		byte tfan1[] = {
							1,0,3,
							1,3,2,
							1,2,6,
							1,6,5,
							1,5,4,
							1,4,0
						 };
		
		byte tfan2[] = {
							7,4,5,
							7,5,6,
							7,6,2,
							7,2,3,
							7,3,0,
							7,0,4
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
		
		mTfan1 = ByteBuffer.allocateDirect(tfan1.length);
		mTfan1.put(tfan1);
		mTfan1.position(0);
		
		// Buffer de índices
		mTfan2 = ByteBuffer.allocateDirect(tfan2.length);
		mTfan2.put(tfan2);
		mTfan2.position(0);
	}
	
	public void draw(GL10 gl)
	{
		gl.glVertexPointer(3, GL11.GL_FLOAT, 0, mFVertexBuffer);
		gl.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 0, mColorBuffer);
		
		gl.glDrawElements(GL10.GL_TRIANGLE_FAN, 6 * 3, GL10.GL_UNSIGNED_BYTE, mTfan1);
		gl.glDrawElements(GL10.GL_TRIANGLE_FAN, 6 * 3, GL10.GL_UNSIGNED_BYTE, mTfan2);
	}
}
