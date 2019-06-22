package com.lixin.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class LineView extends View {

    private static final String TAG = "LineView";

    private int width, height;
    /**
     * 绘制坐标轴的画笔
     */
    private Paint axesPaint;
    /**
     * 绘制图的时候画笔
     */
    private Paint linePaint;

    /**
     * x最下面那条文字的画笔
     */
    private Paint xTextPaint;

    /**
     * y轴左边文字的画笔
     */
    private Paint yTextPaint;

    /**
     * 折线图转折点的画笔
     */
    private Paint dotPaint;

    private Paint whitePaint;

    //折线的Path
    private Path path;

    private int[] data;
    private String[] lables;
    /**
     * 存放横坐标的值
     */
    private float x[];
    /**
     * 存放纵坐标的值
     */
    private float y[];

    private DisplayMetrics dm;

    /**
     * 折线的颜色
     */
    private int lineColor;
    /**
     * 折线的粗细
     */
    private float lineWidth;

    /**
     * x轴上文字的大小
     */
    private float xTextSize;

    /**
     * x轴上文字颜色
     */
    private int xTextColor;
    /**
     * x轴上文字的大小
     */
    private float yTextSize;

    /**
     * 折线点的半径大小
     */
    private float dotRadius;

    /**
     * 折线点的宽度
     */
    private float dotWidth;

    /**
     * y轴文字的颜色
     */
    private int yTextColor;



    private final int[] yLabels = {15, 12, 9, 6, 3, 0};

    public LineView(Context context) {
        this(context, null);
    }

    public LineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.LineView);
        lineColor = ta.getColor(R.styleable.LineView_line_colors, ContextCompat.getColor(getContext(), R.color.colorAccent));
        lineWidth = ta.getDimension(R.styleable.LineView_line_Width, 3);
        dotRadius = ta.getDimension(R.styleable.LineView_dot_Radius, 10);
        dotWidth = ta.getDimension(R.styleable.LineView_dot_Width,7);
        xTextSize = ta.getDimension(R.styleable.LineView_xTextSize, getResources().getDimension(R.dimen.x_text_size));
        xTextColor = ta.getColor(R.styleable.LineView_xTextColor, ContextCompat.getColor(getContext(), R.color.black));
        yTextSize = ta.getDimension(R.styleable.LineView_yTextSize, getResources().getDimension(R.dimen.y_text_size));
        yTextColor = ta.getColor(R.styleable.LineView_yTextColor,ContextCompat.getColor(getContext(),R.color.black));

        ta.recycle();
        init();
    }

    private void init() {
        dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);

        width = 1000;
        height = 400;
        axesPaint = new Paint();
        axesPaint.setColor(yTextColor);
        axesPaint.setAntiAlias(true);

        xTextPaint = new Paint();
        xTextPaint.setColor(xTextColor);
        xTextPaint.setAntiAlias(true);
        xTextPaint.setTextSize(xTextSize);


        yTextPaint = new Paint();
        yTextPaint.setColor(yTextColor);
        yTextPaint.setAntiAlias(true);
        yTextPaint.setTextSize(yTextSize);

        dotPaint = new Paint();
        dotPaint.setColor(lineColor);
        dotPaint.setAntiAlias(true);
        dotPaint.setStyle(Paint.Style.STROKE);
        dotPaint.setStrokeWidth(dotWidth);

        whitePaint = new Paint();
        whitePaint.setColor(getResources().getColor(android.R.color.white));
        whitePaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setColor(lineColor);
        linePaint.setAntiAlias(true);
        path = new Path();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        if (modeWidth == MeasureSpec.EXACTLY) {
            width = sizeWidth;
        }
        if (modeHeight == MeasureSpec.EXACTLY) {
            height = Math.min(sizeHeight, sizeWidth);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data == null || lables == null) {
            return;
        }
        if (data.length == 0 || lables.length == 0) {
            return;
        }

        canvas.save();

        x = new float[lables.length];
        y = new float[data.length];
        drawXTextView(canvas);
        drawXAxes(canvas);
        drawLine(canvas);

        canvas.restore();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        return super.onTouchEvent(event);
    }

    /**
     * 绘制折现
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        path.reset();
        for (int i = 0; i < y.length; i++) {

            /**
             * 绘制折线
             */
            if (i == 0) {
                path.moveTo(x[i], y[i]);
            } else {
                path.lineTo(x[i], y[i]);
            }
            linePaint.setStyle(Paint.Style.STROKE);//设置空
            canvas.drawPath(path, linePaint);
        }
        /**
         * 画 圆点
         */
        for (int i = 0; i < y.length; i++){
            canvas.drawCircle(x[i], y[i], dotRadius, whitePaint);
            canvas.drawCircle(x[i],y[i],dotRadius,dotPaint);
        }

    }

    /**
     *绘制X轴上面的横线
     *
     * @param canvas
     */
    private void drawXAxes(Canvas canvas) {

        // 这个是 值为15的那条横线的 坐标的值
        final float topLineCoordinateValue = height / 7;
        for (int i = 0; i < yLabels.length; i++) {
            String str = String.valueOf(yLabels[i]);
            if (str.length() == 1) {
                str = "\r\r" + str; //加两个占位符 让字体看起来排列一直
            }
            canvas.drawText(str, getPaddingLeft(), topLineCoordinateValue * (i+1), yTextPaint);
            if (i == yLabels.length - 1) {
                axesPaint.setColor(xTextColor);
                canvas.drawLine(getPaddingLeft() * 2, topLineCoordinateValue * (i + 1), width - getPaddingRight(), height / (yLabels.length + 1) * (i + 1), axesPaint);
                axesPaint.setColor(yTextColor);
            } else {
                canvas.drawLine(getPaddingLeft() * 2, topLineCoordinateValue * (i + 1), width - getPaddingRight(), height / (yLabels.length + 1) * (i + 1), axesPaint);
            }

        }


        for (int i = 0; i < data.length; i++) {
            /**
             *  值为15的横线的纵坐标的值 * 6 正好的 值为0的横线纵坐标的值
             *
             */
            y[i] = topLineCoordinateValue * 6 - ((topLineCoordinateValue / 3) * data[i]);
        }

    }

    /**
     *
     * 绘制x轴上面的文字
     * @param canvas
     */
    private void drawXTextView(Canvas canvas) {
        /**
         * 绘制X横坐标
         */

        for (int i = 0; i < lables.length; i++) {
            if (i == lables.length - 1) {
                xTextPaint.setColor(lineColor);
                canvas.drawText(lables[i], ((width - getPaddingLeft() - getPaddingRight()) / lables.length) * (i + 1), height - 18, xTextPaint);
                xTextPaint.setColor(xTextColor);
            } else {
                canvas.drawText(lables[i], ((width - getPaddingLeft() - getPaddingRight()) / lables.length) * (i + 1), height - 18, xTextPaint);
            }
            x[i] = ((width - getPaddingLeft() - getPaddingRight()) / lables.length) * (i + 1) + 30;
        }
    }

    public void setLables(String[] lables) {
        this.lables = lables;
        postInvalidate();
    }

    public void setData(int[] data) {
        this.data = data;
        postInvalidate();
    }

}
