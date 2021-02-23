/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.messages.builtins.primitives;

import com.ga2230.Idan.base.messages.RosVariable;

/**
 * Represents a string
 */
public class Str extends RosVariable {
    public String data;

    /**
     * Construct the Str variable
     * 
     * @param data The string
     */
    public Str(String data) {
        this.data = data;
    }

    /**
     * Construct the Str variable from bytes
     * 
     * @param data serialized data
     */
    public Str(byte[] data) {
        unpack(data);
    }

    /**
     * Serializes the long
     * 
     * @return The bytes array from the long
     */
    @Override
    public byte[] pack() {
        return data.getBytes();
    }

    /**
     * Deserializes the long
     * 
     * @param serialized The serialized long
     */
    @Override
    public void unpack(byte[] serialized) {
        this.data = new String(serialized);
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
