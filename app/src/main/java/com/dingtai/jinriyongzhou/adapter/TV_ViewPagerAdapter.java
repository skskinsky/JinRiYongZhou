package com.dingtai.jinriyongzhou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
  
public class TV_ViewPagerAdapter extends FragmentPagerAdapter{  
    ArrayList<Fragment> list;  
    public TV_ViewPagerAdapter(FragmentManager fm,ArrayList<Fragment> list) {  
        super(fm);  
        this.list = list;  
          
    }  
      
      
    @Override  
    public int getCount() {  
        return list.size();  
    }  
      
    @Override  
    public Fragment getItem(int arg0) {  
        return list.get(arg0);  
    }  
      
      
      
      
}  