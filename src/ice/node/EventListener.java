package ice.node;

/**
 * User: jason
 * Date: 12-4-17
 * Time: 下午5:24
 */
public interface EventListener<T> extends EventChannel {

    boolean onEvent(Overlay overlay, T event);

}
