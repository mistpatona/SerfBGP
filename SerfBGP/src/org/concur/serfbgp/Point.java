package org.concur.serfbgp;

public class Point {
	final float x, y;

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	Point(float x_, float y_) {
		x = x_;
		y = y_;
	}

	Point(Point p) {
		x = p.x;
		y = p.y;
	}

	public float distanceTo(Point p) {
		float dx = x - p.x;
		float dy = y - p.y;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	public String toString() {
		return "Point(" + x + ", " + y + ")";
	}
}
