/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.messages;

/**
 * Inherit if you want to pass the variables to
 * Forces you to make serialisations to your object
 */
public abstract class RosVariable extends IdanVariable{
    // All ROS Idan variables must be serializable
    public abstract byte[] pack();
    public abstract void unpack(byte[] serialized);
}
