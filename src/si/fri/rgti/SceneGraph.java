package si.fri.rgti;

public class SceneGraph
    implements Drawable, Updateable {

    Terrain terrain;
    Player player;

    SceneGraph() {

        // make terrain
        terrain = new Terrain();
        player = new Player();
    }

    @Override
    public void draw() {

    }

    @Override
    public void update() {

    }
}
