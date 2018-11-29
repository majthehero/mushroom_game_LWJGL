package si.fri.rgti;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20C.glUniformMatrix4fv;

public class SceneGraph
    implements Drawable, Updateable {

    Terrain terrain;
    Player player;

    SceneGraph(long window_ref) {

        // make terrain
        terrain = new Terrain();
        player = new Player(window_ref);
    }

    @Override
    public void draw(Matrix4f mvMat) {
        Vector3f camPosition = new Vector3f(player.x, player.y, player.z);
        Vector3f lightPosition = new Vector3f(1.0f, 0.6f, 0.3f); // some light added
        Matrix4f mvMatrix = player.modelMatrix(mvMat, player.x, player.y, player.z, 60, 60, 60);
        Matrix4f pMatrix = player.perspective(4.0);

        FloatBuffer mvMatBuffer = BufferUtils.createFloatBuffer(4*4);
        FloatBuffer pMatBuffer = BufferUtils.createFloatBuffer(4*4);

        mvMatrix.get(mvMatBuffer);
        pMatrix.get(pMatBuffer);

        // set shaders
        int vectorShader = AssetLoader.getInstance().SHADERS.get("vector_basic");
        int fragmentShader = AssetLoader.getInstance().SHADERS.get("fragment_basic");

        // bind camPosition and lightPosition
        glUniform3f(glGetUniformLocation(fragmentShader, "lightVector"),
                lightPosition.x, lightPosition.y, lightPosition.z);
        glUniform3f(glGetUniformLocation(fragmentShader, "cameraVector"),
                camPosition.x, camPosition.y, camPosition.z);

        // bind current matrices - perspective will be always the same,
        glUniformMatrix4fv(glGetUniformLocation(fragmentShader, "mvMatrix"),
                false, mvMatBuffer);

        // mvMatrix will be diff for eah object
        glUniformMatrix4fv(glGetUniformLocation(fragmentShader, "pMatrix"),
                false, pMatBuffer);

        // draw next level of stuff
        terrain.draw(mvMatrix);

    }

    @Override
    public void update() {

    }
}
