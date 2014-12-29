/*
 * This file is part of DraWall.
 * DraWall is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * DraWall is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of the GNU
 * General Public License along with DraWall. If not, see <http://www.gnu.org/licenses/>.
 * © 2012–2014 Nathanaël Jourdane
 * © 2014 Victor Adam
 */

package cc.drawall;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFExporter extends Exporter {

	private final List<Integer> xref = new ArrayList<>(4);

	public PDFExporter() {
		super("% % m", "% % l", null, "% % % % % % c", "h");
	}

	@Override
	protected AffineTransform writeHeader(final Drawing drawing) throws IOException {
		final Rectangle bounds = drawing.getBounds();
		out.writeBytes("%PDF-1\n");
		writeObj("<</Pages 1 0 R/Kids[2 0 R]/Count 1>>");
		writeObj("<</Contents 3 0 R/MediaBox[0 0 " + bounds.width + " " + bounds.height + "]>>");
		writeObj("<</Length 4 0 R>>stream\n%");
		return new AffineTransform(1, 0, 0, -1, -bounds.x, bounds.y + bounds.height);
	}

	@Override
	protected void writeColor(final Color color) throws IOException {
		final float[] rgb = color.getRGBColorComponents(null);
		writeFormat("f %.3f %.3f %.3f rg%n", rgb[0], rgb[1], rgb[2]);
	}

	@Override
	protected void writeFooter() throws IOException {
		out.writeBytes("f endstream\nendobj\n");
		writeObj(" " + (out.size() - xref.get(2) - 48) + " "); // Magic!
		final int startxref = out.size();
		out.writeBytes("xref\n1 " + xref.size() + "\n");
		for (final int pos: xref) {
			out.writeBytes(String.format("%010d 00000  n%n", pos));
		}
		writeFormat("trailer<</Size %d/Root 1 0 R>>", xref.size() + 1);

		out.writeBytes("startxref " + startxref + "\n%%EOF");
	}

	private void writeObj(final String content) throws IOException {
		xref.add(out.size());
		out.writeBytes(xref.size() + " 0 obj" + content + "endobj\n");
	}
}
