package ice.model.vertex;

import ice.node.Overlay;

import static android.opengl.GLES11.*;

/**
 * User: jason
 * Date: 13-1-15
 */
public class VertexArray extends AbstractVertexData {

    public VertexArray() {

    }

    public VertexArray(int verticesCount, VertexAttributes attributes) {
        super(verticesCount, attributes);
    }

    @Override
    public void attach() {

        for (int i = 0, size = attributes.size(); i < size; i++) {

            VertexAttribute attribute = attributes.get(i);
            int offset = attribute.getOffset();
            int dimension = attribute.getDimension();

            switch (attribute.getUsage()) {
                case Position:
                    glEnableClientState(GL_VERTEX_ARRAY);
                    srcData.position(offset);
                    glVertexPointer(dimension, GL_FLOAT, attributes.vertexSize, srcData);
                    break;

                case Color:
                    glEnableClientState(GL_COLOR_ARRAY);
                    srcData.position(offset);
                    glColorPointer(dimension, GL_FLOAT, attributes.vertexSize, srcData);
                    break;

                case Normal:
                    glEnableClientState(GL_NORMAL_ARRAY);
                    srcData.position(offset);
                    glNormalPointer(GL_FLOAT, attributes.vertexSize, srcData);
                    break;

                case TextureCoordinates:
                    glEnableClientState(GL_TEXTURE_COORD_ARRAY);
                    //gl.glClientActiveTexture(GL.GL_TEXTURE0 + textureUnit++);
                    srcData.position(offset);
                    glTexCoordPointer(dimension, GL_FLOAT, attributes.vertexSize, srcData);
                    break;
            }
        }

    }

    @Override
    public boolean detach(Overlay overlay) {
        glDisableClientState(GL_VERTEX_ARRAY);
        return true;
    }


    @Override
    public void onDrawVertex() {

    }


}
