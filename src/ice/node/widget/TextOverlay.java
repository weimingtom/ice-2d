package ice.node.widget;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import ice.util.TextDrawer;

/**
 * //TODO 效率或者可以优化一下
 * <p/>
 * User: jason
 * Date: 12-2-6
 * Time: 上午10:19
 */
public class TextOverlay extends BitmapOverlay {

    public TextOverlay(float width, float height) {
        super(width, height);

        painter = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 警告：text 的bitmap生成后的改变颜色暂不处理！
     *
     * @param color
     */
    public void setColor(int color) {
        painter.setColor(color);
    }

    public void setText(String text) {
        setText(text, (int) getHeight());
    }

    public void setText(String text, int size) {
        setText(text, size, alignCenter);
    }

    public void setText(String text, boolean alignCenter) {
        setText(text, (int) getHeight(), alignCenter);
    }

    public void setText(String text, int size, boolean alignCenter) {
        if (text.equals(this.text)) return;

        this.text = text;

        this.alignCenter = alignCenter;

        setBitmap(createTextTexture(text, size));
    }

    private Bitmap createTextTexture(String text, int size) {
        Bitmap textTexture = Bitmap.createBitmap((int) getWidth(), (int) getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(textTexture);

        Rect region = new Rect(0, 0, (int) getWidth(), size);

        realWidth = TextDrawer.drawTextInLine(canvas, painter, text, region, alignCenter);

        return textTexture;
    }

    public float getRealWidth() {
        return realWidth;
    }

    private Paint painter;
    private String text;
    private float realWidth;
    private boolean alignCenter;
}
