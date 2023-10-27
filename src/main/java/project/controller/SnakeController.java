package project.controller;

import project.enums.*;
import project.model.*;
import project.view.*;

import java.util.*;

public class SnakeController implements Controller {
	private Model model;
	private View view;
	private Result lastResult;
	private static final int MOVE_DELAY_MILLISECONDS = 1000;

	public SnakeController(Model model, View view) {
		this.model = model;
		this.view = view;
		this.lastResult = null;
	}

	// Send the result to view so it can decide what to do
	private void interpretResult(Result result) {
		switch (result) {
			case WIN -> view.win();
			case LOSE -> view.lose();
			case NOTHING -> view.render(model.getGrid());
			default -> {
				System.err.println("lastResult is null");
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

	@Override
	public void render() {
		view.render(model.getGrid());
	}

	class StepForward extends TimerTask {
		public void run() {
			Action nextMove = model.nextAction();
			Result result = null;
			switch (nextMove) {
				case LEFT -> {
					result = model.moveLeft();
				}
				case RIGHT -> {
					result = model.moveRight();
				}
				case NOTHING -> {
					result = model.moveForward();
				}
				default -> {
					System.err.println("Invalid nextMove passed to run()");
					System.exit(1);
				}
			}
			// Have the view interpret the result
			switch (result) {
				case WIN -> view.win();
				case LOSE -> view.lose();
				case NOTHING -> view.render(model.getGrid());
				default -> {
					System.err.println("result in run() must be WIN, LOSE, or NOTHING");
					System.exit(1);
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
