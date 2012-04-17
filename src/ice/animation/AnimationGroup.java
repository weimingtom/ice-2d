package ice.animation;

import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AnimationGroup extends Animation {

    public AnimationGroup() {
        super(0);
        children = new ArrayList<Animation>();
    }

    public void add(Animation animation) {
        children.add(animation);

        duration = Math.max(
                duration,
                animation.duration + animation.getOffset()
        );
    }

    @Override
    protected void applyFillAfter(Overlay overlay) {
        if (isFillAfter()) {
            for (Animation animation : children) {
                if (animation.isFillAfter())
                    animation.applyFillAfter(overlay);
            }
        }
    }

    @Override
    protected void start() {
        super.start();
        for (Animation animation : children)
            animation.startTime = 0;
    }

    @Override
    protected void onAttach(GL11 gl, float interpolatedTime) {
        for (Animation animation : children)
            animation.attach(gl);
    }

    @Override
    protected void onDetach(Overlay overlay, GL11 gl) {

        for (Iterator<Animation> iterator = children.iterator(); iterator.hasNext(); ) {

            Animation animation = iterator.next();

            animation.onDetach(overlay, gl);

            if (loopTimes > 0 || loopTimes == FOREVER) {

            }
            else {
                if (animation.isCompleted()) {
                    iterator.remove();
                    animation.onComplete(overlay, gl);
                }
            }


        }
    }

    @Override
    public void setFillAfter(boolean fillAfter) {
        super.setFillAfter(fillAfter);
        for (Animation animation : children) {
            animation.setFillAfter(fillAfter);
        }
    }

    private List<Animation> children;
}
