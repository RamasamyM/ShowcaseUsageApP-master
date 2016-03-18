package com.example.admin.myapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.mobiwise.materialintro.animation.AnimationFactory;
import co.mobiwise.materialintro.animation.AnimationListener;
import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.prefs.PreferencesManager;
import co.mobiwise.materialintro.shape.Circle;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.target.Target;
import co.mobiwise.materialintro.target.ViewTarget;
import co.mobiwise.materialintro.utils.Constants;
import co.mobiwise.materialintro.utils.Utils;

/**
 * Created by Admin on 2/22/2016.
 */

public class CustomClassView extends RelativeLayout {
    private int maskColor;
    private long delayMillis;
    private boolean isReady;
    private boolean isFadeAnimationEnabled;
    private long fadeAnimationDuration;
    private Circle circleShape;
    private Focus focusType;
    private FocusGravity focusGravity;
    private Target targetView;
    private Paint eraser;
    private Handler handler;
    private Bitmap bitmap;
    private Canvas canvas;
    private int padding;
    private int width;
    private int height;
    private boolean dismissOnTouch;
    private View infoView;
    private TextView textViewInfo;
    private int colorTextViewInfo;
    private boolean isInfoEnabled;
    private View dotView;
    private boolean isDotViewEnabled;
    private PreferencesManager preferencesManager;
    private String materialIntroViewId;
    private boolean isLayoutCompleted;
    private MaterialIntroListener materialIntroListener;
    private boolean isPerformClick;

    public CustomClassView(Context context) {
        super(context);
        this.init(context);
    }

