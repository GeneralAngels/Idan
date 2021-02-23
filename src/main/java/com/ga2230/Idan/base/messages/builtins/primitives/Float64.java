/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.messages.builtins.primitives;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.ga2230.Idan.base.messages.RosVariable;

/**
 * Represents a Float (64 bit) or double
 */
public class Float64 extends RosVariable {
    public double data;

    /**
     * Construct the Float64 variable
     * 
     * @param data The double value
     */
    public Float64(double data) {
        this.data = data;
    }

    /**
     * Construct the Float64 variable from bytes
     * 
     * @param data serialized data
     */
    public Float64(byte[] data) {
        unpack(data);
    }

    /**
     * Serializes the double
     * 
     * @return The bytes array from the double
     */
    @Override
    public byte[] pack() {
        ByteBuffer buff = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        buff.putDouble(data);
        return buff.array();
    }

    /**
     * Deserializes the double
     * 
     * @param serialized The serialized double
     */
    @Override
    public void unpack(byte[] serialized) {
        this.data = ByteBuffer.wrap(serialized).order(ByteOrder.LITTLE_ENDIAN).getDouble();
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
