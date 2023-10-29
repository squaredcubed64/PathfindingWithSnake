package project.controller;

import project.enums.*;
import project.model.*;
import static project.utils.Utils.*;
import project.view.*;

import java.awt.*;
import java.util.*;

public class SnakeController implements Controller {
	private final Model model;
	private final View view;
	private Result lastResult;
	private static final int MOVE_DELAY_MILLISECONDS = 125;
	private final int[][] MAX_DISTANCES;
	private Content[][] gridWithPath;
	private ArrayList<Point> reversePath;
	private Mode mode;

	public SnakeController(Model model, View view) {
		this.model = model;
		model.generateAlgorithm();
		this.view = view;
		this.lastResult = null;
		this.MAX_DISTANCES = maxArray(model.getWidth(), model.getHeight());
		this.mode = Mode.CALCULATE;
	}

	// Send the result to view, so it can decide what to do
	private void interpretResult(Result result) {
		switch (result) {
			case WIN -> view.win();
			case LOSE -> view.lose();
			case EAT, NOTHING -> view.render(model.getGrid(), MAX_DISTANCES);
			default -> {
				System.err.println("result in run() must be WIN, LOSE, EAT, or NOTHING");
				System.exit(1);
			}
		}
	}

	@Override
	public void moveForward() {
		lastResult = model.moveForward();
		interpretResult(lastResult);
	}

	@Override
	public void moveLeft() {
		lastResult = model.moveLeft();
		interpretResult(lastResult);
	}

	@Override
	public void moveRight() {
		lastResult = model.moveRight();
		interpretResult(lastResult);
	}

	private Content[][] copyOf(Content[][] grid) {
		if (grid == null) {
			return null;
		}
		final Content[][] copy = new Content[grid.length][grid[0].length];
		for (int i = 0; i < grid.length; i++) {
			System.arraycopy(grid[i], 0, copy[i], 0, grid[i].length);
		}
		return copy;
	}

	class StepForward extends TimerTask {
		public void run() {
			if (model.getMode() == Mode.FOLLOW) {
				Action nextAction = model.nextAction();
				Result result = null;
				switch (nextAction) {
					case LEFT -> result = model.moveLeft();
					case RIGHT -> result = model.moveRight();
					case FORWARD -> result = model.moveForward();
					case WAIT -> {
						model.generateAlgorithm();
						mode = Mode.CALCULATE;
						result = Result.EAT;
					}
					default -> {
						System.err.println("Invalid nextAction passed to run()");
						System.exit(1);
					}
				}

				// Have the view act based on the result
				interpretResult(result);
			}
			else {
				int[][] distances = model.computeStep();
				if (distances != null) {
					view.render(model.getGrid(), distances);
				} else {
					mode = Mode.FOLLOW;
					view.render(model.getGrid(), MAX_DISTANCES);
				}
			}
		}
	}

	@Override
	public void startGame() {
		Timer timer = new Timer();
		timer.schedule(new StepForward(), 0, MOVE_DELAY_MILLISECONDS);
	}
}
