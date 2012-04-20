package ice.node;


import ice.animation.Animation;
import ice.graphic.GlRes;
import ice.graphic.gl_status.ColorController;
import ice.graphic.gl_status.GlStatusController;
import ice.graphic.gl_status.MatrixController;
import ice.model.Point3F;

import javax.microedition.khronos.opengles.GL11;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * User: ice
 * Date: 11-11-14
 * Time: 上午10:40
 */
public abstract class Overlay implements GlRes {

    private static long maxId;

    public synchronized static long requestId() {
        maxId++;

        if (maxId == Long.MAX_VALUE) {
            throw new IllegalStateException("ID reuse !");
        }

        return maxId;
    }

    public synchronized static void resetId() {
        maxId = 0;
    }

    public Overlay() {
        this(0, 0, 0);
    }

    public Overlay(float posX, float posY, float posZ) {
        visible = true;
        id = requestId();

        statusControllers = new ArrayList<GlStatusController>(3);
        statusControllers.add(new MatrixController());

        setPos(posX, posY, posZ);
    }

    @Override
    public void onEGLContextLost() {
    }

    @Override
    public void prepare(GL11 gl) {
    }

    @Override
    public void release(GL11 gl) {
    }

    public void draw(GL11 gl) {

        if (!visible) return;

        ensureStatusControllers();

        int controllerSize = statusControllers.size();

        for (int i = 0; i < controllerSize; i++) {
            GlStatusController controller = statusControllers.get(i);
            controller.attach(gl);
        }

        onDraw(gl);

        for (int i = controllerSize - 1; i >= 0; i--) {
            GlStatusController controller = statusControllers.get(i);
            boolean effectMoreFrame = controller.detach(gl, this);
            if (!effectMoreFrame) statusControllers.remove(i);
        }


    }

    private void ensureStatusControllers() {
        if (controllerEvents != null && controllerEvents.size() > 0) {
            List<GlStatusControllerEvent> copy = new ArrayList<GlStatusControllerEvent>(controllerEvents);
            controllerEvents.clear();

            for (GlStatusControllerEvent glStatusControllerEvent : copy) {
                if (glStatusControllerEvent.add) {
                    statusControllers.add(glStatusControllerEvent.controller);
                }
                else {
                    statusControllers.remove(glStatusControllerEvent.controller);
                }
            }
        }
    }

    protected abstract void onDraw(GL11 gl);

    public void startAnimation(Animation animation) {

        if (this.animation != null && !this.animation.isCompleted()) {
            throw new IllegalStateException("Another animation not finished yet !" + this.animation);
        }

        this.animation = animation;

        addGlStatusController(animation);

        if (!visible)
            setVisible(true);
    }

    public void cancelAnimation() {
        animation.cancel();
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void removeSelf() {
        if (parent != null)
            parent.remove(this);
    }

    public boolean hitTest(float x, float y) {
        return false;
    }

    protected <T> boolean onEvent(String channel, T event) {

        if (eventListeners == null)
            return false;

        for (EventListener listener : eventListeners) {

            if (listener.getChannel().equals(channel)) {
                boolean breakDispatch = listener.onEvent(this, event);

                if (breakDispatch)
                    return true;

            }

        }

        return false;
    }

    public void setPos(float posX, float posY) {
        getMatrixController().setPos(posX, posY);
    }

    public void setPos(float posX, float posY, float posZ) {
        getMatrixController().setPos(posX, posY, posZ);
    }

    public float getPosX() {
        return getMatrixController().getPosX();
    }

    public float getPosY() {
        return getMatrixController().getPosY();
    }

    public float getPosZ() {
        return getMatrixController().getPosZ();
    }

    public void setPosZ(float posZ) {
        getMatrixController().setPosZ(posZ);
    }

    public void setColor(int color) {
        addGlStatusController(new ColorController(color));
    }

    public void setColor(float[] color) {
        addGlStatusController(new ColorController(color));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Overlay overlay = (Overlay) o;

        if (id != overlay.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public Point3F getAbsolutePos() {
        MatrixController matrixController = getMatrixController();

        Point3F absolute = new Point3F(
                matrixController.getPosX(),
                matrixController.getPosY(),
                matrixController.getPosZ()
        );

        if (parent != null) {
            Point3F parentAbsolutePos = parent.getAbsolutePos();
            absolute.x += parentAbsolutePos.x;
            absolute.y += parentAbsolutePos.y;
            absolute.z += parentAbsolutePos.z;
        }

        return absolute;
    }

    protected void setParent(OverlayParent parent) {
        this.parent = parent;
    }

    public void setScale(float scaleX, float scaleY, float scaleZ) {
        getMatrixController().setScale(scaleX, scaleY, scaleZ);
    }

    public void setRotate(float rotate, float axleX, float axleY, float axleZ) {
        getMatrixController().setRotate(rotate, axleX, axleY, axleZ);
    }

    public Animation getAnimation() {
        return animation;
    }

    public void addGlStatusController(GlStatusController controller) {
        if (controllerEvents == null)
            controllerEvents = new CopyOnWriteArrayList<GlStatusControllerEvent>();

        controllerEvents.add(
                new GlStatusControllerEvent(controller, true)
        );
    }

    public void removeGlStatusController(GlStatusController controller) {
        if (controllerEvents == null)
            controllerEvents = new CopyOnWriteArrayList<GlStatusControllerEvent>();

        controllerEvents.add(
                new GlStatusControllerEvent(controller, false)
        );
    }

    public MatrixController getMatrixController() {
        return (MatrixController) statusControllers.get(0);
    }

    public OverlayParent getParent() {
        return parent;
    }

    public void addEventListener(EventListener listener) {

        if (eventListeners == null) {
            synchronized (this) {
                if (eventListeners == null)
                    eventListeners = new ArrayList<EventListener>(3);
            }
        }

        eventListeners.add(listener);
    }

    public boolean removeEventListener(EventListener listener) {
        return (eventListeners == null)
                ? false
                : eventListeners.remove(listener);
    }

    private volatile Animation animation;

    private volatile List<GlStatusControllerEvent> controllerEvents;

    protected List<GlStatusController> statusControllers;

    private List<EventListener> eventListeners;

    private boolean visible;

    /**
     * 用于获取绝对位置
     */
    private OverlayParent parent;
    private long id;

    private static final class GlStatusControllerEvent {

        private GlStatusControllerEvent(GlStatusController controller, boolean add) {
            this.controller = controller;
            this.add = add;
        }

        GlStatusController controller;
        boolean add;
    }
}