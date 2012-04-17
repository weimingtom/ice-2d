package ice.util;

import javax.microedition.khronos.opengles.GL11;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import static ice.model.Constants.SIZE_OF_INTEGER;
import static javax.microedition.khronos.opengles.GL10.GL_EXTENSIONS;

/**
 * User: jason
 * Date: 12-2-8
 * Time: 下午5:55
 */
public class GlUtil {

    public static boolean isEnabled(GL11 gl, int name) {
        return gl.glIsEnabled(name);
    }

    public static int getInteger(GL11 gl, int name) {
        ByteBuffer vfb = ByteBuffer.allocateDirect(SIZE_OF_INTEGER);
        vfb.order(ByteOrder.nativeOrder());

        IntBuffer buffer = vfb.asIntBuffer();

        gl.glGetIntegerv(name, buffer);

        return buffer.get(0);
    }

    public static boolean queryN_P_O_TSupported(GL11 gl) {
        String extensions = gl.glGetString(GL_EXTENSIONS);

        return extensions.contains("GL_APPLE_texture_2D_limited_npot")
                || extensions.contains("GL_ARB_texture_non_power_of_two");
    }
}
