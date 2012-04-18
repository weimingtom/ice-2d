package ice.model.vertex;

import static javax.microedition.khronos.opengles.GL10.GL_TRIANGLES;

/**
 * User: jason
 * Date: 12-4-6
 * Time: 下午2:46
 */
public abstract class Rect implements VertexData {
    public static final byte[] CCW_INDICES;
    public static final byte[] CW_INDICES;

    private static int sharedMode = GL_TRIANGLES;

    static {
        CCW_INDICES = new byte[]{
                0, 1, 2,
                2, 3, 0
        };

        CW_INDICES = new byte[]{
                0, 3, 2,
                2, 1, 0
        };
    }


    public static int getSharedMode() {
        return sharedMode;
    }

    public static void setSharedMode(int sharedMode) {
        Rect.sharedMode = sharedMode;
    }

    protected Rect(float width, float height, boolean ccw) {
        this.ccw = ccw;

        buildVertexData(width, height, 0, 1, 0, 1);

        mode = sharedMode;
    }

    public final void setBounds(float width, float height) {
        if (this.width != width || this.height != height) {
            this.width = width;
            this.height = height;

            onSetBounds(width, height);
        }
    }

    public final void setTextureCoord(float uRight, float vBottom) {
        setTextureCoord(0, uRight, 0, vBottom);
    }

    public final void setTextureCoord(float uLeft, float uRight, float vTop, float vBottom) {
        this.uLeft = uLeft;
        this.uRight = uRight;
        this.vTop = vTop;
        this.vBottom = vBottom;

        onsetTextureCoord(uLeft, uRight, vTop, vBottom);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    protected final void buildVertexData(float width, float height, float uLeft, float uRight, float vTop, float vBottom) {
        this.width = width;
        this.height = height;
        this.uLeft = uLeft;
        this.uRight = uRight;
        this.vTop = vTop;
        this.vBottom = vBottom;

        onBuildVertexData(width, height, uLeft, uRight, vTop, vBottom);
    }

    protected abstract void onBuildVertexData(float width, float height, float uLeft, float uRight, float vTop, float vBottom);

    protected abstract void onSetBounds(float width, float height);

    protected abstract void onsetTextureCoord(float uLeft, float uRight, float vTop, float vBottom);

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    protected int mode;

    protected boolean ccw;
    protected float width, height;
    protected float uLeft, uRight, vTop, vBottom;
}
