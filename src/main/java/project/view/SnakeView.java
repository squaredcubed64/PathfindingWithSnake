package project.view;

import project.enums.Content;

import javax.swing.*;
import java.awt.*;

public class SnakeView extends JFrame implements View {
	@Override
	public void render(Content[][] grid) {

	}

	@Override
	public void win() {

	}

	@Override
	public void lose() {

	}
	/*private int GRID_WIDTH;
	private int GRID_HEIGHT;

	public SnakeView(int GRID_WIDTH, int GRID_HEIGHT) {
		this.GRID_WIDTH = GRID_WIDTH;
		this.GRID_HEIGHT = GRID_HEIGHT;
		setTitle("Snake AI Visualizer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(GRID_HEIGHT, GRID_WIDTH)); // Adjust the rows and columns as needed
		initComponents();
		pack();
		setLocationRelativeTo(null); // Center the frame
	}

	private void initComponents() {
		for (int i = 0; i < GRID_HEIGHT; i++) {
			for (int j = 0; j < GRID_WIDTH; j++) {
				JPanel cell = createGridCell(i, j);
				add(cell);
			}
		}
	}

	private JPanel createGridCell(int row, int col) {
		JPanel cell = new JPanel();
		cell.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add a border for visualization
		// Customize the cell's appearance or add components to represent its content
		return cell;
	}*/
}
