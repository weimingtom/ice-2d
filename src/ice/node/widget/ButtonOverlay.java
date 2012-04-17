package ice.node.widget;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import ice.audio.AudioAgent;
import ice.node.EventChannel;
import ice.node.EventListener;
import ice.node.Overlay;
import ice.res.Res;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ice
 * Date: 11-12-2
 * Time: 下午12:09
 */
public class ButtonOverlay extends BitmapOverlay {
    private static String clickSoundRes;
    private static final String NO_CLICK_EFFECT = null;

    public static class State {
        public static final String NORMAL = "normal";
        public static final String PRESSED = "pressed";
        public static final String DISABLED = "disabled";

        public State(String name, int resId) {
            this(name, Res.getBitmap(resId));
        }

        public State(String name, Bitmap bitmap) {
            this.name = name;
            this.bitmap = Res.tryAdjustToPot(bitmap);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof State)) return false;

            State state = (State) o;

            if (name != null ? !name.equals(state.name) : state.name != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

        String name;
        Bitmap bitmap;
    }

    public static void setClickSoundRes(String clickSoundRes) {
        ButtonOverlay.clickSoundRes = clickSoundRes;
    }

    public interface OnClickListener {
        void onClick(ButtonOverlay btn);
    }

    public ButtonOverlay(int normal, int pressed) {
        this(
                Res.getBitmap(normal),
                Res.getBitmap(pressed)
        );
    }

    public ButtonOverlay(Bitmap normal, Bitmap pressed) {
        this((float) normal.getWidth(), (float) normal.getHeight());

        State normalState = new State(State.NORMAL, normal);
        State pressedState = new State(State.PRESSED, pressed);

        addState(normalState, pressedState);
        setState(normalState);
    }

    public ButtonOverlay(float width, float height) {
        super(width, height);

        states = new ArrayList<State>(4);

        touchAble = true;

        addEventListener(new ClickHandler());
    }

    protected void onClick() {
        if (onClickListener != null)
            onClickListener.onClick(this);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    protected void onGetTouchFocus() {
        setState(State.PRESSED);

        playClickSound();
    }

    protected void playClickSound() {
        if (clickSoundRes != NO_CLICK_EFFECT)
            AudioAgent.get().playSound(clickSoundRes);
    }

    protected void onLostTouchFocus() {
        setState(State.NORMAL);
    }

    public void setState(State state) {
        if (currentState != state) {
            currentState = state;
            setBitmap(state.bitmap);
        }
    }

    public void setState(String stateName) {
        for (State state : states) {
            if (stateName.equals(state.name)) {
                setState(state);
                return;
            }
        }

        throw new IllegalArgumentException("requests state not exist !" + stateName);
    }

    public void addState(State... states) {
        for (int i = 0; i < states.length; i++) {
            State state = states[i];
            addState(state);
        }
    }

    public boolean removeState(State state) {
        return states.remove(state);
    }

    public void addState(State state) {
        if (states.contains(state))
            throw new IllegalStateException("state exists !");

        states.add(state);
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setTouchAble(boolean touchAble) {
        this.touchAble = touchAble;
    }

    public boolean isTouchAble() {
        return touchAble;
    }

    private boolean focusing;
    private boolean touchAble;
    private OnClickListener onClickListener;

    private State currentState;
    protected List<State> states;

    public class ClickHandler implements EventListener<MotionEvent> {

        @Override
        public boolean onEvent(Overlay overlay, MotionEvent event) {
            if (!isVisible() || !touchAble) return false;

            int action = event.getAction();

            int x = (int) event.getX();
            int y = (int) event.getY();

            boolean hitTest = hitTest(x, y);

            if (hitTest) {
                this.hitTest = true;
            }
            else {
                if (this.hitTest) {
                    action = MotionEvent.ACTION_CANCEL;
                    this.hitTest = false;
                }
                else {
                    this.hitTest = false;
                    return false;
                }
            }

            if (action == MotionEvent.ACTION_UP) {
                if (focusing) {
                    focusing = false;
                    onLostTouchFocus();
                    onClick();
                }
            }
            else if (action == MotionEvent.ACTION_CANCEL) {
                if (focusing) {
                    focusing = false;
                    onLostTouchFocus();
                }
            }
            else {
                if (!focusing) {
                    focusing = true;
                    onGetTouchFocus();
                }
            }

            return hitTest;
        }

        @Override
        public String getChannel() {
            return EventChannel.TOUCH_CHANNEL;
        }

        private boolean hitTest;
    }
}
