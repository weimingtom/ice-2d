package ice.graphic.texture;

import android.graphics.Bitmap;
import android.opengl.GLUtils;
import ice.res.Res;

import javax.microedition.khronos.opengles.GL11;

import static ice.util.MathUtil.powerOfTwoTest;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE_2D;

/**
 * User: jason
 * Date: 12-3-30
 * Time: 下午3:46
 */
public class BitmapTexture extends Texture {
    public static final String TAG = BitmapTexture.class.getSimpleName();

    public BitmapTexture(int bitmapId) {
        this(Res.getBitmap(bitmapId));
    }

    public BitmapTexture(Bitmap bitmap) {
        this(bitmap, Params.LINEAR_CLAMP_TO_EDGE);
    }

    public BitmapTexture(Bitmap bitmap, Params params) {
        this(bitmap, NOT_EXIST, params);
    }

    public BitmapTexture(Bitmap bitmap, int glResId, Params params) {
        super(glResId, params);

        Bitmap adjusted = Res.tryAdjustToPot(bitmap);

        this.bitmap = adjusted;
    }

    @Override
    public void attach(GL11 gl) {
        super.attach(gl);

        if (reload) {
            reload = false;
            onLoadTextureData(gl);
        }

        if (subProvider != null) {
            GLUtils.texSubImage2D(GL_TEXTURE_2D, 0, xOffset, yOffset, subProvider);
            subProvider = null;
        }

    }

    @Override
    protected void onLoadTextureData(GL11 gl) {

        if (!Texture.isNpotSupported()) {
            if (!powerOfTwoTest(bitmap.getWidth(), bitmap.getHeight())) {
                throw new IllegalArgumentException(
                        "NPOT is not supported on this device !"
                );
            }
        }

        GLUtils.texImage2D(
                GL_TEXTURE_2D,
                0,
                GLUtils.getInternalFormat(bitmap),
                bitmap,
                0
        );
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        Bitmap adjusted = Res.tryAdjustToPot(bitmap);

        if (this.bitmap.getWidth() != adjusted.getWidth() || this.bitmap.getHeight() != adjusted.getHeight()) {
            this.bitmap = adjusted;
            reload = true;
        }
        else {
            postSubData(0, 0, adjusted);
        }

        this.bitmap = adjusted;
    }

    public void postSubData(int xoffset, int yoffset, Bitmap subPixel) {
        //TODO Warning !   subProvider 的这种处理可能导致按钮的状态不正常
        this.xOffset = xoffset;
        this.yOffset = yoffset;
        this.subProvider = subPixel;
    }

    private int xOffset, yOffset;
    private boolean reload;

    private Bitmap bitmap;
    private volatile Bitmap subProvider;
}
