/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataPreProcess;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Moliang
 */
public class DataConvert {

    private static List<ConvertedTrace> ConvertedTraces;

    public static List<ConvertedTrace> Convert(List<Trace> traces) {
        ConvertedTraces = new ArrayList<>();
        for (Trace t : traces) {
            ConvertedTrace ct = new ConvertedTrace(t);
            ConvertedTraces.add(ct);
        }
        return ConvertedTraces;
    }

}
