package ice.graphic.gl_status;

import ice.node.Overlay;

/**
 * User: jason
 * Date: 12-2-21
 * Time: 上午10:25
 */
public interface GlStatusController {

    void attach();

    /**
     * 恢复之前的状态.
     *
     *
     * @param overlay
     * @return effect More Frame  true else false
     */
    boolean detach(Overlay overlay);

}
