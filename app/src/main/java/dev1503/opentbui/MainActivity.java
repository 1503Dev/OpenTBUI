package dev1503.opentbui;

import static dev1503.opentbui.Utils.dp2px;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.CollapsibleActionView;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dev1503.opentbui.view.CircleSwitch;
import dev1503.opentbui.view.Cube3DView;
import dev1503.opentbui.widgets.TBAction;
import dev1503.opentbui.widgets.TBBlockList;
import dev1503.opentbui.widgets.TBColor;
import dev1503.opentbui.widgets.TBEditText;
import dev1503.opentbui.widgets.TBSlider;
import dev1503.opentbui.widgets.TBToggle;

public class MainActivity extends AppCompatActivity {

    OpenTBUI tbUI;

    @SuppressLint("UseCompatLoadingForDrawables")
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
        TextView logs = findViewById(R.id.logs);


        tbUI = OpenTBUI.fromPopup(this);

        tbUI.setTheme(new TBTheme(Color.parseColor("#00E676"), Color.parseColor("#43A047")));

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

        Category categoryRender = tbUI.addCategory("渲染", R.drawable.ic_help_outline_black_24dp);
        TBToggle toggleXray = categoryRender.addToggle("透视");
        TBEditText editTextXraySelected = toggleXray.addEditText("现行选中项");;
        TBBlockList blockList = toggleXray.addBlockList()
                .addItem("minecraft:crafting_table",
                        getBitmapFromRes(R.drawable.crafting_table_top),
                        getBitmapFromRes(R.drawable.crafting_table_front),
                        getBitmapFromRes(R.drawable.crafting_table_side))
                .addItem("minecraft:diamond_ore", getBitmapFromRes(R.drawable.diamond_ore))
                .addItem("minecraft:chest",
                        getBitmapFromRes(R.drawable.chest_top),
                        getBitmapFromRes(R.drawable.chest_front),
                        getBitmapFromRes(R.drawable.chest_side))
                .addItem("minecraft:lava", getBitmapFromRes(R.drawable.lava_flow))
                .addItem("minecraft:redstone_ore", getBitmapFromRes(R.drawable.redstone_ore))
                .addItem("minecraft:diamond_block", getBitmapFromRes(R.drawable.diamond_block))
                .addItem("minecraft:dirt", getBitmapFromRes(R.drawable.dirt))
                .addItem("minecraft:amethyst_block", getBitmapFromRes(R.drawable.amethyst_block))
                .setOnSelectedItemChangeListener((selectedIds) -> {
                    if (selectedIds.length > 0) {
                        StringBuilder selected = new StringBuilder();
                        for (String selectedId : selectedIds) {
                            selected.append(selectedId).append(", ");
                        }
                        selected.setLength(selected.length() - 2);
                        editTextXraySelected.setText(selected.toString());
                    } else {
                        editTextXraySelected.setText("");
                    }
                });
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

        tbUI.addCategory("命令", R.drawable.ic_arrow_forward_black_24dp)
                .addAction("To do ...");
        Category categoryCombat = tbUI.addCategory("战斗", R.drawable.ic_help_outline_black_24dp);
        TBToggle toggleKillaura = categoryCombat.addToggle("范围自动攻击");
        toggleKillaura.addToggle("攻击生物");
        toggleKillaura.addToggle("攻击玩家");
        toggleKillaura.addRangeSlider("攻击间隔", -1, 20);
        categoryCombat.addToggle("抗击退");
        categoryCombat.addToggle("弓箭自瞄");
        categoryCombat.addAction("传送到玩家");
        TBToggle toggleHitbox = categoryCombat.addToggle("击中范围扩大");
        toggleHitbox.addRangeSlider("生物击中范围", 1, 8);
        toggleHitbox.addRangeSlider("玩家击中范围", 1, 8);
        categoryCombat.addToggle("自动穿装");

