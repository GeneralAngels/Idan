/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.structure;

import com.ga2230.Idan.base.utils.Function;
import java.util.*;

/**
 * The subscriber object
 * @param <T> Shut up compiler
 */
public class IdanSubscriber <T> extends Idan {
    // Functionality variables
    public final Function callback;

    // To handle a queue
    private final int queueSize;
    private Deque<T> deque;
    private boolean waiting;
    private String nodeName;

    /**
     * Constructor for a subscriber.
     *  @param subscription A string, ROS-formatted as 'address' (/cmd_vel, /robot/left_motor, ETC...)
     * @param nodeName The node name it came from
     * @param queueSize Max Queue size to save memory.
     * @param callback Callback function to handle the input.
     */
    public IdanSubscriber(String subscription, String nodeName, int queueSize, Function callback) {
        super(subscription, Type.SUBSCRIBER);

        this.callback = callback;
        this.queueSize = queueSize;
        this.deque = new ArrayDeque<T>();
        this.waiting = false;
        this.nodeName = nodeName;
    }

    /**
     * Update the queue.
     * 
     * @param message Input message.
     */
    public void update(T message){
        // In case deque overflows
        if(deque.size() > queueSize){
            deque.poll();
        }

        // Add the message to queue.
        deque.addLast(message);

        // Send the message to the callback
        send();
    }
    
    /**
     * Send the input to the callback function.
     */
    private void send(){
        // Check that it's not in the callback right now.
        if (waiting){
            return;
        }

        // Loop on the queue and send the input.
        waiting = true;
        new Thread(() -> {
            while (deque.size() != 0) {
                try {
                    callback.execute(deque.pop());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            waiting = false;
        }).start();
    }
}
