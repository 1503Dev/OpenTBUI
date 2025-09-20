package dev1503.opentbui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dev1503.opentbui.widgets.TBToggle;

public class MainActivity extends AppCompatActivity {

    OpenTBUI tbUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(params);
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        startActivity(new Intent(this, TestNativeActivity.class));

        tbUI = OpenTBUI.fromGlobal(this);
        tbUI.addCategory("动态", R.drawable.ic_settings_black_24dp)
                .addToggle("飞行")
                .addToggle("穿透飞行")
                .addToggle("穿透")
                .addToggle("空中连跳")
                .addToggle("跳高")
                .addToggle("速度")
                .addToggle("自动奔跑")
                .addToggle("拉弓行走不减速")
                .addToggle("慢速掉落")
                .addToggle("水上行走")
                .addToggle("停止发送数据包（仅多人游戏）")
                .addToggle("鞘翅飞行")
                .addToggle("点击传送");
        tbUI.addCategory("世界", R.drawable.ic_discord_black_24dp)
                .addToggle("空中行者（须手持可放置项目）")
                .addToggle("快速拿取箱子物品")
                .addToggle("范围破坏")
                .addToggle("快速挖掘")
                .addToggle("允许作弊获得成就")
                .addToggle("快速建造")
                .addToggle("快速建造 v2")
                .addAction("获取项目", v -> {
                    Toast.makeText(this, "点击了获取项目", Toast.LENGTH_SHORT).show();
                })
                .addAction("附魔", v -> {
                    Toast.makeText(this, "点击了附魔", Toast.LENGTH_SHORT).show();
                })
                .addToggle("NBT编辑器")
                .addToggle("触及范围")
                .addToggle("触及范围修复（线上）")
                .addToggle("覆盖名称");
        tbUI.addCategory("渲染", R.drawable.small_colored_add_icon);
        tbUI.addCategory("命令", R.drawable.ic_launcher_foreground);
        tbUI.addCategory("战斗", R.drawable.ic_help_outline_black_24dp);

        tbUI.selectCategory(0);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    public void start(View view) {
        tbUI.show();
    }
}