package project.model;

import project.enums.*;

public interface Brain {
	Action nextAction(Content[][] grid);
}
