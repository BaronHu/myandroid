package com.example.alphabitmap;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	
	public static int alpha = 0;
	private MyView mv;
	private SeekBar bar;
	private ImageView heart;
	private ImageView rose;
	private AnimatorSet as;
	private ObjectAnimator oa;
	private LinearLayout ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rose = new ImageView(this);
		rose.setImageResource(R.drawable.love1);
		
		mv = new MyView(this);
		mv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 260));
		
		bar = new SeekBar(this);
		bar.setMax(255);
		bar.setProgress(alpha);
		
		heart = new ImageView(this);
		heart.setImageResource(R.drawable.heart);
		
		ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setBackgroundColor(Color.WHITE);
		ll.addView(mv);
		ll.addView(bar);
		
		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				alpha = progress;
				setTitle("alpha=" + alpha);
				mv.postInvalidate();
				as = new AnimatorSet();
				oa = ObjectAnimator.ofFloat(heart, "scaleX", 0.7f, 1.0f);
				oa.setDuration(1000);
				oa.setRepeatCount(5);
				if (alpha == 255) {
					ll.removeView(heart);
					ll.addView(heart);
					oa.addListener(al);
					as.play(oa);
					as.start();
				} else {
					as.removeListener(al);
					ll.removeView(heart);
					ll.removeView(rose);
				}
			}
		});
		
		setContentView(ll);
	}


	AnimatorListener al = new AnimatorListener() {
		
		@Override
		public void onAnimationStart(Animator animation) {
			
		}
		
		@Override
		public void onAnimationRepeat(Animator animation) {
			
		}
		
		@Override
		public void onAnimationEnd(Animator animation) {
			Log.d("baron", "animation end...");
			ll.removeView(heart);
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
			ll.removeView(rose);
			ll.addView(rose);
			
		}
		
		@Override
		public void onAnimationCancel(Animator animation) {
			as.removeAllListeners();
			ll.removeView(rose);
			
		}
	}; 
}
