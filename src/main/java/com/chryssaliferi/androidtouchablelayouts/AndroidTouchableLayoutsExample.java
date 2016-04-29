package com.chryssaliferi.androidtouchablelayouts;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class AndroidTouchableLayoutsExample extends Activity {


    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        final TextView status = (TextView) findViewById(R.id.status);

        TouchableFrameLayout frame = (TouchableFrameLayout) findViewById(R.id.touchable_frame);
        frame.setTouchListener(new TouchableFrameLayout.OnTouchListener() {
            @Override
            public void onTouch() {
                status.setText(R.string.onTouchKey);
            }

            @Override
            public void onRelease() {
                status.setText(R.string.onReleaseKey);
            }

            @Override
            public void onPinchIn() {
                status.setText(R.string.onPinchInKey);
            }

            @Override
            public void onPinchOut() {
                status.setText(R.string.onPinchOutKey);
            }

            @Override
            public void onMove() {
                status.setText(R.string.onMoveKey);
            }

            @Override
            public void onTwoFingersDrag() {
                status.setText(R.string.onTwoFingersDragKey);
            }

            @Override
            public void onSecondFingerOnLayout() {
                status.setText(R.string.onSecondFingerOnLayout);
            }

        });
    }
}
