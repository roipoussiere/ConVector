package cc.drawall;

import java.nio.channels.ReadableByteChannel;

/** Base interface for plugins. */
@FunctionalInterface
public interface Importer {
	/** Interprets bytes read from `input` and draws on `output`. Each implementing
	  * class is a way to interpret bytes as a vector image.
	  * @param input the channel in which to read the data to be parsed
	  * @return the resulting vector */
	void process(final ReadableByteChannel input, final Output output);
}
