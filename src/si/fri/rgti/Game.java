package si.fri.rgti;

public class Game {

    private SceneGraph sceneGraph;

    Game() {
        // init scene graph
        sceneGraph = new SceneGraph();

    }

    void run() {

        while (true) {
            sceneGraph.update();
            sceneGraph.draw();
        }
    }

}
