/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataPreProcess;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Moliang
 */
public class Trace {

    private String ID;
    private Date start;
    private Date end;

    private ArrayList<Activity> activities;
    private HashMap<String, Date[]> phase;
    private HashMap<String, Integer> Activity_set;
    private TreeMap<String, Integer> sorted_Activity_set;

    public Trace(String ID) {
        this.ID = ID;
        activities = new ArrayList<>();
        phase = new HashMap<>();
        phase.put("P1", null);
        phase.put("P2", null);
        phase.put("P3", null);
    }

    public void add_activity(Activity ac) {
        activities.add(ac);
        if (activities.size() == 1) {
            initial_time(ac);
        } else {
            update_time(ac);
        }
        check_phase(ac);
    }

    @SuppressWarnings("empty-statement")
    private void check_phase(Activity ac) {
        if (ac.get_name().equals("D")) {
            Date[] dates = {start, ac.get_endTime()};
            phase.put("P1", dates);
            if (phase.get("P2") != null) {
                Date[] up_dates = {ac.get_endTime(), (Date) phase.get("P2")[1]};
                phase.put("P2", up_dates);
            } else {
                Date[] up_dates = {ac.get_endTime(), ac.get_endTime()};
                phase.put("P2", up_dates);
            }
        }
        if (ac.get_name().equals("L")) {
            if (phase.get("P2") != null) {
                Date[] dates = {(Date) phase.get("P2")[0], ac.get_endTime()};
                phase.put("P2", dates);
            } else {
                Date[] dates = {ac.get_endTime(), ac.get_endTime()};
                phase.put("P2", dates);
            }
            Date[] up_dates = {ac.get_endTime(), end};
            phase.put("P3", up_dates);
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

    public TreeMap<String, Integer> get_sorted_activitySet() {
        return sorted_Activity_set = new TreeMap<String, Integer>(this.Activity_set);
    }

}
