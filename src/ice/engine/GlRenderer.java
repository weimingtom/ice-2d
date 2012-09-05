package ice.engine;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;
import ice.graphic.projection.PerspectiveProjection;
import ice.graphic.projection.Projection;
import ice.graphic.texture.Texture;
import ice.node.OverlayRoot;
import ice.util.GlUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import static javax.microedition.khronos.opengles.GL10.*;

/**
 * EGL Context Lost
 * <p/>
 * There are situations where the EGL rendering context will be lost.
 * This typically happens when device wakes up after going to sleep.
 * When the EGL context is lost, all OpenGL resources (such as textures) that are associated with that context will be automatically deleted.
 * In order to keep rendering correctly, a renderer must recreate any lost resources that it still needs.
 * The onSurfaceCreated(GL10, EGLConfig) method is a convenient place to do this.
 * <p/>
 * User: ice
 * Date: 12-1-6
 * Time: 下午4:24
 */
public class GlRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "GlRenderer";

    public interface OnPreparedListener {
        void onPrepared();
    }

    private OnPreparedListener onPreparedListener;
    private int width, height;

    public GlRenderer(Projection projection) {
        this.projection = projection;

        overlayRoot = new OverlayRoot();
    }

    public void setOnPreparedListener(OnPreparedListener listener) {
        onPreparedListener = listener;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Log.w(TAG, "onSurfaceCreated");

        GL11 gl = (GL11) gl10;

        init(gl);

        if (onPreparedListener != null)
            onPreparedListener.onPrepared();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        this.width = width;
        this.height = height;
        Log.w(TAG, "onSurfaceChanged");
        projection.setUp((GL11) gl10, width, height);

        gl10.glMatrixMode(GL_MODELVIEW);
        gl10.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GL11 gl = (GL11) gl10;

        reset(gl, width, height);

        overlayRoot.draw(gl);

        checkError(gl);
    }

    protected void reset(GL11 gl, int width, int height) {

        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();

        float windowZ = 0;

        if (projection instanceof PerspectiveProjection) { //移动z到窗口
            PerspectiveProjection perspectiveProjection = (PerspectiveProjection) projection;
            windowZ = -0.1f - perspectiveProjection.getZFarOfWindow();
        }

        gl.glTranslatef(-width / 2.0f, -height / 2.0f, windowZ);
    }

    public OverlayRoot getOverlayRoot() {
        return overlayRoot;
    }

    private void init(GL11 gl) {
        if (!Texture.isInited()) {
            boolean p_o_tSupported = GlUtil.queryN_P_O_TSupported(gl);
            int maxTextureSize = GlUtil.getInteger(gl, GL_MAX_TEXTURE_SIZE);

            Texture.init(p_o_tSupported, maxTextureSize);
        }

        gl.glClearColor(0, 0, 0, 1.0f);

        System.out.println("GL_RENDERER = " + gl.glGetString(GL_RENDERER));
        System.out.println("GL_VENDOR = " + gl.glGetString(GL_VENDOR));
        System.out.println("GL_VERSION = " + gl.glGetString(GL_VERSION));
        System.out.println("GL_EXTENSIONS = " + gl.glGetString(GL_EXTENSIONS));

        onInit(gl);
    }

    protected void onInit(GL11 gl) {

    }

    private void checkError(GL11 gl) {
        int errorCode = gl.glGetError();

        if (errorCode != GL_NO_ERROR) {
            throw new IllegalStateException(
                    GLU.gluErrorString(errorCode)
            );
        }
    }

    private Projection projection;
    private OverlayRoot overlayRoot;

}
