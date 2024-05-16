import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;


public class Main extends JFrame {

	private static final int WIDTH = 800;
	private static final int HEIGHT = 800;

    private static Vector3d cameraPos = new Vector3d(0, 0, -40);
	private static double[] cameraAngle = {0.0, 0.0, 0.0};
	private static double fov = 60.0;
	private static double mouseSensitivity = 0.7;
	private static ArrayList<Object3D> objects = new ArrayList<Object3D>();
	private static int triangleCount = 0;

 
    public Main() {
        super("3D Cube");
		addKeyListener(new CustomKeyListener());
 
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
		super.addMouseMotionListener( new MyMouseListener() );
	
		Object3D object = new Object3D("./res/objects/cube.obj", new Vector3d(0, -1, 0), new double[]{0.0, 0.0, 0.0}, 20.0);
		objects.add(object);

		// object = new Object3D("./res/objects/cube.obj", new Vector3d(-1, -1, 0), new double[]{0.0, 0.0, 0.0}, 20.0);
		// objects.add(object);

		// object = new Object3D("./res/objects/cutcube.obj", new Vector3d(-1, -2, 0), new double[]{0.0, 0.0, 0.0}, 20.0);
		// objects.add(object);

		for (int y = 0; y < 2; y++) {
			for (int z = 0; z < 5; z++) {
				object = new Object3D("./res/objects/cube.obj", new Vector3d(-1, y, z), new double[]{0.0, 0.0, 0.0}, 20.0);
				objects.add(object);
			}
		}

		for (int y = 0; y < 2; y++) {
			for (int z = 0; z < 5; z++) {
				object = new Object3D("./res/objects/cube.obj", new Vector3d(1, y, z), new double[]{0.0, 0.0, 0.0}, 20.0);
				objects.add(object);
			}
		}
    }
 
    public void paint(Graphics g) {
        super.paint(g);

		ArrayList<Object3D> tmpObjects = new ArrayList<Object3D>();
		for (Object3D object : objects) {
			tmpObjects.add(object);
		}

		Object3D.sortObjects(tmpObjects, cameraPos);
        for (Object3D object : tmpObjects) {
			triangleCount += object.drawToScreen(g, cameraPos, cameraAngle, WIDTH, HEIGHT, fov);
		}

		g.setColor(Color.BLACK);
		g.drawString("(" + cameraPos.getX() + ", " + cameraPos.getY() + ", " + cameraPos.getZ() + ")", 100, 100);
		g.drawString("Triangles: " + triangleCount, 100, 150);
		triangleCount = 0;
    }

	class CustomKeyListener implements KeyListener {
		public void keyTyped(KeyEvent e) {
		}
		public void keyPressed(KeyEvent e) {
		   	if (e.getKeyCode() == KeyEvent.VK_A) {
				cameraPos.setX(cameraPos.getX() - 5);
		   	}
		   	if (e.getKeyCode() == KeyEvent.VK_D) {
				cameraPos.setX(cameraPos.getX() + 5);
			}
			if (e.getKeyCode() == KeyEvent.VK_W) {
				cameraPos.setZ(cameraPos.getZ() + 5);
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
				cameraPos.setZ(cameraPos.getZ() - 5);
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				cameraPos.setY(cameraPos.getY() - 5);
			}
			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				cameraPos.setY(cameraPos.getY() + 5);
			}
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				objects.get(0).adjustRotation( new double[]{0.0, 10.0, 0.0} );
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				objects.get(0).adjustRotation( new double[]{0.0, -10.0, 0.0} );
			}

			repaint();
		}

		public void keyReleased(KeyEvent e) {
		}   
	}
	
	private class MyMouseListener implements MouseMotionListener {
		public void mouseMoved(MouseEvent e) {
			double movementX = (e.getX() - (WIDTH/2)) / ((double)WIDTH/2);
			double movementY = (e.getY() - (WIDTH/2)) / ((double)WIDTH/2);

			movementX *= 180;
			movementY *= 180;

			cameraAngle[0] = (movementY * mouseSensitivity);
			cameraAngle[1] = (movementX * mouseSensitivity);

			repaint();
		}
	
		public void mouseDragged(MouseEvent e) {
			
		}
	}
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });

		System.out.println("Running");
    }
}