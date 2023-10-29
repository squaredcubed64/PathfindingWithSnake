package project.view;

import project.enums.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SnakeView extends JFrame implements View {
	private final int GRID_WIDTH;
	private final int GRID_HEIGHT;
	private static final int WINDOW_WIDTH = 2000;
	private static final int WINDOW_HEIGHT = 2000;
	private static final int CELL_WIDTH = 100;
	private static final int CELL_HEIGHT = 100;
	private static final HashMap<Content, Color> CONTENT_TO_COLOR = new HashMap<>();
	static {
		CONTENT_TO_COLOR.put(Content.EMPTY, Color.WHITE);
		CONTENT_TO_COLOR.put(Content.SNAKE, Color.BLACK);
		CONTENT_TO_COLOR.put(Content.FOOD, Color.RED);
	}
	private static final Color BORDER_COLOR = Color.BLACK;

	public SnakeView(int GRID_WIDTH, int GRID_HEIGHT) {
		this.GRID_WIDTH = GRID_WIDTH;
		this.GRID_HEIGHT = GRID_HEIGHT;
		setTitle("Snake AI Visualizer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLayout(new GridLayout(GRID_HEIGHT, GRID_WIDTH));

		final Content[][] EMPTY_GRID = new Content[GRID_HEIGHT][GRID_WIDTH];
		for (Content[] row: EMPTY_GRID)
			Arrays.fill(row, Content.EMPTY);

		initComponents(EMPTY_GRID);
		pack();
		setLocationRelativeTo(null); // Center the frame
		setVisible(true);
	}

	private void initComponents(Content[][] grid) {
		for (int y = 0; y < GRID_HEIGHT; y++) {
			for (int x = 0; x < GRID_WIDTH; x++) {
				JPanel cell = createGridCell(grid[y][x]);
				add(cell);
			}
		}
	}

	private JPanel createGridCell(Content content) {
		JPanel cell = new JPanel();
		cell.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
		cell.setBackground(CONTENT_TO_COLOR.get(content));
		cell.setSize(CELL_WIDTH, CELL_HEIGHT);
		return cell;
	}

	public JPanel getComponent(int x, int y) {
		return (JPanel) getContentPane().getComponent(y * GRID_WIDTH + x);
	}

	private void updateComponents(Content[][] grid) {
		for (int y = 0; y < GRID_HEIGHT; y++) {
			for (int x = 0; x < GRID_WIDTH; x++) {
				JPanel cell = getComponent(x, y);
				cell.setBackground(CONTENT_TO_COLOR.get(grid[y][x]));
			}
		}
	}

	@Override
	public void render(Content[][] grid) {
		updateComponents(grid);
		pack();
		setLocationRelativeTo(null); // Center the frame
	}

	@Override
	public void win() {
		System.out.println("WIN");
	}

	@Override
	public void lose() {
		System.out.println("LOSE");
	}
}
