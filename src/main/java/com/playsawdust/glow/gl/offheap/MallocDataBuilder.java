package com.playsawdust.glow.gl.offheap;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.system.MemoryUtil;

import com.playsawdust.glow.io.DataBuilder;
import com.playsawdust.glow.offheap.Destroyable;

/**
 * Builds data directly in offheap memory using lwjgl's malloc.
 */
public class MallocDataBuilder implements DataBuilder, Destroyable {
	private static final int DEFAULT_CAPACITY = 128;
	private static final double LOAD_FACTOR = 2.0;
	
	private ByteBuffer data;
	private int dataLength;
	private int writePointer = 0;
	private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
	
	public MallocDataBuilder() {
		MemoryUtil.memAlloc(DEFAULT_CAPACITY);
		dataLength = 0;
	}
	
	@Override
	public void reset() {
		byteOrder = ByteOrder.BIG_ENDIAN;
		writePointer = 0;
	}

	@Override
	public void seek(long offset) throws IOException {
		writePointer = (int) offset;
	}

	@Override
	public void skip(long bytes) throws IOException {
		seek(writePointer + bytes);
	}

	@Override
	public void write(int value) throws IOException {
		ensureCapacity(writePointer + 1);
		data.put(writePointer, (byte)value);
		writePointer++;
		ensureDataLength(writePointer);
	}

	@Override
	public void write(long offset, int value) throws IOException {
		if (offset > 0x7FFFFFFF) throw new IndexOutOfBoundsException("Offset value '"+offset+"' out of range for arrays.");
		
		ensureCapacity((int) offset + 1);
		data.put((int) offset, (byte) value);
		ensureDataLength((int) offset + 1);
	}

	@Override
	public long length() {
		return dataLength;
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
	public byte[] toByteArray() {
		byte[] result = new byte[dataLength];
		data.get(0, result);
		return result;
	}
	
	/**
	 * Turns this builder into a DataSlice. After this method has been called, this object should be considered to be
	 * Destroyed, and further interaction with it will produce highly undesirable results. The result of this method is
	 * Destroyable, and care should be taken to destroy it before the program ends to prevent system memory leaks.
	 */
	@Override
	public MallocDataSlice toDataSlice() {
		return new MallocDataSlice(this);
	}
	
	public ByteBuffer getBackingBuffer() {
		return data;
	}
	
	private void ensureCapacity(int capacity) {
		if (capacity < 0) throw new OutOfMemoryError("Overflow in memory reservation request.");
		if (data.capacity() >= capacity) return;
		
		int newCapacity = (int) (data.capacity() * LOAD_FACTOR);
		if (newCapacity < 0 || newCapacity < capacity) {
			newCapacity = capacity;
		}
		
		data = MemoryUtil.memRealloc(data, newCapacity);
	}
	
	private void ensureDataLength(int length) {
		if (dataLength < length) dataLength = length;
	}

	@Override
	public void destroy() {
		MemoryUtil.memFree(data);
		data = null;
		dataLength = 0;
		writePointer = 0;
	}
	
}
