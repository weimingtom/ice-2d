package ice.graphic.gl_status;

import ice.node.Overlay;

import static android.opengl.GLES11.glMultMatrixf;
import static android.opengl.Matrix.setLookAtM;

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
    public void attach() {
        float[] result = new float[4 * 4];

        setLookAtM(
                result, 0,
                eyeX, eyeY, eyeZ,
                centerX, centerY, centerZ,
                upX, upY, upZ
        );

        glMultMatrixf(result, 0);
    }

    @Override
    public boolean detach(Overlay overlay) {
        float[] result = new float[4 * 4];

        setLookAtM(
                result, 0,
                0, 0, 0,
                0, 0, -1,
                0, 1, 0
        );

        glMultMatrixf(result, 0);

        return true;
    }

    private float eyeX, eyeY, eyeZ;
    private float centerX, centerY, centerZ;
    private float upX, upY, upZ;
}
