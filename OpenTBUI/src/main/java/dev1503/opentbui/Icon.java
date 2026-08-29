package dev1503.opentbui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import androidx.core.content.res.ResourcesCompat;

public class Icon {
    public static final int TYPE_BITMAP = 0;
    public static final int TYPE_DRAWABLE = 1;
    public static final int TYPE_RES_ID = 2;
    public static final int DEFAULT_SIZE_DP = 24;

    private static final String B64_ARROW_FORWARD_PNG = "iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAADQklEQVR4Xu3awYraUBiGYcNkYbsZCnVEu6moeAPevzfgHYgIahU6BdtFrTCK5W/HTMZxNGqO5JzvfXZmZfheEyGJSpAWSZ89CEAdVwBxBCCOAMQRgDgCEEcA4ghAHAGIIwBxBCCOAMQRgDgCEEcA4ghAHAGIIwBxBCBOOoDhcHhv+zebzV+qHcgGMB6P61EUfbPh4zh+qNVqj4oRSAaQHn9HNQK5ACaTyZdSqTRNlk9RjEAqgGPj76hFIBNAlvF3lCKQCaDX68WtVuspWfkElQhkAjBE8JZUAIYIXpMLwBDBC8kADBGIB0AEBEAEyreANOXbgex/gH2qERBAimIEBLDn3AhWq9VDu9329lEyARygFAEBvEMlAgI4QiECAjgh9AgIIIOQIyCAjEKNgADOcG4Ed3d3lXq9/iM5UEAEcKbQIiCAC4QUAQFcKJQICOAKIURAAFfyPQICyIHPERBATnyNgABy5GMEBJAz3yIgAAd8ioAAHPElAgJwyIcICMCxokdAADdQ5AgI4EaKGoGTAMbj8SiKoq/JAVxkuVxWOp2O0/cJCKDgXEdAAB5wGQEBeMJVBATgifV6/anRaPzM++sSgAdcjW8IoOBcjm8IoMBcj2+cBIC3RqNROY7jP8mBE24xviGAGyjq+ARwA0UenwAcK/r4hluAIz6MbwjAAV/GNwSQM5/GNwSQI9/GNwSQEx/HNwSQA1/HNwRwJZ/HNwRwBd/HNwRwoRDGNwRwgVDGNwRwpu12W55Op4V7qncpAjjDZDL5YG9rJwdOuNUj3WsQQEYhjm8IIINQxzcEcELI4xsCOCL08Q0BvENhfEMAB6iMbwhgj9L4hgBS1MY3BPBMcXxDAMLjE4D4+PIBqF7202RvAYwvHADjv5C7AjD+a1IB9Pv9j9Vq9ffu5E/x4Xn+tWQCYPzDZAKYz+eV9Xr9PTnzIxR++TsyAZgsESiNb6QCMIPBoFIulw9GoDa+kQvAzGazz5vN5vHfh2eK4xvJAEw6AtXxpQMww+HwfrFYPHW73cyveodG9gqA/whAHAGIIwBxBCCOAMQRgDgCEEcA4ghAHAGIIwBxBCCOAMQRgDgCEEcA4ghAHAGIIwBxBCDuL72wXp/HgwnDAAAAAElFTkSuQmCC";
    private static final String B64_ARROW_DROP_DOWN_PNG = "iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAABpklEQVR4Xu2VyUoFMRBF6/6Dy8qHiSAigoiIAyKiuHFARBx5PEQQEUH8rlT+oyXSZqHi8F7SFUidVedu0n3qpgNqHDT+/SbAGmBHoHGsAY0XwBpgDbAj0DjWgMYLYA2wBtgRaBxrQOMFsAZYA+wINI41oPECWAOyNsB7PwawkoICAHhl5tkUTElWARERGRHRagry8uKcm0urDGQXEBGRWyJaS0EGADwz83wKMlFEQEREroloPQVTAOCJmRdSkJFiAiIhhMuu6zZTMAEAHpl5MQWZKSog4r2/ALCVgv/x4JxbSqsCFBcQEZFzItru9/wTAO6ZeTkFhRhEQEREzohop9/3RwDcMXPR6/SDwQREROSUiHb7vb8FwJiZS12jXxhUQCSEcNJ13V6//2dGzrms1+dvDC4g4r0/BrDfv8M7AG6YeSMFA6EiIOK9PwRwEJ8BXDHzpDfFVKgJiIQQjohoZqgf3neoCqgBE1DDFDQxAZr2a8AE1DAFTUyApv0aMAE1TEETE6BpvwZMQA1T0MQEaNqvARNQwxQ0MQGa9mvABNQwBU2aF/AGe/pGQfU3ruwAAAAASUVORK5CYII=";
    private static final String B64_ARROW_BACK_PNG = "iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAB2klEQVR4Xu3YzUoDMRSG4TPgXgR/UOdEimsvwPvfeAGuXUxGa9tNL6Bw5IMacJiCnSY5J528KzOrfk/TKjY085qZ768A9QbUj8DMqzdg5heg3oB6A+pHQDHv/ScRXTDzndbLULsB+/EP++EbZr7VQFABGIz/7Z2ZX8IpU9kBxsaLyLdz7j48yFhWgLHxRLRk5uGzbGUDsDgeZQGwOj4LgOXxyQGsj08KUMJ4lARgbLyILJ1zat/2h4oOUNJ4FBVgbDwRfTHzYzgZKxpAieNRFIBSx6OTAUoej04CKH08mgxwDuPRJIBzGY+OBjgw3lS73e51sVi8/edFHQVQwniUEmBFRCr/uzumZACoBISkAOgAQsfMT+FUSJMA0LkgTAZAfd+vRKTom3ASACod4WQAVDJCFAA0htA0Tde2rekvxmgAyHu/JqKb8KAAhKgAaAwBj5nZhZOhogOgkhCSAKBSEJIBoBIQkgIg6wjJAawjZAGwjJANAPV9vxaR4d8Jvm1btV+RWQHQGAIeMzOHU8ayA6Cu67ZN01wOdn4w83M4ZUoFAHnvN0R0jZ9FZOucu8q0+U9qAAgIInKhNV4dwEKqN8BCFcDCu6BZBdDUt1AFsPAuaFYBNPUtNHuAH5+3/UGG371vAAAAAElFTkSuQmCC";

