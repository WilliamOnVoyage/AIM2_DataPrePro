/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import DataPreProcess.Activity;
import DataPreProcess.Phase;
import DataPreProcess.Trace;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author Moliang
 */
public class ReadFile {

    private final String[] TRACE_HEADER_MAPPING = {"CaseID", "Activity", "StartTime", "CompleteTime", "Timestamp"};
    private final String[] PHASE_HEADER_MAPPING = {"start time", "end time", "category", "Nth instance", "# descriptors", "descriptors..."};
    private final String st_time = "start time";
    private final String end_time = "end time";
    private final String category = "category";
    private final String Nth = "Nth instance";
    private final String No_des = "# descriptors";
    private final String des = "descriptors...";

    private final String CaseID = "CaseID";
    private final String Activity = "Activity";
    private final String StartTime = "StartTime";
    private final String CompleteTime = "CompleteTime";
    private final String Timestamp = "Timestamp";

    private List<Trace> traces;

    public boolean readTrace(String fileName) {
        FileReader fileReader;
        CSVParser csvFileParser;
        boolean isSuccess = true;
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(TRACE_HEADER_MAPPING);

        try {
            ArrayList<String> Activity_set = new ArrayList<String>();
            HashSet<String> ID_set = new HashSet<String>();
            traces = new ArrayList<Trace>();
            //initialize FileReader object
            System.out.println(fileName);
            fileReader = new FileReader(fileName);

            //initialize CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);
            //Get a list of CSV file records
            List<CSVRecord> csvRecords = csvFileParser.getRecords();
            Trace t = new Trace("");
            //Read the CSV file records starting from the second record to skip the header
            for (int i = 1; i < csvRecords.size(); i++) {
                CSVRecord record = csvRecords.get(i);
                String ID = record.get(CaseID);
                if (!ID_set.contains(ID) || (i == csvRecords.size() - 1)) {
                    //Discard void trace
                    if (i != 1) {
                        traces.add(t);
                    }
                    ID_set.add(ID);
                    t = new Trace(ID);
                }
                Activity ac = new Activity(record.get(Activity), record.get(StartTime), record.get(CompleteTime), record.get(Timestamp));
                t.add_activity(ac);

                if (!Activity_set.contains(ac.get_name())) {
                    Activity_set.add(ac.get_name());
                }
            }
            //sort activity set by string
            Collections.sort(Activity_set);
            
            //sort trace by ID
            Collections.sort(traces, new Comparator<Trace>() {
                @Override
                public int compare(Trace t1, Trace t2) {
                    return Integer.parseInt(t1.get_ID()) < Integer.parseInt(t2.get_ID()) ? -1 : 1;
                }
            });
            //Set activity set for each trace
            for (Trace T : traces) {
                T.set_ActivitySet((List<String>) Activity_set.clone());
            }

        } catch (Exception e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
            isSuccess = false;
            return isSuccess;
        }
        if (isSuccess) {
            try {
                fileReader.close();
                csvFileParser.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader/csvFileParser !!!");
            }
        }
        return isSuccess;
    }

    public List<Phase> readPhase(String fileName) {
        FileReader fileReader;
        CSVParser csvFileParser;
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(PHASE_HEADER_MAPPING);
        List<Phase> phase_list = new ArrayList<>();
        try {
            fileReader = new FileReader(fileName);
            //initialize CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);
            //Get a list of CSV file records
            List<CSVRecord> csvRecords = csvFileParser.getRecords();
            //Read the CSV file records starting from the second record to skip the header
            for (int i = 1; i < csvRecords.size(); i++) {
                CSVRecord record = csvRecords.get(i);
                Phase p = new Phase(record.get(st_time), record.get(end_time), record.get(category));
                phase_list.add(p);
            }
            fileReader.close();
            csvFileParser.close();
            System.out.println(fileName + " Phase file read!");
        } catch (FileNotFoundException e) {
            System.out.println(fileName + " Phase file missing ...");
            return null;
        } catch (IOException ex) {
            System.out.println(fileName + " csv file error !!!");
            return null;
        } catch (ParseException ex) {
            System.out.println(fileName + " phase parsing error !!!");
            return null;
        }
        return phase_list;
    }

    public List<Trace> get_Traces() {
        return this.traces;
    }
}
