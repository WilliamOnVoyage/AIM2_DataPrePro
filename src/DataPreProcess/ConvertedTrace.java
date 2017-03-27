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
import java.util.TreeMap;
import java.lang.Object;
import java.text.DecimalFormat;
import org.apache.commons.math3.distribution.NormalDistribution;

/**
 *
 * @author Moliang
 */
public class ConvertedTrace {

    private final String ID;
    private final Date start;
    private final Date end;
//    private final double Round = 10000;//Rounding for matrix
    private final DecimalFormat round = new DecimalFormat("#.00000000");

    private double scale = 1;

    private final ArrayList<Activity> activities;
    private String[] phase;

    private ArrayList<String> Activity_set;
    private double[][] Matrix;
    private int Length = 3600;  //Normalize into 1 hour

    private double[] phase_percentage = {0, 0, 0, 0};
    private int pt_arrival_length = (int) (Length * 0.25);
    private int Primary_length = (int) (Length * 0.25);
    private int Secondary_length = (int) (Length * 0.25);
    private int Pst_secondary_length = (int) (Length * 0.25);

    private String pre_arr = " Pre-Arrival";
    private String pt = " Pt arrival";
    private String pre_pri = " Pre-Primary";

    private String pri = " Primary";
    private String sec = " Secondary";
    private String pst_sec = " Post-Secondary";

    private String pd = " Pt departure";

    private List<Phase> p;

    public ConvertedTrace(Trace t) {
        this.ID = t.get_ID();
        this.activities = (ArrayList<Activity>) t.get_ActivityList().clone();
        this.start = t.get_start();
        this.end = t.get_end();
//        Length = (int) (end.getTime() - start.getTime()) / 1000;  //Dont use this when normalized
        Length = 3600;
        int scaler = 1;
        Length = Length / scaler;
        double duration = (end.getTime() - start.getTime()) / 1000 / scaler; //in seconds
        scale = duration / Length; //scale in 1s
        this.Activity_set = (ArrayList<String>) t.get_Activity_set();

        Matrix = new double[Activity_set.size()][Length];
        phase = new String[Length];
        construct_Matrix();
    }

    public ConvertedTrace(Trace t, List<Phase> p) {
        this.ID = t.get_ID();
        this.activities = (ArrayList<Activity>) t.get_ActivityList().clone();
        this.start = t.get_start();
        this.end = t.get_end();
        this.Activity_set = (ArrayList<String>) t.get_Activity_set();
        this.p = p;

        Matrix = new double[Activity_set.size()][Length];
        phase = new String[Length];
        construct_Matrix_Phase();
        construct_phase();
    }

    private void construct_phase() {
        for (int index = 0; index < pt_arrival_length; index++) {
            phase[index] = pt;
        }
        for (int index = pt_arrival_length; index < pt_arrival_length + Primary_length; index++) {
            phase[index] = pri;
        }
        for (int index = pt_arrival_length + Primary_length; index < pt_arrival_length + Primary_length + Secondary_length; index++) {
            phase[index] = sec;
        }
        for (int index = pt_arrival_length + Primary_length + Secondary_length; index < pt_arrival_length + Primary_length + Secondary_length + Pst_secondary_length; index++) {
            phase[index] = pst_sec;
        }
    }

