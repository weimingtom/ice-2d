package ice.practical;

import android.view.animation.AnimationUtils;
import ice.graphic.texture.BitmapShareStrategy;
import ice.graphic.texture.Texture;
import ice.node.widget.AtlasSequence;
import ice.res.Res;

/**
 * User: jason
 * Date: 12-3-19
 * Time: 上午10:56
 */
public class DigitSequence extends AtlasSequence {

    public static final String STYLE_0_9 = "0123456789";
    public static final String STYLE_POSITIVE_0_9 = "+0123456789";
    public static final String STYLE_NEGATIVE_0_9 = "-0123456789";
    public static final String STYLE_POSITIVE_NEGATIVE_0_9 = "+-0123456789";
    public static final String STYLE_0_9_POSITIVE_NEGATIVE = "0123456789+-";

    public DigitSequence(String textureStyle, int resId, int eachWidth, int eachHeight, float margin) {
        super(eachWidth, eachHeight, margin);

        this.textureStyle = textureStyle;

        String resName = Res.getResourceEntryName(resId);

        Texture texture = Texture.requestShared(
                resName,
                new BitmapShareStrategy(resId)
        );

        setAtlas(texture, textureStyle.length(), 1);
    }

    public void setDigit(int digit) {

        if (this.digit == digit)
            return;

        this.digit = digit;
        setSequence(translateToSequence(digit));
    }

    public void growTo(int growTo, long growDuring) {
        this.growTo = growTo;
        this.growDuring = growDuring;
        startStamp = AnimationUtils.currentAnimationTimeMillis();

        grow = true;
    }

    @Override
    protected void onDraw() {
        super.onDraw();

        if (grow) {

            long current = AnimationUtils.currentAnimationTimeMillis();

            long sub = current - startStamp;

            if (sub <= growDuring) {
                float step = (float) sub / growDuring;
                int currentDigit = (int) (this.digit + (growTo - this.digit) * step);

                if (currentDigit != lastGrow) {
                    lastGrow = currentDigit;
                    int[] sequence = translateToSequence(currentDigit);
                    setSequence(sequence);
                }
            }
            else {
                setDigit(growTo);
            }
        }

    }

    protected int[] translateToSequence(int digit) {

        char[] digits = String.valueOf(digit).toCharArray();

        int[] sequence = new int[digits.length];

        for (int i = sequence.length - 1, j = 0; i >= 0; i--) {

            char digitChar = digits[i];

            int index = textureStyle.indexOf(digitChar);

            if (index < 0)
                throw new IllegalStateException("digitChar '" + digitChar + "' not found in " + textureStyle);

            sequence[j++] = index;
        }

        return sequence;
    }

    private int lastGrow;

    private boolean grow;

    private String textureStyle;

    private long startStamp;
    private int growTo;
    private long growDuring;

    private int digit = Integer.MIN_VALUE;
}
