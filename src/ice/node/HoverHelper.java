package ice.node;

import android.view.MotionEvent;

/**
 * User: jason
 * Date: 12-3-5
 * Time: 下午4:50
 */
public class HoverHelper {

    private static HoverHelper instance = new HoverHelper();

    public static HoverHelper getInstance() {
        return instance;
    }

    HoverHelper() {
    }

    public boolean onHoverEvent(Overlay overlay, MotionEvent event) {
        if (!overlay.hoverTest())
            return false;

        boolean hitTest = overlay.hitTest(event.getX(), event.getY());

        if (overlay != lastHovered) {

            if (hitTest) {
                if (lastHovered != null)
                    lastHovered.onLoseHover(event);

                overlay.onGetHover(event);
                lastHovered = overlay;
                return true;
            }
            else {
                return false;
            }
        }
        else {
            if (hitTest) {
                if (event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                    overlay.onLoseHover(event);
                    lastHovered = null;
                }
                else {
                    overlay.onMoveHover(event);
                }
                return true;
            }
            else {
                overlay.onLoseHover(event);
                lastHovered = null;
                return true;
            }
        }

    }

    private Overlay lastHovered;
}
