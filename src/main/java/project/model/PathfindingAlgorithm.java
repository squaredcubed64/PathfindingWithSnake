package project.model;

import project.enums.*;

import java.awt.*;

public interface PathfindingAlgorithm {
	Point[] doStep(Content[][] grid, Point snakeHeadLocation);
}
