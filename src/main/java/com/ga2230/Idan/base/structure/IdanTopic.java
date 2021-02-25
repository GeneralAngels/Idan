/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.structure;

import java.util.ArrayList;

/**
 * Holds the information about a topic
 * @param <T> Stfu compiler.
 */
public class IdanTopic<T> extends Idan {
    // List of subscribers subscribed to the topic
    private final ArrayList<IdanSubscriber<T>> subscribers = new ArrayList<>();

    /**
     * Constructor
     * @param ID The address of the topic
     */
    public IdanTopic(String ID){
        super(ID);
    }

    /**
     * Add a subscriber to the list
     * @param subscriber An IdanSubscriber object that wants to subscribe to the topic
     */
    public void addSubscriber(IdanSubscriber<T> subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * Publish sends the data to all of its subscribers
     * @param input Data from the publisher
     */
    public void publish(T input){
        new Thread(() -> {
            for (IdanSubscriber<T> subscriber : subscribers) {
                subscriber.update(input);
            }
        }).start();
    }
}
