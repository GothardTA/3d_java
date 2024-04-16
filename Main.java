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
        super("Lines Drawing Demo");
		addKeyListener(new CustomKeyListener());
 
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
 
    void drawLines(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
 
        for (int[] edge : cubeEdges) {
			double[] first = perspectiveStuff(cubeVertexes[edge[0]]);
			double[] second = perspectiveStuff(cubeVertexes[edge[1]]);

			g.setColor(Color.RED);
			g.drawLine((int) first[0], (int) first[1], (int) second[0], (int) second[1]);
		}
 
    }
 
    public void paint(Graphics g) {
        super.paint(g);
        drawLines(g);
    }

    private double[] perspectiveStuff(double[] point3d) {
		if (point3d.length != 3) {
			return null;
		}

		double x = point3d[0];
		double y = point3d[1];
		double z = point3d[2];

		x *= scale;
		y *= scale;
		z *= scale;

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
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				cameraPos[1] += -5;
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				cameraPos[1] += 5;
			}

			repaint();
		}

		public void keyReleased(KeyEvent e) {
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