package project.view;

import project.enums.*;

public interface View {
	void render(Content[][] grid);
	void win();
	void lose();
}
