package ice.graphic.texture;

import android.content.res.Resources;
import android.util.Log;
import ice.graphic.GlRes;
import ice.graphic.gl_status.GlStatusController;
import ice.node.Overlay;
import ice.res.Res;

import java.util.HashMap;
import java.util.Map;

import static android.opengl.GLES11.*;

/**
 * 在GL2.0以下版本如果硬件支持NPOT，就无需考虑纹理宽高POT的问题.
 */
public abstract class Texture implements GlStatusController, GlRes {

    private static final String TAG = Texture.class.getSimpleName();

    public static final int NOT_EXIST = 0;

    private static int maxSize;
    private static boolean npotSupported;
    private static boolean inited;

    private static Map<String, Texture> shared = new HashMap<String, Texture>();
    private static Map<Texture, Integer> sharedReference = new HashMap<Texture, Integer>();

    public static boolean isInited() {
        return inited;
    }

    public static void init(boolean npotSupported, int maxTextureSize) {
        Texture.npotSupported = npotSupported;
        Texture.maxSize = maxTextureSize;

        inited = true;

        Log.w(TAG, "init npotSupported ? " + npotSupported);
        Log.w(TAG, "init maxSize = " + maxTextureSize);
    }

    public static int getMaxSize() {
        return maxSize;
    }

    public static boolean isNpotSupported() {
        return true;
    }

    public static Texture requestShared(int resId, ShareStrategy strategy) {
        Resources resources = Res.getContext().getResources();
        String name = resources.getResourceName(resId);
        return requestShared(name, strategy);
    }

    public static Texture requestShared(String key, ShareStrategy strategy) {
        synchronized (shared) {
            Texture texture = shared.get(key);

            if (texture != null) {
                sharedReference.put(texture, sharedReference.get(texture) + 1);
            } else {
                texture = strategy.createShared();
                shared.put(key, texture);
                sharedReference.put(texture, 1);
            }

            return texture;
        }
    }

    public static void clearShared() {
        shared.clear();
        sharedReference.clear();
    }

    public Texture() {
        this(NOT_EXIST, Params.LINEAR_CLAMP_TO_EDGE);
    }

    public Texture(int id, Params params) {
        this.nativeTexture = id;
        this.params = params;

        maxU = 1;
        maxV = 1;
    }

    @Override
    public void attach() {
        glEnable(GL_TEXTURE_2D);

        if (!prepared) {
            prepare();
        } else {
            glBindTexture(GL_TEXTURE_2D, nativeTexture);
        }

    }

    @Override
    public boolean detach(Overlay overlay) {
        glDisable(GL_TEXTURE_2D);

        return true;
    }

    @Override
    public void onEGLContextLost() {
        prepared = false;
    }

    @Override
    public void prepare() {
        int[] temp = new int[1];

        glGenTextures(1, temp, 0);

        nativeTexture = temp[0];

        glBindTexture(GL_TEXTURE_2D, nativeTexture);

        /*
         * Always set any texture parameters before loading texture data,
         *By setting the parameters first,
         *OpenGL ES can optimize the texture data it provides to the graphics hardware to match your settings.
         */
        bindTextureParams(params);

        onLoadTextureData();

        prepared = true;
    }

    @Override
    public void release() {
        if (nativeTexture == NOT_EXIST) return;

        if (shared.containsValue(this)) {

            if (sharedReference.containsKey(this)) {

                int count = sharedReference.get(this);

                sharedReference.put(this, --count);

                if (count > 0) {
                    Log.w(TAG, "shared not released !");
                    return;
                }

                Log.w(TAG, "shared released !");
            }
        }

        glDeleteBuffers(1, new int[]{nativeTexture}, 0);

        nativeTexture = NOT_EXIST;
    }

    protected abstract void onLoadTextureData();

    private void bindTextureParams(Params params) {
        for (Map.Entry<Integer, Integer> entry : params.getParamMap().entrySet()) {
            glTexParameterf(
                    GL_TEXTURE_2D,
                    entry.getKey(),
                    entry.getValue()
            );
        }
    }

    protected void setMaxUV(float maxU, float maxV) {
        this.maxU = maxU;
        this.maxV = maxV;
    }

    public float getMaxU() {
        return maxU;
    }

    public float getMaxV() {
        return maxV;
    }

    private int nativeTexture;
    private Params params;
    private float maxU, maxV;
    private boolean prepared;

    public static class Params {
        public static final Params LINEAR_REPEAT;

        public static final Params LINEAR_CLAMP_TO_EDGE;

        static {
            LINEAR_REPEAT = new Params();
            LINEAR_REPEAT.add(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            LINEAR_REPEAT.add(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            LINEAR_REPEAT.add(GL_TEXTURE_WRAP_S, GL_REPEAT);
            LINEAR_REPEAT.add(GL_TEXTURE_WRAP_T, GL_REPEAT);

            LINEAR_CLAMP_TO_EDGE = new Params();
            LINEAR_CLAMP_TO_EDGE.add(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            LINEAR_CLAMP_TO_EDGE.add(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            LINEAR_CLAMP_TO_EDGE.add(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            LINEAR_CLAMP_TO_EDGE.add(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        }

        public Params() {
            paramMap = new HashMap<Integer, Integer>();
        }

        public void add(int pName, int value) {
            paramMap.put(pName, value);
        }

        public Map<Integer, Integer> getParamMap() {
            return paramMap;
        }

        private Map<Integer, Integer> paramMap;
    }
}
