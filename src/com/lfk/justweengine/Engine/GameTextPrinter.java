package com.lfk.justweengine.Engine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * 文字绘制类
 *
 * @author liufengkai
 *         Created by liufengkai on 15/11/27.
 */
public class GameTextPrinter {
    protected Canvas e_canvas;
    private Paint e_paint;
    protected float e_x, e_y;
    private float e_spaceing;
    private String text;

    public GameTextPrinter() {
        this(null);
    }

    public GameTextPrinter(Canvas e_canvas) {
        this.e_canvas = e_canvas;
        e_paint = new Paint();
        e_x = e_y = 0;
        e_spaceing = 22;
    }

    public GameTextPrinter(Canvas e_canvas, String strFontName) {
        this.e_canvas = e_canvas;
        e_paint = new Paint();
        Typeface font = Typeface.create(strFontName, Typeface.NORMAL);
        e_paint.setTypeface(font);
        e_x = e_y = 0;
        e_spaceing = 22;
    }

    
    public void setCanvas(Canvas e_canvas) {
        this.e_canvas = e_canvas;
    }

    public void setLineSpaceing(float e_spaceing) {
        this.e_spaceing = e_spaceing;
    }

    public void setTextSize(int size) {
        e_paint.setTextSize(size);
    }

    public void setTextColor(int color) {
        e_paint.setColor(color);
    }
    
    public void setTextFont(Context context,String path) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), path);
        e_paint.setTypeface(font);
    }

    public void drawText(String text, float x, float y) {
        e_x = x;
        e_y = y;
        drawText(text);
    }

    public void drawText(String text) {
        e_canvas.drawText(text, e_x, e_y, e_paint);
        e_y += e_spaceing;
    }

    public void drawText(String text, float x, float y, float angle) {
        e_x = x;
        e_y = y;
        e_canvas.save();
        if(angle != 0){
        	e_canvas.rotate(-angle, x, y);
        }
        drawText(text);
        e_canvas.restore();
    }

    
    public void setText(String text) {
        this.text = text;
    }

    public void draw() {
        if (text != null)
            drawText(text);
    }

    public void setX(float e_x) {
        this.e_x = e_x;
    }

    public void setY(float e_y) {
        this.e_y = e_y;
    }

    public void setTextPosition(float e_x, float e_y) {
        this.e_x = e_x;
        this.e_y = e_y;
    }
}
