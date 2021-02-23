/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.messages.builtins.primitives;

import com.ga2230.Idan.base.messages.RosVariable;

public class EmptyMsg extends RosVariable {

    public EmptyMsg(){}

    @Override
    public byte[] pack() {
        return new byte[0];
    }

    @Override
    public void unpack(byte[] serialized) {

    }
}
