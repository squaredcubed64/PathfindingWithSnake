package project;

import project.model.*;
import project.view.*;
import project.controller.*;

public class SnakeGame {
	static int WIDTH = 8;
	static int HEIGHT = 5;

	public static void main(String[] args) {
		Model model = new SnakeModel(WIDTH, HEIGHT);
		View view = new TextView();
		Controller controller = new SnakeController(model, view);

		controller.render();
	}
}
