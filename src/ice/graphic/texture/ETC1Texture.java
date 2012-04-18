package ice.graphic.texture;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.opengl.ETC1Util;
import android.opengl.GLES10;
import android.util.Log;
import ice.res.Res;

import javax.microedition.khronos.opengles.GL11;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static android.opengl.GLES10.GL_RGB;
import static android.opengl.GLES10.GL_TEXTURE_2D;
import static ice.util.MathUtil.powerOfTwoTest;

/**
 * User: jason
 * Date: 12-3-30
 * Time: 下午4:40
 */
public class ETC1Texture extends Texture {

    public static final String TAG = ETC1Texture.class.getSimpleName();

    public ETC1Texture(int rawResId) {
        this(rawResId, NOT_EXIST, Params.LINEAR_CLAMP_TO_EDGE);
    }

    public ETC1Texture(int rawResId, int glResId, Params params) {
        super(glResId, params);

        Resources resources = Res.getContext().getResources();

        InputStream input = resources.openRawResource(rawResId);

        try {
            etc1Texture = ETC1Util.createTexture(input);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (input != null) {
                try {
                    input.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public ETC1Texture(Bitmap bitmap) {
        Bitmap textureData = bitmap;

        if (!Texture.isNpotSupported()) {
            if (!powerOfTwoTest(bitmap.getWidth(), bitmap.getHeight())) {
                textureData = Res.tryAdjustToPot(bitmap);
            }
        }

        int size = textureData.getHeight() * textureData.getRowBytes();
        ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        buffer.order(ByteOrder.nativeOrder());

        textureData.copyPixelsToBuffer(buffer);
        buffer.position(0);

        etc1Texture = ETC1Util.compressTexture(buffer, textureData.getWidth(), textureData.getHeight(), 3, 0);
    }

    @Override
    protected void onLoadTextureData(GL11 gl) {
        boolean etc1Supported = ETC1Util.isETC1Supported();
        Log.w(TAG, "ETC1 texture support: " + etc1Supported);

        if (!Texture.isNpotSupported()) {
            if (!powerOfTwoTest(etc1Texture.getWidth(), etc1Texture.getHeight())) {
                throw new IllegalArgumentException(
                        "NPOT is not supported on this device !"
                );
            }
        }

        ETC1Util.loadTexture(GL_TEXTURE_2D, 0, 0, GL_RGB, GLES10.GL_UNSIGNED_SHORT_5_6_5, etc1Texture);
    }

    private ETC1Util.ETC1Texture etc1Texture;
}
