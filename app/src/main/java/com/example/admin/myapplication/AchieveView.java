package com.example.admin.myapplication;

import android.graphics.Point;
import android.view.View;
import  android.app.Activity;

/**
 * Created by Admin on 2/24/2016.
 */

public class AchieveView implements Target {

    private final View mView;

    public AchieveView(View view) {
        mView = view;
    }
public AchieveView(int viewId, Activity activity) {
        mView = activity.findViewById(viewId);
    }



    @Override
    public Point getPoint() {
        int[] location = new int[2];
        mView.getLocationInWindow(location);
        int x = location[0] + mView.getWidth() / 2;
        int y = location[1] + mView.getHeight() / 2;
        return new Point(x, y);
    }
}