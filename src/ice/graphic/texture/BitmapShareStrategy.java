package ice.graphic.texture;

import android.graphics.Bitmap;
import ice.res.Res;

/**
 * User: jason
 * Date: 12-3-31
 * Time: 下午3:16
 */
public class BitmapShareStrategy implements ShareStrategy {

    public BitmapShareStrategy(int bitmapId) {
        this(Res.getBitmap(bitmapId));
    }

    public BitmapShareStrategy(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public Texture createShared() {
        return new BitmapTexture(bitmap);
    }

    private Bitmap bitmap;
}
