package com.example.net;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveState {

    private String stateName ;
    private Context context;
    private SharedPreferences sp;

    public SaveState(String stateName, Context context) {
        this.stateName = stateName;
        this.context = context;
        this.sp = context.getSharedPreferences(stateName , context.MODE_PRIVATE);
    }

    public void setState(int key){
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("key" , key);
        editor.apply();
    }

    public int getState(){
        return sp.getInt("key" , 0);
    }
}
