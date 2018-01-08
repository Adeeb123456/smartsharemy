package mysmartshare.com.smartsharemy.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by adeeb on 6/21/2016.
 */
public class RectImageView extends RoundImageView {
    public RectImageView(Context context) {
        super(context);
    }

    public RectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()/2); //Snap to width
    }
}
