/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataPreProcess;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Moliang
 */
public class Trace {

    private String ID;
    private Date start;
    private Date end;

    private ArrayList<Activity> activities;
    private String[] phase;
    private HashMap<String, Integer> Activity_set;

    public Trace(String ID) {
        this.ID = ID;
        activities = new ArrayList<>();
    }

    public void add_activity(Activity ac) {
        activities.add(ac);
        if (activities.size() == 1) {
            initial_time(ac);
        } else {
            update_time(ac);
        }
    }

    private void initial_time(Activity ac) {
        start = ac.get_startTime();
        end = ac.get_endTime();
    }

    private void update_time(Activity ac) {
        start = ac.get_startTime().before(start) ? ac.get_startTime() : start;
        end = ac.get_endTime().after(end) ? ac.get_endTime() : end;
    }

    public boolean set_ActivitySet(HashMap<String, Integer> Activity_set) {
        this.Activity_set = Activity_set;
        return !Activity_set.isEmpty();
    }

    public String get_ID() {
        return this.ID;
    }

    public ArrayList<Activity> get_ActivityList() {
        return this.activities;
    }

    public HashMap<String, Integer> get_Activity_set() {
        return this.Activity_set;
    }

    public Date get_start() {
        return this.start;
    }

    public Date get_end() {
        return this.end;
    }

    public int get_length() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
