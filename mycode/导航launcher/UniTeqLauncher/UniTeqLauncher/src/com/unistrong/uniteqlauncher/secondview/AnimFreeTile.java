package com.unistrong.uniteqlauncher.secondview;

import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;

public class AnimFreeTile {

	private TranslateAnimation dropAnim;

	public AnimFreeTile(){
				
	}
	
	public Animation getAnim(float x, float y){
		dropAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, x, 
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, y);
		dropAnim.setFillAfter(true);
		dropAnim.setDuration(550);
		dropAnim.setInterpolator(new OvershootInterpolator(2f));
		
		return dropAnim;
	}
	
}
