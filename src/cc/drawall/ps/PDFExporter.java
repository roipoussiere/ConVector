package cc.drawall.ps;

import java.util.ArrayList;
import java.util.List;

import cc.drawall.Exporter;

/** Outputs a vector as a PDF. */
public class PDFExporter extends Exporter {

	private final List<Integer> xref = new ArrayList<>(4);

	/** Constructor. */
	public PDFExporter() {
		super(REVERSE | FLATTEN, "% % m", "% % l", "", "% % % % % % c", "h");
	}

	@Override
	protected void writeHeader(final double width, final double height, final double ratio) {
		write("%%PDF-1\n%% Painstakingly %s ConVector\n", COMMENT);
		writeObj("<</Pages 1 0 R/Kids[2 0 R]/Count 1>>endobj");
		writeObj("<</Contents 3 0 R/MediaBox[0 0 %.3f %.3f]>>endobj", width, height);
		writeObj("<</Length 4 0 R>>stream");
		write("%f 0 0 %f 0 0 cm\n", ratio, ratio);
	}

	@Override
	protected void writeColor(final double red, final double green, final double blue) {
		write("h f %.3f %.3f %.3f rg\n", red, green, blue);
	}

	@Override
	protected void writeFooter() {
		write("f endstream\nendobj\n");
		writeObj(" %d endobj", bytesWritten() - xref.get(2) - 48);
		final int startxref = bytesWritten();
		write("xref\n1 " + xref.size() + "\n");
		for (final int pos: xref) {
			write("%010d 00000  n\n", pos);
		}
		write("trailer<</Size %d/Root 1 0 R>>", xref.size() + 1);
		write("startxref " + startxref + "\n%%%%EOF");
	}

	private void writeObj(final String content, final Object... args) {
		xref.add(bytesWritten());
		write(xref.size() + " 0 obj" + content + '\n', args);
	}
}
