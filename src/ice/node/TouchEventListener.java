package ice.node;

import android.view.MotionEvent;

/**
 * User: jason
 * Date: 12-4-17
 * Time: 下午6:21
 */
public abstract class TouchEventListener implements EventListener<MotionEvent> {

    @Override
    public String getChannel() {
        return EventChannel.TOUCH_CHANNEL;
    }

}
