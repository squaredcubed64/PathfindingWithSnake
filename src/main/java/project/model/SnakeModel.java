package project.model;

import project.enums.*;
import project.utils.Utils;

import static project.utils.Utils.*;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SnakeModel implements Model {
	private final int width;
	private final int height;
	private final Content[][] grid;
	private final Snake snake;
	private final Random rand;
	// Where the snake is currently facing (i.e. in which direction it lasted moved)
	private AbsoluteDirection heading;
	private Point foodLocation;
	private PathfindingAlgorithm algorithm;
	private Mode mode;
	// Will be followed if mode is FOLLOW
	private ArrayList<Point> reversePath;
	private static final Mode DEFAULT_MODE = Mode.CALCULATE;
	private int[][] distances;
	private PathfindingAlgorithmType algorithmType;

	private static Content[][] generateRandomGrid(int width, int height, double densityValue,
	                                              HashSet<Point> reservedPoints, Random rand) {
		Content[][] out = new Content[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (!reservedPoints.contains(new Point(x, y)) && rand.nextDouble() < densityValue) {
					out[y][x] = Content.WALL;
				} else {
					out[y][x] = Content.EMPTY;
				}
			}
		}
		return out;
	}

	private static Content[][] loadPresetGrid(int size) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("presets/" + size + ".txt"));
		Content[][] grid = new Content[size][size];

		String line;
		for (int y = 0; y < size; y++) {
			line = br.readLine();
			for (int x = 0; x < line.length(); x++) {
				if (line.charAt(x) == 'W') {
					grid[y][x] = Content.WALL;
				} else {
					grid[y][x] = Content.EMPTY;
				}
			}
		}
		return grid;
	}

	// Takes a rand to allow for deterministic results while testing
	public SnakeModel(int width, int height, WallType wallType, PathfindingAlgorithmType algorithmType, Random rand) {
		this.width = width;
		this.height = height;
		this.rand = rand;
		this.heading = AbsoluteDirection.EAST;
		this.mode = DEFAULT_MODE;

		// Generate snake and food
		Point snakeHeadLocation = new Point(width / 4, height / 2);
		snake = new Snake(snakeHeadLocation);
		foodLocation = new Point(3 * width / 4, height / 2);

		HashSet<Point> reservedPoints = new HashSet<>();
		reservedPoints.add(snakeHeadLocation);
		reservedPoints.add(foodLocation);

		// Generate grid with WALLs, SNAKE, and FOOD
		Content[][] generatedGrid = null;
		switch (wallType) {
			case ZERO -> generatedGrid = Utils.emptyGrid(width, height);
			case RANDOM -> generatedGrid = generateRandomGrid(width, height, .25, reservedPoints, rand);
			// Only valid for width = 10
			case PRESET -> {
				if (width == 10) {
					try {
						generatedGrid = loadPresetGrid(width);
					} catch (IOException e) {
						System.err.println("No such preset file");
						System.exit(1);
					}
				}
				else {
					System.err.println("Invalid grid size for generating preset");
					System.exit(1);
				}
			}
			default -> {
				System.err.println("Invalid wallType");
				System.exit(1);
			}
		}
		this.grid = generatedGrid;

		place(Content.SNAKE, snakeHeadLocation, grid);
		place(Content.FOOD, foodLocation, grid);

		this.algorithmType = algorithmType;
		generateAlgorithm();
	}

	// Basic constructor
	public SnakeModel(int width, int height, WallType wallType, PathfindingAlgorithmType algorithmType) {
		this(width, height, wallType, algorithmType, new Random());
	}


	private Point randomPoint() {
		int x = rand.nextInt(width);
		int y = rand.nextInt(height);
		return new Point(x, y);
	}

	/*public static boolean[][] deepCopy(boolean[][] original) {
		if (original == null) {
			return null;
		}

		final boolean[][] result = new boolean[original.length][];
		for (int i = 0; i < original.length; i++) {
			result[i] = Arrays.copyOf(original[i], original[i].length);
		}
		return result;
	}*/

	@Override
	public Content[][] getGrid() {
		return grid;

		// Return deepcopy (slow, but avoids external manipulation
		/*if (grid == null) {
			return null;
		}
		final Content[][] copy = new Content[grid.length][grid[0].length];
		for (int i = 0; i < grid.length; i++) {
			System.arraycopy(grid[i], 0, copy[i], 0, grid[i].length);
		}
		return copy;*/
	}

	public Content get(Point point) {
		return grid[point.y][point.x];
	}

	// Precondition: snake doesn't fill up the whole grid. This causes an infinite loop.
	private void generateFood() {
		foodLocation = randomPoint();

		// find a random EMPTY cell
		while (get(foodLocation) != Content.EMPTY) {
			foodLocation = randomPoint();
		}

		place(Content.FOOD, foodLocation, grid);
	}

	private boolean inBounds(Point point) {
		return (0 <= point.x && point.x < width &&
		        0 <= point.y && point.y < height);
	}

	public void doAction(Action action) {
		switch (action) {
			case LEFT -> moveSnake(heading.left());
			case FORWARD -> moveSnake(heading);
			case RIGHT -> moveSnake(heading.right());
			case WAIT -> generateAlgorithm();
			default -> {
				System.err.println("Invalid nextAction passed to doAction()");
				System.exit(1);
			}
		}
	}

	@Override
	public int[][] getDistances() {
		return distances;
	}

	@Override
	public void setPathfindingAlgorithm(PathfindingAlgorithmType algorithmClass) {
		this.algorithmType = algorithmClass;
	}

	private void moveSnake(AbsoluteDirection absoluteDirection) {
		snake.moveHead(absoluteDirection);
		heading = absoluteDirection;
		Point head = snake.getHead();

		// If snake hits the wall or itself, call an error
		// Shouldn't happen, because moveSnake() is only called when a path from head to FOOD is known
		if (!inBounds(head) || get(head) == Content.SNAKE) {
			System.err.println("Snake attempted to move out of bounds");
			System.exit(1);
		}

		// If snake just ate, place the new snake head and generate new food
		if (get(head) == Content.FOOD){
			place(Content.SNAKE, head, grid);
			generateFood();
		}
		// If snake didn't just eat, place the new snake head and move tail
		else {
			place(Content.SNAKE, head, grid);
			Point old_snake_tail = snake.moveTail();
			place(Content.EMPTY, old_snake_tail, grid);
		}
	}

	@Override
	public Point getSnakeHeadLocation() {
		return (Point) snake.getHead().clone();
	}

	private Point add(Point point1, Point point2) {
		return new Point(point1.x + point2.x, point1.y + point2.y);
	}

	@Override
	public Action nextAction() {
		if (mode != Mode.FOLLOW) {
			System.err.println("nextAction called when mode is not FOLLOW");
			throw new AssertionError();
		}
		else {
			if (reversePath.isEmpty()) {
				generateAlgorithm();
				mode = Mode.CALCULATE;
				return Action.WAIT;
			}
			Point nextPointOnPath = reversePath.remove(reversePath.size() - 1);

			Point pointAtLeftTurn = add(getSnakeHeadLocation(), heading.left().jump);
			// Check which move leads to the next cell in the path
			if (pointAtLeftTurn.equals(nextPointOnPath)) {
				return Action.LEFT;
			}

			Point pointInFront = add(getSnakeHeadLocation(), heading.jump);
			if (pointInFront.equals(nextPointOnPath)) {
				return Action.FORWARD;
			}

			Point pointAtRightTurn = add(getSnakeHeadLocation(), heading.right().jump);
			if (pointAtRightTurn.equals(nextPointOnPath)) {
				return Action.RIGHT;
			}
		}
		System.err.println("No action leads to the next point in the path");
		throw new AssertionError();
	}

	@Override
	public Mode getMode() {
		return mode;
	}

	@Override
	public AlgorithmStepResult computeStep() {
		if (mode != Mode.CALCULATE) {
			throw new AssertionError("computeStep called when mode is not CALCULATE");
		}

		AlgorithmStepResult result = algorithm.computeStep();

		// If a path was found
		if (result == AlgorithmStepResult.FOUND) {
			reversePath = algorithm.getReversePath();
			// Mark the cells in reversePath on the grid
			for (Point point : reversePath) {
				if (get(point) != Content.FOOD) {
					place(Content.PATH, point, grid);
				}
			}
			mode = Mode.FOLLOW;
		}

		distances = algorithm.getDistances();
		return result;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void generateAlgorithm() {
		switch (algorithmType) {
			case DIJKSTRA -> algorithm = new DijkstraAlgorithm(grid, snake.getHead());
			case ASTAR -> algorithm = null; //new AStarAlgorithm(grid, snake.getHead());
		}
	}
}
