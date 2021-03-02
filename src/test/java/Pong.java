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

public class Pong extends IdanNode{
    private IdanPublisher pingPub;
    private IdanRate rate;

    /**
     * The constructor must get the node name and it's master as parameters
     * and pass the name and master to the super constructor
     * @param name The name of the node
     * @param master The master of the node
     */
    public Pong(String name, IdanMaster master) {
        super(name, master);

        // Set the rate for the ping-pong
        rate = new IdanRate(500);

        // Make a publisher, taking the node (this), the name of the topic to publish to
        // and the class of the message to publish (In this case it's the empty message)
        pingPub = master.Publisher(this, "pong", Integer.class);

        // Make the subscriber from this node, subscribed to the topic ping,
        // with queue size of 10 (the amount of messages to wait), and calling the function pong
        // (the Idan master call the function "execute").
        master.Subscriber(this,"ping", 10, new Function(){
            @Override
            public void execute(Object parameter) throws Exception {
                pong(parameter);
            }
        });
    }

    // No main loop, void call
    @Override
    public void mainLoop() { }

    /**
     * This is a callback function for topic subscription.
     * NOTICE: This callback function was defined with the master through the "Function" interface as shown above.
     * @param msg The method gets an object representing the new topic value.
     */
    public void pong(Object msg){
        // Sleep
        rate.sleep();

        // Get the number from the parameter wall, cast to Int32, and get the "data" variable
        int value = ((Int32)master.getParam("number")).data;

        // Log the call
        log("ping " + value+", "+msg);

        // Will still run due to threading
        // float a = 1/0;

        // Increment "number" by 1
        master.setParam("number", new Int32(value+1));

        // Publish to call pong
        pingPub.publish((Integer)msg+1);
    }
}
