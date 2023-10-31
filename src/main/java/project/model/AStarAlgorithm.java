/*package project.model;

import project.enums.Content;

import java.awt.*;
import java.util.*;

import static project.utils.Utils.inBounds;

public class AStarAlgorithm implements PathfindingAlgorithm {
	private final Content[][] grid;
	private final Point source;
	private final Point destination;
	private final int[][] distances;
	private final PriorityQueue<Point> q;
	// Keeps track of the last point on the path from the source to this node
	private final HashMap<Point, Point> previous;
	private ArrayList<Point> reversePath;

	public AStarAlgorithm(Content[][] grid, Point source, Point destination) {
		this.grid = grid;
		this.source = source;
		this.destination = destination;
		this.previous = new HashMap<>();

		int width = grid[0].length;
		int height = grid.length;
		distances = new int[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				distances[y][x] = Integer.MAX_VALUE;
			}
		}
		setDistance(source, 0);

		// Should make a custom class to hold the Point with dist from source object
		q = new PriorityQueue<Point>(new Comparator<Point>() {
			public int compare(Point p1, Point p2) {
				int distanceFromSource =
			}
		});
		q.offer(source);
	}

	private Content getContent(Point point) {
		return grid[point.y][point.x];
	}

	private int getDistance(Point point) {
		return distances[point.y][point.x];
	}

	private void setDistance(Point point, int value) {
		distances[point.y][point.x] = value;
	}

	private Point add(Point point1, Point point2) {
		return new Point(point1.x + point2.x, point1.y + point2.y);
	}

	// Run one step of Dijkstra's algorithm and return the current point
	@Override
	public AlgorithmStepResult computeStep() {
		// Out of points to consider => TRAPPED
		if (q.isEmpty()) {
			return AlgorithmStepResult.TRAPPED;
		}

		Point currentPoint = q.removeFirst();

		// Compute path from food to source (backwards)
		if (getContent(currentPoint) == Content.FOOD) {
			reversePath = new ArrayList<>();

			// Iterate backwards using previous
			while (!currentPoint.equals(source)) {
				reversePath.add(currentPoint);
				currentPoint = previous.get(currentPoint);
			}

			// Return null to show a path was found
			return AlgorithmStepResult.FOUND;
		}

		int currentDistance = getDistance(currentPoint);

		// Find the shortest path to surrounding cells
		for (AbsoluteDirection absoluteDirection : AbsoluteDirection.values()) {
			Point nextPoint = add(currentPoint, absoluteDirection.jump);
			if (inBounds(grid, nextPoint) && getContent(nextPoint) != Content.SNAKE
			    && getContent(nextPoint) != Content.WALL && getDistance(nextPoint) == Integer.MAX_VALUE) {
				setDistance(nextPoint, currentDistance + 1);
				previous.put(nextPoint, currentPoint);
				q.addLast(nextPoint);
			}
		}

		return AlgorithmStepResult.CONTINUING;
	}

	@Override
	public ArrayList<Point> getReversePath() {
		return reversePath;
	}

	public int[][] getDistances() {
		return distances;
	}
}*/