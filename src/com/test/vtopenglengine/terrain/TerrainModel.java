package com.test.vtopenglengine.terrain;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

public class TerrainModel {
	public static final int TERRAIN_WIDTH 	= 32;
	public static final int TERRAIN_HEIGHT 	= 32;
	
	private FloatBuffer mFVertexBuffer;	// Buffer de vértices
	private FloatBuffer mFNormalBuffer;	// Buffer de normales
	private ShortBuffer mIndices;		// Buffer de índices
	private ByteBuffer mColorBuffer;
	
	private Random alea;
	
	private int num_vertices;
	private int num_indices;
	
	private float angle = 0.0f;
	
	public TerrainModel()
	{
		// Cuadrado de 128x128 (x,y,z)
		float vertices[] = new float[TERRAIN_WIDTH*TERRAIN_HEIGHT*3];
		// Colores para cada vértice (r,g,b,a)
		byte colors[] = new byte[TERRAIN_WIDTH*TERRAIN_HEIGHT*4];
		// Normales
		float normals[] = new float[TERRAIN_WIDTH*TERRAIN_HEIGHT*3];
		
		num_vertices = TERRAIN_WIDTH*TERRAIN_HEIGHT;
		
		alea = new Random(System.currentTimeMillis());
		// Generamos el terreno aleatoriamente
		//for (int i = 0, j = 0; i < num_vertices; i+=3, j+=4)
		int i = 0;
		int j = 0;
		for (int h = 0; h < TERRAIN_HEIGHT; h++)
		{	
			for (int w = 0; w < TERRAIN_WIDTH; w++)
			{
				float height = alea.nextFloat();
				if (height < 0.8f)
				{
					height = 0.0f;
				} else {
					height *= alea.nextInt(4);
				}
				
				//height = 0.0f;
				// Creamos los vértices en el plano ZX y simplemente ponemos la altura en y
				vertices[i]		= (float) (w - TERRAIN_WIDTH/2);	// x
				vertices[i+1] 	= height;	// y
				vertices[i+2] 	= (float) (h - TERRAIN_HEIGHT/2); //height;					// z
				
				colors[j] 	= (byte) (128.0f*height);
				colors[j+1] = (byte) (64.0f*height);
				
				if (height > 0.0f) {
					colors[j+2] = (byte) 0x00; //50;
				} else {
					colors[j+2] = (byte) 0xFF;
				}
				colors[j+3] = (byte) 0xFF;
				// Ponemos los colores en una escala de marrones, para simular tierra :)
				/*colors[j] 	= (byte) (alea.nextInt(255));
				colors[j+1] = (byte) (alea.nextInt(255)); //(height*4.0f * 255.0f);
				colors[j+2] = (byte) (alea.nextInt(255)); //50;
				colors[j+3] = (byte) 255;
				colors[j] 	= (byte) 0x00;
				colors[j+1] = (byte) 0x00; //(height*4.0f * 255.0f);
				colors[j+2] = (byte) 0xFF; //50;
				colors[j+3] = (byte) 0x55;*/
				
				normals[i] 		= 0.0f;
				normals[i+1]	= 0.0f;
				normals[i+2]	= 1.0f;
				i += 3;
				j += 4;
			}
		}
				
		// Buffer para los vértices
		// Alojamos 4 bytes de cada una de las 3 coordenadas de cada uno de los vértices del terreno
		ByteBuffer vbb = ByteBuffer.allocateDirect(num_vertices*3*4);
		vbb.order(ByteOrder.nativeOrder());
		
		mFVertexBuffer = vbb.asFloatBuffer();
		mFVertexBuffer.put(vertices);
		mFVertexBuffer.position(0);
		
		// Buffer para las normales
		ByteBuffer nbb = ByteBuffer.allocateDirect(num_vertices*3*4);
		nbb.order(ByteOrder.nativeOrder());
		mFNormalBuffer = nbb.asFloatBuffer();
		mFNormalBuffer.put(normals);
		mFNormalBuffer.position(0);
		
		// Cada color está formado por 4 bytes
		mColorBuffer = ByteBuffer.allocateDirect(num_vertices*4);
		mColorBuffer.put(colors);
		mColorBuffer.position(0);
		
		// Hay 128*128 cuadrados, que serán el doble de triángulos y cada triángulo formado por 3 vértices
		//num_indices = (TERRAIN_SIZE-1)*(TERRAIN_SIZE-1)*2*3;
		// Número de vértices por banda*número de bandas + degenerados
		int num_strips = TERRAIN_HEIGHT - 1;
		num_indices = 2*TERRAIN_WIDTH*num_strips + (num_strips-1)*2;
		
		short indices[] = new short[num_indices];
		// v es el indice del vertice
		// f es el indice del cuadrado (2 triángulos) por eso va de 6 en 6
		// num_vertices - TERRAIN_SIZE porque la última fila de vértices sólo se cuenta una
		// vez para generar los triángulos
		/*int f = 0;
		for (int v = 0; v < num_vertices-TERRAIN_SIZE; v++)
		{
			if (v%TERRAIN_SIZE != (TERRAIN_SIZE-1))
			{
				// Pimer triángulo
				indices[f] 		= (short) v;
				indices[f+1] 	= (short) (v+1);
				indices[f+2]	= (short) (v+TERRAIN_SIZE);
				// Segundo triángulo
				indices[f+3]	= (short) (v+1);
				indices[f+4]	= (short) (v+TERRAIN_SIZE+1);
				indices[f+5]	= (short) (v+TERRAIN_SIZE);
				f += 6;
			}
		}*/
		// Vamos a construir el objeto mediante STRIPS
		int ind = 0;
		for (int h = 0; h < TERRAIN_HEIGHT-1; h++)
		{
			for (int w = 0; w < TERRAIN_WIDTH; w++)
			{
				// Empieza la banda, degeneramos el primer vértice (a partir de la segunda banda)
				if (h > 0 && w == 0)
				{									// Los vértices empiezan a contar desde 1 -> Una polla como una olla, empiezan en 0
					indices[ind++] = (short) (h*TERRAIN_WIDTH/*+1*/);
				} 
				
				// Se pone un vértice y el que hay frente a él en la siguiente línea
				indices[ind++] = (short) (h*TERRAIN_WIDTH+w/*+1*/);
				indices[ind++] = (short) ((h+1)*TERRAIN_WIDTH+w/*+1*/);
				
				// Cuando termina la banda, también degeneramos el último vértice
				if (h < TERRAIN_HEIGHT -2 && w == TERRAIN_WIDTH-1) 
				{
					indices[ind++] = (short) ((h+1)*TERRAIN_WIDTH+w/*+1*/);
				}
			}
		}
		
		
		short indices2[] = {1, 2, 6, 7, 3, 8, 4, 9, 5, 10, 15, 9, 14, 8, 13, 7, 12, 6, 11};
		short indices3[] = {0, 5, 1, 6, 2, 7, 3, 8, 4, 9, 14, 8, 13, 7, 12, 6, 11, 5, 10};
		//num_indices = indices3.length;
		
		// x2 porque short tiene dos bytes
		ByteBuffer ibb = ByteBuffer.allocateDirect(num_indices*Short.SIZE/8);
		ibb.order(ByteOrder.nativeOrder());
		mIndices = ibb.asShortBuffer();
		mIndices.put(indices);
		mIndices.position(0);
	}
	
	public void draw(GL10 _gl)
	{
		_gl.glFrontFace(GL10.GL_CCW);
		_gl.glTranslatef(0.0f, 0.0f, -40.0f /*+ (float) Math.cos(mTransY)*/);
		// Rotamos alrededor del eje Y
		_gl.glRotatef(45, 1.0f, 0.0f, 0.0f);
		_gl.glRotatef(angle, 0.0f, 1.0f, 0.0f);
        //gl.glShadeModel(GL10.GL_FLAT);
		_gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		_gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);
		_gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		_gl.glColorPointer(4, GL10.GL_UNSIGNED_BYTE, 0, mColorBuffer);
		//_gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		//_gl.glNormalPointer(GL10.GL_FLOAT, 0, mFNormalBuffer);
		//_gl.glDrawArrays(GL10.GL_POINTS, 0, num_vertices);
						// modo				  numero de indices         tipo de los datos   indices
		
		_gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, num_indices, GL10.GL_UNSIGNED_SHORT, mIndices);
		//gl.glDrawElements(GL10.GL_TRIANGLE_FAN, 6 * 3, GL10.GL_UNSIGNED_BYTE, mTfan2);
		angle += 0.05f;
	}
}