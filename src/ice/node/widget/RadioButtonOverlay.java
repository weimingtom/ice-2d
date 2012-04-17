package ice.node.widget;

/**
 * User: jason
 * Date: 12-1-12
 * Time: 下午4:33
 */
public class RadioButtonOverlay extends ButtonOverlay {

    private static final String LOCK_STATE = "lock";
    private boolean lock;

    public interface OnToggledListener {
        void onToggled(RadioButtonOverlay radioButton);
    }

    public RadioButtonOverlay(int tileNormalId, int tilePressedId, int lockedId) {
        super(tileNormalId, tilePressedId);

        State lockState = new State(LOCK_STATE, lockedId);

        addState(lockState);
    }

    @Override
    protected void onGetTouchFocus() {
        super.onGetTouchFocus();

        RadioButtonOverlay toggled = parent.getToggled();

        if (toggled == null) {

            if (!lock) {
                parent.setToggled(this);

                if (onToggledListener != null) {
                    onToggledListener.onToggled(this);
                }

            }

            return;
        }

        if (toggled != this) {

            toggled.setState(State.NORMAL);

            if (lock) {
                parent.setToggled(null);
            }
            else {
                parent.setToggled(this);

                if (onToggledListener != null) {
                    onToggledListener.onToggled(this);
                }
            }


        }
    }

    @Override
    protected void onLostTouchFocus() {
        if (lock) {
            super.onLostTouchFocus();
        }

    }


    public void setOnToggledListener(OnToggledListener onToggledListener) {
        this.onToggledListener = onToggledListener;
    }

    void setParent(RadioGroup parent) {
        this.parent = parent;
    }

    private RadioGroup parent;
    private OnToggledListener onToggledListener;
}
