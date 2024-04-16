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
		// do stuff here
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

	public void actionPerformed(ActionEvent e) {
		// if (e.getSource().equals(button)) {
		// 	getRootPane().setDefaultButton(button);
		// 	repaint();
		// } else {
		// 	return;
		// }
	}

	private class MyMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			// System.out.println("(" + e.getX() + ", " + e.getY() + ")");
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