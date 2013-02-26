package ice.util;

import java.nio.*;

import static ice.model.Constants.*;
import static java.nio.ByteBuffer.allocateDirect;

/**
 * User: Jason
 * Date: 13-1-23
 */
public class BufferUtil {

    public static FloatBuffer wrap(float... data) {
        ByteBuffer byteBuffer = allocateDirect(data.length * BYTES_PER_FLOAT);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(data);
        floatBuffer.position(0);
        return floatBuffer;
    }

    public static ShortBuffer wrap(short... data) {
        ByteBuffer byteBuffer = allocateDirect(data.length * BYTES_PER_SHORT);
        byteBuffer.order(ByteOrder.nativeOrder());
        ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
        shortBuffer.put(data);
        shortBuffer.position(0);
        return shortBuffer;
    }

    public static ByteBuffer wrap(byte... data) {
        ByteBuffer byteBuffer = allocateDirect(data.length);
        byteBuffer.order(ByteOrder.nativeOrder());
        byteBuffer.put(data);
        byteBuffer.position(0);
        return byteBuffer;
    }

    public static ByteBuffer byteBuffer(int size) {
        ByteBuffer byteBuffer = allocateDirect(size);
        byteBuffer.order(ByteOrder.nativeOrder());
        return byteBuffer;
    }

    public static ShortBuffer shortBuffer(int size) {
        ByteBuffer byteBuffer = allocateDirect(size * BYTES_PER_SHORT);
        byteBuffer.order(ByteOrder.nativeOrder());
        return byteBuffer.asShortBuffer();
    }

    public static IntBuffer intBuffer(int size) {
        ByteBuffer byteBuffer = allocateDirect(size * BYTES_PER_INT);
        byteBuffer.order(ByteOrder.nativeOrder());
        return byteBuffer.asIntBuffer();
    }

    public static FloatBuffer floatBuffer(int size) {
        ByteBuffer byteBuffer = allocateDirect(size * BYTES_PER_FLOAT);
        byteBuffer.order(ByteOrder.nativeOrder());
        return byteBuffer.asFloatBuffer();
    }

}
