package cc.drawall.dov;

import java.awt.geom.PathIterator;

import cc.drawall.Exporter;

/** Outputs a vector to DOV format. */
public class DOVExporter extends Exporter {

	/** Constructor. */
	public DOVExporter() {
		super(FLATTEN | MERGE | SHORTEN);
	}

	@Override
	protected void writeHeader(final double width, final double height, final double ratio) {
		writeChar(0x2339);
		writeChar(0xFFAF);
		writeChar((int) width);
		writeChar((int) height);
	}

	@Override
	protected void writeSegment(final int type, final double[] coords) {
		switch (type) {
		case PathIterator.SEG_MOVETO:
			writeChar(0xFFFF);
			writeChar(0x0001);
			writeChar((int) coords[0]);
			writeChar((int) coords[1]);
			break;
		case PathIterator.SEG_LINETO:
			writeChar((int) coords[0]);
			writeChar((int) coords[1]);
			break;
		default:
			assert false : "Unexpected segment type";
		}
	}
}
