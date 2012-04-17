package ice.practical;

import ice.graphic.gl_status.BlendController;
import ice.node.particle_system.Particle;
import ice.node.particle_system.PointParticleSystem;

import java.util.Random;

import static javax.microedition.khronos.opengles.GL10.GL_ONE;

/**
 * User: ice
 * Date: 11-11-25
 * Time: 上午11:55
 */
public class TestParticleSystem extends PointParticleSystem {

    private static class TestParticle extends Particle {
        public float speed;
        public float speedAngle;
    }


    public TestParticleSystem(int maxParticleNum, int textureRes) {
        super(maxParticleNum, textureRes, false, false);
        addGlStatusController(new BlendController(GL_ONE, GL_ONE));
    }


    @Override
    protected void onInit(Particle[] particles) {

        Random random = new Random();

        for (int i = 0; i < particles.length; i++) {
            TestParticle particle = new TestParticle();
            particle.size = 50 + random.nextInt(50);
            particle.alive = true;
            particle.speed = (i + 20) * 120 / (float) particles.length;
            particle.speedAngle = (i + 10) * 10;
            particle.colorR = (float) Math.random();
            particle.colorG = (float) Math.random();
            particle.colorB = (float) Math.random();
            particles[i] = particle;
        }
    }

    @Override
    protected void onUpdateParticles(Particle[] particles, long current) {
        if (lastUpdateTime == 0) {
            lastUpdateTime = current;
            return;
        }

        float interval = (current - lastUpdateTime) / 1000.0f;

        for (int i = 0, size = particles.length; i < size; i++) {
            TestParticle particle = (TestParticle) particles[i];
            particle.posX = particle.speed * (float) Math.cos(particle.speedAngle);
            particle.posY = particle.speed * (float) Math.sin(particle.speedAngle);
            particle.speedAngle += (i + 20) * interval / size;
        }

        lastUpdateTime = current;
    }

    private long lastUpdateTime;
}
