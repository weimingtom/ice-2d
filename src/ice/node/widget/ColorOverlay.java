package ice.node.widget;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * User: Jason
 * Date: 12-2-4
 * Time: 下午6:21
 */
public class ColorOverlay extends BitmapOverlay {

    public ColorOverlay(int color, float width, float height) {
        super(width, height);
        setBitmap(createColorBitmap(color));
    }

    private static Bitmap createColorBitmap(int color) {
        Bitmap bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        canvas.drawColor(color);

        return bitmap;
    }

}
