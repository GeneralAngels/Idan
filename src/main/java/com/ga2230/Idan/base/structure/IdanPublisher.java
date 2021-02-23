/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.structure;

import com.ga2230.Idan.base.messages.IdanVariable;
import com.ga2230.Idan.base.messages.builtins.primitives.EmptyMsg;
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

    /**
     * Constructor.
     * @param name Name of the topic to publish to
     * @param clazz The class of the objects that are going to be published (classname.class)
     * @param nodeName The name of its parent node
     * @param master The IdanMaster object
     */
    public IdanPublisher(String name, Class clazz, String nodeName, IdanMaster master) {
        super(name, Type.PUBLISHER);
        inputType = clazz;
        this.nodeName = nodeName;
        this.master = master;
    }

    /**
     * Publishes the input to the master
     * @param input The input object
     */
    public void publish(IdanVariable input) {
        // Check if it's the right type to send, and send the appropriate message.
        if (input.getClass() != inputType){
            StringBuilder sb = new StringBuilder();
            sb.append("Publisher published a non registered object (Expected: ");
            sb.append(inputType.getName());
            sb.append(" got: ");
            sb.append(input.getClass().getName());
            sb.append(")");
            log(sb.toString());
            return;
        }

        // UGHHHHHHHHHHHHHHHHHHHHHHHHHH HANDLE PASS BY REFERENCE FUCK THIS SHIT ITS 3AM
        Object dummy = null;
        dummy = input.clone();
        // If ok, publish to master
        if (dummy != null) {
            Object finalDummy = dummy; // The compiler won't shut up
            new Thread(() -> {
                this.master.publish(finalDummy, this);
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
