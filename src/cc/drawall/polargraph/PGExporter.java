package cc.drawall.polargraph;

import java.nio.ByteBuffer;

import cc.drawall.Exporter;

/** Outputs a vector to Polargraph code. */
public class PGExporter extends Exporter {
	private static final int WIDTH = Integer.getInteger("polargraph.width", 7500);

	@Override
	protected ByteBuffer segment(final int type, final double[] coords) {
		final double x = coords[0], y = coords[1];
		return format(
			type == 0 ? "C14,END\nC17,%d,%d,END\nC13,END\n" :  "C17,%d,%d,END\n",
			(int) Math.sqrt(x * x + y * y),
			(int) Math.sqrt((WIDTH - x) * (WIDTH - x) + y * y));
	}
}
