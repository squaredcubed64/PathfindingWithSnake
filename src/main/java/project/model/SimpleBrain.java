package project.model;

import project.enums.Action;
import project.enums.Content;

import java.awt.*;
import java.util.HashMap;

// Tells the snake to simply turn LEFT upon reaching an obstruction.
// Causes the snake to travel around the border of the grid
public class SimpleBrain implements Brain {
	public SimpleBrain() {

	}

	// Turns LEFT upon reaching an obstruction
	@Override
	public Action nextAction(Content[][] grid, Point snakeHeadLocation, Direction heading, Point foodLocation) {
		HashMap<Direction, Integer> distances =
				MetricCalculator.calculateDistancesToNextObstruction(grid, snakeHeadLocation);
		if (distances.get(heading) == 1) {
			return Action.LEFT;
		} else {
			return Action.FORWARD;
		}
	}

	// SimpleBrain doesn't have a neural network, so it has no nodes
	@Override
	public double[][] getNodeValues() {
		return new double[0][];
	}
}
