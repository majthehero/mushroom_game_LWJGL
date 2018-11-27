package si.fri.rgti;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

public class Game {

    private long window;
    private SceneGraph sceneGraph;

    Game(long window) {
        // init scene graph
        sceneGraph = new SceneGraph();
        this.window = window;
    }

    void run() {
        GL.createCapabilities(); // enable gl
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // as promised, checking for exit - don't - prefer to use si.fri.rgti.menu.

        while(!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            sceneGraph.update();
            sceneGraph.draw();


            glfwSwapBuffers(window);
            glfwPollEvents(); // anonymous callback was set, now needs to be called. Weird, usually callbacks are called
        }
    }


}
