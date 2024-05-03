import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.imageio.*;


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

		vertexes = ParseOBJ.getVertexesFromFile("./res/objects/cutcube.obj");
		triangles = ParseOBJ.getTrianglesFromFile("./res/objects/cutcube.obj");

		for (Vector3d vertex : vertexes) {
			vertex.scale(scale);
		}
    }
 
    void drawLines(Graphics g) {
        // Graphics2D g2d = (Graphics2D) g;
		sortTriangles(triangles);
		// BufferedImage img = ImageIO.read( new File(".\\res\\img\\card.png") );
 
        for (int[] triangle : triangles) {
			Graphics2D g2 = (Graphics2D) g;

			Vector3d vertex1 = vertexes.get(triangle[0]-1);
			Vector3d vertex2 = vertexes.get(triangle[1]-1);
			Vector3d vertex3 = vertexes.get(triangle[2]-1);

			double[] first = vertex1.perspective2D(cameraPos, cameraAngle, WIDTH, HEIGHT, fov);
			double[] second = vertex2.perspective2D(cameraPos, cameraAngle, WIDTH, HEIGHT, fov);
			double[] third = vertex3.perspective2D(cameraPos, cameraAngle, WIDTH, HEIGHT, fov);

			g2.setColor(new Color(rand.nextInt(3)<<6, rand.nextInt(3)<<6, rand.nextInt(3)<<6));

			Path2D triShape = new Path2D.Double();
			triShape.moveTo((int) first[0], (int) first[1]);
			triShape.lineTo((int) second[0], (int) second[1]);
			triShape.lineTo((int) third[0], (int) third[1]);
			triShape.closePath();
			g2.fill(triShape);

			// BufferedImage strImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
			// Graphics2D g2d = strImg.createGraphics();
			// g2d.setClip(triShape);
			// g2d.drawImage(img, 0, 0, null);
			// g2d.dispose();

			// g.setColor(Color.BLACK);
			// g.fillPolygon(
			// 	new int[] {(int) first[0], (int) second[0], (int) third[0]},
			// 	new int[] {(int) first[1], (int) second[1], (int) third[1]},
			// 	3
			// );


			// g.setColor(new Color(255, 0, 0));
			// g.drawLine((int) first[0], (int) first[1], (int) second[0], (int) second[1]);
			// g.drawLine((int) second[0], (int) second[1], (int) third[0], (int) third[1]);
			// g.drawLine((int) third[0], (int) third[1], (int) first[0], (int) first[1]);

		}
		
		
    }
 
    public void paint(Graphics g) {
        super.paint(g);
        drawLines(g);
		g.drawString("(" + cameraPos.getX() + ", " + cameraPos.getY() + ", " + cameraPos.getZ() + ")", 100, 100);
    }

	// sorts them from back to front
	private static void sortTriangles(ArrayList<int[]> tris) {
		// MUST DO:
		// Sort triangles from farthest to camera to cloest to camera
		// currently sorts by z value
		ArrayList<int[]> newTris = new ArrayList<int[]>();
		// for (int i = 0; i < tris.size(); i++) {
		// 	int[] tri = tris.get(i);
		// 	double averageZ = (vertexes.get(tri[0]-1).getZ() + vertexes.get(tri[1]-1).getZ() + vertexes.get(tri[2]-1).getZ()) / 3.0;

		// 	if (newTris.size() == 0) {
		// 		newTris.add(tri);
		// 	} else {
		// 		for (int j = 0; j < newTris.size(); j++) {
		// 			int[] tmpTri = newTris.get(j);
		// 			double tmpAverageZ = (vertexes.get(tmpTri[0]-1).getZ() + vertexes.get(tmpTri[1]-1).getZ() + vertexes.get(tmpTri[2]-1).getZ()) / 3.0;
		// 			if (averageZ > tmpAverageZ) {
		// 				newTris.add(j, tri);
		// 				break;
		// 			}
		// 		}
		// 	}
		// }

		for (int[] tri : tris) {
			double distance1 = vertexes.get(tri[0]-1).getDistanceFromPoint(cameraPos);
			double distance2 = vertexes.get(tri[1]-1).getDistanceFromPoint(cameraPos);
			double distance3 = vertexes.get(tri[2]-1).getDistanceFromPoint(cameraPos);
			double average = (distance1 + distance2 + distance3) / 3;

			if (newTris.size() == 0) {
				newTris.add(tri);
				continue;
			}

			boolean added = false;

			for (int i = 0; i < newTris.size(); i++) {
				double otherDistance1 = vertexes.get(newTris.get(i)[0]-1).getDistanceFromPoint(cameraPos);
				double otherDistance2 = vertexes.get(newTris.get(i)[1]-1).getDistanceFromPoint(cameraPos);
				double otherDistance3 = vertexes.get(newTris.get(i)[2]-1).getDistanceFromPoint(cameraPos);
				double otherAverage = (distance1 + distance2 + distance3) / 3;

				if (average < otherAverage) {
					newTris.add(i, tri);
					added = true;
					break;
				}
			}

			if (!added) {
				newTris.add(tri);
			}
		}

		for (int i = 0; i < tris.size(); i++) {
			tris.set(i, newTris.get(tris.size() - i - 1));
		}
	}

	// private static void rotateAllPoints(double x, double y, double z) {
	// 	for (Vector3d vertex : vertexes) {
	// 		vertex.setRX(x);
	// 		vertex.setRY(y);
	// 		vertex.setRZ(z);
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