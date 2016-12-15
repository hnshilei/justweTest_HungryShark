package com.lfk.justweengine.Engine.Layer;

import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.lfk.justweengine.Drawable.Sprite.BaseSub;
import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Utils.logger.LogLevel;
import com.lfk.justweengine.Utils.logger.Logger;

/**
 * 使用了Layer分层的Engine
 *
 * @author liufengkai
 *         Created by liufengkai on 16/5/5.
 */
public abstract class LayerEngine extends Engine {
    protected Screen layerEngineScreen;

    public abstract void init();

    public abstract void load();

    public abstract void update();

    public abstract void touch(MotionEvent event);

    public abstract void collision(BaseSub baseSub);

    public LayerEngine() {
        // init logger
        Logger.init().logLevel(LogLevel.NONE);
    }

    public LayerEngine(boolean isOpenDebug) {
        this.isOpenDebug = isOpenDebug;
        if (!isOpenDebug) {
            Logger.init().logLevel(LogLevel.NONE);
        } else {
            Logger.init();
        }
    }

    private void Engine() {
        this.layerEngineScreen = new Screen(this.getActivity(), new Screen.ScreenListener() {
            @Override
            public void Init() {
                init();
            }

            @Override
            public void Load() {
                load();
            }

            @Override
            public void Update() {
                update();
            }

            @Override
            public void Touch(MotionEvent event) {
                touch(event);
            }
        });
    }

    /**
     * engine onCreate
     *
     * @param savedInstanceState
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
        Logger.d("engine onCreate start");
        super.onCreate(savedInstanceState);

        Logger.d("engine onCreate start");

        this.Engine();
        this.getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
        this.getActivity().setContentView(layerEngineScreen);
        layerEngineScreen.createScreen();

        Logger.d("engine onCreate end");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * engine onResume
     */
    @Override
    public void onResume() {
        super.onResume();
        Logger.d("engine onResume");
        layerEngineScreen.restart();
        // need add...
    }

    /**
     * engine onPause
     */
    @Override
    public void onPause() {
        super.onPause();
        Logger.d("engine onPause");
        layerEngineScreen.pause();
        layerEngineScreen.addPauseTime();
        // need add...
    }

    /**
     * 设定整体背景
     *
     * @param color
     */
    public void setBackgroundColor(int color) {
        this.layerEngineScreen.setBackgroundColor(color);
    }

    /**
     * set screenOrientation
     *
     * @param mode
     */
    public void setScreenOrientation(ScreenMode mode) {
//        setRequestedOrientation(mode.value);
    }


    @Override
    public Canvas getCanvas() {
        return layerEngineScreen.getCanvas();
    }

    public Screen getScreen() {
        return layerEngineScreen;
    }

    @Override
    public void debugDraw(RectF bound) {
        layerEngineScreen.debugDraw(bound);
    }

    /**
     * 添加一层
     *
     * @param layer
     */
    public void addLayer(Layer layer) {
        layerEngineScreen.addLayer(layer);
    }

    /**
     * 在某层之上添加一层
     *
     * @param layer
     * @param name
     */
    public void addLayerBefore(Layer layer, String name) {
        layerEngineScreen.addLayerBefore(layer, name);
    }

    /**
     * 在某层之下添加一层
     *
     * @param layer
     * @param name
     */
    public void addLayerAfter(Layer layer, String name) {
        layerEngineScreen.addLayerAfter(layer, name);
    }


}
