package ice.animation;

import android.util.Log;
import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;

/**
 * User: Mike.Hu
 * Date: 12-3-21
 * Time: 下午4:32
 * 本类是一个摇动动画
 */
public class RockAnimation extends Animation {

    /**
     *
     * @param duration           动画持续时间
     * @param intervalTime      一次摇动中间间隔时间
     * @param xScope             X轴摇动的幅度
     * @param yScope             Y轴摇动的幅度
     * @param zScope             Z轴摇动的幅度
     */
    public RockAnimation(long duration, long intervalTime, float xScope, float yScope, float zScope) {
        super(duration);

        this.xScope = xScope;
        this.yScope = yScope;
        this.zScope = zScope;
        this.intervalTime = intervalTime;
    }

    public RockAnimation(long duration) {
        super(duration);
    }

    @Override
    protected void applyFillAfter(Overlay overlay) {
    }

    @Override
    protected void onAttach(GL11 gl, float interpolatedTime) {
        times = times * -1;
        gl.glTranslatef(xScope * times, yScope * times, zScope * times);

        try {
            Thread.sleep(intervalTime);
        } catch (InterruptedException e) {
            Log.e(getClass().getSimpleName(),getClass().getSimpleName()+" thread wrong");
        }
    }

    @Override
    protected void onDetach(Overlay overlay, GL11 gl) {
        super.onDetach(overlay, gl);
    }

    private int times = 1;
    private float xScope;
    private float yScope;
    private float zScope;
    private long intervalTime;
}
