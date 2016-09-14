/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataPreProcess;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Moliang
 */
public class Activity {

    private String activity_name;
    private Date start;
    private Date end;
    private String note;
    private final DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss"); //Time format

    public Activity(String activity_name, String start, String end, String note) throws ParseException {
        this.activity_name = activity_name;
        this.start = timeFormat.parse(start);
        this.end = timeFormat.parse(end);
        this.note = note;
    }

    public String get_name() {
        return this.activity_name;
    }

    public Date get_startTime() {
        return this.start;
    }

    public Date get_endTime() {
        return this.end;
    }

    public String get_Note() {
        return this.note;
    }
}
