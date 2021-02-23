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
    // A list of possible roles the Idan class can occupy
    public enum Type {
        NODE,
        TOPIC,
        SUBSCRIBER,
        PUBLISHER,
        MASTER
    }
    
    private final String id;
    private final Type type;

    /**
     * The Idan constructor, sets the ID and type for an instance
     * @param ID ID with the ROS naming convension (/robot/rightEncoder, ETC...) or the name of the type (Topic, Node, ETC...)
     * @param type The type of object it represents
     */
    public Idan(String ID, Type type){
        // Set the ID and type
        this.id = ID;
        this.type = type;
    }

    /**
     * A constructor for non instances (Types)
     * @param type
     */
    public Idan(Type type){
        this.type = type;
        this.id = "";
    }

    /**
     * Get ID
     * @return ID
     */
    public String getID(){
        return this.id;
    }

    /**
     * Get type
     * @return Type of object
     */
    public Type getType(){
        return this.type;
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
