package com.unistrong.uniteqlauncher.secondview;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimLaucherBg {

	private View view;
	private TranslateAnimation leftAnim;
	private TranslateAnimation rightAnim;

	public AnimLaucherBg(View v){
		
		this.view = v;
		
	}
	
	public Animation getAnim(){
		rightAnim = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -0.3f,
				Animation.RELATIVE_TO_PARENT, -1.5f,
				Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
				0f);
		leftAnim = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.5f,
				Animation.RELATIVE_TO_PARENT, -0.3f,
				Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
				0f);
		rightAnim.setDuration(25000);
		leftAnim.setDuration(25000);
		rightAnim.setFillAfter(true);
		leftAnim.setFillAfter(true);

		rightAnim.setAnimationListener(new Animation.AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
			}

			
			public void onAnimationRepeat(Animation animation) {
			}

			
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				view.startAnimation(leftAnim);
			}
		});
		leftAnim.setAnimationListener(new Animation.AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
			}

			
			public void onAnimationRepeat(Animation animation) {
			}

			
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				view.startAnimation(rightAnim);
			}
		});

		return leftAnim;
	}
	
}
