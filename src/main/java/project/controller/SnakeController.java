package project.controller;

import project.enums.PathfindingAlgorithmType;
import project.enums.WallType;
import project.model.*;
import static project.utils.Utils.*;
import project.view.*;

import java.util.*;

public class SnakeController implements Controller {
	private Model model;
	private final View view;
	private static final int MOVE_DELAY_MILLISECONDS = 60;
	private int size;
	private WallType wallType;
	private PathfindingAlgorithmType algorithmType;

	public SnakeController(Model model, View view) {
		this.model = model;
		this.view = view;
		this.size = model.getWidth();
		this.wallType = WallType.ZERO;
		this.algorithmType = PathfindingAlgorithmType.DIJKSTRA;
	}

	private void setWallTypeAndResetModel(WallType wallType) {
		this.wallType = wallType;
		this.model = new SnakeModel(size, size, wallType, algorithmType);
	}

	private void setSizeAndResetModelAndView(int size) {
		this.size = size;
		this.model = new SnakeModel(size, size, wallType, algorithmType);
		this.view.setSize(size);
	}

	private void setPathfindingAlgorithmAndUpdateModel(PathfindingAlgorithmType algorithmType) {
		this.algorithmType = algorithmType;
		model.setPathfindingAlgorithm(algorithmType);
	}

	class StepForward extends TimerTask {
		public void run() {
			if (view.getLastButtonEvent() != null) {
				switch (view.getLastButtonEvent()) {
					case ZERO -> setWallTypeAndResetModel(WallType.ZERO);
					case RANDOM -> setWallTypeAndResetModel(WallType.RANDOM);
					case PRESET -> setWallTypeAndResetModel(WallType.PRESET);
					case SMALL -> setSizeAndResetModelAndView(10);
					case MEDIUM -> setSizeAndResetModelAndView(20);
					case LARGE -> setSizeAndResetModelAndView(30);
					case DIJKSTRA -> setPathfindingAlgorithmAndUpdateModel(PathfindingAlgorithmType.DIJKSTRA);
					case ASTAR -> setPathfindingAlgorithmAndUpdateModel(PathfindingAlgorithmType.ASTAR);
				}
				view.resetLastButtonEvent();
			}
			switch (model.getMode()) {
				case FOLLOW -> {
					model.doAction(model.nextAction());
					view.render(model.getGrid(), null);
				}
				case CALCULATE -> {
					AlgorithmStepResult result = model.computeStep();
					switch (result) {
						case CONTINUING -> view.render(model.getGrid(), model.getDistances());
						case FOUND -> view.render(model.getGrid(), null);
						case TRAPPED -> view.lose();
					}
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
