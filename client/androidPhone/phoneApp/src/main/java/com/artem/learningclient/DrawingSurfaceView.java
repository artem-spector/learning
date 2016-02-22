package com.artem.learningclient;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.artem.client.drawing.DrawingRawData;

/**
 * created by artem on 1/23/16.
 */
public class DrawingSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawingRawData rawData = new DrawingRawData();
    private DrawingProcessor drawingProcessor;

    public DrawingSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("DrawingProcessor", "surface view created");

        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawingProcessor = new DrawingProcessor(rawData);
        drawingProcessor.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        drawingProcessor.setSurface(holder);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() > 1) {
            return false;
        }
        return drawingProcessor.onTouch(event);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawingProcessor.clearSurface();
    }

    public DrawingRawData getData() {
        return rawData;
    }

    public void clear() {
        drawingProcessor.clear();
    }

}
