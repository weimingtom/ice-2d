package ice.graphic.gl_status;

import ice.node.Overlay;
import ice.util.GlUtil;

import static android.opengl.GLES11.*;

/**
 * User: jason
 * Date: 12-2-21
 * Time: 上午10:50
 */
public class BlendController implements GlStatusController {

    public static final BlendController BLEND_S_ONE_D_ONE = new BlendController(GL_ONE, GL_ONE);

    /**
     * 关闭混合
     */
    public BlendController() {
    }

    /**
     * 开启混合
     *
     * @param blend_S
     * @param factor_D
     */
    public BlendController(int blend_S, int factor_D) {
        blend = true;
        this.factorS = blend_S;
        this.factorD = factor_D;
    }


    @Override
    public void attach() {
        originalBlend = GlUtil.isEnabled(GL_BLEND);
        originalFactorS = GlUtil.getInteger(GL_BLEND_SRC);
        originalFactorD = GlUtil.getInteger(GL_BLEND_DST);

        if (blend) {
            glEnable(GL_BLEND);
            glBlendFunc(factorS, factorD);
        } else {
            glDisable(GL_BLEND);
        }
    }

    @Override
    public boolean detach(Overlay overlay) {
        if (originalBlend) {
            glEnable(GL_BLEND);
        } else {
            glDisable(GL_BLEND);
        }

        glBlendFunc(originalFactorS, originalFactorD);

        return true;
    }

    private boolean blend;
    private boolean originalBlend;
    private int factorS, factorD;
    private int originalFactorS, originalFactorD;
}
