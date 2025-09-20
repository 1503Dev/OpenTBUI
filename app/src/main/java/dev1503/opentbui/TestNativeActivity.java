package dev1503.opentbui;

import android.app.NativeActivity;
import android.os.Bundle;

public class TestNativeActivity extends NativeActivity {
    OpenTBUI tbUI;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tbUI = OpenTBUI.fromPopup(this);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            tbUI.show();
        }
    }
}
