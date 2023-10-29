package project.view;

import project.enums.*;

public interface View {
	void render(Content[][] grid, int[][] distances);
	void win();
	void lose();
}
