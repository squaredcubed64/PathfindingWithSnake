package project.model;

import project.enums.*;

import java.awt.*;
import java.util.*;

public interface Model {
	Content[][] getGrid();
	// For testing purposes
	Content get(Point point);
	Result moveForward();
	Result moveLeft();
	Result moveRight();
	Point getFoodLocation();
	Point getSnakeHeadLocation();
	Direction getHeading();
	Action nextAction();
	Mode getMode();
	int[][] computeStep();
	int getWidth();
	int getHeight();
	ArrayList<Point> getReversePath();
	void generateAlgorithm();
}
