/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.messages.builtins.primitives;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.ga2230.Idan.base.messages.RosVariable;

/**
 * Represents a short or int16
 */
public class Short16 extends RosVariable {
    public short data;

    /**
     * Construct the Short16 variable
     * 
     * @param data The short value
     */
    public Short16(short data) {
        this.data = data;
    }

    /**
     * Construct the Short16 variable from bytes
     * 
     * @param data serialized data
     */
    public Short16(byte[] data) {
        unpack(data);
    }

    /**
     * Serializes the short
     * 
     * @return The bytes array from the short
     */
    @Override
    public byte[] pack() {
        ByteBuffer buff = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        buff.putShort(data);
        return buff.array();
    }

    /**
     * Deserializes the short
     * 
     * @param serialized The serialized short
     */
    @Override
    public void unpack(byte[] serialized) {
        this.data = ByteBuffer.wrap(serialized).order(ByteOrder.LITTLE_ENDIAN).getShort();
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
