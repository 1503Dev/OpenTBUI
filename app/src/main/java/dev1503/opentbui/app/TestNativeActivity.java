package dev1503.opentbui.app;

import android.app.NativeActivity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import dev1503.opentbui.Category;
import dev1503.opentbui.Icon;
import dev1503.opentbui.OpenTBUI;
import dev1503.opentbui.TBTheme;
import dev1503.opentbui.picker.ItemSelector;

public class TestNativeActivity extends NativeActivity {
    OpenTBUI tbUI;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tbUI = OpenTBUI.fromPopup(this);
        tbUI.setTheme(new TBTheme(Color.parseColor("#8C9EFF"), Color.parseColor("#3F51B5")));
        tbUI.addCategory("Test Category", new Icon(R.drawable.deployed_code_24px)).addColor("Test Color", Color.parseColor("#FF4081"));
        Category category2 = tbUI.addCategory("Test Category 2", new Icon(R.drawable.swords_24px));
        category2.addAction("Test Action", v -> {
            Toast.makeText(this, "Test Action Clicked", Toast.LENGTH_SHORT).show();
        });
        category2.addToggle("Test Toggle");
        category2.addEditText("Test Edit Text");
        Bitmap bm1 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        bm1.eraseColor(Color.parseColor("#FF4081"));
        category2.addBlockList().addItem("Test Block List Item", bm1);
        category2.addRangeSlider("Test Range Slider", 0, 100);
        category2.addAction("Test Action 2", v -> {
            new ItemSelector(this, tbUI.getTheme(), new String[]{"Item 1", "Item 2", "Item 3"}, (index, items) -> {
                Toast.makeText(this, "Test Action 2 Clicked: " + items[index], Toast.LENGTH_SHORT).show();
            });
        });
        tbUI.addExtraButton(new Icon(R.drawable.logout_24px), v -> {
            finish();
        });
        tbUI.hideSystemUI();
        tbUI.setOnHideListener(()->{
            new Handler().postDelayed(()->{
                tbUI.show();
            }, 150);
        });
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            tbUI.show();
        }
    }
}
