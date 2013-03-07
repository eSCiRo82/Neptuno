package com.test.vtopenglengine;

import com.test.vtopenglengine.R;
import com.test.vtopenglengine.example_1.Square3DRenderer;

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
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		GLSurfaceView view = new GLSurfaceView(this);
		view.setRenderer(new Square3DRenderer(true));
		setContentView(view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_vtopen_glengine, menu);
		return true;
	}

}
