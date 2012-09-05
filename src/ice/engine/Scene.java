package ice.engine;


import android.util.Log;
import ice.node.Overlay;
import ice.node.OverlayParent;

import javax.microedition.khronos.opengles.GL11;

/**
 * User: ice
 * Date: 11-11-14
 * Time: 上午10:41
 */
public class Scene extends OverlayParent<Overlay> {
    public Scene() {
    }

    @Override
    public void prepare(GL11 gl) {
        super.prepare(gl);

        Log.i("Scene: " + getClass().getSimpleName(), "prepare");
    }

    @Override
    public void release(GL11 gl) {
        super.release(gl);

        Log.i("Scene: " + getClass().getSimpleName(), "release");
    }

}
