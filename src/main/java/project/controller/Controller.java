package project.controller;

import project.enums.Content;
import project.model.*;
import project.view.*;

import java.awt.*;

public interface Controller {
	void moveForward();
	void moveLeft();
	void moveRight();
	void render();
}
