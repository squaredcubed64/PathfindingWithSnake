package project.controller;

import project.enums.*;
import project.model.*;
import project.view.*;

public class SnakeController implements Controller {
	private Model model;
	private View view;
	private Result lastResult;

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
}
