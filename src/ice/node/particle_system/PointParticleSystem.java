package ice.node.particle_system;

import android.view.animation.AnimationUtils;
import ice.graphic.texture.BitmapTexture;
import ice.graphic.texture.Texture;
import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static ice.model.Constants.BYTES_PER_FLOAT;
import static android.opengl.GLES11.*;

/**
 * User: jason
 * Date: 12-1-11
 * Time: 下午3:29
 */
public abstract class PointParticleSystem extends Overlay {

    public PointParticleSystem(int maxParticleNum, int textureRes, boolean sameSize) {
        this(maxParticleNum, textureRes, sameSize, true);
    }

    public PointParticleSystem(int maxParticleNum, int resId, boolean sameSize, boolean sameColor) {
        this.sameSize = sameSize;
        this.sameColor = sameColor;

        texture = new BitmapTexture(resId);

        ByteBuffer vfb = ByteBuffer.allocateDirect(BYTES_PER_FLOAT * 2 * maxParticleNum);
        vfb.order(ByteOrder.nativeOrder());
        vertexBuffer = vfb.asFloatBuffer();

        if (!sameSize) {
            vfb = ByteBuffer.allocateDirect(BYTES_PER_FLOAT * 1 * maxParticleNum);
            vfb.order(ByteOrder.nativeOrder());
            sizeBuffer = vfb.asFloatBuffer();
        }

        if (!sameColor) {
            vfb = ByteBuffer.allocateDirect(BYTES_PER_FLOAT * 4 * maxParticleNum);
            vfb.order(ByteOrder.nativeOrder());
            colorBuffer = vfb.asFloatBuffer();
        }

        particles = new Particle[maxParticleNum];

        for (int i = 0; i < particles.length; i++)
            particles[i] = new Particle();

        onInit(particles);

        addGlStatusController(texture);
    }

    @Override
    public void onEGLContextLost() {
        texture.onEGLContextLost();
    }

    @Override
    protected void onDraw() {

        int liveCount = fillActive();

        if (liveCount > 0)
            drawActiveParticles(liveCount);

        onUpdateParticles(particles, AnimationUtils.currentAnimationTimeMillis());
    }

    protected abstract void onInit(Particle[] particles);

    protected abstract void onUpdateParticles(Particle[] particles, long current);

    private void drawActiveParticles(int liveCount) {

        glEnable(GL_POINT_SPRITE_OES);
        glEnableClientState(GL_VERTEX_ARRAY);

        //An advantage of representing particles with point sprites is that texture coordinate generation can be handled by the system
        glTexEnvi(GL_POINT_SPRITE_OES, GL_COORD_REPLACE_OES, GL_TRUE);

        vertexBuffer.position(0);
        vertexBuffer.limit(2 * liveCount);
        glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);

        if (!sameColor) {
            colorBuffer.position(0);
            colorBuffer.limit(4 * liveCount);
            glEnableClientState(GL_COLOR_ARRAY);
            glColorPointer(4, GL_FLOAT, 0, colorBuffer);
        }

        if (!sameSize) {
            glEnableClientState(GL_POINT_SIZE_ARRAY_OES);

            sizeBuffer.position(0);
            sizeBuffer.limit(liveCount);
            glPointSizePointerOES(GL_FLOAT, 0, sizeBuffer);
        }


        glDrawArrays(GL_POINTS, 0, liveCount / 2);

        glDisableClientState(GL_VERTEX_ARRAY);

        if (!sameColor)
            glDisableClientState(GL_COLOR_ARRAY);

        if (!sameSize)
            glDisableClientState(GL11.GL_POINT_SIZE_ARRAY_OES);

        glDisable(GL_POINT_SPRITE_OES);
    }

    private int fillActive() {

        vertexBuffer.position(0);
        vertexBuffer.limit(vertexBuffer.capacity());
        if (!sameSize) {
            sizeBuffer.position(0);
            sizeBuffer.limit(sizeBuffer.capacity());
        }

        if (!sameColor) {
            colorBuffer.position(0);
            colorBuffer.limit(colorBuffer.capacity());
        }


        int liveCount = 0;

        for (int index = 0; index < particles.length; index++) {

            Particle particle = particles[index];

            if (particle.alive) {

                vertexBuffer.put(
                        new float[]{particle.posX, particle.posY}
                );

                if (!sameSize) {
                    sizeBuffer.put(particle.size);
                }

                if (!sameColor) {
                    colorBuffer.put(
                            new float[]{particle.colorR, particle.colorG, particle.colorB, particle.colorA}
                    );
                }

                liveCount++;
            }

        }

        return liveCount;
    }

    private Texture texture;
    private boolean sameColor;
    private boolean sameSize;
    protected Particle particles[];
    private FloatBuffer vertexBuffer;
    private FloatBuffer sizeBuffer;
    private FloatBuffer colorBuffer;
}
