import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Main extends JFrame {

    private static int cameraPos[] = {0, 0, -55};
	private static double fov = 60.0;
	private static double n = 1.0 / Math.tan((fov * 3.1415 / 180.0) / 180.0);
	private static double scale = 50.0;

	private static double[][] cubeVertexes = {
		{-1, -1, -1},
		{-1, -1, 1},
		{-1, 1, 1},
		{-1, 1, -1},
		{1, -1, -1},
		{1, -1, 1},
		{1, 1, 1},
		{1, 1, -1}
	};

	private static int[][] cubeEdges = {
		// front face
		{0, 1},
		{1, 2},
		{2, 3},
		{3, 0},
		// back face
		{4, 5},
		{5, 6},
		{6, 7},
		{7, 4},
		// connect faces
		{0, 4},
		{1, 5},
		{2, 6},
		{3, 7}
	};

 
    public Main() {
        super("3D Cube");
		addKeyListener(new CustomKeyListener());
 
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

		for (int row = 0; row < cubeVertexes.length; row++) {
			for (int col = 0; col < cubeVertexes[0].length; col++) {
				cubeVertexes[row][col] *= scale;
			}
		}

		super.addMouseMotionListener( new MyMouseListener() );
    }
 
    static void drawLines(Graphics g) {
        // Graphics2D g2d = (Graphics2D) g;
 
        for (int[] edge : cubeEdges) {
			double[] vertex1 = cubeVertexes[edge[0]];
			double[] vertex2 = cubeVertexes[edge[1]];

			double[] first = perspectiveStuff(vertex1);
			double[] second = perspectiveStuff(vertex2);

			g.setColor(Color.BLACK);
			g.drawLine((int) first[0], (int) first[1], (int) second[0], (int) second[1]);
		}
		
		
    }
 
    public void paint(Graphics g) {
        super.paint(g);
        drawLines(g);
    }

	private static double[] rotateX3D(double[] point, double angle) {
		double x = point[0];
		double y = point[1];
		double z = point[2];

		double[] newPoint = new double[3];
		newPoint[0] = point[0];
		newPoint[1] = point[1];
		newPoint[2] = point[2];

		newPoint[1] = (y * Math.cos(Math.toRadians(angle))) - (z * Math.sin(Math.toRadians(angle)));
		newPoint[2] = (z * Math.cos(Math.toRadians(angle))) + (y * Math.sin(Math.toRadians(angle)));

		return newPoint;
	}

	private static double[] rotateY3D(double[] point, double angle) {
		double x = point[0];
		double y = point[1];
		double z = point[2];

		double[] newPoint = new double[3];
		newPoint[0] = point[0];
		newPoint[1] = point[1];
		newPoint[2] = point[2];

		newPoint[0] = (x * Math.cos(Math.toRadians(angle))) - (z * Math.sin(Math.toRadians(angle)));
		newPoint[2] = (z * Math.cos(Math.toRadians(angle))) + (x * Math.sin(Math.toRadians(angle)));

		return newPoint;
	}

	private static double[] rotateZ3D(double[] point, double angle) {
		double x = point[0];
		double y = point[1];
		double z = point[2];

		double[] newPoint = new double[3];
		newPoint[0] = point[0];
		newPoint[1] = point[1];
		newPoint[2] = point[2];

		newPoint[0] = (x * Math.cos(Math.toRadians(angle))) - (y * Math.sin(Math.toRadians(angle)));
		newPoint[1] = (y * Math.cos(Math.toRadians(angle))) + (x * Math.sin(Math.toRadians(angle)));

		return newPoint;
	}

	private static void rotateAllPoints(double x, double y, double z) {
		for (int i = 0; i < cubeVertexes.length; i++) {
			cubeVertexes[i] = rotateX3D(cubeVertexes[i], x);
			cubeVertexes[i] = rotateY3D(cubeVertexes[i], y);
			cubeVertexes[i] = rotateZ3D(cubeVertexes[i], z);
		}
	}

    private static double[] perspectiveStuff(double[] point3d) {
		if (point3d.length != 3) {
			return null;
		}

		double x = point3d[0];
		double y = point3d[1];
		double z = point3d[2];

		x -= cameraPos[0];
		y -= cameraPos[1];
		z -= cameraPos[2];

		double xp = (x * n) / z;
		double yp = (y * n) / z;

		// normalize the points to the center of the screen
		xp += 800 / 2;
		yp += 800 / 2;

		double[] point2d = {xp, yp};
		return point2d;
	}

	class CustomKeyListener implements KeyListener {
		public void keyTyped(KeyEvent e) {
		}
		public void keyPressed(KeyEvent e) {
		   	if (e.getKeyCode() == KeyEvent.VK_A) {
				cameraPos[0] += -5;
		   	}
		   	if (e.getKeyCode() == KeyEvent.VK_D) {
				cameraPos[0] += 5;
			}
			if (e.getKeyCode() == KeyEvent.VK_W) {
			 	cameraPos[2] += 5;
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
				cameraPos[2] += -5;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				cameraPos[1] += 5;
			}
			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				cameraPos[1] += -5;
			}
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				rotateAllPoints(5, 0, 0);
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				rotateAllPoints(-5, 0, 0);
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				rotateAllPoints(0, -5, 0);
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rotateAllPoints(0, 5, 0);
			}
			if (e.getKeyCode() == KeyEvent.VK_EQUALS) {
				rotateAllPoints(0, 0, 5);
			}
			if (e.getKeyCode() == KeyEvent.VK_MINUS) {
				rotateAllPoints(0, 0, -5);
			}

			repaint();
		}

		public void keyReleased(KeyEvent e) {
		}   
	}
	
	private class MyMouseListener implements MouseMotionListener {
		public void mouseMoved(MouseEvent e) {
			rotateAllPoints(
				(MouseInfo.getPointerInfo().getLocation().x - 400) * 0.001,
				(MouseInfo.getPointerInfo().getLocation().y - 400) * 0.001,
				0
			);
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
    }
}