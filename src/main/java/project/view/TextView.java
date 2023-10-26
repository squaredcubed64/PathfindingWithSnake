package project.view;

import project.enums.*;
import java.util.*;

public class TextView implements View {
	// Char representations of contents
	final HashMap<Content, Character> contentToChar;
	public TextView() {
		this.contentToChar = new HashMap<>();
		this.contentToChar.put(Content.EMPTY, '.');
		this.contentToChar.put(Content.SNAKE, 'O');
		this.contentToChar.put(Content.FOOD, 'X');
	}

	@Override
	public void render(Content[][] grid) {
		// Arrange content characters in a grid shape
		StringBuilder sb = new StringBuilder();
		for (Content[] row : grid) {
			for (Content cell : row) {
				sb.append(contentToChar.get(cell));
			}
			sb.append('\n');
		}
		System.out.println(sb);
	}

	@Override
	public void win() {
		System.out.println("WIN");
	}

	@Override
	public void lose() {
		System.out.println("LOSE");
	}
}
