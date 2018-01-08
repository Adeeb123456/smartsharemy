package mysmartshare.com.smartsharemy.views;

/**
 * Created by Zahid on 2/25/2016.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.android.volley.toolbox.NetworkImageView;

import mysmartshare.com.smartsharemy.R;


/**
 * The Class RoundImageView.<br/>
 * <code>RoundImageView</code><br/> Is a Custom <code>ImageView</code> class.<br/> which
 * provides all the functionalities of rounded edges for the <code>ImageView</code> with gradient effect.<br/>
 * This Class is dependent on StreamDrawable from <b>RomainGuy</b> in his Image With Rounded Corners Demo but with little modifications.
 * @author siddhesh
 * @version 1.2
 * @see <a href ="https://android.googlesource.com/platform/frameworks/volley">Volley<a/>
 * @see <a href ="https://android.googlesource.com/platform/frameworks/volley/+/master/src/com/android/volley/toolbox/NetworkImageView.java">NetworkImageView<a/>
 * @see <a href ="http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/">Rounded Corner Images<a/>
 */
public class RoundImageView extends NetworkImageView {

    /** The m radius. */
    private int mRadius;

    /** The is circular. */
    private boolean isCircular;

    /** The drawable. */
    private StreamDrawable mDrawable;

    /** The m margin. */
    private int mMargin;

    /** The is shadowed. */
    private boolean isShadowed;

    /** The m circle paint. */
    private Paint mCirclePaint;

    /** The draw circle. */
    private boolean drawCircle;

    /** The m circle color. */
    private int mCircleColor;

    /**
     * Instantiates a new round image view.
     *
     * @param context the context
     */
    public RoundImageView(Context context) {
        this(context,null);
    }

    /**
     * Instantiates a new round image view.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public RoundImageView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    /**
     * Instantiates a new round image view.
     *
     * @param context the context
     * @param attribs the attribs
     * @param def the def
     */
    public RoundImageView(Context context, AttributeSet attribs, int def) {
        super(context,attribs,def);
        TypedArray a=getContext().obtainStyledAttributes(
                attribs,
                R.styleable.RoundImageView);
        mRadius = a.getInt(
                R.styleable.RoundImageView_radius,4);
        mMargin = a.getInt(
                R.styleable.RoundImageView_margin,0);
        isCircular = a.getBoolean(R.styleable.RoundImageView_isCircular, false);
        isShadowed = a.getBoolean(
                R.styleable.RoundImageView_isShadowPresent,false);

        drawCircle = a.getBoolean(R.styleable.RoundImageView_drawCircle, false);

        mCircleColor = a.getColor(R.styleable.RoundImageView_circleColor, Color.WHITE);

        int src_resource = attribs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src",0);
        if(src_resource != 0)
            setImageResource(src_resource);
        a.recycle();
        mCirclePaint = new Paint();
        mCirclePaint.setStrokeWidth(4f);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Style.STROKE);
        if(isCircular)
            isShadowed = false;
    }

    /**
     * Sets the radius.
     *
     * @param radius the new radius
     */
    public void setRadius(int radius){
        mRadius = radius;
    }

    /**
     * Sets the isCircular.
     *
     * @param isCircular true for circular ImageView
     */
    public void setCircular(boolean isCircular){
        this.isCircular = isCircular;
    }

    /**
     * Gets the radius.
     *
     * @return the radius
     */
    public int getRadius() {
        return mRadius;
    }

    /**
     * Checks if is circular.
     *
     * @return true, if is circular
     */
    public boolean isCircular() {
        return isCircular;
    }

    /**
     * Gets the margin.
     *
     * @return the m margin
     */
    public int getmMargin() {
        return mMargin;
    }

    /**
     * Sets the margin.
     *
     * @param margin the new margin
     */
    public void setmMargin(int margin) {
        this.mMargin = margin;
    }

    /* (non-Javadoc)
     * @see android.widget.ImageView#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isCircular && drawCircle)
            canvas.drawCircle((this.getWidth()*1.0f)/2, (this.getHeight()*1.0f)/2, (this.getHeight()*1.0f)/2-1.8f, mCirclePaint);
    }

    /* (non-Javadoc)
     * @see android.widget.ImageView#setImageBitmap(android.graphics.Bitmap)
     */
    @Override
    public void setImageBitmap(final Bitmap bm) {
        if (bm != null) {
            if(isCircular){
                mRadius = (int) Math.max(this.getWidth()/2, this.getHeight()/2);
                if(mRadius == 0){
                    ViewTreeObserver ob =  getViewTreeObserver();
                    ob.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN) @SuppressWarnings("deprecation")
                        @Override
                        public void onGlobalLayout() {
                            mRadius = (int) Math.max(RoundImageView.this.getWidth()/2, RoundImageView.this.getHeight()/2);
                            mDrawable = new StreamDrawable(bm, mRadius, mMargin,isShadowed,RoundImageView.this.getScaleType());
                            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            else
                                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            RoundImageView.super.setImageDrawable(mDrawable);
                        }
                    });
                }else{
                    mDrawable = new StreamDrawable(bm, mRadius, mMargin,isShadowed,this.getScaleType());
                    super.setImageDrawable(mDrawable);
                }
            }else{
                mDrawable = new StreamDrawable(bm, mRadius, mMargin,isShadowed,this.getScaleType());
                super.setImageDrawable(mDrawable);
            }

        } else {
            mDrawable = null;
        }


    }

    /* (non-Javadoc)
     * @see android.widget.ImageView#setImageResource(int)
     */
    @Override
    public void setImageResource(int resId) {
        setImageBitmap(BitmapFactory.decodeResource(getResources(), resId));

    }

    /* (non-Javadoc)
     * @see android.widget.ImageView#setImageDrawable(android.graphics.drawable.Drawable)
     */
    @Override
    public void setImageDrawable(Drawable drawable) {
        if(drawable == null){
            return;
        }
        if(drawable instanceof BitmapDrawable){
            setImageBitmap(((BitmapDrawable)drawable).getBitmap());
        }else if(drawable instanceof StreamDrawable){
            super.setImageDrawable(drawable);
            //TODO: CHECK AND ADD SUPPORT FOR COLOR DRAWABLE
        }else{
            int width = drawable.getIntrinsicWidth() == -1?60:drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight() == -1?60:drawable.getIntrinsicHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            setImageBitmap(bitmap);
        }
    }

}