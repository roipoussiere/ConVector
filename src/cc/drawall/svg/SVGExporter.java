package cc.drawall.svg;

import java.nio.ByteBuffer;

import cc.drawall.Exporter;

/** Outputs a vector as SVG. */
public class SVGExporter extends Exporter {

	public SVGExporter() {
		super("M%,%", "L%,%", "Q%,% %,%", "C%,% %,% %,%", "Z");
	}

	@Override
	protected ByteBuffer header(final double width, final double height, final double ratio) {
		return format("<?xml version='1.0' encoding='UTF-8' standalone='yes'?>\n"
			+ "<!-- Superbly %s -->\n"
			+ "<svg xmlns='http://www.w3.org/2000/svg' width='%f' height='%f'>\n"
			+ "<g transform='scale(%f)' stroke-width='%f' fill='none' "
			+ "stroke='black' stroke-linecap='round' stroke-linejoin='round'><path d='",
			COMMENT, width, height, ratio, .5 / ratio);
	}

	@Override
	protected ByteBuffer color(final double red, final double green, final double blue) {
		return format("'/><path stroke='none' fill='#%02x%02x%02x' d='",
			(int) (red * 255), (int) (green * 255), (int) (blue * 255));
	}

	@Override
	protected ByteBuffer footer() {
		return format("'/></g></svg>\n");
	}
}
