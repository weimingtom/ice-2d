package ice.res;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import ice.graphic.texture.Texture;
import ice.util.BitmapUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import static ice.util.MathUtil.powerOfTwoTest;

/**
 * User: tosmart
 * Date: 11-10-20
 * Time: 下午4:38
 */
public class Res {
    private static Context context;
    private static Map<Integer, SoftReference<Bitmap>> bitmapBuffer;
    private static Map<Integer, SoftReference<Bitmap[]>> bitmapArrayBuffer;

    public static void built(Context context) {
        Res.context = context;
        bitmapBuffer = new HashMap<Integer, SoftReference<Bitmap>>();
        bitmapArrayBuffer = new HashMap<Integer, SoftReference<Bitmap[]>>();
    }

    public static Bitmap getBitmap(int resourceId) {

        Bitmap bitmap = null;

        if (bitmapBuffer.containsKey(resourceId)) {
            SoftReference<Bitmap> softReference = bitmapBuffer.get(resourceId);
            bitmap = softReference.get();
        }

        if (bitmap == null) {
            bitmap = BitmapUtils.loadResource(context, resourceId);
            bitmapBuffer.put(resourceId, new SoftReference<Bitmap>(bitmap));
        }

        return bitmap;
    }

    public static Typeface getTypeface() {
        return null;
    }

    public static Bitmap[] loadArray(int resourceId, int totalCols) {
        return loadArray(resourceId, totalCols, totalCols);
    }

    public static Bitmap[] loadArray(int resourceId, int toBeLoadedCols, int totalCols) {

        if (bitmapArrayBuffer.containsKey(resourceId)) {
            SoftReference<Bitmap[]> softReference = bitmapArrayBuffer.get(resourceId);
            Bitmap[] buffer = softReference.get();
            if (buffer != null)
                return buffer;
        }

        Bitmap bitmap = BitmapUtils.loadResource(context, resourceId);

        float cellWidth = bitmap.getWidth() * 1f / totalCols;
        int cellHeight = bitmap.getHeight();

        Bitmap[] array = new Bitmap[toBeLoadedCols];

        for (int i = 0; i < array.length; i++) {
            array[i] = BitmapUtils.slices(bitmap, (int) (cellWidth * i), 0, (int) cellWidth, cellHeight);
        }

        bitmap.recycle();

        bitmapArrayBuffer.put(resourceId, new SoftReference<Bitmap[]>(array));

        return array;
    }

    public static InputStream openAssets(String fileName) {

        AssetManager assets = context.getAssets();

        try {
            return assets.open(fileName);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Context getContext() {
        return context;
    }

    public static String getResourceEntryName(int resId) {
        return context.getResources().getResourceEntryName(resId);
    }

    public static String getText(int id) {
        return context.getText(id).toString();
    }

    /**
     * 不支持NpotSupported时调整宽高到2的指数次方
     *
     * @param bitmap
     * @return
     */
    public static Bitmap tryAdjustToPot(Bitmap bitmap) {
        if (bitmap.isRecycled())
            bitmap.prepareToDraw();

        if (Texture.isNpotSupported()) return bitmap;

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();

        int widthToFit = originalWidth;
        int heightToFit = originalHeight;

        if (!powerOfTwoTest(bitmap.getWidth(), widthToFit)) {
            int i = 1;
            while (i < widthToFit)
                i *= 2;
            widthToFit = i;
        }

        if (!powerOfTwoTest(bitmap.getWidth(), heightToFit)) {
            int i = 1;
            while (i < heightToFit)
                i *= 2;
            heightToFit = i;
        }

        int maxTextureSize = Texture.getMaxSize();

        while (widthToFit > maxTextureSize || heightToFit > maxTextureSize) {
            widthToFit /= 2;
            heightToFit /= 2;
        }

        if (widthToFit != originalWidth || heightToFit != originalHeight) {
            return Bitmap.createScaledBitmap(bitmap, widthToFit, heightToFit, true);
        }

        return bitmap;
    }
}
