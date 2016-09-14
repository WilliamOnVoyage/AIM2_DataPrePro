/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataPreProcess;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Moliang
 */
public class Trace {

    private String ID;
    private int scale;
    
    private ArrayList<Activity> activities;
    private HashMap<String, Integer[]> phase;

    public Trace(String ID) {
        this.ID = ID;
        phase = new HashMap<>();
        activities = new ArrayList<>();
    }

    public void add_activity(Activity ac) {
        activities.add(ac);
    }
}
