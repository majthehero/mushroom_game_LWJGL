import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Game {

    private long window;


    Game(long window) {
        // init scene graph
        this.window = window;
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

        GL.createCapabilities();
        glfwSwapInterval(1); // this enables vsync
        glfwShowWindow(window); // now show it, after it's set up


    }

    int program;

    void run() {

        gl_init();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        float[] vertices = {
                  0f,     0f,  0.1f,
                -0.1f,    0f, -0.1f,
                -0.1f, -0.1f, -0.1f,
                -0.1f,  0.1f, -0.1f,
        };
        int[] triangles = {
                0, 1, 2,
                1, 3, 2,
                1, 0, 3,
                0, 2, 3,
        };
        IntBuffer triBufer = BufferUtils.createIntBuffer(triangles.length);
        triBufer.put(triangles);
        triBufer.flip();
        int triBuffID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, triBuffID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, triangles.length, GL_STATIC_DRAW);

        FloatBuffer vertBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertBuffer.put(vertices);
        vertBuffer.flip();
        int vertBuffID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertBuffID);
        glBufferData(GL_ARRAY_BUFFER, vertBuffer, GL_STATIC_DRAW);

        Matrix4f translate = new Matrix4f();
        translate.lookAt(new Vector3f(3, 1, 8),
                new Vector3f(0, 0, 0),
                new Vector3f(0, 0, 1));

        while (!glfwWindowShouldClose(window)) {
            ARBShaderObjects.glUseProgramObjectARB(program);

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // render box
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0f, 0.0f, 10.0f);
            glDrawElements(GL_TRIANGLES, triangles.length, GL_INT, 0);


            glfwSwapBuffers(window);
            glfwPollEvents(); // anonymous callback was set, now needs to be called. Weird, usually callbacks are called

            ARBShaderObjects.glUseProgramObjectARB(0);
        }
    }

    void makeShader() {

        int program = 0;

        int vertShader = 0, fragShader = 0;

        try {
            vertShader = createShader("shaders/screen.vert", ARBVertexShader.GL_VERTEX_SHADER_ARB);
            fragShader = createShader("shaders/screen.frag", ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
        } catch (Exception exc) {
            exc.printStackTrace();
            return;
        } finally {
            if (vertShader == 0 || fragShader == 0)
                return;
        }

        program = ARBShaderObjects.glCreateProgramObjectARB();

        if (program == 0)
            return;

        /*
         * if the vertex and fragment shaders setup sucessfully,
         * attach them to the shader program, link the sahder program
         * (into the GL context I suppose), and validate
         */
        ARBShaderObjects.glAttachObjectARB(program, vertShader);
        ARBShaderObjects.glAttachObjectARB(program, fragShader);

        ARBShaderObjects.glLinkProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
            System.out.println("Failed to compile.");
            return;
        }

        ARBShaderObjects.glValidateProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
            System.out.println("Failed to validate.");
            return;
        }

    }

    private int createShader(String filename, int shaderType) throws Exception {
        int shader = 0;
        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

            if (shader == 0)
                return 0;

            ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
            ARBShaderObjects.glCompileShaderARB(shader);

            if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
                throw new RuntimeException("Error creating shader: ");

            return shader;
        } catch (Exception exc) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            throw exc;
        }
    }

    private String readFileAsString(String filename) throws Exception {
        StringBuilder source = new StringBuilder();

        FileInputStream in = new FileInputStream(filename);

        Exception exception = null;

        BufferedReader reader;
        try{
            reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));

            Exception innerExc= null;
            try {
                String line;
                while((line = reader.readLine()) != null)
                    source.append(line).append('\n');
            }
            catch(Exception exc) {
                exception = exc;
            }
            finally {
                try {
                    reader.close();
                }
                catch(Exception exc) {
                    if(innerExc == null)
                        innerExc = exc;
                    else
                        exc.printStackTrace();
                }
            }

            if(innerExc != null)
                throw innerExc;
        }
        catch(Exception exc) {
            exception = exc;
        }
        finally {
            try {
                in.close();
            }
            catch(Exception exc) {
                if(exception == null)
                    exception = exc;
                else
                    exc.printStackTrace();
            }

            if(exception != null)
                throw exception;
        }

        return source.toString();
    }
}
