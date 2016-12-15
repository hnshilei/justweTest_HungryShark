package com.lfk.justweengine.Engine.Layer;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lfk.justweengine.Info.UIdefaultData;

/**
 * 默认的分层引擎实现
 * 添加了默认的主布局层
 * Created by liufengkai on 16/5/9.
 */
public abstract class SimpleLayerEngine extends LayerEngine {
    // 默认Layer层
    private DefaultLayer defaultMainLayer;
    // 默认Layer监听器
    private Layer.LayerListener defaultMainLayerListener;

    public SimpleLayerEngine() {

    }

    public SimpleLayerEngine(boolean isOpenDebug) {
        super(isOpenDebug);

    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        addDefaultLayer();
//    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	addDefaultLayer();
    	return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 添加默认Layer
     */
    private void addDefaultLayer() {
        defaultMainLayer = new DefaultLayer(getScreen(),
                new Rect(0, 0, UIdefaultData.screenWidth,
                        UIdefaultData.screenHeight));
        defaultMainLayer.setLayerName("MainLayer");
        layerEngineScreen.addLayer(defaultMainLayer);
    }

    public Layer.LayerListener getDefaultMainLayerListener() {
        return defaultMainLayerListener;
    }

    public void setDefaultMainLayerListener(Layer.LayerListener defaultMainLayerListener) {
        this.defaultMainLayerListener = defaultMainLayerListener;
    }

    public DefaultLayer getDefaultMainLayer() {
        return defaultMainLayer;
    }
}
