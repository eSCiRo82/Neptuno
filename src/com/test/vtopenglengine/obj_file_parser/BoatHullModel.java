package com.test.vtopenglengine.obj_file_parser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;

public class BoatHullModel {
	private ObjParser parser;
	
	private FloatBuffer mFVertexBuffer;
	private FloatBuffer mFNormalBuffer;
	private ByteBuffer mIndices;

	private ByteBuffer mColorBuffer;
	private float mAngleY = 0.0f;
	private float mAngleX = 0.0f;
	
	public BoatHullModel(Context _context)
	{
		parser = new ObjParser(_context, "casco_t.obj");
		ByteBuffer vbb = ByteBuffer.allocateDirect(parser.getVertices().length * Float.SIZE / 8);
		vbb.order(ByteOrder.nativeOrder());
		
		// Creamos los buffers de cada uno de los elementos
		// Buffer de vértices
		
		mFVertexBuffer = vbb.asFloatBuffer();
		mFVertexBuffer.put(parser.getVertices());
		mFVertexBuffer.position(0);
		
		ByteBuffer nbb = ByteBuffer.allocateDirect(parser.getNormals().length * Float.SIZE / 8);
		nbb.order(ByteOrder.nativeOrder());
		
		mFNormalBuffer = nbb.asFloatBuffer();
		mFNormalBuffer.put(parser.getNormals());
		mFNormalBuffer.position(0);
		
		byte colors[] = new byte[parser.getNumVertices() * 4];
				
		for (int i = 0; i < colors.length; i++) {
			colors[i] = (byte) 0xFF;
		}
		
		mColorBuffer = ByteBuffer.allocateDirect(colors.length);
		mColorBuffer.put(colors);
		mColorBuffer.position(0);
		
		ByteBuffer buffer = ByteBuffer.allocate(parser.getFaceV().length /** Short.SIZE / 8*/);
		buffer.order(ByteOrder.nativeOrder());
		
		byte[] aux = new byte[parser.getFaceV().length];
		for (int i = 0; i < aux.length; i++)
		{
			aux[i] = (byte) parser.getFaceV()[i];
		}
		mIndices = ByteBuffer.allocate(parser.getFaceV().length /** Short.SIZE / 8*/);
		mIndices.put(aux);
		mIndices.position(0);
		//mIndices.flip();
		
	}
	
	public void draw(GL10 _gl)
	{
		_gl.glFrontFace(GL10.GL_CCW);
		//_gl.glTranslatef(0.0f, 0.0f, -150.0f /*+ (float) Math.cos(mTransY)*/);
		// Rotamos alrededor del eje Y
		_gl.glRotatef(mAngleX, 1.0f, 0.0f, 0.0f);
		_gl.glRotatef(mAngleY, 0.0f, 1.0f, 0.0f);
        //gl.glShadeModel(GL10.GL_FLAT);
        
		_gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		_gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);
		_gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		_gl.glColorPointer(4, GL10.GL_UNSIGNED_BYTE, 0, mColorBuffer);
		_gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		_gl.glNormalPointer(GL10.GL_FLOAT, 0, mFNormalBuffer);
		//gl.glDrawArrays(GL10.GL_TRIANGLES, 0, parser.getNumVertices());
						// modo				  numero de indices         tipo de los datos   indices
		_gl.glDrawElements(GL10.GL_TRIANGLES, parser.getFaceV().length, GL10.GL_UNSIGNED_BYTE, mIndices);
		//gl.glDrawElements(GL10.GL_TRIANGLE_FAN, 6 * 3, GL10.GL_UNSIGNED_BYTE, mTfan2);
		mAngleX += 0.02f;
		mAngleY += 0.04f;
	}
}
