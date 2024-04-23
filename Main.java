import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;


public class Main extends JFrame {

	private static final int WIDTH = 800;
	private static final int HEIGHT = 800;

    private static Vector3d cameraPos = new Vector3d(0, 0, -55);
	private static double[] cameraAngle = {0.0, 0.0, 0.0};
	private static double fov = 60.0;
	private static double scale = 20.0;
	private static double mouseSensitivity = 0.7;

	private static ArrayList<Vector3d> vertexes;
	private static ArrayList<int[]> triangles;

	Random rand = new Random();

 
    public Main() {
        super("3D Cube");
		addKeyListener(new CustomKeyListener());
 
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
		super.addMouseMotionListener( new MyMouseListener() );

		vertexes = ParseOBJ.getVertexesFromFile("./res/objects/cube.obj");
		triangles = ParseOBJ.getTrianglesFromFile("./res/objects/cube.obj");

		for (Vector3d vertex : vertexes) {
			vertex.scale(scale);
		}
    }
 
    void drawLines(Graphics g) {
        // Graphics2D g2d = (Graphics2D) g;
		sortTriangles(triangles);
 
        for (int[] triangle : triangles) {
			Vector3d vertex1 = vertexes.get(triangle[0]-1);
			Vector3d vertex2 = vertexes.get(triangle[1]-1);
			Vector3d vertex3 = vertexes.get(triangle[2]-1);

			double[] first = vertex1.perspective2D(cameraPos, cameraAngle, WIDTH, HEIGHT, fov);
			double[] second = vertex2.perspective2D(cameraPos, cameraAngle, WIDTH, HEIGHT, fov);
			double[] third = vertex3.perspective2D(cameraPos, cameraAngle, WIDTH, HEIGHT, fov);

			g.setColor(new Color(rand.nextInt(2)<<7, rand.nextInt(2)<<7, rand.nextInt(2)<<7));
			g.fillPolygon(
				new int[] {(int) first[0], (int) second[0], (int) third[0]},
				new int[] {(int) first[1], (int) second[1], (int) third[1]},
				3
			);

			// g.setColor(new Color(255, 0, 0));
			// g.drawLine((int) first[0], (int) first[1], (int) second[0], (int) second[1]);
			// g.drawLine((int) second[0], (int) second[1], (int) third[0], (int) third[1]);
			// g.drawLine((int) third[0], (int) third[1], (int) first[0], (int) first[1]);

		}
		
		
    }
 
    public void paint(Graphics g) {
        super.paint(g);
        drawLines(g);
		// g.drawString("(" + cameraAngle[0] + ", " + cameraAngle[1] + ", " + cameraAngle[2] + ")", 100, 100);
    }

	// sorts them from back to front
	private static void sortTriangles(ArrayList<int[]> tris) {
		// MUST DO:
		// Sort triangles from farthest to camera to cloest to camera
		// currently sorts by z value
		ArrayList<int[]> newTris = new ArrayList<int[]>();
		for (int i = 0; i < tris.size(); i++) {
			int[] tri = tris.get(i);
			double averageZ = (vertexes.get(tri[0]-1).getZ() + vertexes.get(tri[1]-1).getZ() + vertexes.get(tri[2]-1).getZ()) / 3.0;

			if (newTris.size() == 0) {
				newTris.add(tri);
			} else {
				for (int j = 0; j < newTris.size(); j++) {
					int[] tmpTri = newTris.get(j);
					double tmpAverageZ = (vertexes.get(tmpTri[0]-1).getZ() + vertexes.get(tmpTri[1]-1).getZ() + vertexes.get(tmpTri[2]-1).getZ()) / 3.0;
					if (averageZ > tmpAverageZ) {
						newTris.add(j, tri);
						break;
					}
				}
			}
		}

		tris = newTris;
	}

	// private static void rotateAllPoints(double x, double y, double z) {
	// 	for (Vector3d vertex : vertexes) {
	// 		vertex.rotateX(x);
	// 		vertex.rotateY(y);
	// 		vertex.rotateZ(z);
	// 	}
	// }

	// private static void rotateAllPointsAroundPoint(Vector3d point, double x, double y, double z) {
	// 	for (Vector3d vertex : vertexes) {
	// 		vertex.rotateXAroundPoint(point, x);
	// 		vertex.rotateYAroundPoint(point, y);
	// 		vertex.rotateZAroundPoint(point, z);
	// 	}
	// }

	// private static void resetRotateAllPoints() {
	// 	for (Vector3d vertex : vertexes) {
	// 		vertex.reset();
	// 		vertex.scale(scale);
	// 	}
	// }

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
			// rotation before mouse could do it
			// if (e.getKeyCode() == KeyEvent.VK_UP) {
			// 	rotateAllPoints(5, 0, 0);
			// }
			// if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			// 	rotateAllPoints(-5, 0, 0);
			// }
			// if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			// 	rotateAllPoints(0, -5, 0);
			// }
			// if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			// 	rotateAllPoints(0, 5, 0);
			// }
			// if (e.getKeyCode() == KeyEvent.VK_EQUALS) {
			// 	rotateAllPoints(0, 0, 5);
			// }
			// if (e.getKeyCode() == KeyEvent.VK_MINUS) {
			// 	rotateAllPoints(0, 0, -5);
			// }
			// if (e.getKeyCode() == KeyEvent.VK_R) {
			// 	resetRotateAllPoints();
			// }

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
    }
}