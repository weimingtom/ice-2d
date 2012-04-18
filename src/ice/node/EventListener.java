package ice.node;

/**
 * User: jason
 * Date: 12-4-17
 * Time: 下午5:24
 */
public interface EventListener<T> extends EventChannel {

    /**
     * handle event.
     *
     * @param overlay
     * @param event
     * @return break dispatch true else false
     */
    boolean onEvent(Overlay overlay, T event);

}
