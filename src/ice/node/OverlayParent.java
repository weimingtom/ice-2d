package ice.node;

import android.view.MotionEvent;

import javax.microedition.khronos.opengles.GL11;
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
    public void prepare(GL11 gl) {
        super.prepare(gl);

        for (T child : children)
            child.prepare(gl);
    }

    @Override
    public void release(GL11 gl) {
        super.release(gl);

        for (T child : children)
            child.release(gl);
    }

    @Override
    protected void onDraw(GL11 gl) {

        for (T overlay : children)
            overlay.draw(gl);

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
    protected boolean onTouchEvent(MotionEvent event) {

        for (int index = size() - 1; index >= 0; index--) {
            if (get(index).onTouchEvent(event))
                return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected boolean hoverTest() {
        return isVisible();
    }

    @Override
    protected boolean onHoverEvent(MotionEvent event) {
        if (!hoverTest())
            return false;

        for (int index = size() - 1; index >= 0; index--) {
            if (get(index).onHoverEvent(event))
                return true;
        }

        return super.onHoverEvent(event);
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
