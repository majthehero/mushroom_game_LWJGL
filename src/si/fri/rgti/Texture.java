package si.fri.rgti;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Texture {
	private int id;
	private int width;
	private int height;
	
	public Texture(String filename) {
		BufferedImage bi;
		try {
			bi = ImageIO.read(new File(filename));
			width = bi.getWidth();
			height = bi.getHeight();
			
			int[] pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);
			
			ByteBuffer pixels = BufferUtils.createByteBuffer(width*height*4);
			
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < height; j++) {
					int pixel = pixels_raw[i*width+j];
					pixels.put((byte)((pixel >> 16)&0xFF)); //RED
					pixels.put((byte)((pixel >> 8)&0xFF)); //GREEN
					pixels.put((byte)((pixel >> 0)&0xFF)); //BLUE
					pixels.put((byte)((pixel >> 24)&0xFF)); //ALPHA
				}
			}
			
			//webgl baje ghoce flipped buffer for some reason k ga jz ne vem
			pixels.flip();
			
			id = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, id);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, height, width, 0, GL_RGBA, GL_BYTE, pixels);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
		
	}
	
}


// https://www.youtube.com/watch?v=crOzRjzqI-o