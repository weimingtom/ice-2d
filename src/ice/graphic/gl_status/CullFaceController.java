package ice.graphic.gl_status;

import ice.node.Overlay;
import ice.util.GlUtil;


import static javax.microedition.khronos.opengles.GL11.GL_FRONT_FACE;
import static android.opengl.GLES11.*;

/**
 * User: jason
 * Date: 12-2-21
 * Time: 下午2:22
 */
public class CullFaceController implements GlStatusController {

    public enum FaceMode {
        Front, Back, BothSide
    }

    public CullFaceController(FaceMode faceMode) {
        this.faceMode = faceMode;
    }

    @Override
    public void attach() {

        originalCullFace = glIsEnabled(GL_CULL_FACE);
        originalFaceMode = GlUtil.getInteger(GL_FRONT_FACE);

        switch (faceMode) {

            case Front:
                if (!originalCullFace)
                    glEnable(GL_CULL_FACE);
                glFrontFace(GL_CCW);
                break;

            case Back:
                if (!originalCullFace)
                    glEnable(GL_CULL_FACE);
                glFrontFace(GL_CW);
                break;

            case BothSide:
                glDisable(GL_CULL_FACE);
                break;
        }


    }

    @Override
    public boolean detach(Overlay overlay) {
        if (originalCullFace) {
            glEnable(GL_CULL_FACE);
        } else {
            glDisable(GL_CULL_FACE);
        }

        glFrontFace(originalFaceMode);

        return true;
    }

    private boolean originalCullFace;
    private int originalFaceMode;
    private FaceMode faceMode;
}
