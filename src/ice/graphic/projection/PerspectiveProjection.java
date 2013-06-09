package ice.graphic.projection;

import android.opengl.GLU;
import android.util.Log;

import javax.microedition.khronos.opengles.GL11;

import static javax.microedition.khronos.opengles.GL11.GL_PROJECTION;

/**
 * gl.glFrustumf(-width / 2, width / 2, -height / 2, height / 2, zNear, zFar);
 * 与
 * glu.gluPerspective(
 * gl,
 * fovy,
 * (float) width / (float) height,
 * zNear,
 * zFar
 * );
 * 等价.
 * <p/>
 * 还有个问题值得非常注意！
 * <p/>
 * 参考下这篇文章吧：Learning to Love your Z-buffer （通常 设的接近于0 如0.1,0.001 也没多大意义)
 * <p/>
 * Z Near 太近容易出现深度测试的问题!!! 这里默认情况下取 ZFarOfWindow*0.5f
 * <p/>
 * User: Jason
 * Date: 11-12-3
 * Time: 下午5:43
 */
public class PerspectiveProjection implements Projection {
    public static float DEFAULT_DEPTH = 100;
    private static final String TAG = "PerspectiveProjection";

    private int height;

    public PerspectiveProjection(float fovy) {
        this(fovy, 0, 0);
    }

    public PerspectiveProjection(float fovy, float zNear, float zFar) {
        this.fovy = fovy;
        this.zNear = zNear;
        this.zFar = zFar;
    }

    @Override
    public void setUp(GL11 gl, int width, int height) {
        this.height = height;

        if (zNear == 0 && zFar == 0) {
            float zFarOfWindow = getZFarOfWindow(fovy);
            zFar = zFarOfWindow + DEFAULT_DEPTH;
            zNear = zFarOfWindow * 0.7f;
        }

        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();

        GLU.gluPerspective(
                gl,
                fovy,
                (float) width / (float) height,
                zNear,
                zFar
        );

        Log.i(TAG, "zNear :" + zNear + "zFar :" + zFar);
    }

    public float getZFarOfWindow(float fovy) {
        double halfFovy = Math.toRadians(fovy / 2.0f);

        return (float) (height / (2 * Math.tan(halfFovy)));
    }

    public float getZFarOfWindow() {
        return getZFarOfWindow(fovy);
    }

    private float fovy;
    private float zNear, zFar;
}
