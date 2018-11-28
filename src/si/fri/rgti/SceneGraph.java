package si.fri.rgti;

import org.joml.Matrix4f;
import org.joml.Vector3f;

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
        Matrix4f mvMatrix = player.getMVMatrix(4.0);




    }

    @Override
    public void update() {

    }
}
