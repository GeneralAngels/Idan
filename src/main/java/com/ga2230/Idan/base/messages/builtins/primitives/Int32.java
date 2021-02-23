/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.messages.builtins.primitives;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.ga2230.Idan.base.messages.RosVariable;

/**
 * Represents an int (32 bit)
 */
public class Int32 extends RosVariable {
    public int data;

    /**
     * Construct the int32 variable
     * 
     * @param data The int value
     */
    public Int32(int data) {
        this.data = data;
    }

    /**
     * Construct the Int32 variable from bytes
     * 
     * @param data serialized data
     */
    public Int32(byte[] data) {
        unpack(data);
    }

    /**
     * Serializes the int
     * 
     * @return The bytes array from the int
     */
    @Override
    public byte[] pack() {
        ByteBuffer buff = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        buff.putInt(data);
        return buff.array();
    }

    /**
     * Deserializes the int
     * 
     * @param serialized The serialized int
     */
    @Override
    public void unpack(byte[] serialized) {
        this.data = ByteBuffer.wrap(serialized).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    /**
     * Returns the data
     * 
     * @return The object stringified
     */
    @Override
    public String toString() {
        return "" + data;
    }

}
