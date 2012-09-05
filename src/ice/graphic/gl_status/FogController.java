package ice.graphic.gl_status;

import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static ice.model.Constants.BYTE_OF_FLOAT;
import static javax.microedition.khronos.opengles.GL11.*;

/**
 * Fog .
 * //TODO Not finished yet !
 * User: jason
 * Date: 12-3-29
 * Time: 上午11:48
 */
public class FogController implements GlStatusController {
    private float start, end;

    public FogController(float start, float end) {
        ByteBuffer bb = ByteBuffer.allocateDirect(BYTE_OF_FLOAT * 4);
        bb.order(ByteOrder.nativeOrder());
        color = bb.asFloatBuffer();

        color.put(new float[]{0.8f, 0.8f, 0, 0.5f});
        color.position(0);

        density = 0.5f;
    }

    @Override
    public void attach(GL11 gl) {

        gl.glEnable(GL_FOG);

        gl.glFogx(GL_FOG_MODE, GL_LINEAR); //  GL_EXP2

        gl.glFogfv(GL_FOG_COLOR, color);

        gl.glFogf(GL_FOG_DENSITY, density);

        gl.glFogf(GL_FOG_START, start);
        gl.glFogf(GL_FOG_END, end);

        gl.glHint(GL_FOG_HINT, GL_NICEST);
    }

    @Override
    public boolean detach(GL11 gl, Overlay overlay) {
        gl.glDisable(GL_FOG);
        return true;
    }

    private float density;
    private FloatBuffer color;
}
