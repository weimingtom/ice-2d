package ice.node.widget;

import android.graphics.Color;
import ice.engine.EngineContext;
import ice.engine.Scene;


public abstract class ConfirmDialog extends Scene {

    public ConfirmDialog() {
    }

    protected ConfirmDialog(int width, int height) {
        super(width, height);

        int color = Color.argb(125, 0, 0, 0);

        ColorOverlay colorOverlay = new ColorOverlay(
                color,
                EngineContext.getAppWidth(),
                EngineContext.getAppHeight()
        );

        addChild(colorOverlay);


    }

    public ButtonOverlay getConfirmButton() {
        return confirmButton;
    }

    public ButtonOverlay getCancelButton() {
        return cancelButton;
    }

    @Override
    protected <T> boolean onEvent(String channel, T event) {
        super.onEvent(channel, event);

        return true;    //禁止越过此dialog 操作其他控件
    }

    protected ButtonOverlay confirmButton;
    protected ButtonOverlay cancelButton;
}
