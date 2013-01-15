package ice.model.vertex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * User: jason
 * Date: 13-1-15
 */
public abstract class AbstractVertexData implements VertexData {

    protected AbstractVertexData() {
    }

    protected AbstractVertexData(int verticesCount, VertexAttributes attributes) {
        init(verticesCount, attributes);

    }

    protected void init(int verticesCount, VertexAttributes attributes) {
        this.attributes = attributes;

        srcData = ByteBuffer.allocateDirect(attributes.vertexSize * verticesCount);
        srcData.order(ByteOrder.nativeOrder());
    }

    public void setVertices(float[] vertices) {
        FloatBuffer floatBuffer = srcData.asFloatBuffer();
        floatBuffer.put(vertices);

        srcData.position(0);
        srcData.limit(srcData.capacity());
    }

    public FloatBuffer viewData() {
        return srcData.asFloatBuffer();
    }

    public int getVerticesCount() {
        return srcData.capacity() / attributes.vertexSize;
    }

    protected ByteBuffer srcData;
    protected VertexAttributes attributes;
}
