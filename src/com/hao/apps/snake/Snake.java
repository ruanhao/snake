package com.hao.apps.snake;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Snake extends Activity implements SensorEventListener{
	SnakeView snakeView = null;
	
	
	
	public SensorManager mSensorManager = null;
	public Sensor mSensor = null;

	
	
	public static final int PAD_ORIENTATION_EAST = 0;
	public static final int PAD_ORIENTATION_WEST = 1;
	public static final int PAD_ORIENTATION_SOUTH = 2;
	public static final int PAD_ORIENTATION_NORTH = 3;
	public static final int PAD_ORIENTATION_EAST_SOUTH = 4;
	public static final int PAD_ORIENTATION_EAST_NORTH = 5;
	public static final int PAD_ORIENTATION_WEST_SOUTH = 6;
	public static final int PAD_ORIENTATION_WEST_NORTH = 7;
	public static final int PAD_ORIENTATION_STILL = 8;
	
	public static final int PAD_ROLLING_DELTA = 12;
	
	public int mPadOrientation;
	
	public int mInterval = 1000;
	public int mScore = 0;
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        snakeView = new SnakeView(this);
        
        mPadOrientation = PAD_ORIENTATION_STILL;
        mInterval = 800;
        
        setContentView(snakeView);
        
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_NORMAL);
		

    }
    
    

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
    	mSensorManager.unregisterListener(this);
		super.onPause();
	}

	public int getmPadOrientation() {
		return mPadOrientation;
	}

	public int getWindowWidth() {
    	return getWindowManager().getDefaultDisplay().getWidth();
    }
    
    public int getWindowHeight() {
    	return getWindowManager().getDefaultDisplay().getHeight();
    }

    
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		/*
		 * forward: f[1] > 0
		 * backward: f[1] < 0
		 * leftward: f[2] > 0
		 * rightward: f[2] < 0
		 */
		float fOrB = event.values[1];
		float lOrR = event.values[2];
//		System.out.println(fOrB + "\\\\" + lOrR);

		if (fOrB > 0 && lOrR > 0 && Math.abs(fOrB) > Snake.PAD_ROLLING_DELTA
				&& Math.abs(lOrR) > Snake.PAD_ROLLING_DELTA) {
			mPadOrientation = PAD_ORIENTATION_WEST_NORTH;
			// System.out.println("West-North");
		} else if (fOrB > 0 && lOrR < 0 && Math.abs(fOrB) > Snake.PAD_ROLLING_DELTA
				&& Math.abs(lOrR) > Snake.PAD_ROLLING_DELTA) {
			mPadOrientation = PAD_ORIENTATION_EAST_NORTH;
			//System.out.println("East-North");
		} else if (fOrB < 0 && lOrR < 0 && Math.abs(fOrB) > Snake.PAD_ROLLING_DELTA
				&& Math.abs(lOrR) > Snake.PAD_ROLLING_DELTA) {
			mPadOrientation = PAD_ORIENTATION_EAST_SOUTH;
			//System.out.println("East-South");
		} else if (fOrB < 0 && lOrR > 0 && Math.abs(fOrB) > Snake.PAD_ROLLING_DELTA
				&& Math.abs(lOrR) > Snake.PAD_ROLLING_DELTA) {
			mPadOrientation = PAD_ORIENTATION_WEST_SOUTH;
			//System.out.println("South-West");
		} else if (Math.abs(fOrB) <= Snake.PAD_ROLLING_DELTA && Math.abs(lOrR) <= Snake.PAD_ROLLING_DELTA) {
			mPadOrientation = PAD_ORIENTATION_STILL;
			//System.out.println("Still");
		} else if (fOrB >= 0 && lOrR >= 0) {
			if (Math.abs(fOrB) > Snake.PAD_ROLLING_DELTA && Math.abs(lOrR) < Snake.PAD_ROLLING_DELTA) {
				mPadOrientation = PAD_ORIENTATION_NORTH;
				//System.out.println("North");
			} else if (Math.abs(fOrB) < Snake.PAD_ROLLING_DELTA && Math.abs(lOrR) > Snake.PAD_ROLLING_DELTA) {
				mPadOrientation = PAD_ORIENTATION_WEST;
				//System.out.println("West");
			}
		} else if (fOrB >= 0 && lOrR <= 0) {
			if (Math.abs(fOrB) > Snake.PAD_ROLLING_DELTA && Math.abs(lOrR) < Snake.PAD_ROLLING_DELTA) {
				mPadOrientation = PAD_ORIENTATION_NORTH;
				//System.out.println("North");
			} else if (Math.abs(fOrB) < Snake.PAD_ROLLING_DELTA && Math.abs(lOrR) > Snake.PAD_ROLLING_DELTA) {
				mPadOrientation = PAD_ORIENTATION_EAST;
				//System.out.println("East");
			}
		} else if (fOrB <= 0 && lOrR <= 0) {
			if (Math.abs(fOrB) > Snake.PAD_ROLLING_DELTA && Math.abs(lOrR) < Snake.PAD_ROLLING_DELTA) {
				mPadOrientation = PAD_ORIENTATION_SOUTH;
				//System.out.println("South");
			} else if (Math.abs(fOrB) < Snake.PAD_ROLLING_DELTA && Math.abs(lOrR) > Snake.PAD_ROLLING_DELTA) {
				mPadOrientation = PAD_ORIENTATION_EAST;
				//System.out.println("East");
			}
		} else if (fOrB <= 0 && lOrR >= 0) {
			if (Math.abs(fOrB) > Snake.PAD_ROLLING_DELTA && Math.abs(lOrR) < Snake.PAD_ROLLING_DELTA) {
				mPadOrientation = PAD_ORIENTATION_SOUTH;
				//System.out.println("South");
			} else if (Math.abs(fOrB) < Snake.PAD_ROLLING_DELTA && Math.abs(lOrR) > Snake.PAD_ROLLING_DELTA) {
				mPadOrientation = PAD_ORIENTATION_WEST;
				//System.out.println("West");
			}
		} else {
			System.out.println("I don't know: " + fOrB + "\\\\" + lOrR);
		}
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
    
    
    
    
    
}





