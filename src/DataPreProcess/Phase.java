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
public class Phase {

    private final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss"); //Time format
    public String phaseName;
    public Date start;
    public Date end;

    public Phase(String start, String end, String name) throws ParseException {
        this.phaseName = name;
        this.start = timeFormat.parse(start);
        this.end = timeFormat.parse(end);
    }
}
