package project;

import org.junit.jupiter.api.*;
import project.controller.*;
import project.model.*;
import project.view.*;

import java.util.*;

public class TextSanityCheck {
	static  int WIDTH = 8;
	static final int HEIGHT = 5;
	static final int SEED = 0;
	static Controller controller;

	@BeforeEach
	void setupTest() {
		Random rand = new Random(SEED);
		Model model = new SnakeModel(WIDTH, HEIGHT, rand);
		View view = new TextView();
		controller = new SnakeController(model, view);
	}

	// Move the snake around a cell
	@Test
	void circleTest() {
		controller.render();
		controller.moveForward();
		controller.moveForward();
		for (int i = 0; i < 3; i++) {
			controller.moveLeft();
			controller.moveForward();
		}
	}

	@Test
	void foodTest() {
		controller.render();
		for (int i = 0; i < 4; i++) {
			controller.moveForward();
		}
		for (int i = 0; i < 3; i++) {
			controller.moveLeft();
			controller.moveForward();
		}
	}

	// Test that initialization won't call an error
	public static void bruteForceTest() {
		for (int i = 0; i < 1000; i++) {
			Model model = new SnakeModel(WIDTH, HEIGHT);
			View view = new TextView();
			Controller controller = new SnakeController(model, view);

			controller.render();
		}
	}
}
