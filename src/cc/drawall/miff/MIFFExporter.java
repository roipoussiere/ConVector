package cc.drawall.miff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;

import cc.drawall.Drawing;
import cc.drawall.Exporter;

/** Outputs a vector to PostScript code. */
public class MIFFExporter extends Exporter {

	private BufferedImage img;
	private Graphics2D g;

	public MIFFExporter() {
		super(0);
	}

	@Override
	protected void output(final Drawing drawing, final ByteBuffer out) {
		final Rectangle2D bounds = drawing.getBounds();
		final int width = (int) (bounds.getWidth() + .5);
		final int height = (int) (bounds.getHeight() + .5);
		out.put(("id=ImageMagick\ncolumns=" + width + " rows=" + height
			+ "\n{Magically " + COMMENT + "}:\n").getBytes());
		img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		g = img.createGraphics();
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, width, height);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

		for (final Drawing.Splash splash: drawing) {
			final javafx.scene.paint.Color color = splash.color;
			g.setColor(new Color((float) color.getBlue(), (float) color.getGreen(), (float) color.getRed()));
			g.fill(splash.shape);
		}

		byte[] buf = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		out.put(buf);
	}

	@Override
	protected void writeHeader(final double width, final double height, final double ratio) {
		// We have to get the unscaled width and height directly from the drawing
	}
}
