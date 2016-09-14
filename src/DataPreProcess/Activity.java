/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataPreProcess;

/**
 *
 * @author Moliang
 */
public class Activity {

    private String activity_name;
    private long start;
    private long end;
    private String note;

    public Activity(String activity_name, String start, String end, String note) {
        this.activity_name = activity_name;
        this.start = Long.parseLong(start);
        this.end = Long.parseLong(end);
        this.note = note;
    }

    
    public String get_name() {
        return this.activity_name;
    }

    public long get_startTime() {
        return this.start;
    }

    public long get_endTime() {
        return this.end;
    }
}
