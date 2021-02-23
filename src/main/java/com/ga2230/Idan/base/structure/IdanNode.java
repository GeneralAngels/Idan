/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.structure;

/**
 * A node abstract class.
 * To use, inherit and override its mainLoop (Can stay empty)
 * Place the publishers and subscribers in the constructor.
 */
public abstract class IdanNode extends Idan {
    public IdanMaster master;

    /**
     * Yes.
     * @param name Name of node
     * @param master An IdanMaster instance
     */
    public IdanNode(String name, IdanMaster master) {
        super(name, Type.NODE);
        this.master = master;
    }

    /**
     * MainLoop. override and change.
     * Can stay hollow.
     */
    public abstract void mainLoop();
}
