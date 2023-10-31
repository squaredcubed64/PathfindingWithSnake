package project.view;

import project.enums.*;
import project.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SnakeView extends JFrame implements View {
	private int GRID_WIDTH;
	private int GRID_HEIGHT;
	private static int CELL_WIDTH = 40;
	private static int CELL_HEIGHT = 40;
	private static final HashMap<Content, Color> CONTENT_TO_COLOR = new HashMap<>();
	static {
		CONTENT_TO_COLOR.put(Content.EMPTY, Color.WHITE);
		CONTENT_TO_COLOR.put(Content.SNAKE, Color.BLUE);
		CONTENT_TO_COLOR.put(Content.FOOD, Color.RED);
		CONTENT_TO_COLOR.put(Content.WALL, Color.BLACK);
		CONTENT_TO_COLOR.put(Content.PATH, Color.GREEN);
	}
	private static final Color BORDER_COLOR = Color.WHITE;
	private JPanel gridPanel;
	private final JPanel settingsPanel;
	private ButtonEvent lastButtonEvent;

	public SnakeView(int GRID_WIDTH, int GRID_HEIGHT) {
		this.GRID_WIDTH = GRID_WIDTH;
		this.GRID_HEIGHT = GRID_HEIGHT;
		this.lastButtonEvent = null;
		setTitle("Snake AI Visualizer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		gridPanel = new JPanel(new GridLayout(GRID_HEIGHT, GRID_WIDTH));
		settingsPanel = new JPanel(new GridLayout(4, 3));
		add(gridPanel);
		add(settingsPanel);

		initComponents(Utils.emptyGrid(GRID_WIDTH, GRID_HEIGHT));
		pack();
		setLocationRelativeTo(null); // Center the frame
		setVisible(true);
	}

	private JButton makeButton(String text, ButtonEvent event) {
		JButton button = new JButton(text);
		button.addActionListener(e -> lastButtonEvent = event);
		return button;
	}

	private void initGrid(Content[][] grid) {
		gridPanel.removeAll();
		for (int y = 0; y < GRID_HEIGHT; y++) {
			for (int x = 0; x < GRID_WIDTH; x++) {
				gridPanel.add(createGridCell(grid[y][x]));
			}
		}
	}

	private void initComponents(Content[][] grid) {
		initGrid(grid);
		// Row 1
		settingsPanel.add(new JLabel("Walls"));
		settingsPanel.add(new JLabel("Map size"));
		settingsPanel.add(new JLabel("Algorithm"));
		// Row 2
		settingsPanel.add(makeButton("Zero", ButtonEvent.ZERO));
		settingsPanel.add(makeButton("Small", ButtonEvent.SMALL));
		settingsPanel.add(makeButton("Dijkstra's", ButtonEvent.DIJKSTRA));
		// Row 3
		settingsPanel.add(makeButton("Random", ButtonEvent.RANDOM));
		settingsPanel.add(makeButton("Medium", ButtonEvent.MEDIUM));
		settingsPanel.add(makeButton("A*", ButtonEvent.ASTAR));
		// Row 4
		settingsPanel.add(makeButton("Maze", ButtonEvent.PRESET));
		settingsPanel.add(makeButton("Large", ButtonEvent.LARGE));
		settingsPanel.add(makeButton("", null));
	}

	private JPanel createGridCell(Content content) {
		JPanel cell = new JPanel();
		cell.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
		cell.setBackground(CONTENT_TO_COLOR.get(content));
		cell.setPreferredSize(new Dimension(CELL_WIDTH, CELL_HEIGHT));
		return cell;
	}

	private JPanel getGridCell(int x, int y) {
		return (JPanel) gridPanel.getComponent(y * GRID_WIDTH + x);
	}

	// Computes a color to represent a distance from the snake head, as the pathfinding algorithm is calculating
	// Darker colors represent longer distances
	// Distances of MAX_VALUE represent cells that haven't been calculated
	private Color computeColor(int distance) {
		if (distance == Integer.MAX_VALUE) {
			return Color.WHITE;
		}
		int brightnessIncrement = 400 / (GRID_WIDTH + GRID_HEIGHT);
		int brightness = Math.max(255 - brightnessIncrement * distance, 0);
		return new Color(brightness, brightness, brightness);
	}

	private void updateGrid(Content[][] grid, int[][] distances) {
		for (int y = 0; y < GRID_HEIGHT; y++) {
			for (int x = 0; x < GRID_WIDTH; x++) {
				JPanel cell = getGridCell(x, y);
				cell.setBackground(CONTENT_TO_COLOR.get(grid[y][x]));
				if (grid[y][x] == Content.EMPTY) {
					cell.setBackground(computeColor(distances[y][x]));
				}
			}
		}
	}

	@Override
	public void render(Content[][] grid, int[][] distances) {
		// In FOLLOW mode, distances is irrelevant
		if (distances == null) {
			distances = Utils.maxArray(GRID_WIDTH, GRID_HEIGHT);
		}
		updateGrid(grid, distances);
		pack();
		setLocationRelativeTo(null); // Center the frame
	}

	@Override
	public void lose() {
		System.out.println("LOSE");
	}

	@Override
	public ButtonEvent getLastButtonEvent() {
		return lastButtonEvent;
	}

	@Override
	public void resetLastButtonEvent() {
		this.lastButtonEvent = null;
	}

	public void setSize(int size) {
		GRID_WIDTH = size;
		GRID_HEIGHT = size;
		CELL_WIDTH = 400 / size;
		CELL_HEIGHT = 400 / size;
		remove(gridPanel);
		remove(settingsPanel);
		gridPanel = new JPanel(new GridLayout(GRID_HEIGHT, GRID_WIDTH));
		initGrid(Utils.emptyGrid(GRID_WIDTH, GRID_HEIGHT));
		add(gridPanel);
		add(settingsPanel);
	}
}