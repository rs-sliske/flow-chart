package uk.sliske.graphics.rendering.software;

public class Offset {

	public int	x, y;

	public Offset(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public Offset set(final Offset newPos) {
		this.x = newPos.x;
		this.y = newPos.y;
		return this;
	}

	public Offset offset(final int x, final int y) {
		return new Offset(this.x + x, this.y + y);
	}

}
