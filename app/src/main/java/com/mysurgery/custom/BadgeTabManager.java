/*
 * Copyright (C) 2011 Nilisoft
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
package com.mysurgery.custom;

/**
 * Singleton for managing tab badges.
 * 
 * @author Emil Nilimaa, Nilisoft
 * 
 */
public class BadgeTabManager
{
	private static BadgeTabManager instance;

	private BadgeTabWidget widget;

	private BadgeTabManager( BadgeTabWidget widget )
	{
		this.widget = widget;
	}

	public static void init( BadgeTabWidget widget )
	{
		instance = new BadgeTabManager( widget );
	}

	public static BadgeTabManager getInstance()
	{
		if ( instance == null )
		{
			throw new RuntimeException(
					"BadgeTabManager has not been initialized with a BadgeTabWidget!" );
		}

		return instance;
	}

	public int getBadgeNumAtIndex( int index )
	{
		return widget.getBadgeNumAtIndex( index );
	}

	public void setBadgeAtIndex( int num, int index )
	{
		widget.setBadgeAtIndex( num, index );
	}
}