/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.messages.builtins.primitives;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.ga2230.Idan.base.messages.RosVariable;

/**
 * Represents a long (64 bit) or int64
 */
public class Int64 extends RosVariable {
    public long data;

    /**
     * Construct the int64 variable
     * 
     * @param data The long value
     */
    public Int64(long data) {
        this.data = data;
    }

    /**
     * Construct the Int64 variable from bytes
     * 
     * @param data serialized data
     */
    public Int64(byte[] data) {
        unpack(data);
    }

    /**
     * Serializes the long
     * 
     * @return The bytes array from the long
     */
    @Override
    public byte[] pack() {
        ByteBuffer buff = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        buff.putLong(data);
        return buff.array();
    }

    /**
     * Deserializes the long
     * 
     * @param serialized The serialized long
     */
    @Override
    public void unpack(byte[] serialized) {
        this.data = ByteBuffer.wrap(serialized).order(ByteOrder.LITTLE_ENDIAN).getLong();
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
