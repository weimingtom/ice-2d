package ice.node.widget;

import ice.graphic.GlRes;
import ice.graphic.texture.Texture;
import ice.model.vertex.VertexData;
import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;

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
    protected void onDraw(GL11 gl) {

        vertexData.attach(gl);

        Texture theTexture = texture;
        boolean useTexture = (theTexture != null);

        if (useTexture)
            theTexture.attach(gl);

        vertexData.onDrawVertex(gl);

        if (useTexture) theTexture.detach(gl, this);

        vertexData.detach(gl, this);

    }

    public T getVertexData() {
        return vertexData;
    }

    public void setVertexData(T vertexData) {
        this.vertexData = vertexData;
    }

    public void setTexture(Texture texture) {
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
    public void prepare(GL11 gl) {
        super.prepare(gl);

        if (texture != null)
            texture.prepare(gl);

        if (vertexData != null && vertexData instanceof GlRes)
            ((GlRes) vertexData).prepare(gl);
    }

    @Override
    public void release(GL11 gl) {
        super.release(gl);

        if (texture != null)
            texture.release(gl);

        if (vertexData != null && vertexData instanceof GlRes)
            ((GlRes) vertexData).release(gl);
    }

    private Texture texture;
    private T vertexData;
}
