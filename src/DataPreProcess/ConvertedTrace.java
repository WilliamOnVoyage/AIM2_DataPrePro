/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataPreProcess;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Moliang
 */
public class ConvertedTrace {

    private String ID;
    private Date start;
    private Date end;
    private ArrayList<Activity> activities;
    private HashMap<String, Integer[]> phase;
    private HashSet<String> Activity_set;

    private int[][] Matrix;
    private final int Length = 1000;

    public ConvertedTrace(Trace t) {
        this.ID = t.get_ID();
        this.activities = t.get_ActivityList();
        this.start = t.get_start();
        this.end = t.get_end();
        this.Activity_set = t.get_Activity_set();
        Matrix = new int[Activity_set.size()][Length];
        construct_Matrix();
    }

    private void construct_Matrix() {
        
    }

}
