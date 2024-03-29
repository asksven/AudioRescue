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

import com.asksven.audiorescue.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;


/**
 * Activity for managing preferences using Android's preferences framework
 * @see http://www.javacodegeeks.com/2011/01/android-quick-preferences-tutorial.html
 * 
 * Access prefs goes like this:
 *   SharedPreferences sharedPrefs = 
 *   	PreferenceManager.getDefaultSharedPreferences(this);
 *   sharedPrefs.getBoolean("perform_updates", false));
 *   
 * @author sven
 *
 */
public class PreferencesActivity extends PreferenceActivity
{
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	

}
