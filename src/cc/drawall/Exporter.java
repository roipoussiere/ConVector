package cc.drawall;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import javafx.scene.paint.Color;

/** The base class for all Exporter plugins.
  * Provides a common template for all output filetypes. Abstract methods should be overriden
  * to implement the details relevant to a particular filetype. */
public abstract class Exporter {

	private static final Charset ASCII = Charset.forName("US-ASCII");

	// Buffer to hold PathIterator coordinates
	private final double[] coords = new double[6];

	/** Flag indicating the generated image should be vertically reversed.
	  * Set this if the ouput filetype has the 0,0 point at the bottom-left corner. */
	protected static final int REVERSE = 1 << 0;

	/** Flag indicating that all Bézier curves should be segmented into straight lines.
	  * Set this if the output filetype can only handle straight lines. */
	protected static final int FLATTEN = 1 << 1;

	/** Flag indicating the shapes composing the image should be rearranged so as to minimize
	  * total travelled distance. Set this when targeting a physical plotter. */
	protected static final int SHORTEN = 1 << 2;

	/** Flag indicating that all layers in the image should be merged together.
	  * Set this if the output filetype doesn’t handle superposition. */
	protected static final int MERGE   = 1 << 3;

	protected static final String COMMENT = "generated by ConVector - http://convector.drawall.cc";

	// TODO: refactor flags in an enum ?
	private static final int AND_FLAGS
		= (Integer.getInteger("reverse", 1) * REVERSE)
		| (Integer.getInteger("flatten", 1) * FLATTEN)
		| (Integer.getInteger("optimize", 1) * SHORTEN)
		| (Integer.getInteger("merge", 1) * MERGE);

	private static final int OR_FLAGS
		= (Integer.getInteger("reverse", 0) * REVERSE)
		| (Integer.getInteger("flatten", 0) * FLATTEN)
		| (Integer.getInteger("optimize", 0) * SHORTEN)
		| (Integer.getInteger("merge", 0) * MERGE);

	private ByteBuffer out;
	private final String[] format;
	private final int flags;

	/** Constructs an exporter with the specified flags and segement formatting strings.
	  * @param flags a bit mask, constructed by OR-ing together the flags that apply.
	  * @param format a list of format strings for the different segment types:
	  * moveTo, lineTo, quadTo, curveTo and closePath. */
	protected Exporter(final int flags, final String... format) {
		this.flags = (flags & AND_FLAGS) | OR_FLAGS;
		this.format = format;
	}

	protected void output(final Drawing drawing, final ByteBuffer out) {
		this.out = out;
		final int flatness = (flags & FLATTEN) == 0 ? -1 : Integer.getInteger("flatness", 1);
		if ((flags & MERGE) != 0) {
			drawing.mergeLayers();
		}
		if ((flags & SHORTEN) != 0) {
			drawing.optimize();
		}
		final Rectangle2D bounds = drawing.getBounds();
		final double ratio = 65535 / Math.max(bounds.getWidth(), bounds.getHeight());
		final int reverse = (flags & REVERSE) == 0 ? 1 : -1;
		final AffineTransform normalize = new AffineTransform(ratio, 0, 0, ratio * reverse,
			0, (flags & REVERSE) * bounds.getHeight() * ratio);
		writeHeader(bounds.getWidth(), bounds.getHeight(), 1 / ratio);
		for (final Drawing.Splash splash: drawing) {
			final Color color = splash.color;
			writeColor(color.getRed(), color.getGreen(), color.getBlue());
			for (final PathIterator itr = splash.iterator(normalize, flatness);
				!itr.isDone(); itr.next()) {
				writeSegment(itr.currentSegment(coords), coords);
			}
		}
		writeFooter();
	}

	/** Writes the beginning of the output file.
	  * @param width the width of the original drawing
	  * @param height the height of the original drawing
	  * @param ratio the scaling ratio that should be applied to this drawing to return
	  * it to its original size */
	protected abstract void writeHeader(final double width, final double height,
		final double ratio);

	/** Writes the end of the output file.
	  * By default, this does nothing; subclasses should override this if the
	  * target filetype requires some form of footer. */
	protected void writeFooter() {
		/* Do nothing by default */
	}

	/** Writes the necessary instructions to change the Color of the drawing.
	  * By default, this does nothing; subclasses should override this if the
	  * target filetype supports color.
	  * @param color the new painting color */
	@SuppressWarnings("unused")
	protected void writeColor(final double red, final double green, final double blue) {
		/* Do nothing by default */
	}

	/** Writes a single segment to the output stream.
	  * By default, this formats the coordinates using one of the format strings
	  * passed to the constructor. */
	protected void writeSegment(final int type, final double[] coords) {
		int i = 0;
		for (final char chr: format[type].toCharArray()) {
			out.put(chr == '%' ? Integer.toString((int) coords[i++]).getBytes(ASCII) : new byte[] {(byte) chr});
		}
		out.put((byte) '\n');
	}

	/** A convenience method to write a formatted string to the output stream
	  * using the specified format string and arguments. */
	protected final void write(final String format, final Object... args) {
		out.put(String.format(format, args).getBytes(ASCII));
	}

	/** Writes a char value, which is comprised of two bytes, to the output stream. */
	protected final void writeChar(final int c) {
		out.putChar((char) c);
	}

	protected final void write(final byte[] data) {
		out.put(data);
	}

	/** Returns the number of bytes written to the output stream so far.
	  * @return the number of bytes written to the output stream so far */
	protected final int bytesWritten() {
		return out.position();
	}
}
