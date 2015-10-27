package com.garik.android.gameoflife;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by garik on 23/10/2015.
 */
public class GameView extends View {

    private GameModel model;
    private int cellSize = 0;
    private Paint strokePaint;
    private Paint fillPaint;
    private boolean doUpdate = false;

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setup(GameModel model) {
        this.model = model;

        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(Color.LTGRAY);

        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(Color.BLUE);
    }

    private int getCellSize() {
        if(model == null)
            return 0;

        return Math.min(getMeasuredWidth() / model.getCols(),
                getMeasuredHeight() / model.getRows());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(model == null)
            return;

        cellSize = getCellSize();

        for (int r = 0; r < model.getRows(); r++) {
            for (int c = 0; c < model.getCols(); c++) {
                if(!model.isAlive(r, c))
                    continue;

                fillPaint.setColor(0xff000000 + model.getColor(r,c ));

                canvas.drawRect(c*cellSize, r*cellSize, c*cellSize + cellSize, r*cellSize + cellSize, fillPaint);
            }
        }
    }

    public void start(final int interval) {
        final Handler handler = new Handler();
        final Runnable updater = new Runnable() {
            @Override
            public void run() {
                if(!doUpdate)
                    return;

                handler.postDelayed(this, interval);
                if(model != null)
                    model.next();
                invalidate();
            }
        };
        doUpdate = true;
        handler.postDelayed(updater, interval);
    }

    public void stop() {
        doUpdate = false;
    }
}
