import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengles.GLES20.*;

public class Game {

    private long window;


    Game(long window) {
        // init scene graph
        this.window = window;
    }

    void run() {
        GL.createCapabilities(); // enable gl
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);


        float[] box = {
             0, 0, 1,
            -1, 0,-1,
            -1,-1,-1,
            -1, 1,-1
        };
        int[] triangles = {
                0, 2, 1,
                1, 2, 3,
                1, 3, 0,
                0, 3, 2
        };
        IntBuffer buffer = BufferUtils.createIntBuffer(triangles.length);
        glGenBuffers(triangles);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, triangles);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, triangles.length, GL_STATIC_DRAW);


        while(!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // create box



            glfwSwapBuffers(window);
            glfwPollEvents(); // anonymous callback was set, now needs to be called. Weird, usually callbacks are called
        }
    }


}
