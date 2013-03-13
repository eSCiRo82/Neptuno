package com.test.vtopenglengine.obj_file_parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import android.content.Context;
import android.content.res.AssetManager;

public class ObjParser {
	private String file;
	private Vector<Float> v_vertices;
	private Vector<Float> v_normals;
	private Vector<Float> v_textures;
	
	private Vector<Integer> v_face_v;
	private Vector<Integer> v_face_n;
	private Vector<Integer> v_face_t;
	
	private float vertices[];	// Los vértices tienen 3 componentes
	private int num_vertices;	// Número de vértices (longitud del array / 3)
	private float textures[];	// Los vértices de las texturas tienen 2 componentes
	private int num_textures;	// Número de vértices de texturas (longitud del array / 2)
	private float normals[];	// Las normales tienen 3 componentes	
	private int num_normals;	// Número de vértices (longitud del array / 3)
	
	private int face_v[];
	private int num_face_v;
	private int face_n[];
	private int num_face_n;
	private int face_t[];
	private int num_face_t;
	
	public ObjParser()
	{
		file = null;
	}
	
	public ObjParser(Context _context, String _file)
	{
		parse(_context, _file);
	}
	
	public void parse(Context _context, String _file)
	{
		file = _file;
		AssetManager am = _context.getAssets();
		InputStream is;
		InputStreamReader isr;
		BufferedReader br;
		try {
			is = am.open(file);
			isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);
			parse(br);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void parse(BufferedReader _br)
	{
		String line = null;
		v_vertices = new Vector<Float>();
		num_vertices = 0;
		v_textures = new Vector<Float>();
		num_textures = 0;
		v_normals = new Vector<Float>();
		num_normals = 0;
		
		v_face_v = new Vector<Integer>();
		num_face_v = 0;
		v_face_n = new Vector<Integer>();
		num_face_n = 0;
		v_face_t = new Vector<Integer>();
		num_face_t = 0;
		try {
			while ((line = _br.readLine()) != null) 
			{
				if (line.startsWith("f")) // Cara
				{
					parseFaces(line);
				}
				// OJO: poner primero esto porque todas empiezan por v, ¡qué cabeza!
				else if (line.startsWith("vt"))
				{
					parseTextureVertices(line);
				}
				else if (line.startsWith("vn")) // Normal
				{
					parseNormalVertices(line);
				}
				else if (line.startsWith("v")) // Vértice
				{
					parseVertices(line);
				}
				else if (line.startsWith("usemtl")) // Usar material
				{
					// TODO Más adelante
				}
				else if (line.startsWith("mtllib")) // Librería de materiales
				{
					// TODO Más adelante
				}
			}
			if (v_vertices.size() > 0) {
				vertices = toFloatArray(v_vertices);
			}
			if (v_textures.size() > 0) {
				textures = toFloatArray(v_textures);
			}
			if (v_normals.size() > 0) {
				normals = toFloatArray(v_normals);
			}
			if (v_face_v.size() > 0) face_v = toIntArray(v_face_v);
			if (v_face_n.size() > 0) face_n = toIntArray(v_face_n);
			if (v_face_t.size() > 0) face_t = toIntArray(v_face_t);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Parsea os vértices
	private void parseVertices(String _vertex)
	{
		String tokens[] = _vertex.split("[ ]+");
		
		for (int i = 1; i < tokens.length; i++)
		{
			v_vertices.add(Float.valueOf(tokens[i]));
		}
		
		num_vertices++;
	}
	
	// Parsea os vértices asociados a las texturas
	private void parseTextureVertices(String _vertex)
	{
		String tokens[] = _vertex.split("[ ]+");
		
		for (int i = 1; i < tokens.length; i++)
		{
			v_textures.add(Float.valueOf(tokens[i]));
		}
		
		
	}
	
	// Parsea os vértices asociados a las normales
	private void parseNormalVertices(String _vertex)
	{
		String tokens[] = _vertex.split("[ ]+");
		
		for (int i = 1; i < tokens.length; i++)
		{
			v_normals.add(Float.valueOf(tokens[i]));
		}
		
		if (v_normals.size() > 0) {
			normals = toFloatArray(v_normals);
			num_normals++;
		}
	}
	
	// Parsea las caras
	private void parseFaces(String _face)
	{
		String tokens[] = _face.split("[ ]+"); // Separamos por espacios
		int i = 0;
		Vector<Integer> aux_v = new Vector<Integer>();
		Vector<Integer> aux_n = new Vector<Integer>();
		Vector<Integer> aux_t = new Vector<Integer>();
		if (tokens[1].matches("[0-9]+")) // f v
		{
			for (i = 1; i < tokens.length; i++)
				aux_v.add(getIndex(tokens[i], v_vertices.size()));
			num_face_v++;
		}
		else if (tokens[1].matches("[0-9]+/[0-9]+")) // f v/vt
		{
			for (i = 1; i < tokens.length; i++)
			{
				String components[] = tokens[i].split("/"); // Separamos por / (&#47;)
				aux_v.add(getIndex(components[0], v_vertices.size()));
				aux_t.add(getIndex(components[1], v_textures.size()));
			}
			num_face_v++;
			num_face_t++;
		}
		else if (tokens[1].matches("[0-9]+/[0-9]+/[0-9]+")) // f v/vt/vn
		{
			for (i = 1; i < tokens.length; i++)
			{
				String components[] = tokens[i].split("/");
				aux_v.add(getIndex(components[0], v_vertices.size()));
				aux_t.add(getIndex(components[1], v_textures.size()));
				aux_n.add(getIndex(components[2], v_normals.size()));
			}
			num_face_v++;
			num_face_t++;
			num_face_n++;
		}
		else if (tokens[1].matches("[0-9]+//[0-9]+")) // f v//vn
		{
			for (i = 1; i < tokens.length; i++)
			{
				String components[] = tokens[i].split("//"); // Separamos por //
				aux_v.add(getIndex(components[0], v_vertices.size()));
				aux_n.add(getIndex(components[2], v_normals.size()));
			}
			num_face_v++;
			num_face_n++;
		}
		
		if (aux_v.size() == 3) {
			v_face_v.addAll(aux_v);
		}
		
		if (aux_n.size() == 3) {
			v_face_n.addAll(aux_n);
		}
		
		if (aux_t.size() == 3) {
			v_face_t.addAll(aux_t);
		}
	}
	
	// Pueden existir valores negativos, con lo que los ajustamos
	// a los valores positivos conocidos
	private int getIndex(String _index, int size)
	{
		int idx = Integer.valueOf(_index);
		if (idx < 0)
		{
			return size + idx;
		} else {
			return idx - 1;
		}
	}
	
	private int[] toIntArray(Vector<Integer> _v)
	{
		int[] a = new int[_v.size()];
		
		for (int i = 0; i < _v.size(); i++)
		{
			a[i] = _v.get(i).intValue();
		}
		
		return a;
	}
	
	private float[] toFloatArray(Vector<Float> _v)
	{
		float[] a = new float[_v.size()];
		
		for (int i = 0; i < _v.size(); i++)
		{
			a[i] = _v.get(i).floatValue();
		}
		
		return a;
	}

	// GETTERS
	public float[] getVertices() {
		return vertices;
	}

	public float[] getTextures() {
		return textures;
	}

	public float[] getNormals() {
		return normals;
	}

	public int[] getFaceV() {
		return face_v;
	}

	public int[] getFaceN() {
		return face_n;
	}

	public int[] getFaceT() {
		return face_t;
	}
	
	public int getNumVertices() {
		return num_vertices;
	}
	
	public int getNumTextures() {
		return num_textures;
	}
	
	public int getNumNormals() {
		return num_normals;
	}
	
	public int getNumFaceV() {
		return num_face_v;
	}
	
	public int getNumFaceT() {
		return num_face_t;
	}
	
	public int getNumFaceN() {
		return num_face_n;
	}
}
