package cc.drawall.svg;

import cc.drawall.Exporter;

/** Outputs a vector as SVG. */
public class SVGExporter extends Exporter {

	/** Constructor. */
	public SVGExporter() {
		super(0, "M%,%", "L%,%", "Q%,% %,%", "C%,% %,% %,%", "Z");
	}

	@Override
	protected void writeHeader(final double width, final double height, final double ratio) {
		write("<?xml version='1.0' encoding='UTF-8' standalone='yes'?>\n"
			+ "<!-- Superbly %s -->\n"
			+ "<svg xmlns='http://www.w3.org/2000/svg' width='%f' height='%f'>\n"
			+ "<g transform='scale(%f)' stroke-width='%f' fill='none'><g id='g",
			COMMENT, width, height, ratio, 1 / ratio);
	}

	@Override
	protected void writeColor(final double red, final double green, final double blue) {
		boolean line = System.getProperty("line") != null;
		write("'/><path %s='#%02x%02x%02x' d='", line ? "stroke" : "fill",
			(int) (red * 255), (int) (green * 255), (int) (blue * 255));
	}

	@Override
	protected void writeFooter() {
		write("'/></g></svg>\n");
	}
}
