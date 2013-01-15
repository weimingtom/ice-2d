package ice.node.widget;

import android.graphics.Bitmap;
import ice.graphic.texture.BitmapTexture;
import ice.graphic.texture.Texture;
import ice.res.Res;

/**
 * User: ice
 * Date: 11-11-30
 * Time: 下午12:31
 */
public class BitmapOverlay extends RectOverlay {

    public BitmapOverlay(int bitmapId) {
        this(Res.getBitmap(bitmapId));
    }

    public BitmapOverlay(Bitmap bitmap) {
        this(bitmap.getWidth(), bitmap.getHeight());

        setBitmap(bitmap);
    }

    public BitmapOverlay(float width, float height) {
        super(width, height);
    }

    public void setBitmap(int bitmap) {
        setBitmap(Res.getBitmap(bitmap));
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap == null) throw new IllegalArgumentException("bitmap null !");

        Texture texture = getTexture();

        if (texture == null) {
            texture = new BitmapTexture(bitmap);
        }
        else {
            ((BitmapTexture) texture).setBitmap(bitmap);
        }

        setTexture(texture);
    }

}
