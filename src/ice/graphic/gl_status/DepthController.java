package ice.graphic.gl_status;

import ice.node.Overlay;
import ice.util.GlUtil;

import static android.opengl.GLES11.*;

/**
 * User: jason
 * Date: 12-2-21
 * Time: 上午10:26
 */
public class DepthController implements GlStatusController {

    public DepthController(boolean depthTest) {
        this.depthTest = depthTest;
    }

    @Override
    public void attach() {

        originalDepthTest = GlUtil.isEnabled(GL_DEPTH_TEST);

        if (originalDepthTest != depthTest) {

            if (depthTest) {
                glEnable(GL_DEPTH_TEST);
            } else {
                glDisable(GL_DEPTH_TEST);
            }

        }
    }

    @Override
    public boolean detach(Overlay overlay) {

        if (originalDepthTest != depthTest) {

            if (originalDepthTest) {
                glEnable(GL_DEPTH_TEST);
            } else {
                glDisable(GL_DEPTH_TEST);
            }

        }

        return true;
    }

    private boolean originalDepthTest;
    private boolean depthTest;
}