    public static final Icon ICON_ARROW_FORWARD = new Icon(decodePng(B64_ARROW_FORWARD_PNG));
    public static final Icon ICON_ARROW_DROP_DOWN = new Icon(decodePng(B64_ARROW_DROP_DOWN_PNG));
    public static final Icon ICON_ARROW_BACK = new Icon(decodePng(B64_ARROW_BACK_PNG));

    private int type;

    private Bitmap bitmap;
    private Drawable drawable;
    private int resId;

    public Icon(Drawable drawable) {
        setDrawable(drawable);
    }

    public Icon(Bitmap bitmap) {
        setBitmap(bitmap);
    }

    public Icon(int resId) {
        setResId(resId);
    }

    public Icon setBitmap(Bitmap bitmap) {
        this.type = TYPE_BITMAP;
        this.bitmap = bitmap;
        this.drawable = null;
        this.resId = 0;
        return this;
    }

    public Icon setDrawable(Drawable drawable) {
        this.type = TYPE_DRAWABLE;
        this.drawable = drawable;
        this.bitmap = null;
        this.resId = 0;
        return this;
    }

    public Icon setResId(int resId) {
        this.type = TYPE_RES_ID;
        this.resId = resId;
        this.bitmap = null;
        this.drawable = null;
        return this;
    }

    public int getType() {
        return type;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public int getResId() {
        return resId;
    }

    public Drawable resolve(Context context) {
        switch (type) {
            case TYPE_RES_ID:
                return ResourcesCompat.getDrawable(context.getResources(), resId, null);
            case TYPE_DRAWABLE:
                return drawable;
            case TYPE_BITMAP:
                float density = context.getResources().getDisplayMetrics().density;
                int size = Math.round(DEFAULT_SIZE_DP * density);
                return new BitmapDrawable(context.getResources(), scaleTo(bitmap, size));
        }
        return null;
    }

    private static Bitmap scaleTo(Bitmap src, int size) {
        int width = src.getWidth();
        int height = src.getHeight();
        if (width == size && height == size) {
            return src;
        }
        float scale = Math.min((float) size / width, (float) size / height);
        int newWidth = Math.max(1, Math.round(width * scale));
        int newHeight = Math.max(1, Math.round(height * scale));
        return Bitmap.createScaledBitmap(src, newWidth, newHeight, true);
    }

    private static Bitmap decodePng(String base64) {
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
