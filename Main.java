import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Main extends JFrame {

    private static double[] cameraPos = {0, 0, -55};
	private static double[] cameraAngle = {0.0, 0.0, 0.0};
	private static double fov = 60.0;
	private static double n = 1.0 / Math.tan((fov * 3.1415 / 180.0) / 180.0);
	private static double scale = 50.0;

	private static final double[][] cubeVertexes = {
		{-1, -1, -1},
		{-1, -1, 1},
		{-1, 1, 1},
		{-1, 1, -1},
		{1, -1, -1},
		{1, -1, 1},
		{1, 1, 1},
		{1, 1, -1}
	};

	private static final int[][] cubeEdges = {
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

	private static double[][] vertexes = cubeVertexes.clone();
	private static int[][] edges = cubeEdges.clone();

 
    public Main() {
        super("3D Cube");
		addKeyListener(new CustomKeyListener());
 
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

		for (int row = 0; row < vertexes.length; row++) {
			for (int col = 0; col < vertexes[0].length; col++) {
				vertexes[row][col] *= scale;
			}
		}

		super.addMouseMotionListener( new MyMouseListener() );
    }
 
    static void drawLines(Graphics g) {
        // Graphics2D g2d = (Graphics2D) g;
 
        for (int[] edge : edges) {
			double[] vertex1 = vertexes[edge[0]];
			double[] vertex2 = vertexes[edge[1]];

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

	private static void rotateAllPoints(double x, double y, double z) {
		cameraAngle[0] += x;
		cameraAngle[1] += y;
		cameraAngle[2] += z;

		for (int i = 0; i < vertexes.length; i++) {
			vertexes[i] = Rotation.rotateX3D(vertexes[i], x);
			vertexes[i] = Rotation.rotateY3D(vertexes[i], y);
			vertexes[i] = Rotation.rotateZ3D(vertexes[i], z);
		}
	}

	private static void rotateAllPointsAroundPoint(double x, double y, double z, double[] point) {
		cameraAngle[0] += x;
		cameraAngle[1] += y;
		cameraAngle[2] += z;

		for (int i = 0; i < vertexes.length; i++) {
			vertexes[i] = Rotation.rotateX3DAroundPoint(vertexes[i], point, x);
			vertexes[i] = Rotation.rotateY3DAroundPoint(vertexes[i], point, y);
			vertexes[i] = Rotation.rotateZ3DAroundPoint(vertexes[i], point, z);
		}
	}

	private static void resetRotateAllPoints() {
		vertexes = cubeVertexes.clone();

		cameraAngle[0] = 0;
		cameraAngle[1] = 0;
		cameraAngle[2] = 0;
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
			if (e.getKeyCode() == KeyEvent.VK_R) {
				resetRotateAllPoints();
			}

			repaint();
		}

		public void keyReleased(KeyEvent e) {
		}   
	}
	
	private class MyMouseListener implements MouseMotionListener {
		public void mouseMoved(MouseEvent e) {
			resetRotateAllPoints();
			rotateAllPointsAroundPoint(
				(MouseInfo.getPointerInfo().getLocation().y - 400),
				(MouseInfo.getPointerInfo().getLocation().x - 400),
				0,
				cameraPos
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