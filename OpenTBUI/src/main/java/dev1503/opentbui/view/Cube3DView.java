package dev1503.opentbui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Cube3DView extends View {
    private class GLMatrix extends android.opengl.Matrix {}

    private static final float[] TEXTURE_COORDS = {
            0.0f, 1.0f, // 左下
            1.0f, 1.0f, // 右下
            1.0f, 0.0f, // 右上
            0.0f, 0.0f  // 左上
    };

    private Bitmap[] textures; // [正面, 右侧面, 顶面]
    private Paint paint;
    private Matrix[] drawMatrices; // 2D绘制矩阵
    private float[] vertexBuffer = new float[24]; // 存储3个面(8坐标×3面)的顶点数据

    public Cube3DView(Context context) {
        super(context);
        init();
    }
    public Cube3DView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setFilterBitmap(false); // 禁用滤波保证清晰度
        setWillNotDraw(false); // 必须调用才能触发onDraw
    }

    /**
     * 设置单张纹理（所有面使用相同纹理）
     */
    public void setTexture(Bitmap texture) {
        setTextures(new Bitmap[]{texture, texture, texture});
    }

    /**
     * 设置三个面的独立纹理 [正面, 右侧面, 顶面]
     */
    public void setTextures(Bitmap[] textures) {
        if (this.textures == textures) return;
        this.textures = new Bitmap[]{
                textures[2],
                textures[1],
                textures[0]
        };
        updateTransforms();
        invalidate();
    }

    /**
     * 更新变换矩阵（尺寸或纹理变化时调用）
     */
    private void updateTransforms() {
        if (textures == null || textures[0] == null || getWidth() == 0) return;

        calculateVertexCoords(vertexBuffer);

        drawMatrices = new Matrix[3];
        Bitmap baseTexture = textures[0];

        for (int i = 0; i < 3; i++) {
            drawMatrices[i] = new Matrix();
            float[] src = TEXTURE_COORDS.clone();
            float[] dst = new float[8];
            System.arraycopy(vertexBuffer, i * 8, dst, 0, 8);

            float texW = baseTexture.getWidth();
            float texH = baseTexture.getHeight();

            for (int j = 0; j < 4; j++) {
                src[j * 2] *= texW;
                src[j * 2 + 1] *= texH;
            }

            drawMatrices[i].setPolyToPoly(src, 0, dst, 0, 4);
        }
    }

    /**
     * 使用OpenGL矩阵计算3D顶点坐标
     */
    private void calculateVertexCoords(float[] out) {
        // 1. 正交投影矩阵
        float[] projectionMatrix = new float[16];
        GLMatrix.orthoM(projectionMatrix, 0, -2.0f, 2.0f, 3.82f, -0.18f, 0.1f, 100.0f);

        // 2. 视图矩阵（摄像机设置）
        float[] viewMatrix = new float[16];
        GLMatrix.setLookAtM(viewMatrix, 0,
                -2.0f, 2.0f, 2.0f,  // 摄像机位置 (x,y,z)
                0.0f, 0.0f, 0.0f,   // 观察点
                0.0f, 1.0f, 0.0f    // 上向量
        );

        // 3. 模型矩阵（物体变换）
        float[] modelMatrix = new float[16];
        GLMatrix.setIdentityM(modelMatrix, 0);
        GLMatrix.translateM(modelMatrix, 0, 0.5f, 0.0f, 0.0f);

        // 4. 合并MVP矩阵
        float[] mvpMatrix = new float[16];
        GLMatrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        GLMatrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, modelMatrix, 0);

        // 5. 定义三个面的原始3D坐标（每个面4个顶点）
        float[][][] faces = {
                { // 正面
                        {0.0f, 0.0f, 0.0f, 1.0f},  // 左下
                        {1.0f, 0.0f, 0.0f, 1.0f},  // 右下
                        {1.0f, 1.0f, 0.0f, 1.0f},  // 右上
                        {0.0f, 1.0f, 0.0f, 1.0f}   // 左上
                },
                { // 右侧面
                        {0.0f, 0.0f, -1.0f, 1.0f},
                        {0.0f, 0.0f, 0.0f, 1.0f},
                        {0.0f, 1.0f, 0.0f, 1.0f},
                        {0.0f, 1.0f, -1.0f, 1.0f}
                },
                { // 顶面
                        {0.0f, 1.0f, -1.0f, 1.0f},
                        {0.0f, 1.0f, 0.0f, 1.0f},
                        {1.0f, 1.0f, 0.0f, 1.0f},
                        {1.0f, 1.0f, -1.0f, 1.0f}
                }
        };

        // 6. 变换顶点坐标
        float[] temp = new float[4];
        for (int face = 0; face < 3; face++) {
            for (int vertex = 0; vertex < 4; vertex++) {
                GLMatrix.multiplyMV(temp, 0, mvpMatrix, 0, faces[face][vertex], 0);
                int outPos = (face * 8) + (vertex * 2);
                out[outPos] = temp[0] / temp[3]; // 透视除法 (x/w)
                out[outPos + 1] = temp[1] / temp[3]; // (y/w)
            }
        }

        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;

        for (int i = 0; i < 24; i += 2) {
            float x = out[i];
            float y = out[i + 1];
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }

        float widthRange = maxX - minX;
        float heightRange = maxY - minY;

        int viewWidth = getWidth();
        int viewHeight = getHeight();

        float scaleX = viewWidth / widthRange;
        float scaleY = viewHeight / heightRange;
        float scale = Math.min(scaleX, scaleY) * 0.8f;

        float centerX = (minX + maxX) / 2.0f;
        float centerY = (minY + maxY) / 2.0f;

        float newCenterX = (viewWidth / 2.0f) / scale;
        float newCenterY = (viewHeight / 2.0f) / scale;

        float translateX = newCenterX - centerX;
        float translateY = newCenterY - centerY;

        for (int i = 0; i < 24; i += 2) {
            out[i] = (out[i] + translateX) * scale;
            out[i + 1] = (out[i + 1] + translateY) * scale;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateTransforms();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (textures == null || drawMatrices == null) return;

        // 绘制三个面
        for (int i = 0; i < 3; i++) {
            if (i < textures.length && textures[i] != null) {
                canvas.drawBitmap(textures[i], drawMatrices[i], paint);
            }
        }
    }
}