package project.model;

import java.awt.*;
import java.util.*;

public interface PathfindingAlgorithm {
	// Should return null until the shortest path is found, at which point it is returned
	int[][] computeStep();
	ArrayList<Point> getReversePath();
}
