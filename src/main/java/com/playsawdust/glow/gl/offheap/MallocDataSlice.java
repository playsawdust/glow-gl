package com.playsawdust.glow.gl.offheap;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.system.MemoryUtil;

import com.playsawdust.glow.io.ArrayDataSlice;
import com.playsawdust.glow.io.DataSlice;
import com.playsawdust.glow.offheap.Destroyable;

/**
 * Represents an offheap region of memory made using lwjgl's malloc bindings. MUST be destroyed before the program ends
 * in order to prevent memory leaks.
 */
public class MallocDataSlice implements DataSlice, Destroyable {
	ByteBuffer data;
	protected int length;
	protected int pointer = 0;
	protected ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
	
	/**
	 * Creates a DataSlice containing length bytes of offheap memory. This memory will *not* be zeroed out and will
	 * likely contain garbage from previous activity on this computer.
	 */
	public MallocDataSlice(int length) {
		data = MemoryUtil.memAlloc(length);
		this.length = length;
		this.pointer = 0;
		this.byteOrder = ByteOrder.BIG_ENDIAN;
	}
	
	/**
	 * Creates a DataSlice whose offheap backing store contains a copy of data.
	 */
	public MallocDataSlice(byte[] data) {
		this.data = MemoryUtil.memAlloc(data.length);
		this.data.put(data);
		this.length = data.length;
		this.pointer = 0;
		this.byteOrder = ByteOrder.BIG_ENDIAN;
	}
	
	/* package_protected */ MallocDataSlice(MallocDataBuilder builder) {
		data = builder.getBackingBuffer();
		this.length = (int) builder.length();
		this.pointer = 0;
		this.byteOrder = builder.getByteOrder();
	}

	@Override
	public void seek(long offset) throws IOException {
		if (offset<0 || offset>length) throw new IOException("Out of bounds.");
		pointer = (int) offset;
	}

	@Override
	public int read() throws IOException {
		if (pointer>=length) throw new IOException("Out of bounds.");
		int value = data.get(pointer) & 0xFF;
		pointer++;
		return value;
	}

	@Override
	public int read(long offset) throws IOException {
		return data.get((int) offset) & 0xFF;
	}

	@Override
	public ByteOrder getByteOrder() {
		return byteOrder;
	}

	@Override
	public void setByteOrder(ByteOrder order) {
		this.byteOrder = order;
	}

	@Override
	public long position() {
		return pointer;
	}

	@Override
	public long length() throws IOException {
		return length;
	}

	/**
	 * Returns an ArrayDataSlice with a *copy* of the information because it's extremely risky to use the same offheap
	 * backing memory. There wouldn't be a good way to mark the object as being a piece of a Destroyable!
	 */
	@Override
	public DataSlice slice(long offset, long length) {
		if (length > Integer.MAX_VALUE) throw new IllegalArgumentException();
		if (offset<0 || offset > this.length) throw new ArrayIndexOutOfBoundsException();
		if (length<0 || offset+length > this.length) throw new ArrayIndexOutOfBoundsException();
		
		byte[] exportData = new byte[(int) length];
		data.get((int) offset, exportData);
		
		return new ArrayDataSlice(exportData);
	}

	@Override
	public void close() throws IOException {
		// Nothing
	}
	
	public ByteBuffer getBackingBuffer() {
		data.position(length);
		data.flip();
		return data;
	}

	@Override
	public void destroy() {
		MemoryUtil.memFree(data);
		length = 0;
		pointer = 0;
	}
}
