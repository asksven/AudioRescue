/*
 * Copyright (C) 2012 asksven
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.asksven.audiorescue;

import java.lang.reflect.Method;

import com.asksven.audiorescue.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
	private static final String TAG 					= "MainActivity";
	
	private static final int DEVICE_IN_WIRED_HEADSET 	= 0x400000;
	private static final int DEVICE_OUT_EARPIECE 		= 0x1;
	private static final int DEVICE_OUT_WIRED_HEADSET 	= 0x4;
	private static final int DEVICE_STATE_UNAVAILABLE 	= 0;
	private static final int DEVICE_STATE_AVAILABLE 	= 1;
	
	private static final String ON_WIRED_HEADSET		= "HeadsetOn";
	private static final String ON_EARPIECE 			= "EarpieceOn";
	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean bShowToast = sharedPrefs.getBoolean("show_toasts", true);

		refreshStatus();
	}

	public void onForceWiredClick(View view)
	{
		setState(this, true);
		refreshStatus();

	}

	public void onForceEarpieceClick(View view)
	{
		reset(this);
		refreshStatus();

	}

	public static void reset(Context context)
	{
		if (((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).isWiredHeadsetOn())
		{
			setState(context, false);
		}
	}

	private static void setState(Context context, boolean bHeadset)
	{
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		boolean bShowToast = sharedPrefs.getBoolean("show_toasts", true);
		boolean bHandleHeadset = sharedPrefs.getBoolean("headset_control", true);
		boolean bHandleEarpiece = sharedPrefs.getBoolean("earpiece_control", true);

		String statusHeadset = "";
		String statusEarpiece = "";
		
		if (bHeadset)
		{
			if (bHandleHeadset)
			{
				setDeviceConnectionState(DEVICE_IN_WIRED_HEADSET, DEVICE_STATE_AVAILABLE, "");
				setDeviceConnectionState(DEVICE_OUT_WIRED_HEADSET, DEVICE_STATE_AVAILABLE, "");
				statusHeadset = "Headset enabled";
			}
			
			if (bHandleEarpiece)
			{
				setDeviceConnectionState(DEVICE_OUT_EARPIECE, DEVICE_STATE_UNAVAILABLE, "");
				statusEarpiece = "Earpiece disabled";
			}
			
		}
		else
		{
			if (bHandleHeadset)
			{
				setDeviceConnectionState(DEVICE_IN_WIRED_HEADSET, DEVICE_STATE_UNAVAILABLE, "");
				setDeviceConnectionState(DEVICE_OUT_WIRED_HEADSET, DEVICE_STATE_UNAVAILABLE, "");
				statusHeadset = "Headset disabled";

			}

			if (bHandleEarpiece)
			{
				setDeviceConnectionState(DEVICE_OUT_EARPIECE, DEVICE_STATE_AVAILABLE, "");
				statusEarpiece = "Earpiece enabled";

			}
			
		}

		if (bShowToast)
		{
			String status = "";
			if (!statusHeadset.equals(""))
			{
				status = statusHeadset;
				if (!statusEarpiece.equals(""))
				{
					status = status + ", " + statusEarpiece;
				}
			}
			else if (!statusEarpiece.equals(""))
			{
				status = statusEarpiece;
			}
			else
			{
				status = "No change";
			}
				
			Toast.makeText(context, status, Toast.LENGTH_LONG).show();
		}
	}

	private static void setDeviceConnectionState(int device, int state, String address)
	{
		try
		{
			Class audioSystem = Class.forName("android.media.AudioSystem");
			Method setDeviceConnectionState = audioSystem.getMethod("setDeviceConnectionState", int.class, int.class, String.class);
			setDeviceConnectionState.invoke(audioSystem, device, state, address);
		}
		catch (Exception e)
		{
			Log.e(TAG, "setDeviceConnectionState failed: " + e);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void refreshStatus()
	{
        TextView tvHeadset = (TextView) findViewById(R.id.textViewHeadsetStatus);

		AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        tvHeadset.setText(ON_WIRED_HEADSET + ": " + audio.isWiredHeadsetOn());

	}
    /** 
     * Add menu items
     * 
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    public boolean onCreateOptionsMenu(Menu menu)
    {  
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }  

    /** 
     * Define menu action
     * 
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    public boolean onOptionsItemSelected(MenuItem item)
    {  
        switch (item.getItemId())
        {  
	        case R.id.preferences:  
	        	Intent intentPrefs = new Intent(this, PreferencesActivity.class);
	            this.startActivity(intentPrefs);
	        	break;	


            case R.id.about:
            	// About
            	Intent intentAbout = new Intent(this, AboutActivity.class);
                this.startActivity(intentAbout);
            	break;

//            case R.id.test:
//            	// Test something
//            	AlarmsDumpsys.getAlarms();
//            	break;	

        }  
        return false;  
    }    
	
}