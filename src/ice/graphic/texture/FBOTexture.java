package ice.graphic.texture;

import ice.util.MathUtil;

import javax.microedition.khronos.opengles.GL11;

import static javax.microedition.khronos.opengles.GL10.GL_RGB;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE_2D;

/**
 * User: jason
 * Date: 12-4-18
 * Time: 上午11:10
 */
public class FBOTexture extends Texture {

    public FBOTexture(int x, int y, int width, int height) {
        if (!Texture.isNpotSupported()) {
            if (!MathUtil.powerOfTwoTest(width, height))
                throw new IllegalArgumentException("Texture NPOT not supported !");
        }

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    protected void onLoadTextureData(GL11 gl) {
        gl.glCopyTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGB,
                x, y,
                width, height,
                0
        );

//        void glCopyTexSubImage2D(
//                GLenum  	target,
//                GLint  	level,
//                GLint  	xoffset,
//                GLint  	yoffset,
//                GLint  	x,
//                GLint  	y,
//                GLsizei  	width,
//                GLsizei  	height);
    }

    private int x, y;
    private int width, height;
}
