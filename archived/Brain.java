package project.model;

import project.enums.*;

import java.awt.*;

public interface Brain {
	Action nextAction(Content[][] grid, Point snakeHeadLocation, AbsoluteDirection heading, Point foodLocation);
	double[][] getNodeValues();
}
