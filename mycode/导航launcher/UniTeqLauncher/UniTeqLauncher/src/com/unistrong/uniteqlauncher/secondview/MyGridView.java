package com.unistrong.uniteqlauncher.secondview;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

public class MyGridView extends GridView {

	private int dragPosition;
	private int dropPosition;

	private ImageView dragImageView;

	private WindowManager windowManager;
	private WindowManager.LayoutParams windowParams;

	private int itemHeight, itemWidth;

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyGridView(Context context) {
		super(context);
	}

	boolean flag = false;

	public void setLongFlag(boolean temp) {
		
		flag = temp;

	}
	
	public boolean setOnItemLongClickListener(final MotionEvent ev) {
		this.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				int x = (int) ev.getX();
				int y = (int) ev.getY();
				dragPosition = dropPosition = arg2;
				
				if(dragPosition == 0){
					return false;
				}
				
				System.out.println(dragPosition + "--" + arg2);
				if (dragPosition == AdapterView.INVALID_POSITION) {

				}
				ViewGroup itemView = (ViewGroup) getChildAt(dragPosition
						- getFirstVisiblePosition());
				itemHeight = itemView.getHeight();
				itemWidth = itemView.getWidth();
				itemView.destroyDrawingCache();
				itemView.setDrawingCacheEnabled(true);
				Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache());
				startDrag(bm, x, y);
				return false;
			};
		});
		return super.onInterceptTouchEvent(ev);
	}

	
	//该方法会监听所有的触屏事件
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			//如果为  触屏按下事件，则调用自定义的setOnItemLongClickListener
			return setOnItemLongClickListener(ev);
		}
		return super.onInterceptTouchEvent(ev);
	}

	private void startDrag(Bitmap bm, int x, int y) {
		stopDrag();
		windowParams = new WindowManager.LayoutParams();
		windowParams.gravity = Gravity.TOP | Gravity.LEFT;
		windowParams.x = x - itemWidth / 2;
		windowParams.y = y - itemHeight / 2;
		windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;

		ImageView iv = new ImageView(getContext());
		iv.setImageBitmap(bm);
		windowManager = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);// "window"
		windowManager.addView(iv, windowParams);
		dragImageView = iv;
	}

	
	public boolean onTouchEvent(MotionEvent ev) {
		if (dragImageView != null
				&& dragPosition != AdapterView.INVALID_POSITION) {
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			switch (ev.getAction()) {
			case MotionEvent.ACTION_MOVE:
				onDrag(x, y);
				break;
			case MotionEvent.ACTION_UP:
				stopDrag();
//				onDrop(x, y);
				break;
			}
		}
		return super.onTouchEvent(ev);
	}

	private void onDrag(int x, int y) {
		if (dragImageView != null) {
			windowParams.alpha = 0.6f;
			windowParams.x = x - itemWidth / 2;
			windowParams.y = y - itemHeight / 2;
			windowManager.updateViewLayout(dragImageView, windowParams);
		}
	}

	private void onDrop(int x, int y) {
		
		int tempPosition = pointToPosition(x, y);
		if (tempPosition != AdapterView.INVALID_POSITION && tempPosition != 0) {
			dropPosition = tempPosition;
		}
		ViewGroup fromView = (ViewGroup) getChildAt(dragPosition- getFirstVisiblePosition());
		ViewGroup toView = (ViewGroup) getChildAt(dropPosition	- getFirstVisiblePosition());
		
		if (dropPosition != dragPosition) {
			if(dragPosition%2 == 0){
			
				
				Animation a = AnimationManager.getDropItemAnim((dropPosition%2==dragPosition%2)?0:1,(dropPosition/2-dragPosition/2));
				fromView.startAnimation(a);
				
				toView.startAnimation(AnimationManager.getDropItemAnim((dragPosition%2==dropPosition%2)?0:-1,(dragPosition/2-dropPosition/2)));
				final AdapterMyView adapter = (AdapterMyView) this.getAdapter();
				a.setAnimationListener(new Animation.AnimationListener() {		
					
					public void onAnimationStart(Animation arg0) {}
					
					public void onAnimationRepeat(Animation arg0) {}
					
					
					public void onAnimationEnd(Animation arg0) {
						// TODO Auto-generated method stub
						adapter.exchange(dragPosition, dropPosition);
					}
				});
			}
			else{
				Animation a = AnimationManager.getDropItemAnim((dropPosition%2==dragPosition%2)?0:-1,(dropPosition/2-dragPosition/2));
				fromView.startAnimation(a);
				toView.startAnimation(AnimationManager.getDropItemAnim((dragPosition%2==dropPosition%2)?0:1,(dragPosition/2-dropPosition/2)));
				final AdapterMyView adapter = (AdapterMyView) this.getAdapter();
				a.setAnimationListener(new Animation.AnimationListener() {		
					
					public void onAnimationStart(Animation arg0) {}
					
					public void onAnimationRepeat(Animation arg0) {}
					
					
					public void onAnimationEnd(Animation arg0) {
						// TODO Auto-generated method stub
						adapter.exchange(dragPosition, dropPosition);
					}
				});
			}
		}
	}

	private void stopDrag() {
		if (dragImageView != null) {
			windowManager.removeView(dragImageView);
			dragImageView = null;
		}
	}
	
	
}