package edu.cmu.cs.able.eseb;

import java.io.Closeable;
import java.io.IOException;

import edu.cmu.cs.able.typelib.type.DataValue;

/**
 * Output stream that writes data types.
 */
public interface DataTypeOutputStream extends Closeable {
	/**
	 * Writes the data value to the output stream.
	 * @param dt the data type
	 * @throws IOException failed to write the data type; the stream is
	 * probably corrupted and should no longer be used
	 */
	public void write(DataValue dt) throws IOException;
	
	/**
	 * Writes the data value to the output stream.
	 * @param bd the datum to write
	 * @throws IOException failed to write the data type; the stream is
	 * probably corrupted and should no longer be used
	 */
	public void write(BusData bd) throws IOException;
	
	/**
	 * Closes this stream and the underlying stream.
	 * @throws IOException failed to close the stream
	 */
	@Override
	public void close() throws IOException;
}