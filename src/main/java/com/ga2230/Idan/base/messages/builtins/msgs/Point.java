/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */


package com.ga2230.Idan.base.messages.builtins.msgs;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.ga2230.Idan.base.messages.RosVariable;
import com.ga2230.Idan.base.messages.builtins.primitives.Float32;
import com.ga2230.Idan.base.messages.builtins.primitives.Float64;

public class Point extends RosVariable {
    public RosVariable x;
    public RosVariable y;
    public RosVariable z;
    private int byteSize;
    private short FLOAT_LEN = 4;
    private short DOUBLE_LEN = 8;
    private short numVars = 3;

    /**
     * Constructs the point from float (32 bit)
     * 
     * @param x
     * @param y
     * @param z
     */
    public Point(float x, float y, float z) {
        this.x = new Float32(x);
        this.y = new Float32(y);
        this.z = new Float32(z);
        this.byteSize = FLOAT_LEN; // 4 bytes per variable
    }

    /**
     * Constructs the point from double (64 bit)
     * 
     * @param x
     * @param y
     * @param z
     */
    public Point(double x, double y, double z) {
        this.x = new Float64(x);
        this.y = new Float64(y);
        this.z = new Float64(z);
        this.byteSize = DOUBLE_LEN; // 8 bytes per variable
    }

    /**
     * Constructs the object from a serialized form
     * @param serialized The serialized object
     */
    public Point(byte[] serialized){
        this.byteSize = serialized.length/numVars;

        ByteBuffer buff = ByteBuffer.wrap(serialized);
        byte[] temp = new byte[this.byteSize];

        // If float-sized
        if (byteSize == FLOAT_LEN){
            // Unpacking the buffer
            buff.get(temp, 0, this.byteSize);
            this.x = new Float32(temp);
            buff.get(temp, 0, this.byteSize);
            this.y = new Float32(temp);
            buff.get(temp, 0, this.byteSize);
            this.z = new Float32(temp);
        } else{ // Double sized 
            // Unpacking the buffer
            buff.get(temp, 0, this.byteSize);
            this.x = new Float64(temp);
            buff.get(temp, 0, this.byteSize);
            this.y = new Float64(temp);
            buff.get(temp, 0, this.byteSize);
            this.z = new Float64(temp);
        }
    }

    /**
     * Prepare to sending
     * 
     * @return The serialized form of the class
     */
    @Override
    public byte[] pack() {
        // Allocate memory for 3 variables
        ByteBuffer buff = ByteBuffer.allocate(this.byteSize * numVars).order(ByteOrder.LITTLE_ENDIAN);

        // Serialize
        buff.put(x.pack());
        buff.put(y.pack());
        buff.put(z.pack());
        return buff.array();
    }

    @Override
    public void unpack(byte[] serialized) {
        ByteBuffer buff = ByteBuffer.wrap(serialized);//.order(ByteOrder.LITTLE_ENDIAN);
        byte[] temp = new byte[this.byteSize]; // Allocate memory

        // Unpacking the buffer
        buff.get(temp, 0, this.byteSize);
        x.unpack(temp);
        buff.get(temp, 0, this.byteSize);
        y.unpack(temp);
        buff.get(temp, 0, this.byteSize);
        z.unpack(temp);
    }

    @Override
    public String toString() {
        final String NEWLINE = "\n\r";
        StringBuilder sb = new StringBuilder();

        sb.append("Point:" + NEWLINE);
        sb.append("\tx: " + x + NEWLINE);
        sb.append("\ty: " + y + NEWLINE);
        sb.append("\tz: " + z + NEWLINE);

        return sb.toString();
    }
}
