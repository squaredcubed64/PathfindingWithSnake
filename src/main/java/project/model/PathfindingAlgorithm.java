package project.model;

import java.awt.*;
import java.util.*;

public interface PathfindingAlgorithm {
	// Should return null until the shortest path is found, at which point it is returned
	AlgorithmStepResult computeStep();
	ArrayList<Point> getReversePath();
	int[][] getDistances();
}
