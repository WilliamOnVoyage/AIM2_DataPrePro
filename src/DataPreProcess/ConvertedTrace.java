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
    private int scale = 1;

    private ArrayList<Activity> activities;
    private HashMap<String, Integer[]> phase;
    private HashMap<String, Integer> Activity_set;

    private int[][] Matrix;
    private final int Length = 1000;

    public ConvertedTrace(Trace t) {
        this.ID = t.get_ID();
        this.activities = (ArrayList<Activity>) t.get_ActivityList().clone();
        this.start = t.get_start();
        this.end = t.get_end();
        scale = (int) (end.getTime() - start.getTime()) / 1000; //scale in 1s
        this.Activity_set = t.get_Activity_set();
        Matrix = new int[Activity_set.size()][Length];
        construct_Matrix();
    }

    private void construct_Matrix() {
        for (Activity ac : activities) {
            //Check the range by start and end
            //Set corresponding cells to 1
        }
    }

}
