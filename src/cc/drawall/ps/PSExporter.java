package cc.drawall.ps;

import java.nio.ByteBuffer;

import cc.drawall.Exporter;

/** Outputs a vector to PostScript code. */
public class PSExporter extends Exporter {

	public PSExporter() {
		super("% % m", "% % l", "% % % % q", "% % % % % % c", "h");
	}

	@Override
	protected ByteBuffer header(final double width, final double height, final double ratio) {
		return format("%%!PS\n"
			+ "%% Lovingly %s\n"
			+ "%%%%BoundingBox: 0 0 %d %d\n"
			+ "/d{load def}bind def/m/moveto d/l/lineto d/c/curveto d"
			+ "/h/closepath d/f/fill d/rg/setrgbcolor d\n"
			+ "/Z{3 1 roll add 3 div}def\n/q{4 2 roll 2 mul exch 2 mul 2 copy "
			+ "currentpoint Z Z 6 2 roll 3 index 3 index Z Z 4 2 roll c}def\n"
			+ "%f %f scale\n", COMMENT, (int) (width + .5), (int) (height + .5), ratio, ratio);
	}

	@Override
	protected ByteBuffer color(final double red, final double green, final double blue) {
		return format("f %.3f %.3f %.3f rg\n", red, green, blue);
	}

	@Override
	protected ByteBuffer footer() {
		return format("f\n");
	}
}
