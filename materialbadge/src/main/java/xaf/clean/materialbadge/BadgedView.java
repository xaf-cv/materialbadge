package xaf.clean.materialbadge;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.FrameLayout;

/**
 * Created by xAF (about.me/0x0af) at 0x0af@ukr.net
 * <p/>
 * 2/26/2016 / 6:43 AM
 */
public class BadgedView extends FrameLayout {

    private static final String DEF_BADGE_TEXT = "x";
    private static final int DEF_BADGE_TEXT_COLOR = Color.WHITE;
    private static final int DEF_BADGE_TEXT_SIZE_SP = 10;
    private static final int DEF_BADGE_COLOR = Color.RED;
    private static final int DEF_OFFSET_X_DP = 4;
    private static final int DEF_OFFSET_Y_DP = 4;
    private static final int DEF_BADGE_SIZE_DP = 16;

    public static final int POSITION_START = 1;
    public static final int POSITION_TOP = 2;
    public static final int POSITION_END = 4;
    public static final int POSITION_BOTTOM = 8;

    private static final int DEF_POSITION = POSITION_TOP | POSITION_END;

    public static final int SHAPE_HEXAGON = 0;
    public static final int SHAPE_ROMB = 1;
    public static final int SHAPE_SQUARE = 2;
    public static final int SHAPE_ROMB_FLUFF = 3;
    public static final int SHAPE_CIRCLE = 4;
    public static final int SHAPE_SQUARE_FLUFF = 5;
    public static final int SHAPE_OCTAGON = 6;
    public static final int SHAPE_STAR = 7;

    private static final int DEF_SHAPE = SHAPE_CIRCLE;

    private String badgeText;
    private int badgeTextColor;
    private float badgeTextSize;
    private int badgeColor;
    private float badgeSize;
    private float badgeOffsetX;
    private float badgeOffsetY;
    private int badgePosition;
    private int badgeShape;

    private boolean badgeIsVisible;

    private Paint bTextPaint;
    private Paint bBadgePaint;

    private float bCenterX;
    private float bCenterY;

    public BadgedView(Context context) {
        super(context);
        initAttributes(context, null, 0, 0);
    }

