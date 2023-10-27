package project.model;

import project.model.*;
import project.enums.*;

import java.util.HashMap;
import java.awt.Point;

public class MetricCalculator {
	// Calculates the distance to the nearest obstruction in each cardinal direction
	// A distance of 1 means that it is directly next to the snake in that direction
	public static HashMap<Direction, Integer> calculateDistancesToNextObstruction(Content[][] grid,
	                                                                              Point point) {
		HashMap<Direction, Integer> distances = new HashMap<>();
		for (Direction direction : Direction.values()) {
			int distance = 1;
			Point cellToTest = new Point(point.x + direction.dx, point.y + direction.dy);
			// Test if cellToTest is an obstruction
			while (inBounds(grid, cellToTest) && grid[cellToTest.y][cellToTest.x] != Content.SNAKE) {
				cellToTest.translate(direction.dx, direction.dy);
				distance++;
			}
			distances.put(direction, distance);
		}
		return distances;
	}

	// Precondition: grid is rectangular
	private static boolean inBounds(Content[][] grid, Point point) {
		int height = grid.length;
		int width = grid[0].length;
		return (0 <= point.x && point.x < width && 0 <= point.y && point.y < height);
	}
}
