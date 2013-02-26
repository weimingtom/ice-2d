package ice.model.vertex;

import android.graphics.Color;
import ice.node.Overlay;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static ice.model.Constants.BYTES_PER_FLOAT;
import static android.opengl.GLES11.*;

/**
 * User: jason
 * Date: 12-2-23
 * Time: 下午12:05
 */
public class NormalRect extends Rect {
    private static final ByteBuffer CCW_BUFFER;
    private static final ByteBuffer CW_BUFFER;

    static {
        CW_BUFFER = ByteBuffer.allocateDirect(CW_INDICES.length);
        CCW_BUFFER = ByteBuffer.allocateDirect(CCW_INDICES.length);

        CW_BUFFER.put(CW_INDICES);
        CCW_BUFFER.put(CCW_INDICES);
    }

    public NormalRect(float width, float height) {
        this(width, height, true);
    }

    public NormalRect(float width, float height, boolean ccw) {
        super(width, height, ccw);
    }

    @Override
    protected void onBuildVertexData(float width, float height, float uLeft, float uRight, float vTop, float vBottom) {
        onSetBounds(width, height);
        onsetTextureCoord(uLeft, uRight, vTop, vBottom);
    }

    @Override
    public void attach() {
        vertexCoord.position(0);
        glEnableClientState(GL_VERTEX_ARRAY);
        glVertexPointer(2, GL_FLOAT, 0, vertexCoord);

        if (textureCoord != null) {
            textureCoord.position(0);
            glEnableClientState(GL_TEXTURE_COORD_ARRAY);
            glTexCoordPointer(2, GL_FLOAT, 0, textureCoord);
        }

        if (colorCoord != null) {
            colorCoord.position(0);
            glEnableClientState(GL_COLOR_ARRAY);
            glColorPointer(4, GL_FLOAT, 0, colorCoord);
        }
    }

    @Override
    public void onDrawVertex() {
        ByteBuffer indices = ccw ? CCW_BUFFER : CW_BUFFER;

        indices.position(0);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, indices);
    }

    @Override
    public boolean detach(Overlay overlay) {
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);

        if (colorCoord != null)
            glDisableClientState(GL_COLOR_ARRAY);

        return true;
    }

    @Override
    protected void onSetBounds(float width, float height) {
        ByteBuffer vbb = ByteBuffer.allocateDirect(BYTES_PER_FLOAT * 2 * 4);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer coord = vbb.asFloatBuffer();

        float halfWidth = width / 2;
        float halfHeight = height / 2;

        float[] fourPoints = new float[]{
                -halfWidth, +halfHeight,
                -halfWidth, -halfHeight,
                +halfWidth, -halfHeight,
                +halfWidth, +halfHeight
        };

        coord.put(fourPoints);

        vertexCoord = vbb;
    }

    @Override
    protected void onsetTextureCoord(float uLeft, float uRight, float vTop, float vBottom) {
        ByteBuffer vbb = ByteBuffer.allocateDirect(BYTES_PER_FLOAT * 2 * 4);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer coord = vbb.asFloatBuffer();

        coord.put(
                new float[]{
                        uLeft, vTop,
                        uLeft, vBottom,
                        uRight, vBottom,
                        uRight, vTop
                }
        );

        textureCoord = vbb;
    }

    public void setColorCoord(int topLeft, int bottomLeft, int bottomRight, int topRight) {
        ByteBuffer vbb = ByteBuffer.allocateDirect(BYTES_PER_FLOAT * 4 * 4);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer coord = vbb.asFloatBuffer();

        coord.put(
                new float[]{
                        // topLeft
                        Color.red(topLeft) / 255.0f,
                        Color.green(topLeft) / 255.0f,
                        Color.blue(topLeft) / 255.0f,
                        Color.alpha(topLeft) / 255.0f,

                        //bottomLeft
                        Color.red(bottomLeft) / 255.0f,
                        Color.green(bottomLeft) / 255.0f,
                        Color.blue(bottomLeft) / 255.0f,
                        Color.alpha(bottomLeft) / 255.0f,

                        //bottomRight
                        Color.red(bottomRight) / 255.0f,
                        Color.green(bottomRight) / 255.0f,
                        Color.blue(bottomRight) / 255.0f,
                        Color.alpha(bottomRight) / 255.0f,

                        // topRight
                        Color.red(topRight) / 255.0f,
                        Color.green(topRight) / 255.0f,
                        Color.blue(topRight) / 255.0f,
                        Color.alpha(topRight) / 255.0f,
                }
        );

        colorCoord = vbb;
    }

    private ByteBuffer colorCoord;
    private ByteBuffer vertexCoord;
    private ByteBuffer textureCoord;
}
