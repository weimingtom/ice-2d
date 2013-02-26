package ice.graphic.gl_status;

import ice.node.Overlay;
import ice.util.BufferUtil;

import java.nio.FloatBuffer;

import static android.opengl.GLES11.*;

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
        color = BufferUtil.wrap(0.8f, 0.8f, 0, 0.5f);

        color.put(new float[]{0.8f, 0.8f, 0, 0.5f});
        color.position(0);

        density = 0.5f;
    }

    @Override
    public void attach() {

        glEnable(GL_FOG);

        glFogx(GL_FOG_MODE, GL_LINEAR); //  GL_EXP2

        glFogfv(GL_FOG_COLOR, color);

        glFogf(GL_FOG_DENSITY, density);

        glFogf(GL_FOG_START, start);
        glFogf(GL_FOG_END, end);

        glHint(GL_FOG_HINT, GL_NICEST);
    }

    @Override
    public boolean detach(Overlay overlay) {
        glDisable(GL_FOG);
        return true;
    }

    private float density;
    private FloatBuffer color;
}
