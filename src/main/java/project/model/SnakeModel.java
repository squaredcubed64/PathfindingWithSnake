package project.model;

import project.enums.*;
import static project.utils.Utils.*;

import java.awt.Point;
import java.util.*;

public class SnakeModel implements Model {
	private final int width;
	private final int height;
	private final Content[][] grid;
	private final Snake snake;
	private final Random rand;
	// Where the snake is currently facing (i.e. in which direction it lasted moved)
	private Direction heading;
	private Point foodLocation;
	private PathfindingAlgorithm algorithm;
	private Mode mode;
	// Will be followed if mode is FOLLOW
	private ArrayList<Point> reversePath;
	private static final Mode DEFAULT_MODE = Mode.CALCULATE;

	// Takes a rand to allow for deterministic results while testing
	public SnakeModel(int width, int height, Random rand) {
		this.width = width;
		this.height = height;
		this.grid = new Content[height][width];
		this.rand = rand;
		this.heading = Direction.EAST;
		this.mode = DEFAULT_MODE;

		// Set grid to all EMPTY
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				grid[y][x] = Content.EMPTY;
			}
		}

		// Generate snake
		Point snakeHeadLocation = new Point(width / 4, height / 2);
		place(Content.SNAKE, snakeHeadLocation, grid);
		snake = new Snake(snakeHeadLocation);

		// Generate food
		foodLocation = new Point(3 * width / 4, height / 2);
		place(Content.FOOD, foodLocation, grid);
	}

	// Basic constructor
	public SnakeModel(int width, int height) {
		this(width, height, new Random());
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

	// Return a deepcopy of the grid to avoid external manipulation
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

	// Returns
	private Result moveSnake(Direction direction) {
		snake.moveHead(direction);
		heading = direction;
		Point head = snake.getHead();

		// If snake hits the wall or itself, lose
		if (!inBounds(head) || get(head) == Content.SNAKE) {
			return Result.LOSE;
		}
		// If snake is at max size, win
		if (snake.size() == width * height) {
			return Result.WIN;
		}

		// If snake just ate, place the new snake head and generate new food
		if (get(head) == Content.FOOD){
			place(Content.SNAKE, head, grid);
			generateFood();
			return Result.EAT;
		}
		// If snake didn't just eat, place the new snake head and move tail
		else {
			place(Content.SNAKE, head, grid);
			Point old_snake_tail = snake.moveTail();
			place(Content.EMPTY, old_snake_tail, grid);
			return Result.NOTHING;
		}
	}

	@Override
	public Result moveForward() {
		return moveSnake(heading);
	}

	@Override
	public Result moveLeft() {
		return moveSnake(heading.left());
	}

	@Override
	public Result moveRight() {
		return moveSnake(heading.right());
	}

	@Override
	public Point getFoodLocation() {
		return (Point) foodLocation.clone();
	}

	@Override
	public Point getSnakeHeadLocation() {
		return (Point) snake.getHead().clone();
	}

	public Direction getHeading() {
		return heading;
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
	public int[][] computeStep() {
		if (mode != Mode.CALCULATE) {
			System.err.println("computeStep called when mode is not CALCULATE");
			throw new AssertionError();
		}

		int[][] distances = algorithm.computeStep();

		// If a path was found
		if (distances == null) {
			reversePath = algorithm.getReversePath();
			// Mark the cells in reversePath on the grid
			for (Point point : reversePath) {
				if (get(point) != Content.FOOD) {
					place(Content.PATH, point, grid);
				}
			}
			mode = Mode.FOLLOW;
		}

		return distances;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	public ArrayList<Point> getReversePath() {
		return reversePath;
	}

	@Override
	public void generateAlgorithm() {
		algorithm = new DijkstraAlgorithm(grid, snake.getHead());
	}
}
