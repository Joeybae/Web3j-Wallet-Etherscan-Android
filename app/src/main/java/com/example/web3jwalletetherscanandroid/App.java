package com.example.web3jwalletetherscanandroid;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;


/**
 * Created by pc on 2018/1/22.
 */

public class App extends Application {
    private static Context instance;

    public static Context getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        MultiDex.install(this) ;

    }
}
