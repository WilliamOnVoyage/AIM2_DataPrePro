/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import DataPreProcess.Activity;
import DataPreProcess.Trace;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author Moliang
 */
public class ReadFile {

    private final String[] FILE_HEADER_MAPPING = {"CaseID", "Activity", "StartTime", "CompleteTime", "Timestamp"};
    private final String CaseID = "CaseID";
    private final String Activity = "Activity";
    private final String StartTime = "StartTime";
    private final String CompleteTime = "CompleteTime";
    private final String Timestamp = "Timestamp";

    private List<Trace> traces;

    public boolean readCsvFile(String fileName) {
        FileReader fileReader;
        CSVParser csvFileParser;
        boolean isSuccess = true;
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);

        try {
            HashMap<String, Integer> Activity_set = new HashMap<String, Integer>();
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
            int activity_index = 0;
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

                if (!Activity_set.containsKey(record.get(Activity))) {
                    Activity_set.put(record.get(Activity), activity_index);
                    activity_index++;
                }
            }
            for (Trace t_ : traces) {
                t_.set_ActivitySet((HashMap<String, Integer>) Activity_set.clone());
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

    public List<Trace> get_Traces() {
        return this.traces;
    }
}
