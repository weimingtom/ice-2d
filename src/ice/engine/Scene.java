package ice.engine;


import android.util.Log;
import ice.node.Overlay;
import ice.node.OverlayParent;

/**
 * User: ice
 * Date: 11-11-14
 * Time: 上午10:41
 */
public class Scene extends OverlayParent<Overlay> {
    public Scene() {
    }

    @Override
    public void prepare() {
        super.prepare();

        Log.i("Scene: " + getClass().getSimpleName(), "prepare");
    }

    @Override
    public void release() {
        super.release();

        Log.i("Scene: " + getClass().getSimpleName(), "release");
    }

}
