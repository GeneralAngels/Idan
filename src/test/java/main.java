/**
 * Copyright (c) 2021 Hali Lev Ari or General Angels
 * // TODO: add github
 */


import com.ga2230.Idan.base.structure.IdanMaster;
import com.ga2230.Idan.base.utils.IdanRate;

public class main {
    public static void main(String[] args){
        // Init the master
        IdanMaster master = new IdanMaster("Master");

        // Init the ping node
        master.initNode("ping", Ping.class);

        // An alternative and equivalent way to register a node
        Pong pong = new Pong("pong", master);
        master.addNode(pong);

        // Start the master (Call each mainLoop of every node, then deletes them from memory)
        // sets master.is_running() to true (USE THAT VARIABLE FOR THREAD SAFETY)
        master.start();

        new IdanRate(10000).sleep();

        // Stops all publishing activities (To stop inter-node loops)
        // and sets master.is_running() to false
        master.stop();
    }
}
