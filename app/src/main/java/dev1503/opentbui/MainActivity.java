package dev1503.opentbui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.CollapsibleActionView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dev1503.opentbui.widgets.TBAction;
import dev1503.opentbui.widgets.TBColor;
import dev1503.opentbui.widgets.TBSlider;
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

        tbUI = OpenTBUI.fromPopup(this);
        Category categoryMovement = tbUI.addCategory("动态", R.drawable.ic_settings_black_24dp);
        categoryMovement.addToggle("飞行");
        categoryMovement.addToggle("穿透飞行");
        categoryMovement.addToggle("穿透");
        categoryMovement.addToggle("空中连跳");
        categoryMovement.addToggle("跳高")
                .addSlider("高度", new float[]{1f, 2f, 3f, 4f, 5f, 6f, 7f}, (tbSlider, value, fromUser) -> {
                    Toast.makeText(this, "高度: " + value, Toast.LENGTH_SHORT).show();
                });
        categoryMovement.addToggle("速度")
                .addSlider("速度", new float[]{1f, 2f, 3f, 4f, 5f, 6f, 7f}, (tbSlider, value, fromUser) -> {
                    Toast.makeText(this, "速度: " + value, Toast.LENGTH_SHORT).show();
                });
        categoryMovement.addToggle("自动奔跑");
        categoryMovement.addToggle("拉弓行走不减速");
        categoryMovement.addToggle("慢速掉落");
        categoryMovement.addToggle("水上行走");
        categoryMovement.addToggle("停止发送数据包（仅多人游戏）");
        categoryMovement.addToggle("鞘翅飞行");
        categoryMovement.addToggle("点击传送")
                .addToggle("服务器模式");

        Category categoryWorld = tbUI.addCategory("世界", R.drawable.ic_discord_black_24dp);
        categoryWorld.addToggle("空中行者（须手持可放置项目）");
        categoryWorld.addToggle("快速拿取箱子物品");
        categoryWorld.addToggle("范围破坏")
                .addSlider("范围", new float[]{3f, 5f, 7f});
        categoryWorld.addToggle("快速挖掘")
                .addSlider("等级", new float[]{1f, 2f, 3f, 4f, 5f, 6f, 7f});
        categoryWorld.addToggle("允许作弊获得成就");
        categoryWorld.addToggle("快速建造")
                .addToggle("命令模式");
        TBToggle fastBuild = categoryWorld.addToggle("快速建造 v2");
        fastBuild.addSlider("重复", new float[]{1f, 2f, 3f, 4f, 5f, 6f, 7f});
        fastBuild.addSlider("间隔", new float[]{1f, 2f, 3f, 4f, 5f, 6f, 7f});
        fastBuild.addToggle("锁定方向");
        fastBuild.addToggle("选区模式");
        categoryWorld.addAction("获取项目", v -> {
                    Toast.makeText(this, "点击了获取项目", Toast.LENGTH_SHORT).show();
                });
        categoryWorld.addAction("附魔", v -> {
                    Toast.makeText(this, "点击了附魔", Toast.LENGTH_SHORT).show();
                });
        categoryWorld.addToggle("NBT编辑器");
        categoryWorld.addToggle("触及范围")
                .addRangeSlider("范围", 1f, 256f)
                .setValue(128)
                .setDecimalScale(1);
        categoryWorld.addToggle("触及范围修复（线上）");
        categoryWorld.addToggle("覆盖名称")
                .addEditText("", "Open Toolbox User Interface");

        Category categoryRender = tbUI.addCategory("渲染", R.drawable.small_colored_add_icon);
        categoryRender.addToggle("透视");
        categoryRender.addToggle("箱子追踪");
        categoryRender.addToggle("玩家追踪");
        categoryRender.addToggle("方块更新追踪");
        TBToggle tracer = categoryRender.addToggle("实体追踪");
        tracer.addColor("实体颜色");
        tracer.addColor("玩家颜色");
        tracer.addRangeSlider("尺寸", 0.1f, 3.0f)
                                .setDecimalScale(1);
        categoryRender.addToggle("实体轮廓")
                        .addRangeSlider("尺寸", 1, 8);
        categoryRender.addToggle("自由视角");
        categoryRender.addToggle("夜视");
        categoryRender.addToggle("装备耐久值");
        categoryRender.addToggle("血量条");
        TBToggle tinyMap = categoryRender.addToggle("小地图");
        tinyMap.addRangeSlider("半径", 1, 8).setValue(4);
        tinyMap.addRangeSlider("尺寸", 64, 320).setValue(128);
        tinyMap.addToggle("显示玩家");
        categoryRender.addToggle("截断文字")
                        .addSlider("长度", new float[]{0, 16, 32, 64, 128, 512});
        categoryRender.addToggle("放大")
                        .addRangeSlider("倍数", -100, 100);

        tbUI.addCategory("命令", R.drawable.ic_arrow_forward_black_24dp);
        tbUI.addCategory("战斗", R.drawable.ic_help_outline_black_24dp);

        Category tbuiCategory = tbUI.addCategory("OpenTBUI", R.drawable.ic_arrow_back_black_24dp);
        TBToggle themeToggle = tbuiCategory.addToggle("主题", true);
        TBColor color1 = themeToggle.addColor("主要颜色", Color.parseColor("#00E676"), color -> {
            TBTheme theme = tbUI.getTheme();
            tbUI.setTheme(new TBTheme(color, theme.getColor2()));
        });
        TBColor color2 = themeToggle.addColor("次要颜色", Color.parseColor("#43A047"), color -> {
            TBTheme theme = tbUI.getTheme();
            tbUI.setTheme(new TBTheme(theme.getColor1(), color));
        });
        themeToggle.addAction("重置", view -> {
            color1.setColor(Color.parseColor("#00E676"));
            color2.setColor(Color.parseColor("#43A047"));
            tbUI.setTheme(new TBTheme(Color.parseColor("#00E676"), Color.parseColor("#43A047")));
        });
        tbuiCategory.addToggle("Test toggle").setChecked(true).addAction("action");
        tbuiCategory.addSlider("slider", new float[]{0, 2, 50, 1503});
        tbuiCategory.addRangeSlider("range slider", -1503, 1503);
        tbuiCategory.addRangeSlider("Premium", 0, 15031503, (slider, v)->{
            tbUI.setPremiumExpireSeconds((long) v);
        });
        tbuiCategory.addEditText("EditText", "abc");
        tbuiCategory.addToggle("套娃").addToggle("套娃").addToggle("套娃").addToggle("套娃").addToggle("套娃").addToggle("套娃").addToggle("套娃").addToggle("套娃").addToggle("套娃").addToggle("套娃").addToggle("套娃").addToggle("套娃").addToggle("套娃").addToggle("套娃").addToggle("套娃").addToggle("套娃").addToggle("套娃").addToggle("套娃").addToggle("套娃").addToggle("套娃");


        tbUI.selectCategory(0);

        tbUI.setTheme(new TBTheme(Color.parseColor("#00E676"), Color.parseColor("#43A047")));
        tbUI.setPremiumExpireSeconds(500L);
        tbUI.startUpdatePremiumExpireTimeTextTimer();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    public void start(View view) {
        tbUI.show();
    }

    public void gh(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/1503Dev/OpenTBUI"));
        startActivity(intent);
    }
}