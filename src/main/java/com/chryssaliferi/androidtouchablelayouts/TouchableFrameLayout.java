package com.chryssaliferi.androidtouchablelayouts;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class TouchableFrameLayout extends FrameLayout {

    private SparseArray<PointF> mActivePointers = new SparseArray<PointF>();

    private OnTouchListener onTouchListener;

    float lastXPosition;
    float lastYPosition;
    double lastdist = 0;

    Context ctx;
    private boolean onScaleMove = false;

    public TouchableFrameLayout(Context context) {
        super(context);
        ctx = context;
    }

    public TouchableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
    }

    public TouchableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ctx = context;
    }


    @Override
    public boolean dispatchTouchEvent(final MotionEvent event) {
        if (onTouchListener == null)
            return false;

        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                lastYPosition = event.getY();
                lastXPosition = event.getX();
                onTouchListener.onTouch();
                return true;

            case MotionEvent.ACTION_UP:
                onScaleMove = false;
                lastdist = 0;
                onTouchListener.onRelease();
                break;

            case MotionEvent.ACTION_MOVE:
                int diffY = (int) (event.getY() - lastYPosition);
                int diffX = (int) (event.getX() - lastXPosition);

                lastYPosition = event.getY();
                lastXPosition = event.getX();

                //Check if the action was jitter
                if (Math.abs(diffX) > 4 || Math.abs(diffY) > 4) {

                    if (onScaleMove) {
                        double dist = 0;

                        if (event.getPointerCount() >= 2) {
                            dist = Math.sqrt(Math.pow(event.getX(0) - event.getX(1), 2) + Math.pow(event.getY(0) - event.getY(1), 2));
                        }

                        if ((Math.abs(dist - lastdist) > 10) && (lastdist > 0) && (dist > 0)) {
                            if (dist < lastdist) {
                                onTouchListener.onPinchIn();
                            } else if (dist == lastdist) {
                                //    onTouchListener.onPinchStable();
                            } else {
                                onTouchListener.onPinchOut();
                            }
                        } else {
                            onTouchListener.onTwoFingersDrag();
                        }

                        lastdist = dist;
                        return false;
                    } else {
                        onTouchListener.onMove();
                    }

                }
                break;
            case MotionEvent.ACTION_CANCEL: {
                onScaleMove = false;
                mActivePointers.remove(pointerId);
                onTouchListener.onRelease();
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN:
                onScaleMove = true;
                onTouchListener.onSecondFingerOnLayout();
                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                mActivePointers.put(pointerId, f);

                return false;
        }
        return super.dispatchTouchEvent(event);
    }


    public interface OnTouchListener {
        void onTouch();

        void onRelease();

        void onPinchIn();

        void onPinchOut();

        void onMove();

        void onTwoFingersDrag();

        void onSecondFingerOnLayout();
    }

    public void setTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }
}




