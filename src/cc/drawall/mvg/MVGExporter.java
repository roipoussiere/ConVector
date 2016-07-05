package cc.drawall.mvg;

import java.nio.ByteBuffer;

import cc.drawall.Exporter;

/** Outputs a vector as SVG. */
public class MVGExporter extends Exporter {

	/** Constructor. */
	public MVGExporter() {
		super("M%,%", "L%,%", "Q%,% %,%", "C%,% %,% %,%", "Z");
	}

	@Override
	protected ByteBuffer header(final double width, final double height, final double ratio) {
		return format("viewbox 0 0 %f %f\nscale %f, %f\nopacity 0 text 0,0\n'Magically %s",
			width, height, ratio, ratio, COMMENT);
	}

	@Override
	protected ByteBuffer color(final double red, final double green, final double blue) {
		return format("'\nfill '#%02x%02x%02x'\npath '", (int) (red * 255),
			(int) (green * 255), (int) (blue * 255));
	}
}
