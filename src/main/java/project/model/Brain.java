package project.model;

import project.enums.*;

import java.awt.*;

public interface Brain {
	Action nextAction(Content[][] grid, Point snakeHeadLocation, Direction heading, Point foodLocation);
	double[][] getNodeValues();
}
