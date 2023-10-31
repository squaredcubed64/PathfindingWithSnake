package project.view;

import project.enums.*;

import java.awt.*;

public interface View {
	void render(Content[][] grid, int[][] distances);
	void lose();
	ButtonEvent getLastButtonEvent();
	void resetLastButtonEvent();
	void setSize(int size);
}
