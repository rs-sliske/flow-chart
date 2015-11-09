package uk.sliske.graphics.rendering.software;

import static uk.sliske.graphics.rendering.software.Maths.*;

public class Edge {

	private float	x;
	private float	xStep;
	private int		yStart;
	private int		yEnd;
	private float	texCoordX;
	private float	texCoordXStep;
	private float	texCoordY;
	private float	texCoordYStep;
	private float	oneOverZ;
	private float	oneOverZStep;
	private float	depth;
	private float	depthStep;

	public Edge(Gradients gradients, Vertex minYVert, Vertex maxYVert, int minYVertIndex) {
		yStart = ceil(minYVert.getY());
		yEnd = ceil(maxYVert.getY());

		float yDist = maxYVert.getY() - minYVert.getY();
		float xDist = maxYVert.getX() - minYVert.getX();

		if (yDist <= 0) return;

		xStep = (float) xDist / (float) yDist;
		float yPrestep = yStart - minYVert.getY();
		x = minYVert.getX() + yPrestep * xStep;

		float xPrestep = x - minYVert.getX();

		texCoordX = gradients.getTexCoordX(minYVertIndex) + (gradients.getTexCoordXXStep()) * xPrestep + gradients
				.getTexCoordXYStep() * yPrestep;
		texCoordXStep = gradients.getTexCoordXYStep() + gradients.getTexCoordXXStep() * xStep;

		texCoordY = gradients.getTexCoordY(minYVertIndex) + (gradients.getTexCoordYXStep()) * xPrestep + gradients
				.getTexCoordYYStep() * yPrestep;
		texCoordYStep = gradients.getTexCoordYYStep() + gradients.getTexCoordYXStep() * xStep;

		oneOverZ = gradients.getOneOverZ(minYVertIndex) + (gradients.getOneOverZXStep()) * xPrestep + gradients
				.getOneOverZYStep() * yPrestep;
		oneOverZStep = gradients.getOneOverZYStep() + gradients.getOneOverZXStep() * xStep;
		
		depth = gradients.getDepth(minYVertIndex) + (gradients.getDepthXStep()) * xPrestep + gradients
				.getDepthYStep() * yPrestep;
		depthStep = gradients.getDepthYStep() + gradients.getDepthXStep() * xStep;
	}

	public void step() {
		x += xStep;
		texCoordX += texCoordXStep;
		texCoordY += texCoordYStep;
		oneOverZ += oneOverZStep;
		depth += depthStep;
	}

	public float getX() {
		return x;
	}

	public int getYStart() {
		return yStart;
	}

	public int getYEnd() {
		return yEnd;
	}

	public float getTexCoordX() {
		return texCoordX;
	}

	public float getTexCoordY() {
		return texCoordY;
	}

	public float getOneOverZ() {
		return oneOverZ;
	}
	public float getDepth(){
		return depth;
	}

}
