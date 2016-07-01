package de.gimik.app.allpresanapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

import de.gimik.app.allpresanapp.R;

/**
 * Created by Mr.Binh on 11/6/2015.
 */
public class CornerImageView extends ImageView {
    private int conrnerColor,borderSize, lineLong;
    private Paint cornerPaint;
    private int width, height;


    public CornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CornerImageView, 0, 0);
        try {
            conrnerColor = a.getInt(R.styleable.CornerImageView_cornerColor, Color.parseColor("#b1af9b"));
            borderSize = a.getInt(R.styleable.CornerImageView_borderSize, 5);
            lineLong = a.getInt(R.styleable.CornerImageView_lineLong, 20);
        }finally {
            a.recycle();
        }
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init(){
        cornerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cornerPaint.setColor(conrnerColor);
        cornerPaint.setStrokeWidth(borderSize);
    }

    public int getConrnerColor() {
        return conrnerColor;
    }

    public void setConrnerColor(int conrnerColor) {
        this.conrnerColor = conrnerColor;
        invalidate();
    }

    public int getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(int borderSize) {
        this.borderSize = borderSize;
        invalidate();
        requestLayout();
    }

    public int getLineLong() {
        return lineLong;
    }

    public void setLineLong(int lineLong) {
        this.lineLong = lineLong;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        width = getWidth();
        height = getHeight();

        canvas.drawLine(0, 0, lineLong, 0, cornerPaint);
        canvas.drawLine(0, 0, 0, lineLong, cornerPaint);
        canvas.drawLine(width - lineLong, 0, width, 0, cornerPaint);
        canvas.drawLine(width, 0, width, lineLong, cornerPaint);

        canvas.drawLine(width, height - lineLong, width, height, cornerPaint);
        canvas.drawLine(width, height, width - lineLong, height, cornerPaint);
        canvas.drawLine(lineLong, height, 0, height, cornerPaint);
        canvas.drawLine(0, height, 0, height - lineLong, cornerPaint);
        super.onDraw(canvas);
    }
}
