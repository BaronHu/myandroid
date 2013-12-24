package com.unistrong.uniteqlauncher.secondview;

import android.view.View;
import android.view.animation.Animation;

public class AnimationManager {

	public static Animation getDropItemAnim(float x, float y){
		
		return new AnimFreeTile().getAnim(x, y);
	}
	
	public static Animation getBackgroundAnim(View v){
		
		return new AnimLaucherBg(v).getAnim();
	}
	
}
