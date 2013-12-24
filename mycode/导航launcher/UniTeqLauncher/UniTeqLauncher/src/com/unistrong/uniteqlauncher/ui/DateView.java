package com.unistrong.uniteqlauncher.ui;

import com.unistrong.uniteqlauncher.R;
import com.unistrong.uniteqlauncher.util.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * display date / time / week
 * @author Baron
 * */
public class DateView extends View {
	
	public static final String LOG_TAG = "DateView";
	
	private Paint mPaint;
	private Bitmap mBitmap, newBitmap;
	private Matrix mMatrix;

	public DateView(Context context) {
		super(context);
	}
	
	public DateView(Context context, AttributeSet attr) {
		super(context, attr);
	}
	
	public DateView(Context context, AttributeSet attr, int style) {
		super(context, attr, style);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPaint = new Paint();
		String sHour = null;
		String sMin = null;
		int hour = Utils.getHour();
		int min = Utils.getMin();
		if (hour < 10) 
			sHour = "0" + hour;
		else
			sHour = "" + hour;
		if (min < 10)
			sMin = "0" + min;
		else
			sMin = "" + min;
		
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.date_bg_c);
		mMatrix = new Matrix();
		mMatrix.postScale(1.2f, 1.2f);
		newBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), mMatrix, true);
		
			
		mPaint.setColor(getResources().getColor(R.color.white));
		mPaint.setTextSize(110.0f);
		mPaint.setStyle(Style.FILL_AND_STROKE);
		canvas.drawText(sHour + "", getWidth() / 8 - sHour.length() * 12, getHeight() / 2 + 12, mPaint);
		canvas.drawText(":", getWidth() / 4 + mPaint.getTextSize() / 2 + 8, getHeight() / 2 + 8, mPaint);
		canvas.drawText(sMin + "", getWidth() / 2 + sHour.length() * 8, getHeight() / 2 + 12, mPaint);
		canvas.drawBitmap(newBitmap, 0, 0, null);  
		
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Style.FILL_AND_STROKE);
		mPaint.setTextSize(25.0f);
		String date = Utils.getDate();
		canvas.drawText(date, 8, getHeight() / 2 + getHeight() / 3 + 10, mPaint);
		
		canvas.drawText(Utils.getWeekOfDate(), getWidth() / 2 - date.length(), 
				getHeight() / 2 + getHeight() / 3 + 10, mPaint);
		
		String am_pm = null;
		if (Utils.getHour() >= 12)
			am_pm = getResources().getString(R.string.pm);
		else
			am_pm = getResources().getString(R.string.am);
		
		canvas.drawText(am_pm, getWidth() - date.length() * 6 - Utils.getWeekOfDate().length() * 8,
				getHeight() / 2 + getHeight() / 3 + 10, mPaint);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	

}
