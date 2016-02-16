package com.example.admin.myapplication;

import android.support.v4.app.FragmentActivity;

/**
 * Created by Admin on 2/4/2016.
 */

public abstract class DemoBase extends FragmentActivity {

    protected String[] mParties = new String[] {
            "Equity/Growth Funds", "Debt/Income Funds ", "Balanced Funds ", "Money Market/ Liquid Funds", "Gilt Funds"
    };

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }
}