    private void construct_Matrix_Phase() {
        long time_shift = 0;
        Date cali_st = null;
        Date pt_st = null;
        Date pt_ed = null;
        Date pri_st = null;
        Date pri_ed = null;
        Date sec_st = null;
        Date sec_ed = null;
        Date pst_sec_st = null;
        Date pst_sec_ed = null;
//        Date pd_st = null;
//        Date pd_ed = null;
        //Get all the date
        for (Phase ph : p) {
            if (pt.contains(ph.phaseName)) {
                cali_st = ph.start;
                pt_st = ph.start;
                pt_ed = ph.end;
            }
            if (pre_pri.contains(ph.phaseName)) {       //merge preprimary to patient arrival
                pt_ed = ph.end;
            }
            if (pri.contains(ph.phaseName)) {
                pt_ed = ph.start;
                pri_st = ph.start;
                pri_ed = ph.end;
            }
            if (sec.contains(ph.phaseName)) {
                pri_ed = ph.start;
                sec_st = ph.start;
                sec_ed = ph.end;
            }
            if (pst_sec.contains(ph.phaseName)) {
                sec_ed = ph.start;
                pst_sec_st = ph.start;
                pst_sec_ed = ph.end;
            }
//            if (pd.contains(ph.phaseName)) {
//                pd_st = ph.start;
//                pd_ed = ph.end;
//            }
        }

        if (cali_st != null) {
            time_shift = cali_st.getTime();
        }

//        Calibrate start time
        pt_st = new Date(pt_st.getTime() - time_shift);
        pt_ed = new Date(pt_ed.getTime() - time_shift);
        pri_st = new Date(pri_st.getTime() - time_shift);
        pri_ed = new Date(pri_ed.getTime() - time_shift);
        sec_st = new Date(sec_st.getTime() - time_shift);
        sec_ed = new Date(sec_ed.getTime() - time_shift);
        pst_sec_st = new Date(pst_sec_st.getTime() - time_shift);
        pst_sec_ed = new Date(pst_sec_ed.getTime() - time_shift);

        phase_percentage[0] = (double) (pt_ed.getTime() - pt_st.getTime());
        phase_percentage[1] = (double) (pri_ed.getTime() - pri_st.getTime());
        phase_percentage[2] = (double) (sec_ed.getTime() - sec_st.getTime());
        phase_percentage[3] = (double) (pst_sec_ed.getTime() - pst_sec_st.getTime());

        for (Activity ac : activities) {
            int start_index = 0;
            int end_index = 0;
            Date ac_st = ac.get_startTime();
            Date ac_end = ac.get_endTime();
            if (ac_st.after(pst_sec_ed)) {
                start_index = Length - 1;
                //wrong situation, start after end
            } else if (ac_st.after(pst_sec_st) || ac_st.equals(pst_sec_st)) {
                start_index = pt_arrival_length + Primary_length + Secondary_length + (int) ((double) (ac_st.getTime() - pst_sec_st.getTime()) / phase_percentage[3] * Pst_secondary_length);
            } else if (ac_st.after(sec_st) || ac_st.equals(sec_st)) {
                start_index = pt_arrival_length + Primary_length + (int) ((double) (ac_st.getTime() - sec_st.getTime()) / phase_percentage[2] * Secondary_length);
            } else if (ac_st.after(pri_st) || ac_st.equals(pri_st)) {
                start_index = pt_arrival_length + (int) ((double) (ac_st.getTime() - pri_st.getTime()) / phase_percentage[1] * Primary_length);
            } else if (ac_st.after(pt_st) || ac_st.equals(pt_st)) {
                start_index = (int) ((double) (ac_st.getTime() - pt_st.getTime()) / phase_percentage[0] * pt_arrival_length);
            } else {
                start_index = 0;
            }

            if (ac_end.before(pt_st)) {
                end_index = 0;
                //wrong situation, end before start
            } else if (ac_end.before(pt_ed) || ac_end.equals(pt_ed)) {
                end_index = pt_arrival_length - (int) ((double) (pt_ed.getTime() - ac_end.getTime()) / phase_percentage[0] * pt_arrival_length);
            } else if (ac_end.before(pri_ed) || ac_end.equals(pri_ed)) {
                end_index = pt_arrival_length + Primary_length - (int) ((double) (pri_ed.getTime() - ac_end.getTime()) / phase_percentage[1] * Primary_length);
            } else if (ac_end.before(sec_ed) || ac_end.equals(sec_ed)) {
                end_index = pt_arrival_length + Primary_length + Secondary_length - (int) ((double) (sec_ed.getTime() - ac_end.getTime()) / phase_percentage[2] * Secondary_length);
            } else if (ac_end.before(pst_sec_ed) || ac_end.equals(pst_sec_ed)) {
                end_index = pt_arrival_length + Primary_length + Secondary_length + Pst_secondary_length - (int) ((double) (pst_sec_ed.getTime() - ac_end.getTime()) / phase_percentage[3] * Pst_secondary_length);
            } else {
                end_index = Length;
                //Patient departure, not included
            }
            end_index = end_index >= Length ? Length : end_index; // Avoid out of boundry

            //Set corresponding cells to 1
            int row_index = Activity_set.indexOf(ac.get_name());
            try {
                if (end_index < start_index) {
                    System.out.println("Activity: " + ac.get_name() + " Index incorrect: " + " st:" + start_index + " ed:" + end_index);
                } else {
                    if (end_index == start_index && end_index < Length) {
                        end_index++;
                    }
                    fill_activity(row_index, start_index, end_index);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Activity: " + ac.get_name() + " st:" + start_index + " ed:" + end_index + " OutOfBounds!!!");
            }
            normalize_matrix_row();
        }

        double duration = pst_sec_ed.getTime() - pt_st.getTime();
        phase_percentage[0] /= duration;
        phase_percentage[1] /= duration;
        phase_percentage[2] /= duration;
        phase_percentage[3] /= duration;
    }

    private void construct_Matrix() {
        for (Activity ac : activities) {
            //Check the range by start and end
            int start_index = (int) ((int) (ac.get_startTime().getTime() - this.start.getTime()) / 1000 / scale);
            int end_index = (int) ((int) (ac.get_endTime().getTime() - this.start.getTime()) / 1000 / scale);
            end_index = end_index >= Length ? Length : end_index; // Avoid out of boundry
            //Set corresponding cells to 1
            int row_index = Activity_set.indexOf(ac.get_name());
            fill_activity(row_index, start_index, end_index);

        }
        normalize_matrix_row();
    }

    private void fill_activity(int row, int st, int ed) {
        int mid = (ed + st) / 2;
        double sd = (double) (ed - st) / 4;
        NormalDistribution ND = new NormalDistribution(mid, sd);
        for (int col = 0; col < Length; col++) {
            double pro_density = ND.density(col);
            Matrix[row][col] += pro_density;
        }
//        for (int col = st; col < ed; col++) {
//            Matrix[row][col] = 1;
//        }
    }

    private void normalize_matrix_row() {//Normalize each type of activities to scale 0~1
        for (int row = 0; row < Matrix.length; row++) {
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;
            for (int col = 0; col < Matrix[row].length; col++) {
                min = Matrix[row][col] < min ? Matrix[row][col] : min;
                max = Matrix[row][col] > max ? Matrix[row][col] : max;
            }
            for (int col = 0; col < Matrix[row].length; col++) {
                if ((max - min) == 0) {
                    Matrix[row][col] = 0;
                } else {
                    double val = (Matrix[row][col] - min) / (max - min);
                    Matrix[row][col] = Double.parseDouble(round.format(val));
                }
            }
        }
    }

    public double[][] get_Matrix() {
        return this.Matrix;
    }

    public String get_ID() {
        return this.ID;
    }

    public double get_scale() {
        return this.scale;
    }

    public List<String> get_Activity_set() {
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

    public double[] get_phasePercentage() {
        return this.phase_percentage;
    }
}
