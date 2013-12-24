package com.unistrong.uniteqlauncher.adapter;

import java.util.ArrayList;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class AdapterPager extends PagerAdapter{

	private ArrayList<View> viewsList;
	
	public AdapterPager(ArrayList<View> list){
		
		this.viewsList = (ArrayList<View>) list;
		
	}
	
	public int getCount() {  
         
		return viewsList.size();  
         
	}  
 
	public boolean isViewFromObject(View arg0, Object arg1) { 
		
		return arg0 == arg1;  
		
	}  

    public int getItemPosition(Object object) {  
              	
        return POSITION_NONE;
        
    }  

    public void destroyItem(View arg0, int arg1, Object arg2) {  
    	 
    	((ViewPager) arg0).removeView((View) viewsList.get(arg1));  
    	
    }  
 
    public Object instantiateItem(View arg0, int arg1) {  
     	
    	((ViewPager) arg0).addView((View) viewsList.get(arg1));
  
        return viewsList.get(arg1);  
    }  
   
    public void restoreState(Parcelable arg0, ClassLoader arg1) {  

    }  

    public Parcelable saveState() {  

         return null;  
         
    }  

    public void startUpdate(View arg0) {  
     	
    }  

    public void finishUpdate(View arg0) {  

    }

}
