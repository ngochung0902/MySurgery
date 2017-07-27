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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TabWidget;

import com.mysurgery.R;

import java.util.HashMap;


@SuppressLint("UseSparseArrays")
public class BadgeTabWidget extends TabWidget {
	HashMap<Integer, Badge> map;
	Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

	public BadgeTabWidget(Context context) {
		super(context);

		map = new HashMap<Integer, Badge>();

	}

	public BadgeTabWidget(Context c, AttributeSet set) {
		super(c, set);

		map = new HashMap<Integer, Badge>();
	}

	public int getBadgeNumAtIndex(int index) {
		Badge b = map.get(index);

		if (b == null) {
			return 0;
		} else {
			return b.getNum();
		}
	}

	public void setBadgeAtIndex(int num, int index) {
		Badge b = map.get(index);

		if (b == null) {
			b = new Badge();
			map.put(index, b);
		}

		b.setNum(num);
 		this.getChildAt(index).setVisibility(View.INVISIBLE);
		this.getChildAt(index).setVisibility(View.VISIBLE);
	}

	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		boolean b = super.drawChild(canvas, child, drawingTime);

		// figure out our index in the tabs, need it for the badge number
		int index = 0;
		for (int i = 0; i < this.getTabCount(); i++) {
			if (this.getChildAt(i) == child) {
				index = i;
				break;
			}
		}

		int num = this.getBadgeNumAtIndex(index);

		if (num > 0) {
			Bitmap badge = BitmapFactory.decodeResource(this.getResources(),
					R.drawable.badge);
			// Bitmap badge = Bitmap.createScaledBitmap( src, 24, 19, true );

			int x = child.getRight() - badge.getWidth() - 5;

			canvas.drawBitmap(badge, x, 5, new Paint());

			Typeface face = Typeface.create("Verdana", Typeface.BOLD);
			paint.setTypeface(face);
			paint.setTextSize(getResources().getDimension(R.dimen.badge_size));
			paint.setARGB(255, 255, 255, 255);

			String text = "" + num;
			Rect bounds = new Rect();
			paint.getTextBounds(text, 0, text.length(), bounds);

			canvas.drawText(text, (x + badge.getWidth() / 2) - bounds.width()
					/ 2,
			// -1 here because the badge icon is looking as it does with some
			// more space at the bottom for shadows, might need change
			// if the
			// size of the icon is changed
					5 + (badge.getHeight() / 2) + bounds.height() / 2, paint);

		}

		return b;
	}

	private class Badge {
		int num;

		public Badge() {
			num = 0;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public int getNum() {
			return num;
		}
	}
}