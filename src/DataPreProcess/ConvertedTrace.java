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
public class ConvertedTrace {

    private String ID;
    private Date start;
    private Date end;
    private int scale = 1;

    private ArrayList<Activity> activities;
    private String[] phase;
    private HashMap<String, Integer> Activity_set;

    private int[][] Matrix;
    private final int Length = 1000;

    public ConvertedTrace(Trace t) {
        this.ID = t.get_ID();
        this.activities = (ArrayList<Activity>) t.get_ActivityList().clone();
        this.start = t.get_start();
        this.end = t.get_end();
        scale = (int) (end.getTime() - start.getTime()) / 1000 / Length + 1; //scale in 1s
        this.Activity_set = (HashMap<String, Integer>) t.get_Activity_set().clone();
        Matrix = new int[Activity_set.size()][Length];
        phase = new String[Length];
        construct_Matrix();
    }

    private void construct_Matrix() {
        for (Activity ac : activities) {
            //Check the range by start and end
            int start_index = (int) (ac.get_startTime().getTime() - this.start.getTime()) / 1000 / scale;
            int end_index = (int) (ac.get_endTime().getTime() - this.start.getTime()) / 1000 / scale;
            //Set corresponding cells to 1
            int row_index = Activity_set.get(ac.get_name());
            for (int i = start_index; i <= end_index; i++) {
                Matrix[row_index][i] = 1;
            }
        }
    }

    public int[][] get_Matrix() {
        return this.Matrix;
    }

    public String get_ID() {
        return this.ID;
    }

    public int get_scale() {
        return this.scale;
    }

    public HashMap<String, Integer> get_Activity_set() {
        return this.Activity_set;
    }

    public boolean set_phase(String[] divided_phase) {
        this.phase = divided_phase;
        return true;
    }

    public String[] get_phase() {
        return this.phase;
    }

    public int get_length() {
        return this.Length;
    }
}
