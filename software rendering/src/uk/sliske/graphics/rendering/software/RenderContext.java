package uk.sliske.graphics.rendering.software;

import static uk.sliske.graphics.rendering.software.Maths.ceil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RenderContext extends Bitmap {

	private float[]	zBuffer;

	public RenderContext(int width, int height) {
		super(width, height);
		zBuffer = new float[width * height];
	}

	public void scanTriangle(Vertex minYVert, Vertex midYVert, Vertex maxYVert, boolean handedness, Bitmap texture) {
		Gradients gradients = new Gradients(minYVert, midYVert, maxYVert);
		Edge topToBottom = new Edge(gradients, minYVert, maxYVert, 0);
		Edge topToMiddle = new Edge(gradients, minYVert, midYVert, 0);
		Edge middleToBottom = new Edge(gradients, midYVert, maxYVert, 1);

		Edge left = topToBottom;
		Edge right = topToMiddle;
		if (handedness) {
			Edge temp = left;
			left = right;
			right = temp;
		}
		int yStart = topToMiddle.getYStart();
		int yEnd = topToMiddle.getYEnd();

		for (int j = yStart; j < yEnd; j++) {
			drawScanLine(gradients, left, right, j, texture);
			left.step();
			right.step();
		}
		left = topToBottom;
		right = middleToBottom;
		if (handedness) {
			Edge temp = left;
			left = right;
			right = temp;
		}
		yStart = middleToBottom.getYStart();
		yEnd = middleToBottom.getYEnd();

		for (int j = yStart; j < yEnd; j++) {
			drawScanLine(gradients, left, right, j, texture);
			left.step();
			right.step();
		}
	}

	public void drawScanLine(Gradients gradients, Edge left, Edge right, int j, Bitmap texture) {
		int xMin = ceil(left.getX());
		int xMax = ceil(right.getX());

		float xPrestep = xMin - left.getX();

		float xDist = right.getX() - left.getX();
		float texCoordXXStep = (right.getTexCoordX() - left.getTexCoordX()) / xDist;
		float texCoordYXStep = (right.getTexCoordY() - left.getTexCoordY()) / xDist;
		float oneOverZXStep = (right.getOneOverZ() - left.getOneOverZ()) / xDist;
		float depthXStep = (right.getDepth() - left.getDepth()) / xDist;

		float texCoordX = left.getTexCoordX() + gradients.getTexCoordXXStep() * xPrestep;
		float texCoordY = left.getTexCoordY() + gradients.getTexCoordYXStep() * xPrestep;
		float oneOverZ = left.getOneOverZ() + oneOverZXStep * xPrestep;
		float depth = left.getDepth() + depthXStep * xPrestep;

		for (int i = xMin; i < xMax; i++) {

			int index = i + j * getWidth();

			if (depth < zBuffer[index]) {
				zBuffer[index] = depth;

				float z = 1.0f / oneOverZ;
				int srcX = (int) ((texCoordX * z) * (texture.getWidth() - 1) + 0.5f);
				int srcY = (int) ((texCoordY * z) * (texture.getHeight() - 1) + 0.5f);

				copyPixel(i, j, srcX, srcY, texture);
			}
			oneOverZ += oneOverZXStep;
			texCoordX += texCoordXXStep;
			texCoordY += texCoordYXStep;
		}
	}

	public void drawTriangle(Vertex v1, Vertex v2, Vertex v3, Bitmap texture) {
		if(v1.isOnScreen()&&v2.isOnScreen()&&v3.isOnScreen()){
			fillTriangle(v1, v2, v3, texture);
			return;
		}
		
		List<Vertex> vertices = new ArrayList<>();
		List<Vertex> auxillaryList = new ArrayList<>();
		
		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);

		if (clipPolygonAxis(vertices, auxillaryList, 0))
			if (clipPolygonAxis(vertices, auxillaryList, 1))
				if (clipPolygonAxis(vertices, auxillaryList, 2)) {
					Vertex initialVertex = vertices.get(0);

					for (int i = 1; i < vertices.size() - 1; i++) {
						fillTriangle(initialVertex, vertices.get(i), vertices.get(i + 1), texture);
					}
				}
	}

	public void fillTriangle(Vertex v1, Vertex v2, Vertex v3, Bitmap texture) {
		Matrix4f screenSpaceTransform = new Matrix4f()
				.initScreenSpaceTransform(getWidth() / 2, getHeight() / 2);
		Vertex minYVert = v1.transform(screenSpaceTransform).perspectiveDivide();
		Vertex midYVert = v2.transform(screenSpaceTransform).perspectiveDivide();
		Vertex maxYVert = v3.transform(screenSpaceTransform).perspectiveDivide();

		if (maxYVert.getY() < midYVert.getY()) {
			Vertex temp = maxYVert;
			maxYVert = midYVert;
			midYVert = temp;
		}
		if (midYVert.getY() < minYVert.getY()) {
			Vertex temp = midYVert;
			midYVert = minYVert;
			minYVert = temp;
		}
		if (maxYVert.getY() < midYVert.getY()) {
			Vertex temp = maxYVert;
			maxYVert = midYVert;
			midYVert = temp;
		}

		float area = minYVert.triangleAreaTimesTwo(maxYVert, midYVert);
		boolean handedness = (area >= 0);
		scanTriangle(minYVert, midYVert, maxYVert, handedness, texture);
	}

	public void clearDepthBuffer(float amt) {
		for (int i = 0; i < zBuffer.length; i++) {
			zBuffer[i] = amt;
		}
	}

	private boolean clipPolygonAxis(List<Vertex> vertices, List<Vertex> auxillaryList, int componentIndex) {
		clipPolygonComponent(vertices, componentIndex, 1.0f, auxillaryList);
		vertices.clear();
		if (auxillaryList.isEmpty()) {
			return false;
		}

		clipPolygonComponent(auxillaryList, componentIndex, -1.0f, vertices);
		auxillaryList.clear();

		return !vertices.isEmpty();
	}

	private void clipPolygonComponent(List<Vertex> vertices, int componentIndex, float componentFactor, List<Vertex> result) {
		Vertex previousVertex = vertices.get(vertices.size() - 1);
		float previousComponent = previousVertex.get(componentIndex) * componentFactor;
		boolean previousInside = previousComponent <= previousVertex.getPos().getW();

		Iterator<Vertex> it = vertices.iterator();
		while (it.hasNext()) {
			Vertex currentVertex = it.next();
			float currentComponent = currentVertex.get(componentIndex) * componentFactor;
			boolean currentInside = currentComponent <= currentVertex.getPos().getW();

			if (currentInside ^ previousInside) {
				float lerpAmt = (previousVertex.getPos().getW() - previousComponent) / ((previousVertex
						.getPos().getW() - previousComponent) - (currentVertex.getPos().getW() - currentComponent));
				result.add(previousVertex.lerp(currentVertex, lerpAmt));
			}
			previousComponent=currentComponent;
			previousInside=currentInside;
			previousVertex=currentVertex;
		}
	}

}
