package ice.node.widget;

import ice.engine.Scene;


public abstract class ConfirmDialog extends Scene {

    public ConfirmDialog() {
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
