package project.model;

import project.enums.*;

import java.util.HashMap;
import java.awt.Point;

import static project.utils.Utils.*;

public class MetricCalculator {
	// Calculates the distance to the nearest obstruction in each cardinal direction
	// A distance of 1 means that it is directly next to the snake in that direction
	public static HashMap<AbsoluteDirection, Integer> calculateDistancesToNextObstruction(Content[][] grid,
	                                                                                      Point point) {
		HashMap<AbsoluteDirection, Integer> distances = new HashMap<>();
		for (AbsoluteDirection absoluteDirection : AbsoluteDirection.values()) {
			int distance = 1;
			Point cellToTest = new Point(point.x + absoluteDirection.dx, point.y + absoluteDirection.dy);
			// Test if cellToTest is an obstruction
			while (inBounds(grid, cellToTest) && grid[cellToTest.y][cellToTest.x] != Content.SNAKE) {
				cellToTest.translate(absoluteDirection.dx, absoluteDirection.dy);
				distance++;
			}
			distances.put(absoluteDirection, distance);
		}
		return distances;
	}
}