import java.awt.Point;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.util.*;

/**
 * This class provides a GUI for solitaire games related to Elevens.
 */
public class CardGameGUI extends JFrame implements ActionListener {

	/** Height of the game frame. */
	private static int DEFAULT_HEIGHT = 800;
	/** Width of the game frame. */
	private static int DEFAULT_WIDTH = 800;

	private static int cameraPos[] = {0, 0, 0};
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
		{1, 2},
		{2, 3},
		{3, 4},
		{4, 1},
		// back face
		{5, 6},
		{6, 7},
		{7, 8},
		{8, 5},
		// connect the faces
		{1, 4},
		{2, 5},
		{3, 6},
		{4, 8}
	};

	/** The main panel containing the game components. */
	private JPanel panel;

	public CardGameGUI() {
		initDisplay();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		repaint();
	}

	/**
	 * Run the game.
	 */
	public void displayGame() {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				setVisible(true);
			}
		});
		repaint();
	}

	/**
	 * Draw the display (cards and messages).
	 */
	public void repaint() {
		for (int[] edge : cubeEdges) {
			double[] first = perspectiveStuff(cubeVertexes[edge[0] - 1]);
			double[] second = perspectiveStuff(cubeVertexes[edge[1] - 1]);

			panel.getGraphics().setColor(Color.RED);
			panel.getGraphics().drawLine((int) first[0], (int) first[1], (int) second[0], (int) second[1]);
		}

		pack();
		panel.repaint();
	}

	/**
	 * Initialize the display.
	 */
	private void initDisplay()	{
		panel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
			}
		};
		
		panel.addMouseListener(new MyMouseListener());
		setTitle("Blackjack");

		this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

		// init swing components

		pack();
		getContentPane().add(panel);
		panel.setVisible(true);
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

		double xp = (x * n) / z;
		double yp = (y * n) / z;

		// normalize the points to the center of the screen
		xp += DEFAULT_WIDTH / 2;
		yp += DEFAULT_HEIGHT / 2;

		double[] point2d = {xp, yp};
		return point2d;
	}

	public void actionPerformed(ActionEvent e) {}

	private class MyMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			// System.out.println("(" + e.getX() + ", " + e.getY() + ")");
			repaint();
		}

		public void mouseExited(MouseEvent e) {
			// System.out.println("Exited");
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
			// System.out.println("Entered");
		}

		public void mousePressed(MouseEvent e) {
		}
	}
}