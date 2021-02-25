/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.structure;

import com.ga2230.Idan.base.utils.Logger;

 /**
  * Idan is a top-level class, responsible for binding things together
  */
public class Idan {
    private final String id;

    /**
     * The Idan constructor, sets the ID for an instance
     * @param ID ID with the ROS naming convension (/robot/rightEncoder, ETC...) or the name of the type (Topic, Node, ETC...)
     */
    public Idan(String ID){
        this.id = ID;
    }

    /**
     * Get ID
     * @return ID
     */
    public String getID(){
        return this.id;
    }

    /**
     * Logs the message with the ID
     * @param message
     */
    protected void log(Object message){
        // Send the message to the logger
        Logger.log(this.id, message);
    }
}
