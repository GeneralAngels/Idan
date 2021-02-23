/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.messages.builtins.primitives;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.ga2230.Idan.base.messages.RosVariable;

/**
 * Represents a boolean
 */
public class Bool extends RosVariable {
    public boolean data;

    /**
     * Construct the bool variable
     * 
     * @param data The bool value
     */
    public Bool(boolean data) {
        this.data = data;
    }
    
    /**
     * Construct the bool variable from bytes
     * 
     * @param data serialized data
     */
    public Bool(byte[] data) {
        unpack(data);
    }

    /**
     * Serializes the bool
     * 
     * @return The bytes array from the bool
     */
    @Override
    public byte[] pack() {
        ByteBuffer buff = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
        buff.put((byte)(data? 1:0));
        return buff.array();
    }

    /**
     * Deserializes the boolean
     * 
     * @param serialized The serialized boolean
     */
    @Override
    public void unpack(byte[] serialized) {
        this.data = ByteBuffer.wrap(serialized).order(ByteOrder.LITTLE_ENDIAN).get() != 0;
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
