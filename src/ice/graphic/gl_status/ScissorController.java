package ice.graphic.gl_status;

import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;

import static javax.microedition.khronos.opengles.GL10.GL_SCISSOR_TEST;

/**
 * User: jason
 * Date: 12-2-21
 * Time: 下午12:15
 */
public class ScissorController implements GlStatusController {

    public static class Region {
        public Region(Region region) {
            this(region.x, region.y, region.width, region.height);
        }

        public Region(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public static Region union(Region regionA, Region regionB) {
            int left = Math.min(regionA.x, regionB.x);
            int bottom = Math.min(regionA.y, regionB.y);

            int rightA = regionA.x + regionA.width;
            int rightB = regionB.x + regionB.width;

            int width = Math.max(rightA, rightB) - left;

            int topA = regionA.y + regionA.height;
            int topB = regionB.y + regionB.height;

            int height = Math.max(topA, topB) - bottom;

            return new Region(left, bottom, width, height);
        }

        @Override
        public String toString() {
            return "Region{" +
                    "x=" + x +
                    ", y=" + y +
                    ", width=" + width +
                    ", height=" + height +
                    '}';
        }

        public int x, y;
        public int width, height;
    }

    public ScissorController() {
        this(new Region(0, 0, 0, 0));
    }

    public ScissorController(Region region) {
        this.region = region;
    }

    @Override
    public void attach(GL11 gl) {
        Region region = this.region;

        gl.glEnable(GL_SCISSOR_TEST);
        gl.glScissor(region.x, region.y, region.width, region.height);
    }

    @Override
    public boolean detach(GL11 gl, Overlay overlay) {
        gl.glDisable(GL_SCISSOR_TEST);

        return true;
    }

    public void set(Region region) {
        this.region = region;
    }

    public Region getRegion() {
        return region;
    }

    private Region region;
}
