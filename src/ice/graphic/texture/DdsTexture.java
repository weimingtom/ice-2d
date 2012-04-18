package ice.graphic.texture;

import android.content.res.Resources;
import ice.graphic.utils.DDSLoader;
import ice.graphic.utils.DDSurfaceDesc2;
import ice.res.Res;

import javax.microedition.khronos.opengles.GL11;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static android.opengl.GLES10.GL_TEXTURE_2D;
import static android.opengl.GLES10.glCompressedTexImage2D;

/**
 * User: jason
 * Date: 12-4-1
 * Time: 下午5:37
 */
public class DdsTexture extends Texture {

    public static final int ATC_RGB_AMD = 0x8C92;

    public static final int ATC_RGBA_EXPLICIT_ALPHA_AMD = 0x8C93;

    public static final int ATC_RGBA_INTERPOLATED_ALPHA_AMD = 0x87EE;


    public DdsTexture(int rawResId) {
        super();

        Resources resources = Res.getContext().getResources();

        InputStream input = null;
        try {
            input = resources.openRawResource(rawResId);

            // create a new DDSurfaceDesc2 object to hold all dds file information
            ddsDesc2 = DDSLoader.readFileHeader(input);

            imageData = DDSLoader.readFileData(input, ddsDesc2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (input != null)
                    input.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onLoadTextureData(GL11 gl) {

        glCompressedTexImage2D(
                GL_TEXTURE_2D,
                0,
                ATC_RGBA_INTERPOLATED_ALPHA_AMD,
                ddsDesc2.getWidth(),
                ddsDesc2.getHeight(),
                0,
                ddsDesc2.getPitchOrLinearSize(),
                imageData
        );

    }

    private DDSurfaceDesc2 ddsDesc2;

    private ByteBuffer imageData;
}