    public BadgedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context, attrs, 0, 0);
    }

    public BadgedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public BadgedView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttributes(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BadgedView, defStyleAttr, defStyleRes);

        try {
            badgeIsVisible = a.getBoolean(R.styleable.BadgedView_badgeIsVisible, true);
            badgeText = (a.getString(R.styleable.BadgedView_badgeText) != null) ? a.getString(R.styleable.BadgedView_badgeText) : DEF_BADGE_TEXT;
            badgeTextColor = a.getColor(R.styleable.BadgedView_badgeTextColor, DEF_BADGE_TEXT_COLOR);
            badgeTextSize = a.getDimension(R.styleable.BadgedView_badgeTextSize,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEF_BADGE_TEXT_SIZE_SP,
                            context.getResources().getDisplayMetrics()));
            badgeColor = a.getColor(R.styleable.BadgedView_badgeColor, DEF_BADGE_COLOR);
            badgeSize = a.getDimension(R.styleable.BadgedView_badgeSize,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_BADGE_SIZE_DP,
                            context.getResources().getDisplayMetrics()));
            badgeOffsetX = a.getDimension(R.styleable.BadgedView_badgeOffsetX,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_OFFSET_X_DP,
                            context.getResources().getDisplayMetrics()));
            badgeOffsetY = a.getDimension(R.styleable.BadgedView_badgeOffsetY,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_OFFSET_Y_DP,
                            context.getResources().getDisplayMetrics()));
            badgePosition = a.getInt(R.styleable.BadgedView_badgePosition, DEF_POSITION);
            badgeShape = a.getInt(R.styleable.BadgedView_badgeShape, DEF_SHAPE);
        } catch (Exception e) {
            Log.e(getClass().getName(), "initAttributes exception", e);
        } finally {
            a.recycle();
        }

        Log.d(getClass().getName(), "initAttributes, badgeIsVisible: " + String.valueOf(badgeIsVisible));
        Log.d(getClass().getName(), "initAttributes, badgeText: " + String.valueOf(badgeText));
        Log.d(getClass().getName(), "initAttributes, badgeTextColor: " + String.valueOf(badgeTextColor));
        Log.d(getClass().getName(), "initAttributes, badgeTextSize: " + String.valueOf(badgeTextSize));
        Log.d(getClass().getName(), "initAttributes, badgeColor: " + String.valueOf(badgeColor));
        Log.d(getClass().getName(), "initAttributes, badgeSize: " + String.valueOf(badgeSize));
        Log.d(getClass().getName(), "initAttributes, badgeOffsetX: " + String.valueOf(badgeOffsetX));
        Log.d(getClass().getName(), "initAttributes, badgeOffsetY: " + String.valueOf(badgeOffsetY));
        Log.d(getClass().getName(), "initAttributes, badgePosition: " + String.valueOf(badgePosition));
        Log.d(getClass().getName(), "initAttributes, badgeShape: " + String.valueOf(badgeShape));

        defineDrawingObjects();
        setWillNotDraw(false);
    }

    private void defineDrawingObjects() {
        bTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bTextPaint.setColor(badgeTextColor);
        if (badgeTextSize == 0) {
            badgeTextSize = bTextPaint.getTextSize();
        } else {
            bTextPaint.setTextSize(badgeTextSize);
        }
        bTextPaint.setTextAlign(Paint.Align.CENTER);
        bTextPaint.setTypeface(Typeface.DEFAULT);

        bBadgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bBadgePaint.setColor(badgeColor);
    }

    public String getBadgeText() {
        return badgeText;
    }

    public void setBadgeText(String badgeText) {
        this.badgeText = badgeText;
        invalidate();
        requestLayout();
    }

    public int getBadgeTextColor() {
        return badgeTextColor;
    }

    public void setBadgeTextColor(int badgeTextColor) {
        this.badgeTextColor = badgeTextColor;
        defineDrawingObjects();
        invalidate();
        requestLayout();
    }

    public int getBadgeColor() {
        return badgeColor;
    }

    public void setBadgeColor(int badgeColor) {
        this.badgeColor = badgeColor;
        defineDrawingObjects();
        invalidate();
        requestLayout();
    }

    public float getBadgeOffsetX() {
        return badgeOffsetX;
    }

    public void setBadgeOffsetX(float badgeOffsetX) {
        this.badgeOffsetX = badgeOffsetX;
        invalidate();
        requestLayout();
    }

    public float getBadgeOffsetY() {
        return badgeOffsetY;
    }

    public void setBadgeOffsetY(float badgeOffsetY) {
        this.badgeOffsetY = badgeOffsetY;
        invalidate();
        requestLayout();
    }

    public int getBadgePosition() {
        return badgePosition;
    }

    public void setBadgePosition(int badgePosition) {
        this.badgePosition = badgePosition;
        invalidate();
        requestLayout();
    }

    public int getBadgeShape() {
        return badgeShape;
    }

    public void setBadgeShape(int badgeShape) {
        this.badgeShape = badgeShape;
        invalidate();
        requestLayout();
    }

    public float getBadgeSize() {
        return badgeSize;
    }

    public void setBadgeSize(float badgeSize) {
        this.badgeSize = badgeSize;
        invalidate();
        requestLayout();
    }

    public boolean isBadgeVisible() {
        return badgeIsVisible;
    }

    public void setBadgeVisible(boolean badgeIsVisible) {
        this.badgeIsVisible = badgeIsVisible;
        invalidate();
        requestLayout();
    }

    private boolean isRTL() {
        return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Log.d(getClass().getName(), "onSizeChanged, w: " + String.valueOf(w) + ", h: " + String.valueOf(h));

        if ((badgePosition & POSITION_START) == POSITION_START) {
            bCenterX = !isRTL() ? 0 - badgeOffsetX : w + badgeOffsetX;
        } else if ((badgePosition & POSITION_END) == POSITION_END) {
            bCenterX = isRTL() ? 0 - badgeOffsetX : w + badgeOffsetX;
        }

        if ((badgePosition & POSITION_TOP) == POSITION_TOP) {
            bCenterY = 0 - badgeOffsetY;
        } else if ((badgePosition & POSITION_BOTTOM) == POSITION_BOTTOM) {
            bCenterY = h + badgeOffsetY;
        }

        Log.d(getClass().getName(), "onSizeChanged, bCenterX: " + String.valueOf(bCenterX)
                + ", bCenterY: " + String.valueOf(bCenterY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(getClass().getName(), "onDraw, bCenterX: " + String.valueOf(bCenterX)
                + ", bCenterY: " + String.valueOf(bCenterY));
        Log.d(getClass().getName(), "onDraw, badgeIsVisible: " + String.valueOf(badgeIsVisible));

        Rect boundsRect = canvas.getClipBounds();
        boundsRect.inset(-(int) (badgeSize + badgeOffsetX), -(int) (badgeSize + badgeOffsetY));
        canvas.clipRect(boundsRect, Region.Op.REPLACE);

        if (badgeIsVisible) {

            switch (badgeShape) {
                // TODO: add shape cases
//                case SHAPE_HEXAGON:
//                break;
                case SHAPE_ROMB:
                    canvas.save();
                    canvas.rotate(45, bCenterX, bCenterY);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        canvas.drawRoundRect(bCenterX - badgeSize / 2, bCenterY - badgeSize / 2,
                                bCenterX + badgeSize / 2, bCenterY + badgeSize / 2, badgeSize / 4, badgeSize / 4, bBadgePaint);
                    } else {
                        canvas.drawRect(bCenterX - badgeSize / 2, bCenterY - badgeSize / 2,
                                bCenterX + badgeSize / 2, bCenterY + badgeSize / 2, bBadgePaint);
                    }
                    canvas.restore();
                    break;
                case SHAPE_SQUARE:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        canvas.drawRoundRect(bCenterX - badgeSize / 2, bCenterY - badgeSize / 2,
                                bCenterX + badgeSize / 2, bCenterY + badgeSize / 2, badgeSize / 4, badgeSize / 4, bBadgePaint);
                    } else {
                        canvas.drawRect(bCenterX - badgeSize / 2, bCenterY - badgeSize / 2,
                                bCenterX + badgeSize / 2, bCenterY + badgeSize / 2, bBadgePaint);
                    }
                    break;
//                case SHAPE_ROMB_FLUFF:
//                break;
                case SHAPE_CIRCLE:
                    canvas.drawCircle(bCenterX, bCenterY, badgeSize / 2, bBadgePaint);
                    break;
//                case SHAPE_SQUARE_FLUFF:
                //                break;
//                case SHAPE_OCTAGON:
                //                break;
//                case SHAPE_STAR:
                //                break;
                default:
                    throw new RuntimeException("Wrong shape value: " + String.valueOf(badgeShape));
            }

            canvas.drawText(badgeText, bCenterX, bCenterY - ((bTextPaint.descent() + bTextPaint.ascent()) / 2), bTextPaint);
        }
    }
}
