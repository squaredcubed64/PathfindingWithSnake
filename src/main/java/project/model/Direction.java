package project.model;

import java.awt.Point;

public enum Direction {
	NORTH(0, -1),
	EAST(1, 0),
	SOUTH(0, 1),
	WEST(-1, 0);

	public final int dx;
	public final int dy;

	Direction(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	Direction right() {
		return switch (this) {
			case NORTH -> EAST;
			case EAST -> SOUTH;
			case SOUTH -> WEST;
			case WEST -> NORTH;
			default -> null;
		};
	}

	Direction left() {
		return switch (this) {
			case NORTH -> WEST;
			case WEST -> SOUTH;
			case SOUTH -> EAST;
			case EAST -> NORTH;
			default -> null;
		};
	}
}
