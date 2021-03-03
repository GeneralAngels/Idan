/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.structure;

import com.ga2230.Idan.base.messages.IdanVariable;
import com.ga2230.Idan.base.utils.Logger;


/**
 * IdanPublisher is the object that is responsible for publishing
 * data to the master
 */
public class IdanPublisher extends Idan {
    // Variables
    private Class inputType;
    private IdanMaster master;
    private String nodeName;


    private class DataHolder extends IdanVariable{
        public final Object data;
        public DataHolder(Object data){
            this.data = data;
        }
    }

    /**
     * Constructor.
     * @param name Name of the topic to publish to
     * @param clazz The class of the objects that are going to be published (classname.class)
     * @param nodeName The name of its parent node
     * @param master The IdanMaster object
     */
    public IdanPublisher(String name, Class clazz, String nodeName, IdanMaster master) {
        super(name);
        inputType = clazz;
        this.nodeName = nodeName;
        this.master = master;
    }

    /**
     * Checking if the input is the promised type, and printing the error message in case.
     * @param input The input to the topic.
     * @return Whether the input is valid.
     */
    private boolean checkPublishing(Object input){
        // Check if it's the right type to send, and send the appropriate message.
        if (input.getClass() != inputType){
            StringBuilder sb = new StringBuilder();
            sb.append("Publisher published a non registered object (Expected: ");
            sb.append(inputType.getName());
            sb.append(" got: ");
            sb.append(input.getClass().getName());
            sb.append(")");
            log(sb.toString());
            return false; // Problem with input
        }
        return true; // Input OK and ready to send
    }

    /**
     * Publishes the input to the master
     * @param input The input object
     */
    public void publish(Object input) {
        if (!checkPublishing(input)){
            return;
        }

        // UGHHHHHHHHHHHHHHHHHHHHHHHHHH HANDLE PASS BY REFERENCE FUCK THIS SHIT ITS 3AM
        Object dummy = ((DataHolder)new DataHolder(input).clone()).data;

        // If ok, publish to master
        if (dummy != null) {
            new Thread(() -> {
                if (!this.master.publish(dummy, this)){
                    log("Publishing not successful. Master \""+master.getID()+"\" and his children are not subscribed to the topic: "+getID());
                }
            }).start();
        }
    }

    /**
     * Send the data down the hierarchy
     * @param input
     *
     */
    public void publishDown(Object input){
        if (!checkPublishing(input)){
            return;
        }

        // UGHHHHHHHHHHHHHHHHHHHHHHHHHH HANDLE PASS BY REFERENCE FUCK THIS SHIT ITS 3AM
        Object dummy = ((DataHolder)new DataHolder(input).clone()).data;

        // If ok, publish to master
        if (dummy != null) {
            new Thread(() -> {
                if (!this.master.publishDown(dummy, this)){
                    log("Publishing not successful. Master \""+master.getID()+"\" and his parents are not subscribed to the topic: "+getID());
                }
            }).start();
        }
    }

    /**
     * Logs the message in the format:
     * NODE_NAME: PUBLISHER_ADDRESS: DATA
     * @param message The message to print
     */
    @Override
    public void log(Object message){
        Logger.log(this.nodeName+ ": " +this.getID(), message);
    }

}
