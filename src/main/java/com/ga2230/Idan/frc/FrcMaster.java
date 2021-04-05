package frc.tempIdan.Idan;

import com.ga2230.Idan.base.structure.IdanMaster;

public class FrcMaster extends IdanMaster {
    // Constants
    private final String AUTONAME = "Autonomous";
    private final String TELENAME = "Teleop";

    // Required node groups
    private IdanMaster autonomousMaster;
    private IdanMaster teleopMaster;

    /**
     * The FRC robot master constructor, requires autonomous master and a teleop master
     * per the competition requirements.
     * @param ID The name of the robot master (typically "Master" or "Robot"
     * @param autonomousMaster IdanMaster object that controls the autonomous period.
     * @param teleopMaster IdanMaster object that controls the teleop period.
     */
    public FrcMaster(String ID, IdanMaster autonomousMaster, IdanMaster teleopMaster) {
        super(ID);

        if(!autonomousMaster.getID().equals(AUTONAME)){
            log("AUTONOMOUS MASTER MUST BE NAMED: \"Autonomous\"");
        }
        if(!teleopMaster.getID().equals(TELENAME)){
            log("AUTONOMOUS MASTER MUST BE NAMED: \"Teleop\"");
        }

        this.autonomousMaster = autonomousMaster;
        this.teleopMaster = teleopMaster;
    }

    /**
     * Starts the teleop period
     */
    public void startTeleop(){
        teleopMaster.start();
        teleopMaster.startGroups();
    }

    /**
     * Starts the autonomous period
     */
    public void startAutonomous(){
        autonomousMaster.start();
        autonomousMaster.startGroups();
    }

    /**
     * Stops the teleop period
     */
    public void stopTeleop(){
        teleopMaster.stop();
    }

    /**
     * Stops the autonomous period
     */
    public void stopAutonomous(){
        autonomousMaster.stop();
    }


}
