package si.fri.rgti;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.io.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;

public class App {

    private long window;

    // entry point
    public static void main(String[] args) {
        new App().run();
    }

    void run() {
        System.out.println("Starting...");

        gl_init();

        Game game = new Game(window);
        game.run();

        // cleanup
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // terminate
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    void gl_init() {
        // error callback - will print to std.err
        GLFWErrorCallback.createPrint(System.err).set();

        // init glfw
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize glfw.");
        }

        // config glfw
        glfwDefaultWindowHints(); // unnecessary
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // keep invisible for a time
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // allow resizing

        window = glfwCreateWindow(1024, 640, "The Thing", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Unable to create a window.");
        }

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // check in loop
            }
        });

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // this enables vsync
        glfwShowWindow(window); // now show it, after it's set up


    }


}
