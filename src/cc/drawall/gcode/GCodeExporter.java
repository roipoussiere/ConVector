package cc.drawall.gcode;

import cc.drawall.Exporter;

/** Outputs a vector to GCode. */
public class GCodeExporter extends Exporter {

	/** Constructor. */
	public GCodeExporter() {
		super(MERGE | SHORTEN | FLATTEN | REVERSE, "G0 X% Y%", "G1 X% Y%", null, null, "");
	}

	@Override
	protected void writeHeader(final double width, final double height, final double ratio) {
		write("; %dx%d\n; Neatly %s\n", (int) width, (int) height, COMMENT);
	}
}
