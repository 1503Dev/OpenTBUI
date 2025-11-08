#include <jni.h>
#include <android/native_activity.h>
#include <android/native_window.h>
#include <android/looper.h>
#include <EGL/egl.h>
#include <GLES2/gl2.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
    EGLDisplay display;
    EGLSurface surface;
    EGLContext context;
    ANativeWindow* window;
    int width;
    int height;
    int animating;
} RendererState;

const char* vertexShaderSource =
        "attribute vec4 aPosition;"
        "void main() {"
        "   gl_Position = aPosition;"
        "}";

const char* fragmentShaderSource =
        "precision mediump float;"
        "void main() {"
        "   gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);"
        "}";

GLuint compileShader(GLenum type, const char* source) {
    GLuint shader = glCreateShader(type);
    glShaderSource(shader, 1, &source, NULL);
    glCompileShader(shader);
    return shader;
}

GLuint createShaderProgram() {
    GLuint vertexShader = compileShader(GL_VERTEX_SHADER, vertexShaderSource);
    GLuint fragmentShader = compileShader(GL_FRAGMENT_SHADER, fragmentShaderSource);

    GLuint program = glCreateProgram();
    glAttachShader(program, vertexShader);
    glAttachShader(program, fragmentShader);
    glLinkProgram(program);

    glDeleteShader(vertexShader);
    glDeleteShader(fragmentShader);

    return program;
}

int initEGL(RendererState* state) {
    const EGLint attribs[] = {
            EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
            EGL_SURFACE_TYPE, EGL_WINDOW_BIT,
            EGL_BLUE_SIZE, 8,
            EGL_GREEN_SIZE, 8,
            EGL_RED_SIZE, 8,
            EGL_NONE
    };

    const EGLint context_attribs[] = {
            EGL_CONTEXT_CLIENT_VERSION, 2,
            EGL_NONE
    };

    EGLint numConfigs;
    EGLConfig config;

    state->display = eglGetDisplay(EGL_DEFAULT_DISPLAY);
    eglInitialize(state->display, 0, 0);
    eglChooseConfig(state->display, attribs, &config, 1, &numConfigs);

    EGLint format;
    eglGetConfigAttrib(state->display, config, EGL_NATIVE_VISUAL_ID, &format);
    ANativeWindow_setBuffersGeometry(state->window, 0, 0, format);

    state->surface = eglCreateWindowSurface(state->display, config, state->window, NULL);
    state->context = eglCreateContext(state->display, config, EGL_NO_CONTEXT, context_attribs);

    if (eglMakeCurrent(state->display, state->surface, state->surface, state->context) == EGL_FALSE) {
        return -1;
    }

    state->width = ANativeWindow_getWidth(state->window);
    state->height = ANativeWindow_getHeight(state->window);

    return 0;
}

void renderLine(RendererState* state) {
    glViewport(0, 0, state->width, state->height);
    glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    GLuint program = createShaderProgram();
    glUseProgram(program);

    GLfloat vertices[] = {
            -1.0f,  0.0f, 0.0f,
            1.0f,  0.0f, 0.0f
    };

    GLint positionLoc = glGetAttribLocation(program, "aPosition");
    glEnableVertexAttribArray(positionLoc);
    glVertexAttribPointer(positionLoc, 3, GL_FLOAT, GL_FALSE, 0, vertices);

    glLineWidth(2.0f);
    glDrawArrays(GL_LINES, 0, 2);
    glDisableVertexAttribArray(positionLoc);

    eglSwapBuffers(state->display, state->surface);
}

void cleanup(RendererState* state) {
    if (state->display != EGL_NO_DISPLAY) {
        eglMakeCurrent(state->display, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT);
        if (state->context != EGL_NO_CONTEXT) {
            eglDestroyContext(state->display, state->context);
        }
        if (state->surface != EGL_NO_SURFACE) {
            eglDestroySurface(state->display, state->surface);
        }
        eglTerminate(state->display);
    }

    if (state->window) {
        ANativeWindow_release(state->window);
    }
}

void onWindowFocusChanged(ANativeActivity* activity, int hasFocus) {
    RendererState* state = (RendererState*)activity->instance;
    state->animating = hasFocus;
    if (hasFocus && state->window) {
        renderLine(state);
    }
}

void onNativeWindowCreated(ANativeActivity* activity, ANativeWindow* window) {
    RendererState* state = (RendererState*)activity->instance;
    state->window = window;
    state->animating = 1;

    if (initEGL(state) == 0) {
        renderLine(state);
    }
}

void onNativeWindowDestroyed(ANativeActivity* activity, ANativeWindow* window) {
    RendererState* state = (RendererState*)activity->instance;
    state->animating = 0;
    state->window = NULL;
    cleanup(state);
}

void onInputQueueCreated(ANativeActivity* activity, AInputQueue* queue) {
    AInputQueue_attachLooper(queue, ALooper_forThread(), ALOOPER_POLL_CALLBACK, NULL, NULL);
}

void onInputQueueDestroyed(ANativeActivity* activity, AInputQueue* queue) {
    AInputQueue_detachLooper(queue);
}

int onInputEvent(int fd, int events, void* data) {
    return 1;
}

extern "C" void ANativeActivity_onCreate(ANativeActivity* activity, void* savedState, size_t savedStateSize) {
    RendererState* state = (RendererState*)malloc(sizeof(RendererState));
    memset(state, 0, sizeof(RendererState));
    activity->instance = state;

    activity->callbacks->onWindowFocusChanged = onWindowFocusChanged;
    activity->callbacks->onNativeWindowCreated = onNativeWindowCreated;
    activity->callbacks->onNativeWindowDestroyed = onNativeWindowDestroyed;
    activity->callbacks->onInputQueueCreated = onInputQueueCreated;
    activity->callbacks->onInputQueueDestroyed = onInputQueueDestroyed;

    ANativeActivity_setWindowFormat(activity, WINDOW_FORMAT_RGBA_8888);
}