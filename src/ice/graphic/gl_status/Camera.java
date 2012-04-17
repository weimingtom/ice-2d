package ice.graphic.gl_status;

import android.opengl.GLU;
import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;

/**
 * User: Jason
 * Date: 11-12-3
 * Time: 下午7:39
 */
public class Camera implements GlStatusController {

    public Camera(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
        this.eyeX = eyeX;
        this.eyeY = eyeY;
        this.eyeZ = eyeZ;
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;
        this.upX = upX;
        this.upY = upY;
        this.upZ = upZ;
    }

    @Override
    public void attach(GL11 gl) {
        GLU.gluLookAt(
                gl,
                eyeX, eyeY, eyeZ,
                centerX, centerY, centerZ,
                upX, upY, upZ
        );
    }

    @Override
    public boolean detach(GL11 gl, Overlay overlay) {
        GLU.gluLookAt(
                gl,
                0, 0, 0,
                0, 0, -1,
                0, 1, 0
        );

        return true;
    }

    private float eyeX, eyeY, eyeZ;
    private float centerX, centerY, centerZ;
    private float upX, upY, upZ;
}
