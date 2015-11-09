package uk.sliske.graphics.rendering.software;

public class Vertex {

	private Vector4f	pos;

	private Vector4f	texCoords;

	public Vertex(Vector4f pos, Vector4f texCoords) {
		this.pos = pos;
		this.texCoords = texCoords;
	}

	public float getX() {
		return pos.getX();
	}

	public float getY() {
		return pos.getY();
	}

	public float getZ() {
		return pos.getZ();
	}

	public Vertex perspectiveDivide() {
		return new Vertex(new Vector4f(pos.getX() / pos.getW(), pos.getY() / pos.getW(),
				pos.getZ() / pos.getW(), pos.getW()), texCoords);
	}

	public Vertex transform(Matrix4f transform) {
		return new Vertex(transform.transform(pos), texCoords);
	}

	public float triangleAreaTimesTwo(Vertex b, Vertex c) {
		float x1 = b.getX() - getX();
		float y1 = b.getY() - getY();

		float x2 = c.getX() - getX();
		float y2 = c.getY() - getY();
		return (x1 * y2 - x2 * y1);
	}

	public Vector4f getTexCoords() {
		return texCoords;
	}

	public Vector4f getPos() {
		return pos;
	}

	public Vertex lerp(Vertex r, float lerpAmt) {
		Vector4f newPos = pos.lerp(r.pos, lerpAmt);
		Vector4f newTex = texCoords.lerp(r.texCoords, lerpAmt);
		return new Vertex(newPos, newTex);
	}

	public float get(int index) {
		switch (index) {
			case 0:
				return pos.getX();
			case 1:
				return pos.getY();
			case 2:
				return pos.getZ();
			case 3:
				return pos.getW();
			default:
				throw new IndexOutOfBoundsException();
		}
	}

	public boolean isOnScreen() {
		if (!(Math.abs(pos.getX()) <= Math.abs(pos.getW()))) return false;
		if (!(Math.abs(pos.getY()) <= Math.abs(pos.getW()))) return false;
		if (!(Math.abs(pos.getZ()) <= Math.abs(pos.getW()))) return false;
		return true;
	}
}