    public CustomClassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public CustomClassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }



    private void init(Context context) {
        this.setWillNotDraw(false);
        this.setVisibility(4);
        this.maskColor = Constants.DEFAULT_MASK_COLOR;
        this.delayMillis = Constants.DEFAULT_DELAY_MILLIS;
        this.fadeAnimationDuration = Constants.DEFAULT_FADE_DURATION;
        this.padding = Constants.DEFAULT_TARGET_PADDING;
        this.colorTextViewInfo = Constants.DEFAULT_COLOR_TEXTVIEW_INFO;
        this.focusType = Focus.ALL;
        this.focusGravity = FocusGravity.CENTER;
        this.isReady = false;
        this.isFadeAnimationEnabled = true;
        this.dismissOnTouch = false;
        this.isLayoutCompleted = false;
        this.isInfoEnabled = false;
        this.isDotViewEnabled = false;
        this.isPerformClick = false;
        this.handler = new Handler();
        this.preferencesManager = new PreferencesManager(context);
        this.eraser = new Paint();
        this.eraser.setColor(-1);
        this.eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.eraser.setFlags(1);
        View layoutInfo = LayoutInflater.from(this.getContext()).inflate(R.layout.card_layout, (ViewGroup)null);
        this.infoView = layoutInfo.findViewById(R.id.info_layout);
        this.textViewInfo = (TextView)layoutInfo.findViewById(co.mobiwise.materialintro.R.id.textview_info);
        this.textViewInfo.setTextColor(this.colorTextViewInfo);
        this.dotView = LayoutInflater.from(this.getContext()).inflate(co.mobiwise.materialintro.R.layout.dotview, (ViewGroup)null);
        this.dotView.measure(0, 0);
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                CustomClassView.this.circleShape.reCalculateAll();
                if(CustomClassView.this.circleShape != null && CustomClassView.this.circleShape.getPoint().y != 0 && !CustomClassView.this.isLayoutCompleted) {
                    if(CustomClassView.this.isInfoEnabled) {
                        CustomClassView.this.setInfoLayout();
                    }

                    if(CustomClassView.this.isDotViewEnabled) {
                        CustomClassView.this.setDotViewLayout();
                    }

                    CustomClassView.removeOnGlobalLayoutListener(CustomClassView.this, this);
                }

            }
        });
    }

    @TargetApi(16)
    public static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if(Build.VERSION.SDK_INT < 16) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.width = this.getMeasuredWidth();
        this.height = this.getMeasuredHeight();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(this.isReady) {
            if(this.bitmap == null || canvas == null) {
                if(this.bitmap != null) {
                    this.bitmap.recycle();
                }

                this.bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888);
                this.canvas = new Canvas(this.bitmap);
            }

            this.canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            this.canvas.drawColor(this.maskColor);
            this.circleShape.draw(this.canvas, this.eraser, this.padding);
            canvas.drawBitmap(this.bitmap, 0.0F, 0.0F, (Paint)null);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        float xT = event.getX();
        float yT = event.getY();
        int xV = this.circleShape.getPoint().x;
        int yV = this.circleShape.getPoint().y;
        int radius = this.circleShape.getRadius();
        double dx = Math.pow((double)(xT - (float)xV), 2.0D);
        double dy = Math.pow((double)(yT - (float)yV), 2.0D);
        boolean isTouchOnFocus = dx + dy <= Math.pow((double)radius, 2.0D);
        switch(event.getAction()) {
            case 0:
                if(isTouchOnFocus && this.isPerformClick) {
                    this.targetView.getView().setPressed(true);
                    this.targetView.getView().invalidate();
                }

                return true;
            case 1:
                if(isTouchOnFocus || this.dismissOnTouch) {
                    this.dismiss();
                }
                if(isTouchOnFocus && this.isPerformClick) {
                    this.targetView.getView().performClick();
                    this.targetView.getView().setPressed(true);
                    this.targetView.getView().invalidate();
                    this.targetView.getView().setPressed(false);
                    this.targetView.getView().invalidate();
                }
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    private void show(Activity activity) {
        if(!this.preferencesManager.isDisplayed(this.materialIntroViewId)) {
            ((ViewGroup)activity.getWindow().getDecorView()).addView(this);
            this.setReady(true);
            this.handler.postDelayed(new Runnable() {
                public void run() {
                    if(CustomClassView.this.isFadeAnimationEnabled) {
                        AnimationFactory.animateFadeIn(CustomClassView.this, CustomClassView.this.fadeAnimationDuration, new AnimationListener.OnAnimationStartListener() {
                            public void onAnimationStart() {
                                CustomClassView.this.setVisibility(0);
                            }
                        });
                    } else {
                        CustomClassView.this.setVisibility(0);
                    }

                }
            }, this.delayMillis);
        }
    }

    private void dismiss() {
        this.preferencesManager.setDisplayed(this.materialIntroViewId);
        AnimationFactory.animateFadeOut(this, this.fadeAnimationDuration, new AnimationListener.OnAnimationEndListener() {
            public void onAnimationEnd() {
                CustomClassView.this.setVisibility(8);
                CustomClassView.this.removeMaterialView();
                if(CustomClassView.this.materialIntroListener != null) {
                    CustomClassView.this.materialIntroListener.onUserClicked(CustomClassView.this.materialIntroViewId);
                }

            }
        });
    }

    private void removeMaterialView() {
        if(this.getParent() != null) {
            ((ViewGroup)this.getParent()).removeView(this);
        }

    }

    private void setInfoLayout() {
        this.handler.post(new Runnable() {
            public void run() {
                CustomClassView.this.isLayoutCompleted = true;
                if(CustomClassView.this.infoView.getParent() != null) {
                    ((ViewGroup)CustomClassView.this.infoView.getParent()).removeView(CustomClassView.this.infoView);
                }

                LayoutParams infoDialogParams = new LayoutParams(-1, -1);
                if(CustomClassView.this.circleShape.getPoint().y < CustomClassView.this.height / 2) {
                    ((RelativeLayout)CustomClassView.this.infoView).setGravity(48);
                    infoDialogParams.setMargins(0, CustomClassView.this.circleShape.getPoint().y + CustomClassView.this.circleShape.getRadius(), 0, 0);
                } else {
                    ((RelativeLayout)CustomClassView.this.infoView).setGravity(80);
                    infoDialogParams.setMargins(0, 0, 0, CustomClassView.this.height - (CustomClassView.this.circleShape.getPoint().y + CustomClassView.this.circleShape.getRadius()) + 2 * CustomClassView.this.circleShape.getRadius());
                }

                CustomClassView.this.infoView.setLayoutParams(infoDialogParams);
                CustomClassView.this.infoView.postInvalidate();
                CustomClassView.this.addView(CustomClassView.this.infoView);
                CustomClassView.this.infoView.setVisibility(0);
            }
        });
    }

    private void setDotViewLayout() {
        this.handler.post(new Runnable() {
            public void run() {
                if(CustomClassView.this.dotView.getParent() != null) {
                    ((ViewGroup)CustomClassView.this.dotView.getParent()).removeView(CustomClassView.this.dotView);
                }

                LayoutParams dotViewLayoutParams = new LayoutParams(-1, -1);
                dotViewLayoutParams.height = Utils.dpToPx(Constants.DEFAULT_DOT_SIZE);
                dotViewLayoutParams.width = Utils.dpToPx(Constants.DEFAULT_DOT_SIZE);
                dotViewLayoutParams.setMargins(CustomClassView.this.circleShape.getPoint().x - dotViewLayoutParams.width / 2, CustomClassView.this.circleShape.getPoint().y - dotViewLayoutParams.height / 2, 0, 0);
                CustomClassView.this.dotView.setLayoutParams(dotViewLayoutParams);
                CustomClassView.this.dotView.postInvalidate();
                CustomClassView.this.addView(CustomClassView.this.dotView);
                CustomClassView.this.dotView.setVisibility(0);
                AnimationFactory.performAnimation(CustomClassView.this.dotView);
            }
        });
    }



    private void setDelay(int delayMillis) {
        this.delayMillis = (long)delayMillis;
    }

    private void enableFadeAnimation(boolean isFadeAnimationEnabled) {
        this.isFadeAnimationEnabled = isFadeAnimationEnabled;
    }

    private void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    private void setTarget(Target target)
    {
        this.targetView = target;
    }

    private void setFocusType(Focus focusType)
    {
        this.focusType = focusType;
    }

    private void setCircle(Circle circleShape)
    {
        this.circleShape = circleShape;
    }




    private void setFocusGravity(FocusGravity focusGravity)
    {
        this.focusGravity = focusGravity;
    }



    private void setTextViewInfo(String textViewInfo)
    {
        this.textViewInfo.setText(textViewInfo);
    }


    private void enableInfoDialog(boolean isInfoEnabled)
    {
        this.isInfoEnabled = isInfoEnabled;
    }

    private void enableDotView(boolean isDotViewEnabled) {
        this.isDotViewEnabled = isDotViewEnabled;
    }



    private void setUsageId(String materialIntroViewId) {
        this.materialIntroViewId = materialIntroViewId;
    }



    private void setPerformClick(boolean isPerformClick) {
        this.isPerformClick = isPerformClick;
    }



    public static class Builder {
        private CustomClassView customView;
        private Activity activity;
        private Focus focusType;

        public Builder(Activity activity) {
            this.focusType = Focus.MINIMUM;
            this.activity = activity;
            this.customView = new CustomClassView(activity);
        }

        public void setTarget(final Target target) {
            setAcheive(target);

        }

        public CustomClassView.Builder setDelayMillis(int delayMillis) {
            this.customView.setDelay(delayMillis);
            return this;
        }

        public CustomClassView.Builder enableFadeAnimation(boolean isFadeAnimationEnabled) {
            this.customView.enableFadeAnimation(isFadeAnimationEnabled);
            return this;
        }

        public CustomClassView.Builder setFocusType(Focus focusType) {
            this.customView.setFocusType(focusType);
            return this;
        }

        public CustomClassView.Builder setFocusGravity(FocusGravity focusGravity) {
            this.customView.setFocusGravity(focusGravity);
            return this;
        }

        public CustomClassView.Builder setTarget(View view) {
            this.customView.setTarget(new ViewTarget(view));
            return this;
        }
        public Builder setAcheive(Target target) {
            customView.setTarget(target);
            return this;
        }

        public CustomClassView.Builder setInfoText(String infoText) {
            this.customView.enableInfoDialog(true);
            this.customView.setTextViewInfo(infoText);
            return this;
        }
        public CustomClassView.Builder setUsageId(String materialIntroViewId) {
            this.customView.setUsageId(materialIntroViewId);
            return this;
        }

        public CustomClassView.Builder enableDotAnimation(boolean isDotAnimationEnabled) {
            this.customView.enableDotView(isDotAnimationEnabled);
            return this;
        }
        public CustomClassView.Builder performClick(boolean isPerformClick) {
            this.customView.setPerformClick(isPerformClick);
            return this;
        }
        public CustomClassView build() {
            Circle circle = new Circle(this.customView.targetView, this.customView.focusType, this.customView.focusGravity, this.customView.padding);
            this.customView.setCircle(circle);
            return this.customView;
        }

        public CustomClassView show() {
            this.build().show(this.activity);
            return this.customView;
        }



    }

}
