/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

package com.ga2230.Idan.base.structure;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.lang.String;

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

    // Nodes, topics, subscriber queue
    protected HashMap<String, IdanTopic> topics = new HashMap<>();
    protected ArrayList<IdanNode> nodes = new ArrayList<>();

    // List of sub-groups
    protected HashMap<String, IdanMaster> nodeGroups = new HashMap<>();
    protected IdanMaster daddy;

    protected boolean running = false;

    /**
     * Constructs an IdanMaster
     * @param ID The name of the master
     */
    public IdanMaster(String ID){
        super(ID);
    }

    /**
     * Constructs an IdanMaster with a parent
     * @param ID The name of the master
     * @param parent The master above this master
     */
    public IdanMaster(String ID, IdanMaster parent){
        super(ID);
        parent.addGroup(this);
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
        // Check the existence of the parameter and return it, otherwise throw exception.
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
     * Initialization of several Idan nodes, if you are lazy and don't want to initialize the node yourself
     * If you have customizations to do init yourself and use master.addNode(node)
     *
     * @param name The name of the node
     * @param clazzList Pass the nameofclass.class and it will be taken care of
     */
    public void initNode(String name, Class[] clazzList){
        for (Class c: clazzList) {
            try {
                Constructor<?> constructor = c.getConstructor(String.class, IdanMaster.class);
                IdanNode node = (IdanNode) constructor.newInstance(name, this);
                nodes.add(node);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
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
     * Adding several nodes to the master's list, if there are customizations you want to do
     * to the node, use this method, otherwise use initNode("nodeName", node.class)
     *
     * @param nodeList List of constructed nodes.
     */
    public void addNode(IdanNode[] nodeList)
    {
        nodes.addAll(Arrays.asList(nodeList));
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

        // Creating a topic for the publisher
        createTopic(ID);

        // Add the subscriber to the topic
        topics.get(ID).addSubscriber(subscriber);
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
     *  new Function()... can be replaced with this:callback
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
     * THIS METHOD IS OPEN FOR THE USER FOR FLEXIBILITY,
     * SHOULD BE
     *
     * @param input The passed object
     * @param pub The publisher object, to find the topic
     * @return Boolean on whether the publishing was successful
     */
    public boolean publish(Object input, IdanPublisher pub) {
        if(running){
            boolean success = false;

            // Send to the masters topics
            if(topics.containsKey(pub.getID())) {
                topics.get(pub.getID()).publish(input);
                success = true;
            }

            if (daddy != null){
                // Send the publishing up the hierarchy
                success |= daddy.publish(input, pub);
            }
            return success;
        }
        return false;
    }

    /**
     * Publish the data down in the hierarchy to the sub-groups.
     * @param input The input data.
     * @param pub The publisher object to find the topics.
     * @return Boolean on whether the publishing was successful.
     */
    public boolean publishDown(Object input, IdanPublisher pub){
        if(running){
            boolean success = false;

            // Send to the masters topics
            if(topics.containsKey(pub.getID())) {
                topics.get(pub.getID()).publish(input);
                success = true;
            }

            // Send down to the kids
            for (IdanMaster child: nodeGroups.values()) {
                success |= child.publishDown(input, pub);
            }
            return success;
        }
        return false;
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
        for (IdanMaster master: nodeGroups.values()) {
            master.stop();
        }
    }

    public void startNodes(){
        // Loop on each node and start its mainLoop
        for (IdanNode node: nodes) {
            new Thread(node::mainLoop).start();
        }
    }

    /**
     * Starts the main loop of the nodes
     */
    public void start(){
        running = true;

        startNodes();
        startGroups();

        nodes.clear(); // Just because memory
    }

    /**
     * @return whether IdanMaster is running
     */
    public boolean is_running() {
        return running;
    }

    /**
     * Add a node group to master
     * @param group An IdanMaster object containing the groups
     */
    public void addGroup(IdanMaster group){
        nodeGroups.put(group.getID(), group);
        group.setDaddy(this);
    }

    /**
     * Get the group by its name
     * @param groupName The name of the group
     * @return The wanted group
     */
    public IdanMaster getGroup(String groupName){
        return nodeGroups.get(groupName);
    }

    /**
     * Starts all of the nodes in a node group
     * @param groupName Name of the node group to start
     */
    public void startGroup(String groupName){
        getGroup(groupName).start();
    }

    /**
     * Stops all of the nodes in a node group
     * @param groupName Name of the node group to stop
     */
    public void stopGroup(String groupName){
        getGroup(groupName).stop();
    }

    /**
     * Starts all groups
     */
    public void startGroups(){
        for (IdanMaster group: nodeGroups.values()) {
            group.start();
        }
    }

    /**
     * Stops all groups
     */
    public void stopGroups(){
        for (IdanMaster group: nodeGroups.values()) {
            group.stop();
        }
    }

    /**
     * Returns the indented version of the sub-groups
     * @param child Child's toString
     * @return Indented toString
     */
    protected String fromChildren(IdanMaster child){
        String[] split = child.toString().split("\n");
        for (int i = 0; i < split.length; i++) {
            split[i] = "\t" + split[i];
        }
        return String.join("\n", split);
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();

        // Add Nodes
        sb.append(getID().toUpperCase() + "\'s nodes:\n");
        if (nodes.size() > 0) {
            for (IdanNode node : nodes) {
                sb.append("\t" + node.getID() + "\n");
            }
        } else{ sb.append("\tNone\n"); }

        // Add parameters
        sb.append(getID().toUpperCase() + "\'s parameters:\n");
        if (parameters.size() > 0){
            for (String paramKey: parameters.keySet()) {
                sb.append("\t"+paramKey+"\n");
            }
        } else { sb.append("\tNone\n"); }

        // Add sub-groups
        sb.append(getID().toUpperCase() + "\'s groups:\n");
        if (nodeGroups.size() > 0){
            for (IdanMaster group: nodeGroups.values()) {
                sb.append(fromChildren(group)+"\n");
            }
        } else{ sb.append("\tNone\n"); }

        return sb.toString();
    }

    /**
     * Sets the parent master (Daddy (; )
     * @param daddy The master of this master
     */
    public void setDaddy(IdanMaster daddy){
        this.daddy = daddy;
        syncParameters(daddy.parameters);
    }

    protected void syncParameters(HashMap<String, IdanVariable> map){
        parameters = map;
    }
}