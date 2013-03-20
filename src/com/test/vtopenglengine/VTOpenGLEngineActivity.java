package com.test.vtopenglengine;

import com.test.vtopenglengine.R;
import com.test.vtopenglengine.example_1.Square3DRenderer;
import com.test.vtopenglengine.example_2.Cube3DRenderer;
import com.test.vtopenglengine.obj_file_parser.BoatRenderer;
import com.test.vtopenglengine.solar_system.SolarSystemRenderer;
import com.test.vtopenglengine.terrain.TerrainRenderer;
import com.test.vtopenglengine.texture.Square3DTextureRenderer;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.WindowManager;

public class VTOpenGLEngineActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_vtopen_glengine);
		int op = 5;
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		GLSurfaceView view = new GLSurfaceView(this);
		//view.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		switch(op) 
		{
			case 0: view.setRenderer(new Square3DRenderer(true)); break;
			case 1: view.setRenderer(new Cube3DRenderer(true)); break;
			case 2: view.setRenderer(new SolarSystemRenderer()); break;
			case 3: view.setRenderer(new Square3DTextureRenderer(true, this.getApplicationContext())); break;
			case 4: view.setRenderer(new BoatRenderer(this.getApplicationContext(), true)); break;
			case 5: view.setRenderer(new TerrainRenderer(this.getApplicationContext(), true)); break;
		}
		
		setContentView(view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_vtopen_glengine, menu);
		return true;
	}

}
