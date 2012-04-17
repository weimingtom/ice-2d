package ice.node.widget;

import ice.graphic.gl_status.BlendController;
import ice.graphic.gl_status.CullFaceController;
import ice.graphic.texture.BitmapTexture;
import ice.graphic.texture.Texture;
import ice.node.OverlayParent;
import ice.res.Res;

import static javax.microedition.khronos.opengles.GL10.GL_ONE;

/**
 * User: jason
 * Date: 12-3-5
 * Time: 上午11:44
 */
public class Simulate3D extends OverlayParent {

    public Simulate3D(int bitmap) {
        this(Res.getBitmap(bitmap).getWidth(), Res.getBitmap(bitmap).getHeight(), bitmap);
    }

    public Simulate3D(float width, float height, int bitmap) {
        this(width, height, new BitmapTexture(bitmap));
    }

    public Simulate3D(float width, float height, Texture texture) {

        Grid gridA = new Grid(width, height);
        gridA.setTexture(texture);

        Grid gridB = new Grid(width, height);
        gridB.setTexture(texture);
        gridB.setRotate(90, 0, 1, 0);

        addChildren(gridA, gridB);

        addGlStatusController(new CullFaceController(CullFaceController.FaceMode.BothSide));
        addGlStatusController(new BlendController(GL_ONE, GL_ONE));
    }
}
