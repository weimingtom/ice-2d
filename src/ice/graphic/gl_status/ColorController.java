package ice.graphic.gl_status;

import android.graphics.Color;
import ice.node.Overlay;
import static android.opengl.GLES11.*;

/**
 * User: jason
 * Date: 12-2-21
 * Time: 上午11:21
 */
public class ColorController implements GlStatusController {

    public ColorController() {
        this(1, 1, 1, 1);
    }

    public ColorController(int color) {
        setColor(color);
    }

    public ColorController(float r, float g, float b, float a) {
        this.color = new float[]{r, g, b, a};
    }

    public ColorController(float[] color) {
        this.color = color;
    }

    @Override
    public void attach() {
        if (color != null)
            glColor4f(color[0], color[1], color[2], color[3]);
    }

    @Override
    public boolean detach(Overlay overlay) {
        if (color != null)   //TODO 先这样吧
            glColor4f(1, 1, 1, 1);

        return true;
    }

    public void setColor(float[] color) {
        this.color = color;
    }

    public void setColor(int color) {
        this.color = new float[]{
                Color.red(color) / 255f,
                Color.green(color) / 255f,
                Color.blue(color) / 255f,
                Color.alpha(color) / 255f
        };
    }

    private float[] color;
}
