package com.unistrong.uniteqlauncher.ui;

import com.unistrong.uniteqlauncher.R;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;

/**
 *  �����������κν������ʾ��
 *  just for testing
 *  @author Baron.hu
 * */
public class FloatingWindow {
	
	private WindowManager wm = null;
	private WindowManager.LayoutParams mLayoutParams = null;
	private ImageButton mButton = null;
	private int x,y,xOffset,yOffset;
	
	private Context mContext;
	
	public FloatingWindow(Context context) {
		super();
		this.mContext = context;
	}
	
	public void showFloatingWindow() {
		wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		mLayoutParams = new WindowManager.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, LayoutParams.TYPE_SYSTEM_ERROR,
				LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
		mLayoutParams.format = 1;
		// mLayoutParams.format = PixelFormat.RGBA_8888;
		// mLayoutParams.type = 2002;
//		mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		//mLayoutParams.type = 2003;
//		mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		mLayoutParams.flags = 40;
		// mLayoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		// ����Ļ�����Ͻ�Ϊ��ʼֵ������x y�ĳ�ֵ
		// Ҳ��������LayoutParams�Ļ�׼�����Ըı�x y����ֵ����Ч��
		// x:X position for this window.ժ�Թٷ��ĵ�
		// When using LEFT or START or RIGHT or END it provides an offset from
		// the given edge.
		mLayoutParams.x = 0;
		mLayoutParams.y = 0;
//		mLayoutParams.width = 150;
//		mLayoutParams.height = 150;
		mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		// mLayoutParams.gravity = Gravity.CENTER;
		mLayoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
		mLayoutParams.alpha = 0.3f;
		mButton = new ImageButton(mContext);
		Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_launcher);
		mButton.setImageDrawable(drawable);
		wm.addView(mButton, mLayoutParams);
		
		// ΪButton���ô����¼�
		mButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
                // wm.removeView(mButton);
				// ������ʹ��getX��getY��Ч���ǲ������ģ�Ҫע��
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					xOffset = (int) event.getRawX();
			        yOffset = (int) event.getRawY();
			        x = mLayoutParams.x;
			        y = mLayoutParams.y;
					break;
				case MotionEvent.ACTION_MOVE:
					mLayoutParams.x = x + (int) event.getRawX() - xOffset;
		            mLayoutParams.y = y + (int) event.getRawY() - yOffset;
					wm.updateViewLayout(mButton, mLayoutParams);
					break;
				case MotionEvent.ACTION_UP:
					
					break;
				default :
					break;
				}
				// return true;
				return false;

			}

		});

	}

}
