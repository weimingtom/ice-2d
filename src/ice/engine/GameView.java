package ice.engine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import ice.node.EventChannel;
import ice.node.Overlay;
import ice.node.OverlayRoot;

/**
 * User: ice
 * Date: 12-1-6
 * Time: 下午3:23
 */
public abstract class GameView extends GLSurfaceView implements AppView {

    public GameView(Context context) {
        super(context);

        setRenderer(
                this.renderer = onCreateGlRenderer()
        );
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

    private MotionEvent touchEvent;
    private GlRenderer renderer;
}