        Category categorySync = tbUI.addCategory("组件同步", R.drawable.ic_settings_black_24dp);
        categorySync.addToggle("path/to/a", "path/to/a");
        categorySync.addToggle("path/to/a", "path/to/a");
        categorySync.addToggle("", (tBToggle, b) -> {
            if (!b) {
                tBToggle.setCheckedWithoutNotify(true);
            }
        })
                .setChecked(true)
                .addToggle("path/to/a", "path/to/a");
        categorySync.addSlider("path/to/a", "path/to/a", new float[]{-2f, -1f, 0f, 1f, 2f});
        categorySync.addSlider("path/to/b", "path/to/b", new float[]{-10f, 0f, 10f});
        categorySync.addRangeSlider("path/to/b", "path/to/b", -20, 20);
        categorySync.addColor("path/to/c", "path/to/c");
        categorySync.addRangeSlider("path/to/c", "path/to/c", -2147483678f, 2147483647);
        categorySync.addAction("action", "this/is/a/action");

        Category customCategory = tbUI.addCategory("自定义", R.drawable.ic_arrow_back_black_24dp);
        TBToggle themeToggle = customCategory.addToggle("主题", true);
        themeToggle.addColor("主要颜色", "theme/color1", Color.parseColor("#00E676"));
        themeToggle.addColor("次要颜色", "theme/color2", Color.parseColor("#43A047"));
        themeToggle.addAction("重置", "reset_theme");
        TBToggle layoutToggle = customCategory.addToggle("布局", true);
        layoutToggle.addRangeSlider("Categories 宽度", "categories/width", 32, 512).setValueWithoutNotify(186);
        layoutToggle.addRangeSlider("Features 宽度", "features/width", 144, 512 ).setValueWithoutNotify(240);

        Category categoryOss = tbUI.addCategory("开放源代码许可", R.drawable.ic_help_outline_black_24dp);
        categoryOss.addToggle("OpenTBUI\n" + OpenTBUI.VERSION_NAME, true).addAction("LGPLv3");
        categoryOss.addToggle("IndicatorSeekBar", true).addAction("Apache-2.0");
        categoryOss.addToggle("AppCompat", true).addAction("Apache-2.0");
        categoryOss.addToggle("MaterialComponents", true).addAction("Apache-2.0");
        categoryOss.addToggle("AndroidX", true).addAction("Apache-2.0");
        categoryOss.addToggle("RecyclerView", true).addAction("Apache-2.0");
        categoryOss.addToggle("FlexBoxLayout", true).addAction("Apache-2.0");

        tbUI.selectCategory(0);

        tbUI.setPremiumExpireSeconds(15031503L);
        tbUI.startUpdatePremiumExpireTimeTextTimer();
        tbUI.refreshTheme();
        final StatusManager sm = tbUI.getStatusManager();

        sm.setListener(new StatusManager.Listener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onValueChange(String path, double value) {
                logs.setText("value_change: " + path + ": " + value + "\n" + logs.getText());
                TBTheme theme;
                switch (path) {
                    case "theme/color1":
                        theme = tbUI.getTheme();
                        tbUI.setTheme(new TBTheme((int) value, theme.getColor2()));
                        break;
                    case "theme/color2":
                        theme = tbUI.getTheme();
                        tbUI.setTheme(new TBTheme(theme.getColor1(), (int) value));
                        break;
                    case "categories/width":
                        tbUI.setCategoriesViewWidth(dp2px(MainActivity.this, (int) value));
                        break;
                    case "features/width":
                        tbUI.setFeaturesViewWidth(dp2px(MainActivity.this, (int) value));
                        break;
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onActionTrigger(String path) {
                logs.setText("action_trigger: " + path + "\n" + logs.getText());
                switch (path) {
                    case "reset_theme":
                        sm.setValue("theme/color1", Color.parseColor("#00E676"));
                        sm.setValue("theme/color2", Color.parseColor("#43A047"));
                        tbUI.setTheme(new TBTheme(Color.parseColor("#00E676"), Color.parseColor("#43A047")));
                        break;
                }
            }
        });
//        tbUI.setFeaturesViewWidth(Utils.dpToPx(this, 320));
//        tbUI.setCategoriesViewWidth(Utils.dpToPx(this, 160));
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
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        if (width <= 0) width = 1;
        if (height <= 0) height = 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    public Bitmap getBitmapFromRes(int resId) {
        return drawableToBitmap(getDrawable(resId));
    }
}