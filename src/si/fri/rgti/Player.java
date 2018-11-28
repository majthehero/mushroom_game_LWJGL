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

import si.fri.rgti.input.*;


public class Player extends GameObject implements Updateable {


    long window_ref;
    // position in game space
    float x;
    float y;
    float z;
    // orientation in game space - in RADIANS - add change of mouse position, divided by something that makes sense
    float rot_x;
    float rot_y;
    float rot_z;

    // velocities
    float x_vel, y_vel, z_vel;


    // We need to strongly reference callback instances.
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    private GLFWCursorPosCallback mouseCallback;

    Player(long window_ref) {
        this.window_ref = window_ref;
    }

    @Override
    public void update() {
        // TODO
    }

    @Override
    public void draw(Matrix4f mvMat) {
        ;; // do nothing
    }

    // check for collision
    public boolean collisionCheck() {

        return false;
    }

    public void movementDirection(){
        glfwSetKeyCallback(window_ref, keyCallback = new KeyboardHandler());
        glfwSetCursorPosCallback(window_ref, mouseCallback = new MouseHandler());


        if(KeyboardHandler.isKeyDown(GLFW_KEY_W)) {
            // naprej = 1.0;
            x_vel = 1;
        } else if(KeyboardHandler.isKeyDown(GLFW_KEY_A)) {
            // levo
            y_vel = 1;
        } else if(KeyboardHandler.isKeyDown(GLFW_KEY_S)) {
            // nazaj
            y_vel = -1;
        } else if(KeyboardHandler.isKeyDown(GLFW_KEY_D)) {
            // desno
            x_vel = -1;
        }

    }

    private void init() {
        glfwSetErrorCallback(new GLFWErrorCallback() {
            @Override
            public void invoke(int error, long description) {
                System.out.println("OpenGL ERROR: " + error + " : " + description);
            }
        });

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
    public void spremembaMiske(long width, long height) {

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









}
