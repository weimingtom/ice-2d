package ice.util;

import javax.microedition.khronos.opengles.GL11;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import static ice.model.Constants.BYTES_PER_INT;
import static android.opengl.GLES11.*;

/**
 * User: jason
 * Date: 12-2-8
 * Time: 下午5:55
 */
public class GlUtil {

    public static boolean isEnabled(int name) {
        return glIsEnabled(name);
    }

    public static int getInteger(int name) {
        ByteBuffer vfb = ByteBuffer.allocateDirect(BYTES_PER_INT);
        vfb.order(ByteOrder.nativeOrder());

        IntBuffer buffer = vfb.asIntBuffer();

        glGetIntegerv(name, buffer);

        return buffer.get(0);
    }

    public static boolean queryN_P_O_TSupported() {
        String extensions = glGetString(GL_EXTENSIONS);

        return extensions.contains("GL_APPLE_texture_2D_limited_npot")
                || extensions.contains("GL_ARB_texture_non_power_of_two");
    }
}
