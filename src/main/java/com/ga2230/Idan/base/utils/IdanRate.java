/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.utils;

/**
 * Used to regulate the mainLoop
 * TODO: Add check for delay and automatic delay control
 */
public class IdanRate {
    private final int milliseconds;

    /**
     * Constructor
     * @param milliseconds The amount of milliseconds to sleep
     */
    public IdanRate(int milliseconds) {
        this.milliseconds = milliseconds;
    }

    /**
     * Makes the thread sleep
     */
    public void sleep() {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
