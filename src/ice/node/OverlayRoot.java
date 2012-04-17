package ice.node;

import android.util.Log;
import android.view.MotionEvent;
import ice.graphic.GlRes;
import ice.graphic.texture.Texture;
import ice.practical.Fps;

import javax.microedition.khronos.opengles.GL11;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * User: jason
 * Date: 12-3-5
 * Time: 下午4:33
 */
public class OverlayRoot extends OverlayParent {

    private static List<GlRes> newlyRemoved;
    private static List<GlRes> newlyAdded;

    private static List<GlRes> autoManaged;

    private static List<GlRes> buffer;

    static {
        newlyRemoved = new CopyOnWriteArrayList<GlRes>();
        newlyAdded = new CopyOnWriteArrayList<GlRes>();

        autoManaged = new ArrayList<GlRes>();
        buffer = new ArrayList<GlRes>();
    }

    public static void scheduleAutoManaged(GlRes res) {
        newlyAdded.add(res);
        autoManaged.add(res);
    }

    public static void scheduleRelease(GlRes res) {
        newlyRemoved.add(res);
        autoManaged.remove(res);
    }

    public static void scheduleAutoManaged(Collection<? extends GlRes> reses) {
        newlyAdded.addAll(reses);
        autoManaged.addAll(reses);
    }

    public static void scheduleRelease(Collection<? extends GlRes> reses) {
        newlyRemoved.addAll(reses);
        autoManaged.removeAll(reses);
    }

    public OverlayRoot() {
        fps = new Fps();
    }

    @Override
    public void draw(GL11 gl) {

        buffer.clear();
        buffer.addAll(newlyAdded);
        for (GlRes res : buffer)
            res.prepare(gl);

        newlyAdded.removeAll(buffer);

        super.draw(gl);

        fps.draw(gl);

        buffer.clear();
        buffer.addAll(newlyRemoved);
        for (GlRes res : buffer)
            res.release(gl);

        newlyRemoved.removeAll(buffer);
    }

    public void dispatchTouch(MotionEvent event) {
        Overlay top = top();

        if (top != null)
            top.onTouchEvent(event);
    }

    public void dispatchHover(MotionEvent event) {
        Overlay top = top();

        if (top != null)
            top.onHoverEvent(event);
    }

    @Override
    public void onEGLContextLost() {
        Texture.clearShared();

        for (GlRes glRes : autoManaged) {
            glRes.onEGLContextLost();
        }

        super.onEGLContextLost();

        fps.onEGLContextLost();

        Log.w("OverlayRoot", "onEGLContextLost");
    }

    private Fps fps;
}
