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
    private ArrayList<Activity> activities;
    private HashMap<String, Integer[]> phase;
    
    private int[][] Matrix;

    public ConvertedTrace(Trace t) {
        this.ID = t.get_ID();
        this.activities = t.get_ActivityList();
        this.start = t.get_start();
        this.end = t.get_end();
        construct_Matrix();
    }

    private void construct_Matrix() {
    }

}
