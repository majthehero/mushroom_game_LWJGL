package si.fri.rgti;

import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glValidateProgram;

public class AssetLoader {

    Dictionary<String, Integer> SHADERS;
    static AssetLoader assetLoader;

    {
        assetLoader = new AssetLoader();
    }

    public static AssetLoader getInstance() {
        return assetLoader;
    }

    /**
     * TODO:
     * load obj files
     * load textures
     * store geometry, pass references to objects
     *
     * DONE:
     * vertex shader loader
     * fragment shader loader
     */

    // audio


    // geometry

    /**
     * Loads an .obj file.
     * @param path path to file
     */
    void loadObj(String path) {
        BufferedReader br;
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<int[]> faces = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("v ")) {
                    String[] ln_splt = line.split(" ");
                    Vector3f v = new Vector3f(
                            Float.parseFloat(ln_splt[1]),
                            Float.parseFloat(ln_splt[2]),
                            Float.parseFloat(ln_splt[3]));
                    vertices.add(v);
                }
                else if (line.startsWith("vn ")) {
                    String[] ln_splt = line.split(" ");
                    Vector3f n = new Vector3f(
                            Float.parseFloat(ln_splt[1]),
                            Float.parseFloat(ln_splt[2]),
                            Float.parseFloat(ln_splt[3]));
                    normals.add(n);
                }
                else if (line.startsWith("f ")) {
                    String[] ln_splt = line.split(" ");
                    // TODO parse face
                }


            }
        } catch (FileNotFoundException e) {
            System.out.println("WARNING: AssetLoader.loadObj: file \"" +
                    path + "\n" + " not found.");
        } catch (IOException e) {
            System.out.println("WARNING: AssetLoader.loadObj: couldn't load file \"" +
                    path + "\"");
            e.printStackTrace();
        }



    }

    // textures


    // shaders

    /**
     * Loads and compiles a fragment shader.
     * @param path Path to shader source.
     * @return int shader ID
     */
    int loadFragmentShader(String path) {
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

        StringBuilder fragmentShaderSrc = new StringBuilder();

        // load shader source file
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/sI/fri/rgti/shader.frag"));
            String line;
            while ((line = br.readLine()) != null)
                fragmentShaderSrc.append(line).append("\n");
        } catch (IOException e) {
            System.out.println("Failed to load fragment shader.");
        }

        glShaderSource(GL_FRAGMENT_SHADER, fragmentShaderSrc);
        glCompileShader(fragmentShader);
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE)
            System.out.println("Failed to compile fragment shader.");

        return fragmentShader;
    }

    /**
     * Loads and compiles a vertex shader.
     * @param path Path to shader source.
     * @return int shader ID
     */
    int loadVertexShader(String path) {
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);

        StringBuilder vertexShaderSrc = new StringBuilder();

        // load shader source file
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                vertexShaderSrc.append(line).append("\n");
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Failed to load vertex shader.");
        }

        // compile shader
        glShaderSource(GL_VERTEX_SHADER, vertexShaderSrc);
        glCompileShader(vertexShader);
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE)
            System.out.println("Failed to compile vertex shader.");

        return vertexShader;
    }

    /**
     * Create an OpenGL shader program from multiple shaders.
     * @param shaders list of shader int IDs
     * @return program int ID
     */
    int createProgram(List<Integer> shaders) {
        int shaderProgram = glCreateProgram();
        for (Integer shader : shaders) {
            glAttachShader(shaderProgram, shader);
        }
        glLinkProgram(shaderProgram);
        glValidateProgram(shaderProgram);
        return shaderProgram;
    }
}
