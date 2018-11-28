package si.fri.rgti;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;


import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Player extends GameObject implements Updateable {


    long window_ref;
    // position in game space
    float x;
    float y;
    float z;


    // We need to strongly reference callback instances.
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback   keyCallback;

    Player(long window_ref) {
        this.window_ref = window_ref;
    }

    @Override
    public void draw() {
        super.draw(); // TODO
    }

    @Override
    public void update() {
        // TODO
    }


    private void init() {
//        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err)); // TODO what?

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window_ref, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(window, true); // We will detect this in our rendering loop
            }
        });
    }


    // h in w zaslona
    public void mouse(long width, long height) {

        double prevX = 0;
        double prevY = 0;

        while (!glfwWindowShouldClose(window_ref)) {

            boolean mouseLocked = false;
            if (glfwGetMouseButton(window_ref, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS) {
                glfwSetCursorPos(window_ref, width/2, height/2);
                mouseLocked = true;
            }

            if (mouseLocked){
                DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

                glfwGetCursorPos(window_ref, x, y);
                x.rewind();
                y.rewind();

                double newX = x.get();
                double newY = y.get();

                double deltaX = newX - 400;
                double deltaY = newY - 300;

                boolean rotX = newX != prevX;
                boolean rotY = newY != prevY;

                prevX = newX;
                prevY = newY;


                glfwSetCursorPos(window_ref, width/2, height/2);
            }

        }

    }



    // matrike iz predavanj

    public Matrix4f getMVMatrix(double d) {
        float[] koordKamere = new float[3];
        koordKamere[0] = this.x;
        koordKamere[0] = this.y;
        koordKamere[0] = this.z;

        Matrix4f matrikaPogleda = new Matrix4f();
        Matrix4f translacijaKamere = translate(-koordKamere[0], -koordKamere[1], -koordKamere[2]);
        Matrix4f perspektivaKamere = perspective(d);
        perspektivaKamere.mul(translacijaKamere, matrikaPogleda);
        return matrikaPogleda;
    }

    public Matrix4f perspective(double d) {
        Matrix4f p = new Matrix4f(1, 0, 0, 0, 0, 1,
                0, 0, 0, 0, 1, (float)(1/d), 0, 0, 0, 0);
        return p;
    }

    public Matrix4f translate(double dx, double dy, double dz) {
        Matrix4f p = new Matrix4f(1, 0, 0, 0, 0, 1,
                0, 0, 0, 0, 1, 0,
                (float)dx, (float)dy, (float)dz, 1);
        return p;
    }

    public static Matrix4f scale(double sx, double sy, double sz) {
        Matrix4f p = new Matrix4f((float)sx, 0, 0, 0,
                0, (float)sy, 0, 0,
                0, 0, (float)sz, 0,
                0, 0, 0, (float)1);
        return p;
    }

    public static Matrix4f rotateY(double kot) {
        // kot spremenimo v radiane
        kot = (kot/180)*Math.PI;
        // matrika za rotacijo iz predavanj
        return new Matrix4f((float)Math.cos(kot), 0, (float)((-1)*Math.sin(kot)),
                0, 0, 1, 0, 0,
                (float)Math.sin(kot), 0, (float)Math.cos(kot),
                0, 0, 0, 0, 1);
    }

    public static Matrix4f rotateX(double kot) {
        // kot spremenimo v radiane
        kot = (kot/180)*Math.PI;
        // matrika za rotacijo iz predavanj
        return new Matrix4f(1f, 0f, 0f, 0f, 0f ,
                (float)Math.cos(kot), (float)Math.sin(kot), 0f, 0f,
                (float)((-1)*Math.sin(kot)), (float)Math.cos(kot), 0f, 0f, 0f, 0f, 1f);
    }

    public static Matrix4f rotateZ(double kot) {
        // kot spremenimo v radiane
        kot = (kot/180)*Math.PI;
        // matrika za rotacijo iz predavanj
        return new Matrix4f((float)Math.cos(kot), (float)Math.sin(kot), 0f, 0f,
                (float)((-1)*Math.sin(kot)), (float)Math.cos(kot),
                0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
    }


}
