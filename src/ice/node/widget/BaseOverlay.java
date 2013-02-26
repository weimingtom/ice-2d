package ice.node.widget;

import ice.graphic.GlRes;
import ice.graphic.texture.Texture;
import ice.model.vertex.VertexData;
import ice.node.Overlay;

/**
 * User: ice
 * Date: 11-11-14
 * Time: 下午2:35
 */
public class BaseOverlay<T extends VertexData> extends Overlay {

    public BaseOverlay() {
        this(null);
    }

    public BaseOverlay(T vertexData) {
        this(vertexData, null);
    }

    public BaseOverlay(T vertexData, Texture texture) {
        this.vertexData = vertexData;
        this.texture = texture;
    }

    @Override
    protected synchronized void onDraw() {

        vertexData.attach();

        Texture theTexture = texture;
        boolean useTexture = (theTexture != null);

        if (useTexture)
            theTexture.attach();

        vertexData.onDrawVertex();

        if (useTexture) theTexture.detach(this);

        vertexData.detach(this);

    }

    public T getVertexData() {
        return vertexData;
    }

    public void setVertexData(T vertexData) {
        this.vertexData = vertexData;
    }

    public synchronized void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    @Override
    public void onEGLContextLost() {
        super.onEGLContextLost();

        if (texture != null)
            texture.onEGLContextLost();

        if (vertexData != null && vertexData instanceof GlRes)
            ((GlRes) vertexData).onEGLContextLost();
    }

    @Override
    public void prepare() {
        super.prepare();

        if (texture != null)
            texture.prepare();

        if (vertexData != null && vertexData instanceof GlRes)
            ((GlRes) vertexData).prepare();
    }

    @Override
    public void release() {
        super.release();

        if (texture != null)
            texture.release();

        if (vertexData != null && vertexData instanceof GlRes)
            ((GlRes) vertexData).release();
    }

    private Texture texture;
    private T vertexData;
}
