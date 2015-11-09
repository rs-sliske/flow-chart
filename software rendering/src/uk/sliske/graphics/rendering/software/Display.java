package uk.sliske.graphics.rendering.software;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Display extends Canvas {
	private static final long		serialVersionUID	= 1L;
	
	private final RenderContext		frameBuffer;
	private final BufferedImage		displayImage;
	private final byte[]			displayComponents;
	private  BufferStrategy	bufferStrategy;
	private  Graphics			graphics;

	public Display(final int width, final int height) {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);

		frameBuffer = new RenderContext(width, height);
		displayImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		displayComponents = ((DataBufferByte) (displayImage.getRaster().getDataBuffer())).getData();

		
		createBufferStrategy(1);
		bufferStrategy = getBufferStrategy();
		graphics = bufferStrategy.getDrawGraphics();

	}

	public void swapBuffers() {
		if(graphics == null){
			createBufferStrategy(1);
			bufferStrategy = getBufferStrategy();
			graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
			return;
		}
		frameBuffer.copyToByteArray(displayComponents);
		graphics.drawImage(displayImage, 0, 0, frameBuffer.getWidth(), frameBuffer.getHeight(), null);
		bufferStrategy.show();
	}

	public RenderContext getFrameBuffer() {
		return frameBuffer;
	}
	
	
	public void setGraphics(Graphics2D g){
		this.graphics = g;
	}
}
