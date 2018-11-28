package si.fri.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class mouseHandler extends GLFWCursorPosCallback {
	public double pos;
	
  @Override
	public void invoke(long window, double xpos, double ypos) {
  		//System.out.println("X: " + xpos + " Y: " + ypos);
		double pos[] = new double[2];
		pos[1] = xpos;
		pos[2] = ypos;
  	}
  
}