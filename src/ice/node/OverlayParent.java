package ice.node;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * User: ice
 * Date: 11-11-14
 * Time: 下午12:00
 */
public class OverlayParent<T extends Overlay> extends Overlay {

    public OverlayParent() {
        children = new CopyOnWriteArrayList<T>();
    }

    @Override
    public void onEGLContextLost() {
        super.onEGLContextLost();

        for (T child : children)
            child.onEGLContextLost();
    }

    @Override
    public void prepare() {
        super.prepare();

        for (T child : children)
            child.prepare();
    }

    @Override
    public void release() {
        super.release();

        for (T child : children)
            child.release();
    }

    @Override
    protected void onDraw() {

        for (T overlay : children)
            overlay.draw();

    }

    /**
     * 建议批量添加！
     *
     * @param child
     */
    public void addChild(T child) {
        addChildren(child);
    }

    public void addChild(int index, T child) {
        child.setParent(this);
        children.add(index, child);
    }

    public void addChildren(T... children) {
        addChildren(Arrays.asList(children));
    }

    public void addChildren(Collection<? extends T> children) {
        if (children.size() == 0)
            throw new IllegalArgumentException("size ==0 !");

        for (T child : children) {
            if (child == null) throw new NullPointerException();
            child.setParent(this);
        }

        this.children.addAll(children);
    }

    public boolean containsChild(T child) {
        return children.contains(child);
    }

    public T get(int index) {
        return children.get(index);
    }

    public int indexOf(T child) {
        return children.indexOf(child);
    }

    public void remove(T child) {
        OverlayRoot.scheduleRelease(child);
        children.remove(child);
    }

    public void remove(T... children) {
        remove(Arrays.asList(children));
    }

    public void remove(Collection<? extends T> children) {
        OverlayRoot.scheduleRelease(children);
        this.children.removeAll(children);
    }

    public void clear() {
        OverlayRoot.scheduleRelease(children);
        children.clear();
    }

    public T top() {
        if (children.size() == 0)
            return null;

        return children.get(children.size() - 1);
    }

    public int size() {
        return children.size();
    }

    @Override
    protected <T> boolean onEvent(String channel, T event) {

        for (int index = size() - 1; index >= 0; index--) {
            if (get(index).onEvent(channel, event))
                return true;
        }

        return super.onEvent(channel, event);
    }

    public Iterator<T> iterator() {
        return children.iterator();
    }

    /**
     * Returns a duplicated buffer that shares its content with this children.
     *
     * @return
     */
    public List<T> duplicate() {
        return new ArrayList<T>(children);
    }

    private List<T> children;
}
