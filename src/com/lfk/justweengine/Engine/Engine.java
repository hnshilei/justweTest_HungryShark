package com.lfk.justweengine.Engine;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by liufengkai on 16/5/8.
 */
public abstract class Engine extends Fragment {

    public enum ScreenMode {
        LANDSCAPE(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE),
        PORTRAIT(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        public int value;

        ScreenMode(int mode) {
            this.value = mode;
        }
    }

    protected boolean isOpenDebug;

    public abstract Canvas getCanvas();

    public abstract void debugDraw(RectF bound);
}
