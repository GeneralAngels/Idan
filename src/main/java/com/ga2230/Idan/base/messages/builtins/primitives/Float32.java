/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.messages.builtins.primitives;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.ga2230.Idan.base.messages.RosVariable;

/**
 * Represents a Float (32 bit)
 */
public class Float32 extends RosVariable{
    public float data;

    /**
     * Construct the Float32 variable
     * @param data The float value
     */
    public Float32(float data){
        this.data = data;
    }

    /**
     * Construct the Float32 variable from bytes
     * 
     * @param data serialized data
     */
    public Float32(byte[] data) {
        unpack(data);
    }

    /**
     * Serializes the float
     * @return The bytes array from the float
     */
    @Override
    public byte[] pack() {
        ByteBuffer buff = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        buff.putFloat(data);
        return buff.array();
    }

    /**
     * Deserializes the float
     * 
     * @param serialized The serialized float
     */
    @Override
    public void unpack(byte[] serialized) {
        this.data = ByteBuffer.wrap(serialized).order(ByteOrder.LITTLE_ENDIAN).getFloat();
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
