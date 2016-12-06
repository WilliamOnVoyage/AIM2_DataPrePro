/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataPreProcess;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Moliang
 */
public class ConvertedTrace {

    private final String ID;
    private final Date start;
    private final Date end;
    private double scale = 1;

    private final ArrayList<Activity> activities;
    private String[] phase;

    private HashMap<String, Integer> Activity_set;
    private int[][] Matrix;
    private int Length = 3600;  //Normalize into 1 hour
    private int pt_arrival_length = (int) (Length * 0.1);
    private int Primary_length = (int) (Length * 0.4);
    private int Secondary_length = (int) (Length * 0.4);
    private int Pst_secondary_length = (int) (Length * 0.1);

    private String pt = "Pt arrival";
    private String pre_pri = "Pre-Primary";
    private String pri = "Primary";
    private String sec = "Secondary";
    private String pst_sec = "Post-Secondary";
    private String pd = "Pt departure";

    private List<Phase> p;

    public ConvertedTrace(Trace t) {
        this.ID = t.get_ID();
        this.activities = (ArrayList<Activity>) t.get_ActivityList().clone();
        this.start = t.get_start();
        this.end = t.get_end();
//        Length = (int) (end.getTime() - start.getTime()) / 1000;  //Dont use this when normalized
        double duration = (end.getTime() - start.getTime()) / 1000; //in seconds
        scale = duration / Length; //scale in 1s
//        scale = 1;
        this.Activity_set = (HashMap<String, Integer>) t.get_Activity_set().clone();
        Matrix = new int[Activity_set.size()][Length];
        phase = new String[Length];
        construct_Matrix();
    }

    public ConvertedTrace(Trace t, List<Phase> p) {
        this.ID = t.get_ID();
        this.activities = (ArrayList<Activity>) t.get_ActivityList().clone();
        this.start = t.get_start();
        this.end = t.get_end();
        this.Activity_set = (HashMap<String, Integer>) t.get_Activity_set().clone();
        this.p = p;

        Matrix = new int[Activity_set.size()][Length];
        phase = new String[Length];
        construct_Matrix_Phase();
        construct_phase();
    }

    private void construct_phase() {
        for (int index = 0; index < pt_arrival_length; index++) {
            phase[index] = pt;
        }
        for (int index = pt_arrival_length; index < Primary_length; index++) {
            phase[index] = pri;
        }
        for (int index = Primary_length; index < Secondary_length; index++) {
            phase[index] = sec;
        }
        for (int index = Secondary_length; index < Pst_secondary_length; index++) {
            phase[index] = pst_sec;
        }
    }

    private void construct_Matrix_Phase() {
        Date pt_st = null;
        Date pt_ed = null;
        Date pri_st = null;
        Date pri_ed = null;
        Date sec_st = null;
        Date sec_ed = null;
        Date pst_sec_st = null;
        Date pst_sec_ed = null;
        Date pd_st = null;
        Date pd_ed = null;
        //Get all the date
        for (Phase ph : p) {
            if (pt.equals(ph.phaseName)) {
                pt_st = ph.start;//Calibrate start time
                pt_ed = ph.end;
            }
            if (pre_pri.equals(ph.phaseName)) {
                pt_ed = ph.end;
            }
            if (pri.equals(ph.phaseName)) {
                pri_st = ph.start;
                pri_ed = ph.end;
            }
            if (sec.equals(ph.phaseName)) {
                sec_st = ph.start;
                sec_ed = ph.end;
            }
            if (pst_sec.equals(ph.phaseName)) {
                pst_sec_st = ph.start;
                pst_sec_ed = ph.end;
            }
            if (pd.equals(ph.phaseName)) {
                pd_st = ph.start;
                pd_ed = ph.end;
            }
        }
        for (Activity ac : activities) {
            int start_index = 0;
            int end_index = 0;
            if (ac.get_startTime().before(pt_ed)) {
                start_index = (int) ((double) (ac.get_startTime().getTime() - pt_st.getTime()) / (double) (pt_ed.getTime() - pt_st.getTime()) * pt_arrival_length);
            } else if (ac.get_startTime().before(pri_ed)) {
                start_index = pt_arrival_length + (int) ((double) (ac.get_startTime().getTime() - pri_st.getTime()) / (double) (pri_ed.getTime() - pri_st.getTime()) * Primary_length);
            } else if (ac.get_startTime().before(sec_ed)) {
                start_index = pt_arrival_length + Primary_length + (int) ((double) (ac.get_startTime().getTime() - sec_st.getTime()) / (double) (sec_ed.getTime() - sec_st.getTime()) * Secondary_length);
            } else if (ac.get_startTime().before(pst_sec_ed)) {
                start_index = pt_arrival_length + Primary_length + Secondary_length + (int) ((double) (ac.get_startTime().getTime() - pst_sec_st.getTime()) / (double) (pst_sec_ed.getTime() - pst_sec_st.getTime()) * Pst_secondary_length);
            } else {
                //Patient departure, not included
            }

            if (ac.get_endTime().before(pt_ed)) {
                end_index = (int) ((double) (ac.get_endTime().getTime() - pt_st.getTime()) / (double) (pt_ed.getTime() - pt_st.getTime()) * pt_arrival_length);
            } else if (ac.get_endTime().before(pri_ed)) {
                end_index = pt_arrival_length + (int) ((double) (ac.get_endTime().getTime() - pri_st.getTime()) / (double) (pri_ed.getTime() - pri_st.getTime()) * Primary_length);
            } else if (ac.get_endTime().before(sec_ed)) {
                end_index = pt_arrival_length + Primary_length + (int) ((double) (ac.get_endTime().getTime() - sec_st.getTime()) / (double) (sec_ed.getTime() - sec_st.getTime()) * Secondary_length);
            } else if (ac.get_endTime().before(pst_sec_ed)) {
                end_index = pt_arrival_length + Primary_length + Secondary_length + (int) ((double) (ac.get_endTime().getTime() - pst_sec_st.getTime()) / (double) (pst_sec_ed.getTime() - pst_sec_st.getTime()) * Pst_secondary_length);
            } else {
                //Patient departure, not included
            }
            end_index = end_index >= Length ? Length : end_index; // Avoid out of boundry
            //Set corresponding cells to 1
            int row_index = Activity_set.get(ac.get_name());
            for (int i = start_index; i < end_index; i++) {
                Matrix[row_index][i] = 1;
            }
        }
    }

    private void construct_Matrix() {
        for (Activity ac : activities) {
            //Check the range by start and end
            int start_index = (int) ((int) (ac.get_startTime().getTime() - this.start.getTime()) / 1000 / scale);
            int end_index = (int) ((int) (ac.get_endTime().getTime() - this.start.getTime()) / 1000 / scale);
            end_index = end_index >= Length ? Length : end_index; // Avoid out of boundry
            //Set corresponding cells to 1
            int row_index = Activity_set.get(ac.get_name());
            for (int i = start_index; i < end_index; i++) {
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

    public double get_scale() {
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
