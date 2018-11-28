package si.fri.rgti;

import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import Input.KeyboardHandler;

import java.nio.ByteBuffer;


import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Player extends GameObject implements Updateable {

    // We need to strongly reference callback instances.
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback   keyCallback;

    // The window handle
    private long window;


    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void update() {

    }


    private void init() {
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( glfwInit() != GL11.GL_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        int WIDTH = 800;
        int HEIGHT = 600;

        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(window, GL_TRUE); // We will detect this in our rendering loop
            }
        });

        // Get the resolution of the primary monitor
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                window,
                (GLFWvidmode.width(vidmode) - WIDTH) / 2,
                (GLFWvidmode.height(vidmode) - HEIGHT) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

    }


    // h in w zaslona
    public void mouse(long width, long height) {
        while (glfwWindowShouldClose(window) == GL_FALSE) {

            if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS) {
                glfwSetCursorPos(window, width/2, height/2);
                mouseLocked = true;
            }

            if (mouseLocked){
                DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

                glfwGetCursorPos(window, x, y);
                x.rewind();
                y.rewind();

                newX = x.get();
                newY = y.get();

                double deltaX = newX - 400;
                double deltaY = newY - 300;

                rotX = newX != prevX;
                rotY = newY != prevY;

                prevX = newX;
                prevY = newY;


                glfwSetCursorPos(window, width/2, height/2);
            }

        }

    }


    // ali je gumb pritisnjen
    public void keyboard(){
        while (Keyboard.next()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_W) {
                // en korak naprej

            } else if (Keyboard.getEventKey() == Keyboard.KEY_S) {
                // en korak nazaj

            } else if (Keyboard.getEventKey() == Keyboard.KEY_D) {
                // en korak desno

            } else if (Keyboard.getEventKey() == Keyboard.KEY_A) {
                // en korak levo

            }
        }
    }



    // matrike iz predavanj

    public static Matrix4f transformacijaKoordinat(double[] koordKamere, double d) {
        Matrix4f matrikaPogleda = new Matrix4f();
        Matrix4f translacijaKamere = translate(-koordKamere[0], -koordKamere[1], -koordKamere[2]);
        Matrix4f perspektivaKamere = perspective(d);
        Matrix4f.mul(matrikaPogleda, perspektivaKamere, translacijaKamere);
        return matrikaPogleda;
    }

    public static Matrix4f perspective(double d) {
        Matrix4f p = new Matrix4f(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1/d, 0, 0, 0, 0);
        return p;
    }

    public static Matrix4f translate(double dx, double dy, double dz) {
        Matrix4f p = new Matrix4f(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, dx, dy, dz, 1);
    }

    public static Matrix4f scale(double sx, double sy, double sz) {
        Matrix4f p = new Matrix4f(sx, 0, 0, 0, 0, sy, 0, 0, 0, 0, sz, 0, 0, 0, 0, 1);
        return p;
    }

    public static Matrix4f rotateY(double kot) {
        // kot spremenimo v radiane
        kot = (kot/180)*Math.PI;
        // matrika za rotacijo iz predavanj
        return new Matrix4f(Math.cos(kot), 0, (-1)*Math.sin(kot), 0, 0, 1, 0, 0, Math.sin(kot), 0, Math.cos(kot), 0, 0, 0, 0, 1);
    }

    public static Matrix4f rotateX(double kot) {
        // kot spremenimo v radiane
        kot = (kot/180)*Math.PI;
        // matrika za rotacijo iz predavanj
        return new Matrix4f(1, 0, 0, 0, 0 , Math.cos(kot), Math.sin(kot), 0, 0, (-1)*Math.sin(kot), Math.cos(kot), 0, 0, 0, 0, 1);
    }

    public static Matrix4f rotateZ(double kot) {
        // kot spremenimo v radiane
        kot = (kot/180)*Math.PI;
        // matrika za rotacijo iz predavanj
        return new Matrix4f(Math.cos(kot), Math.sin(kot), 0, 0, (-1)*Math.sin(kot), Math.cos(kot), 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
    }


}
