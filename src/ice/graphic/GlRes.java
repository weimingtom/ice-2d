package ice.graphic;


/**
 * User: ice
 * Date: 11-11-15
 * Time: 下午3:26
 */
public interface GlRes {

    void onEGLContextLost();

    void prepare();

    //void recycle(GL11 gl);

    void release();

}
