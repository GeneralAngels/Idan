/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */

import com.ga2230.Idan.base.messages.builtins.primitives.EmptyMsg;
import com.ga2230.Idan.base.messages.builtins.primitives.Int32;
import com.ga2230.Idan.base.structure.IdanMaster;
import com.ga2230.Idan.base.structure.IdanNode;
import com.ga2230.Idan.base.structure.IdanPublisher;
import com.ga2230.Idan.base.utils.Function;
import com.ga2230.Idan.base.utils.IdanRate;

public class Ping extends IdanNode{
    private IdanPublisher pongPub;
    private IdanRate rate;
    private IdanRate loopRate;

    /**
     * The constructor must get the node name and it's master as parameters
     * and pass the name and master to the super constructor
     * @param name The name of the node
     * @param master The master of the node
     */
    public Ping(String name, IdanMaster master) {
        super(name, master);

        // Stick 0 to the parameter wall under the name "number"
        master.setParam("number", new Int32(0));

        // Save the sleep rate
        rate = new IdanRate(500);
        loopRate = new IdanRate(3000);

        // Make a publisher, taking the node (this), the name of the topic to publish to
        // and the class of the message to publish (In this case it's the empty message)
        pongPub = master.Publisher(this, "ping", EmptyMsg.class);

        // Make the subscriber from this node, subscribed to the topic ping,
        // with queue size of 10 (the amount of messages to wait), and calling the function ping
        // (the Idan master call the function "execute").
        master.Subscriber(this,"pong", 10, new Function(){
            @Override
            public void execute(Object parameter) throws Exception {
                ping(parameter);
            }
        });
    }

    /**
     * The mainLoop function is a must-have node function.
     * This function is called once at the master.start().
     * These functions suppose to kickstart the program or
     * have a loop in them.
     *
     * IMPORTANT: IT'S HIGHLY RECOMMENDED TO USE master.is_running() IN THE LOOP
     * TO ENSURE THREAD SAFETY.
     */
    @Override
    public void mainLoop() {
        // This function is called at the start

        // Starts the ping-pong with a publish
        pongPub.publish(new EmptyMsg());
        log("ping");

        // A loop to show that the mainLoop can still run
        // MAKE SURE TO ALWAYS master.running IN THE CONDITION FOR THREAD SAFETY
        while(master.is_running()){
            // Place a delay into the loop
            loopRate.sleep();

            // Just log "I'm still running" - should be read like "I'm still standing"
            log("I\'m still running");
        }
    }

    /**
     * This is a callback function for topic subscription.
     * NOTICE: This callback function was defined with the master through the "Function" interface as shown above.
     * @param msg The method gets an object representing the new topic value.
     */
    public void ping(Object msg){
        // Sleep; ~best comment ever
        rate.sleep();

        // Get the number from the parameter wall, cast to Int32, and get the "data" variable
        int value = ((Int32)master.getParam("number")).data;

        // Log the call with the new value
        log("ping " + value);

        // Increment "number" by 1
        master.setParam("number", new Int32(value+1));

        // Publish to the topic
        pongPub.publish(new EmptyMsg());
    }
}
