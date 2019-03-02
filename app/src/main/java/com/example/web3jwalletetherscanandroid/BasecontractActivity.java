package com.example.web3jwalletetherscanandroid;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

/**
 * Created by pc on 2018/1/23.
 */

public abstract class BasecontractActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initView();
        initData();
    }
    protected abstract  int getLayoutId();
    protected abstract void initView();
    protected abstract void initData();




}
