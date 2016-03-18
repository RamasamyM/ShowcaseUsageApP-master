package com.example.admin.myapplication;

import android.graphics.Point;

/**
 * Created by Admin on 2/24/2016.
 */
public interface Target {
    Target NONE = new Target() {
        @Override
        public Point getPoint() {
            return new Point(1000000, 1000000);
        }
    };

    Point getPoint();
}

