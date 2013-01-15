package ice.node.widget;

import ice.model.Point3F;
import ice.model.vertex.NormalRect;
import ice.model.vertex.Rect;
import ice.model.vertex.VboRect;

/**
 * User: jason
 * Date: 12-2-23
 * Time: 下午4:16
 */
public class RectOverlay extends BaseOverlay<Rect> {

    public RectOverlay(float width, float height) {
        this(width, height, true);
    }

    public RectOverlay(boolean vbo, float width, float height) {
        this(width, height, vbo, true);
    }

    public RectOverlay(float width, float height, boolean ccw) {
        this(width, height, true, ccw);
    }

    public RectOverlay(float width, float height, boolean vbo, boolean ccw) {
        if (vbo) {
            setVertexData(new VboRect(width, height, ccw));
        }
        else {
            setVertexData(new NormalRect(width, height, ccw));
        }
    }

    public void setBounds(float width, float height) {
        Rect rectangle = getVertexData();
        rectangle.setBounds(width, height);
    }

    public void setTextureCoord(float uRight, float vBottom) {
        setTextureCoord(0, uRight, 0, vBottom);
    }

    public void setTextureCoord(float uLeft, float uRight, float vTop, float vBottom) {
        Rect rectangle = getVertexData();
        rectangle.setTextureCoord(uLeft, uRight, vTop, vBottom);
    }

    public float getWidth() {
        return getVertexData().getWidth();
    }

    public float getHeight() {
        return getVertexData().getHeight();
    }

    @Override
    public boolean hitTest(float x, float y) { //todo
        Point3F absolutePos = getAbsolutePos();
        float offsetX = x - absolutePos.x;
        float offsetY = absolutePos.y;// (EngineContext.getAppHeight() - y) - absolutePos.y;

        float halfWidth = getWidth() / 2;
        float halfHeight = getHeight() / 2;

        return offsetX >= -halfWidth
                && offsetX <= halfWidth
                && offsetY >= -halfHeight
                && offsetY <= halfHeight;
    }
}
