package ice.engine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
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

        event.setLocation(event.getX(), EngineContext.getAppHeight() - event.getY());

        if (renderer != null) {
            OverlayRoot overlayRoot = renderer.getOverlayRoot();

            overlayRoot.dispatchTouch(event);
        }

        return true;
    }

    @Override
    public boolean onHoverEvent(MotionEvent event) {
        event.setLocation(event.getX(), EngineContext.getAppHeight() - event.getY());

        if (renderer != null) {
            OverlayRoot overlayRoot = renderer.getOverlayRoot();

            overlayRoot.dispatchHover(event);
        }

        return true;
    }

    @Override
    public void onPause() {
        super.onPause();

        renderer.getOverlayRoot().onEGLContextLost();
    }

    private GlRenderer renderer;
}
