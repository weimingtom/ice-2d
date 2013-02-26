package ice.animation;

import ice.node.Overlay;

import static android.opengl.GLES11.*;

/**
 * 依赖 源因子GL_SRC_ALPHA实现，注意Drawable的混合因子设置
 */
public class ColorAnimation extends Animation {
    public static final float[] WHITE = new float[]{1, 1, 1, 1};

    public ColorAnimation(long duration, float[] toColor) {
        this(duration, WHITE, toColor);
    }

    public ColorAnimation(long duration, float[] fromColor, float[] toColor) {
        super(duration);

        this.fromColor = fromColor;
        this.toColor = toColor;
    }

    @Override
    protected void applyFillAfter(Overlay overlay) {
        overlay.setColor(toColor);
    }

    @Override
    protected void onAttach(float interpolatedTime) {
//        gl.glEnable(GL_BLEND);
//        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//        //gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

        float r = fromColor[0] + ((toColor[0] - fromColor[0]) * interpolatedTime);
        float g = fromColor[1] + ((toColor[1] - fromColor[1]) * interpolatedTime);
        float b = fromColor[2] + ((toColor[2] - fromColor[2]) * interpolatedTime);
        float a = fromColor[3] + ((toColor[3] - fromColor[3]) * interpolatedTime);

        glColor4f(r, g, b, a);
    }

    @Override
    protected void onDetach(Overlay overlay) {
        // gl.glDisable(GL_BLEND);

        glColor4f(1, 1, 1, 1);
    }

    private float[] fromColor, toColor;
}
