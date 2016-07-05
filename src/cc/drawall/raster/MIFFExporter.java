package cc.drawall.raster;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;

import cc.drawall.Exporter;

/** Outputs a vector to PostScript code. */
public class MIFFExporter extends Exporter {

	private BufferedImage img;
	private Graphics2D g;
	private Path2D path = new Path2D.Double();

	@Override
	protected ByteBuffer header(final double width, final double height, final double ratio) {
		final int iWidth = (int) (width + .5);
		final int iHeight = (int) (height + .5);
		img = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_3BYTE_BGR);
		g = img.createGraphics();
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, iWidth, iHeight);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		return format("id=ImageMagick\ncolumns=%d rows=%d\n{Magically %s}:\n",
			iWidth, iHeight, COMMENT);
	}

	@Override
	protected ByteBuffer color(final double red, final double green, final double blue) {
		g.setColor(new Color((float) blue, (float) green, (float) red));
		return empty;
	}

	@Override
	protected ByteBuffer footer() {
		return ByteBuffer.wrap(((DataBufferByte) img.getRaster().getDataBuffer()).getData());
	}

	@Override
	protected ByteBuffer segment(final int type, final double[] coords) {
		// TODO: merge this with Drawing
		switch (type) {
		case 0:
			g.fill(path);
			path.reset();
			path.moveTo(coords[0], coords[1]);
			return empty;
		case 1:
			path.lineTo(coords[0], coords[1]);
			return empty;
		case 2:
			path.quadTo(coords[0], coords[1], coords[2], coords[3]);
			return empty;
		case 3:
			path.curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
			return empty;
		case 4:
			path.closePath();
			return empty;
		default:
			assert false;
			return empty;
		}
	}
}
