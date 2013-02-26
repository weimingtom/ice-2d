package ice.animation;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import ice.graphic.gl_status.GlStatusController;
import ice.node.Overlay;

public abstract class Animation implements GlStatusController {

    public static final int FOREVER = Integer.MIN_VALUE;
    private static final long NOT_STARTED = 0;

    public interface Listener {
        void onAnimationEnd(Overlay overlay);
    }

    public Animation() {
        interpolator = new AccelerateDecelerateInterpolator();
        startTime = NOT_STARTED;
    }

    public Animation(long duration) {
        this();

        setDuration(duration);
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    protected void start() {
        finished = false;
        startTime = AnimationUtils.currentAnimationTimeMillis();
    }

    public void cancel() {
        cancel = true;
    }

    @Override
    public void attach() {

        if (startTime == NOT_STARTED)
            start();

        long currentTime = AnimationUtils.currentAnimationTimeMillis();

        if (currentTime - startTime < offset)
            return;

        boolean over = currentTime - (startTime + offset) > duration;

        if (over) {
            if (loopTimes > 0) {
                start();
                loopTimes--;
            } else if (loopTimes == FOREVER) {
                start();
            } else {
                finished = true;
            }
        }

        float normalizedTime = 0;

        if (over) {
            normalizedTime = 1.0f;
        } else {
            if (duration != 0 && currentTime >= startTime + offset) {
                normalizedTime = ((float) (currentTime - startTime - offset)) / (float) duration;
            }
        }

        //根据归一化时间调整时间插值
        float interpolatedTime = interpolator.getInterpolation(normalizedTime);

        onAttach(interpolatedTime);

        attached = true;
    }

    @Override
    public boolean detach(Overlay overlay) {

        if (attached) {
            onDetach(overlay);
            attached = false;
        }

        if (isCompleted()) {
            onComplete(overlay);
            return false;
        }

        return !isCanceled();
    }


    public void onComplete(final Overlay overlay) {

        if (fillAfter)
            applyFillAfter(overlay);

        if (listener != null)
            listener.onAnimationEnd(overlay);
    }

    protected void applyFillAfter(Overlay overlay) {

    }

    protected abstract void onAttach(float interpolatedTime);

    protected void onDetach(Overlay overlay) {

    }

    public long getDuration() {
        return duration;
    }

    public boolean isCompleted() {
        return finished;
    }


    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void setLoopTimes(int loopTimes) {
        this.loopTimes = loopTimes;
    }

    public boolean isCanceled() {
        return cancel;
    }

    public void setFillAfter(boolean fillAfter) {
        this.fillAfter = fillAfter;
    }

    public boolean isFillAfter() {
        return fillAfter;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    private boolean attached;

    private long offset;

    private boolean finished;

    private boolean fillAfter = true;

    protected long startTime;
    protected long duration;

    protected int loopTimes;

    private boolean cancel;

    private Interpolator interpolator;
    private Listener listener;
}
