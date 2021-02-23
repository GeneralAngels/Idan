/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.messages;

import com.ga2230.Idan.base.messages.builtins.primitives.EmptyMsg;
import com.ga2230.Idan.base.utils.Logger;

/**
 * Inherit from this class to create new custom variables
 */
public abstract class IdanVariable implements Cloneable{
    // To get rid of the reference
    public IdanVariable clone() {
        try {
            return (IdanVariable) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            Logger.log(this.getClass().getName(),"Please inherit the IdanVariable object to publish it");
        }
        return new EmptyMsg();
    }
}
