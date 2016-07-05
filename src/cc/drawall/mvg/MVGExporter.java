package cc.drawall.mvg;

import cc.drawall.Exporter;

/** Outputs a vector as SVG. */
public class MVGExporter extends Exporter {

	/** Constructor. */
	public MVGExporter() {
		super(0, "M%,%", "L%,%", "Q%,% %,%", "C%,% %,% %,%", "Z");
	}

	@Override
	protected void writeHeader(final double width, final double height, final double ratio) {
		write("viewbox 0 0 %f %f\nscale %f, %f\nopacity 0 text 0,0\n'Magically %s",
			width, height, ratio, ratio, COMMENT);
	}

	@Override
	protected void writeColor(final double red, final double green, final double blue) {
		write("'\nfill '#%02x%02x%02x'\npath '", (int) (red * 255),
			(int) (green * 255), (int) (blue * 255));
	}
}
