package uk.sliske.graphics.rendering.software;

public class Gradients {

	private float[]	texCoordX;
	private float[]	texCoordY;
	private float[]	oneOverZ;
	private float[]	depth;

	private float	texCoordXXStep;
	private float	texCoordXYStep;
	private float	texCoordYXStep;
	private float	texCoordYYStep;
	private float	oneOverZXStep;
	private float	oneOverZYStep;
	private float	depthXStep;
	private float	depthYStep;

	public Gradients(Vertex minYVert, Vertex midYVert, Vertex maxYVert) {
		texCoordX = new float[3];
		texCoordY = new float[3];
		oneOverZ = new float[3];
		depth = new float[3];

		texCoordX[0] = minYVert.getTexCoords().getX();
		texCoordX[1] = midYVert.getTexCoords().getX();
		texCoordX[2] = maxYVert.getTexCoords().getX();

		texCoordY[0] = minYVert.getTexCoords().getY();
		texCoordY[1] = midYVert.getTexCoords().getY();
		texCoordY[2] = maxYVert.getTexCoords().getY();

		oneOverZ[0] = 1.0f / minYVert.getPos().getW();
		oneOverZ[1] = 1.0f / midYVert.getPos().getW();
		oneOverZ[2] = 1.0f / maxYVert.getPos().getW();

		depth[0] = minYVert.getPos().getZ();
		depth[1] = midYVert.getPos().getZ();
		depth[2] = maxYVert.getPos().getZ();

		for (int i = 0; i < 3; i++) {
			texCoordX[i] *= oneOverZ[i];
			texCoordY[i] *= oneOverZ[i];
		}

		float oneOverDX = 1.0f / (((midYVert.getX() - maxYVert.getX()) * (minYVert.getY() - maxYVert
				.getY())) - ((minYVert.getX() - maxYVert.getX()) * (midYVert.getY() - maxYVert
				.getY())));
		float oneOverDY = -oneOverDX;

		texCoordXXStep = calcXStep(texCoordX, minYVert, midYVert, maxYVert, oneOverDX);
		texCoordXYStep = calcYStep(texCoordX, minYVert, midYVert, maxYVert, oneOverDY);

		texCoordYXStep = calcXStep(texCoordY, minYVert, midYVert, maxYVert, oneOverDX);
		texCoordYYStep = calcYStep(texCoordY, minYVert, midYVert, maxYVert, oneOverDY);

		oneOverZXStep = calcXStep(oneOverZ, minYVert, midYVert, maxYVert, oneOverDX);
		oneOverZYStep = calcYStep(oneOverZ, minYVert, midYVert, maxYVert, oneOverDY);

		depthXStep = calcXStep(depth, minYVert, midYVert, maxYVert, oneOverDX);
		depthYStep = calcYStep(depth, minYVert, midYVert, maxYVert, oneOverDY);

	}

	private float calcXStep(float[] value, Vertex minYVert, Vertex midYVert, Vertex maxYVert, float oneOverDX) {
		return (((value[1] - value[2]) * (minYVert.getY() - maxYVert.getY())) - ((value[0] - value[2]) * (midYVert
				.getY() - maxYVert.getY()))) * oneOverDX;
	}

	private float calcYStep(float[] value, Vertex minYVert, Vertex midYVert, Vertex maxYVert, float oneOverDY) {
		return (((value[1] - value[2]) * (minYVert.getX() - maxYVert.getX())) - ((value[0] - value[2]) * (midYVert
				.getX() - maxYVert.getX()))) * oneOverDY;
	}

	public float getTexCoordX(int i) {
		return texCoordX[i];
	}

	public float getTexCoordY(int i) {
		return texCoordY[i];
	}

	public float getOneOverZ(int i) {
		return oneOverZ[i];
	}
	
	public float getDepth(int i){
		return depth[i];
	}

	public float getTexCoordXXStep() {
		return texCoordXXStep;
	}

	public float getTexCoordXYStep() {
		return texCoordXYStep;
	}

	public float getTexCoordYXStep() {
		return texCoordYXStep;
	}

	public float getTexCoordYYStep() {
		return texCoordYYStep;
	}

	public float getOneOverZXStep() {
		return oneOverZXStep;
	}

	public float getOneOverZYStep() {
		return oneOverZYStep;
	}

	public float getDepthXStep() {
		return depthXStep;
	}

	public float getDepthYStep() {
		return depthYStep;
	}
}
