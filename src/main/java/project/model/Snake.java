package project.model;

import java.awt.Point;
import java.util.*;

public class Snake {
	LinkedList<Point> body;

	public Snake(Point head) {
		this.body = new LinkedList<Point>();
		this.body.add(head);
	}

	public void moveHead(AbsoluteDirection absoluteDirection) {
		Point head = body.getLast();
		Point newHead = new Point(head.x + absoluteDirection.dx, head.y + absoluteDirection.dy);
		body.add(newHead);
	}

	public Point moveTail() {
		return body.removeFirst();
	}

	// Note: doesn't copy
	public LinkedList<Point> getBody() {
		return (LinkedList<Point>) body.clone();
	}

	// Note: doesn't copy
	public Point getTail() {
		return (Point) body.getFirst().clone();
	}

	public Point getHead() {
		return (Point) body.getLast().clone();
	}

	public int size() {
		return body.size();
	}
}