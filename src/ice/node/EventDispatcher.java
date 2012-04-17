package ice.node;

/**
 * User: jason
 * Date: 12-4-17
 * Time: 下午4:40
 */
public interface EventDispatcher<T> {

    void dispatchEvent(String channel, T event);

}
