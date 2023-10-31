package project;

import project.enums.PathfindingAlgorithmType;
import project.enums.WallType;
import project.model.*;
import project.view.*;
import project.controller.*;

public class SnakeGame {
	static int WIDTH = 10;
	static int HEIGHT = 10;

	public static void main(String[] args) {
		Model model = new SnakeModel(WIDTH, HEIGHT, WallType.ZERO, PathfindingAlgorithmType.DIJKSTRA);
		View view = new SnakeView(WIDTH, HEIGHT);
		Controller controller = new SnakeController(model, view);

		controller.startGame();
	}
}
