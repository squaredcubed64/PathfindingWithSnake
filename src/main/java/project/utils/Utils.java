package project.utils;

import project.enums.Content;

import java.awt.*;
import java.util.*;

public class Utils {
	// Precondition: grid is rectangular
	public static boolean inBounds(Content[][] grid, Point point) {
		int height = grid.length;
		int width = grid[0].length;
		return (0 <= point.x && point.x < width && 0 <= point.y && point.y < height);
	}

	public static int[][] maxArray(int width, int height) {
		int[][] arr = new int[height][width];
		for (int[] row : arr) {
			Arrays.fill(row, Integer.MAX_VALUE);
		}
		return arr;
	}

	public static void place(Content content, Point point, Content[][] grid) {
		grid[point.y][point.x] = content;
	}
}
