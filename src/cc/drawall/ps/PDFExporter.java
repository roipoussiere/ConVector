package cc.drawall.ps;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import cc.drawall.Exporter;

/** Outputs a vector as a PDF. */
public class PDFExporter extends Exporter {

	private final List<Integer> xref = new ArrayList<>(4);

	public PDFExporter() {
		super("% % m", "% % l", "", "% % % % % % c", "h");
	}

	private ByteBuffer obj(final String content, final Object... args) {
		xref.add(bytes);
		return format(xref.size() + " 0 obj" + content + '\n', args);
	}

	@Override
	protected ByteBuffer header(final double width, final double height, final double ratio) {
		return (ByteBuffer) ByteBuffer.allocate(512)
			.put(format("%%PDF-1\n%% Painstakingly %s ConVector\n", COMMENT))
			.put(obj("<</Pages 1 0 R/Kids[2 0 R]/Count 1>>endobj"))
			.put(obj("<</Contents 3 0 R/MediaBox[0 0 %.3f %.3f]>>endobj", width, height))
			.put(obj("<</Length 4 0 R>>stream"))
			.put(format("%f 0 0 %f 0 %f cm\n", ratio, -ratio, height))
			.flip();
	}

	@Override
	protected ByteBuffer color(final double red, final double green, final double blue) {
		return format("h f %.3f %.3f %.3f rg\n", red, green, blue);
	}

	@Override
	protected ByteBuffer footer() {
		final ByteBuffer result = ByteBuffer.allocate(256)
			.put(format("f endstream\nendobj\n"))
			.put(obj(" %d endobj", bytes - xref.get(2) - 48));
		final int startxref = bytes;
		result.put(format("xref\n1 " + xref.size() + "\n"));
		for (final int pos: xref) {
			result.put(format("%010d 00000  n\n", pos));
		}
		return (ByteBuffer) result
			.put(format("trailer<</Size %d/Root 1 0 R>>", xref.size() + 1))
			.put(format("startxref " + startxref + "\n%%%%EOF"))
			.flip();
	}
}
