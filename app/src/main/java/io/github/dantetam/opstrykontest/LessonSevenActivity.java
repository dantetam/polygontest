package io.github.dantetam.opstrykontest;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LessonSevenActivity extends Activity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener
{
	/** Hold a reference to our GLSurfaceView */
	private LessonSevenGLSurfaceView mGLSurfaceView;
	private LessonSevenRenderer mRenderer;

    private GestureDetectorCompat mDetector;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.lesson_seven);

		mGLSurfaceView = (LessonSevenGLSurfaceView) findViewById(R.id.gl_surface_view);

		// Check if the system supports OpenGL ES 2.0.
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

		if (supportsEs2) {
			// Request an OpenGL ES 2.0 compatible context.
			mGLSurfaceView.setEGLContextClientVersion(2);

			final DisplayMetrics displayMetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

			// Set the renderer to our demo renderer, defined below.
			mRenderer = new LessonSevenRenderer(this, mGLSurfaceView);
			mGLSurfaceView.setRenderer(mRenderer, displayMetrics.density);
		} else {
			// This is where you could create an OpenGL ES 1.x compatible
			// renderer if you wanted to support both ES 1 and ES 2.
			return;
		}

		findViewById(R.id.button_decrease_num_cubes).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				decreaseCubeCount();
			}
		});

		findViewById(R.id.button_increase_num_cubes).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				increaseCubeCount();
			}
		});

		//findViewById(R.id.button_switch_VBOs).setOnClickListener
		
		//findViewById(R.id.button_switch_stride).setOnClickListener
        mDetector = new GestureDetectorCompat(this,this);
        mDetector.setOnDoubleTapListener(this);
    }

	@Override
	protected void onResume() {
		// The activity must call the GL surface view's onResume() on activity
		// onResume().
		super.onResume();
		mGLSurfaceView.onResume();
	}

	@Override
	protected void onPause() {
		// The activity must call the GL surface view's onPause() on activity
		// onPause().
		super.onPause();
		mGLSurfaceView.onPause();
	}

	private void decreaseCubeCount() {
		mGLSurfaceView.queueEvent(new Runnable() {
			@Override
			public void run() {
				mRenderer.decreaseCubeCount();
			}
		});
	}

	private void increaseCubeCount() {
		mGLSurfaceView.queueEvent(new Runnable() {
			@Override
			public void run() {
				mRenderer.increaseCubeCount();
			}
		});
	}

    public final String DEBUG_TAG = "Debug (Gesture): ";

    @Override
    public boolean onDown(MotionEvent event) {
        System.out.println(DEBUG_TAG + "; onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        System.out.println(DEBUG_TAG + "; onFling: " + event1.toString() + event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        System.out.println(DEBUG_TAG + "; onLongPress: " + event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        System.out.println(DEBUG_TAG + "; onScroll: " + e1.toString() + e2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        System.out.println(DEBUG_TAG + "; onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        System.out.println(DEBUG_TAG + "; onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        System.out.println(DEBUG_TAG + "; onDoubleTap: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        System.out.println(DEBUG_TAG + "; onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        System.out.println(DEBUG_TAG + "; onSingleTapConfirmed: " + event.toString());
        return true;
    }

}