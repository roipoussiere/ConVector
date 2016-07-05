package cc.drawall.gcode;

import java.nio.ByteBuffer;

import cc.drawall.Exporter;

/** Outputs a vector to GCode. */
public class GCodeExporter extends Exporter {

	public GCodeExporter() {
		/* MERGE | SHORTEN | FLATTEN | REVERSE */
		super("G0 X% Y%", "G1 X% Y%", "G5.1 I% J% X% Y%", "G5 I% J% P% Q% X% Y%", "");
	}

	@Override
	protected ByteBuffer header(final double width, final double height, final double ratio) {
		return format("; %dx%d\n; Neatly %s\n", (int) width, (int) height, COMMENT);
	}
}
