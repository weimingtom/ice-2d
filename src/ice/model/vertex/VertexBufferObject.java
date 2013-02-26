package ice.model.vertex;

import ice.graphic.GlRes;
import ice.node.Overlay;

import java.nio.Buffer;

import static android.opengl.GLES11.*;

/**
 * User: ice
 * Date: 11-11-21
 * Time: 下午12:04
 */
public class VertexBufferObject extends AbstractVertexData implements GlRes {

    public enum Usage {
        DynamicDraw(GL_DYNAMIC_DRAW), StaticDraw(GL_STATIC_DRAW);

        private int glUsage;

        private Usage(int glUsage) {
            this.glUsage = glUsage;
        }

        public int getGlUsage() {
            return glUsage;
        }
    }

    private Usage usage = Usage.StaticDraw;

    public VertexBufferObject() {
    }

    public VertexBufferObject(int verticesCount, VertexAttributes attributes) {
        super(verticesCount, attributes);
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    @Override
    public synchronized void setVertices(float[] vertices) {
        super.setVertices(vertices);

        prepared = false;
    }

    @Override
    public synchronized void attach() {

        if (!prepared) {
            prepare();

            pointer();
        } else {

            glBindBuffer(GL_ARRAY_BUFFER, vboBuffer[0]);

            if (subData != null) {
                synchronized (subData) {
                    //todo error   srcData.capacity() should be bytes
                    glBufferSubData(GL_ARRAY_BUFFER, 0, srcData.capacity(), subData);

                    pointer();
                }
                subData = null;
            } else {
                pointer();
            }
        }

    }

    private void pointer() {
        int textureUnit = 0;
        int numAttributes = attributes.size();

        for (int i = 0; i < numAttributes; i++) {

            VertexAttribute attribute = attributes.get(i);
            int offset = attribute.getOffset();
            int dimension = attribute.getDimension();

            switch (attribute.getUsage()) {
                case Position:
                    glEnableClientState(GL_VERTEX_ARRAY);
                    glVertexPointer(dimension, GL_FLOAT, attributes.vertexSize, offset);
                    break;

                case Color:
                    glEnableClientState(GL_COLOR_ARRAY);
                    glColorPointer(dimension, GL_FLOAT, attributes.vertexSize, offset);
                    break;

                case Normal:
                    glEnableClientState(GL_NORMAL_ARRAY);
                    glNormalPointer(GL_FLOAT, attributes.vertexSize, offset);
                    break;

                case TextureCoordinates:
                    glEnableClientState(GL_TEXTURE_COORD_ARRAY);
                    glClientActiveTexture(GL_TEXTURE0 + textureUnit);
                    glTexCoordPointer(dimension, GL_FLOAT, attributes.vertexSize, offset);
                    textureUnit++;
                    break;

                default:
                    // throw new GdxRuntimeException("unkown vertex attribute type: " + attribute.usage);
            }
        }
    }

    @Override
    public void onDrawVertex() {
        glDrawArrays(GL_TRIANGLES, 0, getVerticesCount());
    }

    @Override
    public boolean detach(Overlay overlay) {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return true;
    }

    public synchronized void postSubData(int offset, int size, Buffer subData) {
//        this.subDataOffset = offset;
//        this.subDataSize = size;
        this.subData = subData;
    }

    @Override
    public void onEGLContextLost() {
        prepared = false;
    }

    @Override
    public void prepare() {
        if (vboBuffer != null) {
            release();
        }

        vboBuffer = new int[1];

        glGenBuffers(vboBuffer.length, vboBuffer, 0);
        glBindBuffer(GL_ARRAY_BUFFER, vboBuffer[0]);
        glBufferData(GL_ARRAY_BUFFER, srcData.capacity(), srcData, usage.glUsage);

        prepared = true;
    }

    @Override
    public void release() {
        glDeleteBuffers(vboBuffer.length, vboBuffer, 0);
        vboBuffer = null;
        prepared = false;
    }

    protected boolean prepared;

    private int[] vboBuffer;
    private Buffer subData;
    // private int subDataOffset, subDataSize;
}
