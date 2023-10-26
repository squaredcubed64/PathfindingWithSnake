package project.model;

import project.enums.Content;
import project.enums.Result;

import java.awt.Point;
import java.util.*;

public class SnakeModel implements Model {
	private final int WIDTH;
	private final int HEIGHT;
	private Content[][] grid;
	private Snake snake;
	private final Random rand;
	// Where the snake is currently facing (i.e. in which direction it lasted moved)
	private Direction heading;
	private Point foodLocation;

	// Takes a rand to allow for deterministic results while testing
	public SnakeModel(int WIDTH, int HEIGHT, Random rand) {
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.grid = new Content[HEIGHT][WIDTH];
		this.rand = rand;
		this.heading = Direction.EAST;

		// Set grid to all EMPTY
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				grid[y][x] = Content.EMPTY;
			}
		}

		// Generate snake
		Point snakeHeadLocation = new Point(WIDTH / 4, HEIGHT / 2);
		place(Content.SNAKE, snakeHeadLocation);
		snake = new Snake(snakeHeadLocation);

		// Generate food
		foodLocation = new Point(3 * WIDTH / 4, HEIGHT / 2);
		place(Content.FOOD, foodLocation);
	}

	// Basic constructor
	public SnakeModel(int WIDTH, int HEIGHT) {
		this(WIDTH, HEIGHT, new Random());
	}


	private Point randomPoint() {
		int x = rand.nextInt(WIDTH);
		int y = rand.nextInt(HEIGHT);
		return new Point(x, y);
	}

	private void place(Content content, Point point) {
		grid[point.y][point.x] = content;
	}

	// grid can be mutated by external code, but this shouldn't happen
	@Override
	public Content[][] getGrid() {
		return grid;
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

		place(Content.FOOD, foodLocation);
	}

	private boolean inBounds(Point point) {
		return (0 <= point.x && point.x < WIDTH &&
		        0 <= point.y && point.y < HEIGHT);
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
		if (snake.size() == WIDTH * HEIGHT) {
			return Result.WIN;
		}

		// If snake just ate, place the new snake head and generate new food
		if (get(head) == Content.FOOD){
			place(Content.SNAKE, head);
			generateFood();
		}
		// If snake didn't just eat, place the new snake head and move tail
		else {
			place(Content.SNAKE, head);
			Point old_snake_tail = snake.moveTail();
			place(Content.EMPTY, old_snake_tail);
		}
		return Result.NOTHING;
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
		return foodLocation;
	}

	@Override
	public Point getSnakeHeadLocation() {
		return snake.getHead();
	}
}
