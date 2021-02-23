/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.structure;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import com.ga2230.Idan.base.messages.IdanVariable;
import com.ga2230.Idan.base.messages.builtins.primitives.EmptyMsg;
import com.ga2230.Idan.base.utils.Function;
import com.ga2230.Idan.base.utils.IdanRate;

/**
 * Acts as the 'roscore' of Idan.
 */
public class IdanMaster extends Idan {
    // The ROS-like parameters
    protected HashMap<String, IdanVariable> parameters = new HashMap<>();

    protected HashMap<String, IdanTopic> topics = new HashMap<>();
    protected HashMap<String, ArrayList<IdanSubscriber>> subQueue = new HashMap<>();
    protected ArrayList<IdanNode> nodes = new ArrayList<>();
    protected boolean running = false;

    /**
     * Constructs an IdanMaster
     * @param ID The name of the master
     */
    public IdanMaster(String ID){
        super(ID, Type.MASTER);
    }

    /**
     * Checks if the IdanMaster has a parameter linked to the key
     * 
     * @param key The key to check
     * @return boolean
     */
    public boolean hasParam(String key){
        return parameters.containsKey(key);
    }

    /**
     * Setting a parameter on the parameter wall
     * 
     * @param key The key in which to store the value in
     * @param value An object to save
     */
    public void setParam(String key, IdanVariable value) {
        parameters.put(key, value.clone());
    }

    /**
     * Get a parameter from the parameter wall.
     * 
     * @param key Key of the variable.
     * @return The variable (If found).
     * @throws Exception In case of invalid key.
     */
    public IdanVariable getParam(String key){
        // Check the existance of the parameter and return it, otherwise throw exception.

        try {
            return parameters.get(key).clone();
        } catch (Exception e){
            e.printStackTrace();
            log("NO PARAMETER NAMED: \"" + key+ "\". SENDING AN EMPTY MESSAGE INSTEAD");
            return new EmptyMsg();
        }
    }

    /**
     * Initialization of an Idan node, if you are lazy and don't want to initialize the node yourself
     * If you have customizations to do init yourself and use master.addNode(node)
     *
     * @param name The name of the node
     * @param clazz Pass the nameofclass.class and it will be taken care of
     */
    public void initNode(String name, Class clazz){
        try {
            Constructor<?> constructor = clazz.getConstructor(String.class, IdanMaster.class);
            IdanNode node = (IdanNode) constructor.newInstance(name, this);
            nodes.add(node);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adding a node to the master's list, if there are customizations you want to do
     * to the node, use this method, otherwise use initNode("nodeName", node.class)
     *
     * @param node The node.
     */
    public void addNode(IdanNode node){
        nodes.add(node);
    }

    /**
     * Checks to see if a topic exists.
     * @param key Key of the topic.
     * @return Boolean, if the topic exists,
     */
    private boolean hasTopic(String key){
        return topics.containsKey(key);
    }

    /**
     * Creating the topic
     * @param key The key of the new topic
     */
    private void createTopic(String key) {
        if(!hasTopic(key))
            topics.put(key, new IdanTopic(key));
    }

    /**
     * Registering a subscriber in the master
     * @param subscriber The new subscriber
     */
	private void registerSub(IdanSubscriber subscriber)  {
        String ID = subscriber.getID();

        // If the topic doesn't exist, wait for it to be created
        if(this.hasTopic(ID)){
            topics.get(ID).addSubscriber(subscriber);
            return;
        } else if(!this.subQueue.containsKey(ID)){
            this.subQueue.put(ID, new ArrayList<>());
        }
        this.subQueue.get(ID).add(subscriber);
    }

    /**
     * The subscriber method creates and registers an IdanSubscriber.
     *
     * Example (in node constructor):
     *  master.Subscriber(this, "/topic", 5, new Function(){
     *      @Override
     *      public void execute(Object parameter) {
     *          this.callback(parameter);
     *      }
     *  });
     *
     * @param node The node it initializes from
     * @param name Name of topic subscription.
     * @param queueSize The queue size of the subscriber.
     * @param callback The callback function to handle the input.
     */
    public void Subscriber(IdanNode node, String name, int queueSize, Function callback) {
        registerSub(new IdanSubscriber(name, node.getID(), queueSize, callback));
    }

    /**
     * Registers the publisher with the master
     * @param publisher The new publisher.
     */
    private void registerPub(IdanPublisher publisher)  {
        String ID = publisher.getID();

        // Creating a topic for the publisher
        createTopic(ID);

        // Liberates the waiting subscribers (of there are any)
        if(subQueue.containsKey(ID)){
            for (IdanSubscriber sub: subQueue.get(ID)) {
                registerSub(sub);
            }
            subQueue.remove(ID);
        }
    }

    /**
     * This method returns an IdanPublisher object and registers it with the master
     *
     * Example (in constructor):
     *  this.pub = master.Publisher(this, "/topic", Classname.class);
     *
     * @param name The topic that the publisher will publish to
     * @param clazz The class that the publisher will publish
     * @return A publisher object
     */
    public <T> IdanPublisher Publisher(IdanNode node, String name, Class<T> clazz) {
        // Create the publisher, register and return it
        IdanPublisher pub = new IdanPublisher(name, clazz, node.getID(), this);
        registerPub(pub);
        return pub;
    }

    /**
     * The publisher object calls this method inside
     * @param input The passed object
     * @param pub The publisher object, to find the topic
     */
    public void publish(Object input, IdanPublisher pub) {
        if(running){topics.get(pub.getID()).publish(input);}
    }

    /**
     * Constructs the Rate object (to wait in the main loop)
     * @param milliseconds The amount of time to wait in milliseconds
     * @return The IdanRate object
     */
    public IdanRate Rate(int milliseconds){
        return new IdanRate(milliseconds);
    }

    /**
     * Killing all of the node's main loop
     */
    public void stop(){
        running = false;
    }

    /**
     * Starts the main loop of the nodes
     */
    public void start(){
        running = true;

        // Loop on each node and start its mainLoop
        for (IdanNode node: nodes) {
            new Thread(node::mainLoop).start();
        }
        nodes.clear(); // Just because memory
    }

    /**
     * @return whether IdanMaster is running
     */
    public boolean is_running() {
        return running;
    }
}