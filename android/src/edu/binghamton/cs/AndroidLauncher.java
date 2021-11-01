package edu.binghamton.cs;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import edu.binghamton.cs.CustomDrawing;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//initialize(new CustomDrawing(), config);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// ARCore requires camera permission to operate.
		if (!CameraPermissionHelper.hasCameraPermission(this)) {
			CameraPermissionHelper.requestCameraPermission(this);
			return;
		}

  …
	}
}
