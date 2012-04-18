package ice.model.vertex;

import ice.graphic.GlRes;
import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static ice.model.Constants.BYTE_OF_FLOAT;
import static javax.microedition.khronos.opengles.GL11.*;

/**
 * //TODO 考虑所有VboRect 使用共同的一个index对象，好处：少上传（1-4/6）的顶点数据.
 * User: ice
 * Date: 11-11-21
 * Time: 下午12:04
 */
public class VboRect extends Rect implements GlRes {

    public VboRect(float width, float height) {
        this(width, height, true);
    }

    public VboRect(float width, float height, boolean ccw) {
        super(width, height, ccw);
    }

    @Override
    public void attach(GL11 gl) {
        gl.glEnableClientState(GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        if (!prepared) {
            prepare(gl);
            prepared = true;
        }
        else {
            gl.glBindBuffer(GL_ARRAY_BUFFER, vboIds[0]);
        }

        int stride = (2 + 2) * BYTE_OF_FLOAT;

        gl.glVertexPointer(2, GL_FLOAT, stride, 0);
        gl.glTexCoordPointer(2, GL_FLOAT, stride, stride / 2);

        if (dataChanged && vertexBuffer != null) {
            dataChanged = false;
            gl.glBufferSubData(GL_ARRAY_BUFFER, 0, vertexBuffer.capacity(), vertexBuffer);
            vertexBuffer = null;
        }
    }

    @Override
    public void onDrawVertex(GL11 gl) {
        gl.glDrawArrays(mode, 0, 6);
    }

    @Override
    public boolean detach(GL11 gl, Overlay overlay) {
        gl.glBindBuffer(GL_ARRAY_BUFFER, 0);

        gl.glDisableClientState(GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);

        return true;
    }

    @Override
    public void prepare(GL11 gl) {
        vboIds = new int[1];
        gl.glGenBuffers(vboIds.length, vboIds, 0);

        // Upload the vertex data
        if (vertexBuffer == null)
            buildVertexData(width, height, uLeft, uRight, vTop, vBottom);

        vertexBuffer.position(0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vboIds[0]);
        gl.glBufferData(GL_ARRAY_BUFFER, vertexBuffer.capacity(), vertexBuffer, GL_STATIC_DRAW);

        vertexBuffer = null;
    }

    @Override
    public void release(GL11 gl) {
        prepared = false;

        if (vboIds != null)
            gl.glDeleteBuffers(vboIds.length, vboIds, 0);

        vboIds = null;
    }

    @Override
    public void onEGLContextLost() {
        prepared = false;
        vboIds = null;
    }

    @Override
    protected void onSetBounds(float width, float height) {
        buildVertexData(width, height, uLeft, uRight, vTop, vBottom);
        dataChanged = true;
    }

    @Override
    protected void onsetTextureCoord(float uLeft, float uRight, float vTop, float vBottom) {
        buildVertexData(width, height, uLeft, uRight, vTop, vBottom);
        dataChanged = true;
    }

    @Override
    protected void onBuildVertexData(float width, float height, float uLeft, float uRight, float vTop, float vBottom) {
        float halfWidth = width / 2;
        float halfHeight = height / 2;

        float[] fourPoints = new float[]{
                -halfWidth, +halfHeight,
                uLeft, vTop,

                -halfWidth, -halfHeight,
                uLeft, vBottom,

                +halfWidth, -halfHeight,
                uRight, vBottom,

                +halfWidth, +halfHeight,
                uRight, vTop
        };

        float[] sixPoints = new float[(2 + 2) * 6];

        byte[] order = ccw ? CCW_INDICES : CW_INDICES;

        int index = 0;
        for (byte orderIndex : order) {
            sixPoints[index * (2 + 2) + 0] = fourPoints[orderIndex * (2 + 2) + 0];
            sixPoints[index * (2 + 2) + 1] = fourPoints[orderIndex * (2 + 2) + 1];

            sixPoints[index * (2 + 2) + 2] = fourPoints[orderIndex * (2 + 2) + 2];
            sixPoints[index * (2 + 2) + 3] = fourPoints[orderIndex * (2 + 2) + 3];

            index++;
        }

        ByteBuffer vbb = ByteBuffer.allocateDirect(BYTE_OF_FLOAT * sixPoints.length);
        vertexBuffer = vbb.order(ByteOrder.nativeOrder());

        FloatBuffer floatBuffer = vertexBuffer.asFloatBuffer();
        floatBuffer.put(sixPoints);
    }

    private boolean dataChanged;
    private int[] vboIds;
    private boolean prepared;
    private ByteBuffer vertexBuffer;
}
