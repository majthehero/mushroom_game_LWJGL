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

        // create shader program
        int shaderProgram = glCreateProgram();
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        StringBuilder vertexShaderSrc = new StringBuilder();
        StringBuilder fragmentShaderSrc = new StringBuilder();
        // load shader source files
        try { // vertex shader
            BufferedReader br = new BufferedReader(new FileReader("src/sI/fri/rgti/shader.vert"));
            String line;
            while ((line = br.readLine()) != null) {
                vertexShaderSrc.append(line).append("\n");
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Failed to load vertex shader.");
        }
        try { // fragment shader
            BufferedReader br = new BufferedReader(new FileReader("src/sI/fri/rgti/shader.frag"));
            String line;
            while ((line = br.readLine()) != null)
                fragmentShaderSrc.append(line).append("\n");
        } catch (IOException e) {
            System.out.println("Failed to load fragment shader.");
        }

        glShaderSource(GL_VERTEX_SHADER, vertexShaderSrc);
        glCompileShader(vertexShader);
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE)
            System.out.println("Failed to compile vertex shader.");

        glShaderSource(GL_FRAGMENT_SHADER, fragmentShaderSrc);
        glCompileShader(fragmentShader);
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE)
            System.out.println("Failed to compile fragment shader.");

    }


}
