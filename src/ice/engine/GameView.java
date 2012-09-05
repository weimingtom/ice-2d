package ice.engine;

import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import ice.node.EventChannel;
import ice.node.Overlay;
import ice.node.OverlayRoot;

import java.util.LinkedList;
import java.util.List;

/**
 * User: ice
 * Date: 12-1-6
 * Time: 下午3:23
 */
public abstract class GameView extends GLSurfaceView implements AppView, AppLifeCycleObserver {
    protected GlRenderer renderer;
    private List<OnSizeChangeListener> sizeChangeListeners;

    public GameView(App app) {
        super(app.getContext());

        setRenderer(
                renderer = onCreateGlRenderer()
        );

        sizeChangeListeners = new LinkedList<OnSizeChangeListener>();
    }

    protected abstract GlRenderer onCreateGlRenderer();

    public GlRenderer getRenderer() {
        return renderer;
    }

    @Override
    public void showScene(Scene scene) {
        OverlayRoot overlayRoot = renderer.getOverlayRoot();

        overlayRoot.addChild(scene);
    }

    @Override
    public void switchScene(Scene newScene) {
        OverlayRoot overlayRoot = renderer.getOverlayRoot();

        Overlay oldScene = overlayRoot.top();

        overlayRoot.remove(oldScene); //先施放资源？
        overlayRoot.addChild(newScene);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (renderer != null) {
            OverlayRoot overlayRoot = renderer.getOverlayRoot();

            overlayRoot.dispatchEvent(EventChannel.TOUCH_CHANNEL, event);
        }

        return true;
    }

    @Override
    public void onPause() {
        super.onPause();

        renderer.getOverlayRoot().onEGLContextLost();
    }

    @Override
    public void onCreate(App app) {
        //TODO
    }

    @Override
    public void onResume(App app) {
        onResume();
    }

    @Override
    public void onPause(App app) {
        onPause();
    }

    @Override
    public void onDestroy(App app) {
        //TODO
    }

    @Override
    public void addOnSizeChangeListener(OnSizeChangeListener listener) {
        sizeChangeListeners.add(listener);
    }

    @Override
    public void removeOnSizeChangeListener(OnSizeChangeListener listener) {
        sizeChangeListeners.remove(listener);
    }

}
