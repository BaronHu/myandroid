package com.example.alphabitmap;

import java.io.InputStream;

import android.R.style;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class MyView extends View {
	
	private Bitmap bitmap;

	public MyView(Context context) {
		super(context);
		InputStream is = context.getResources().openRawResource(R.drawable.love);
		bitmap = BitmapFactory.decodeStream(is);
		setBackgroundColor(Color.WHITE);
	}
	
	public MyView(Context context, AttributeSet attr) {
		super(context, attr);
	}
	
	public MyView(Context context, AttributeSet attr, int style) {
		super(context, attr, style);
	}

	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAlpha(MainActivity.alpha);
		canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
				new Rect(10, 10, 310, 235), paint);
		if (MainActivity.alpha >= 20) {
			paint.setColor(Color.RED);
			paint.setTextSize(15);
			canvas.drawText("Judy", 30, 100, paint);
		}
		
		if (MainActivity.alpha >=80) {
			paint.setColor(Color.RED);
			paint.setTextSize(15);
			canvas.drawText(", I Love you", 65, 100, paint);
		}
		
		if (MainActivity.alpha >=150) {
			paint.setColor(Color.RED);
			paint.setTextSize(15);
			canvas.drawText(", forever and ever", 145, 100, paint);
		}
		
		if (MainActivity.alpha >= 200) {
			paint.setColor(Color.RED);
			paint.setTextSize(15);
			canvas.drawText("--Baron.hu", 160, 140, paint);
		}
		
	}
}
