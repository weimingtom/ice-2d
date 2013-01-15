package ice.model.vertex;

import ice.node.Overlay;

import javax.microedition.khronos.opengles.GL11;

import static javax.microedition.khronos.opengles.GL11.*;

/**
 * User: jason
 * Date: 13-1-15
 */
public class VertexArray extends AbstractVertexData {

    public VertexArray(int verticesCount, VertexAttributes attributes) {
        super(verticesCount, attributes);
    }

    @Override
    public void attach(GL11 gl) {

        for (int i = 0, size = attributes.size(); i < size; i++) {

            VertexAttribute attribute = attributes.get(i);
            int offset = attribute.getOffset();
            int dimension = attribute.getDimension();

            switch (attribute.getUsage()) {
                case Position:
                    gl.glEnableClientState(GL_VERTEX_ARRAY);
                    srcData.position(offset);
                    gl.glVertexPointer(dimension, GL_FLOAT, attributes.vertexSize, srcData);
                    break;

                case Color:
                    gl.glEnableClientState(GL_COLOR_ARRAY);
                    srcData.position(offset);
                    gl.glColorPointer(dimension, GL_FLOAT, attributes.vertexSize, srcData);
                    break;

                case Normal:
                    gl.glEnableClientState(GL_NORMAL_ARRAY);
                    srcData.position(offset);
                    gl.glNormalPointer(GL_FLOAT, attributes.vertexSize, srcData);
                    break;

                case TextureCoordinates:
                    gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
                    //gl.glClientActiveTexture(GL.GL_TEXTURE0 + textureUnit++);
                    srcData.position(offset);
                    gl.glTexCoordPointer(dimension, GL_FLOAT, attributes.vertexSize, srcData);
                    break;
            }
        }

    }

    @Override
    public boolean detach(GL11 gl, Overlay overlay) {
        gl.glDisableClientState(GL_VERTEX_ARRAY);
        return true;
    }


    @Override
    public void onDrawVertex(GL11 gl) {

    }


}
