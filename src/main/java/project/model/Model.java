package project.model;

import project.enums.*;

import java.awt.*;

public interface Model {
	Content[][] getGrid();
	// For testing purposes
	Content get(Point point);
	Point getSnakeHeadLocation();
	Action nextAction();
	Mode getMode();
	AlgorithmStepResult computeStep();
	int getWidth();
	int getHeight();
	void generateAlgorithm();
	void doAction(Action action);
	int[][] getDistances();
	void setPathfindingAlgorithm(PathfindingAlgorithmType algorithmClass);
}
