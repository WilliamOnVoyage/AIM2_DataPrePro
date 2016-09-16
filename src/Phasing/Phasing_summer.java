/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Phasing;

import DataPreProcess.ConvertedTrace;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Moliang
 */
public class Phasing_summer {

    private static String[] phase;

    private static enum phase_code {
        start, A, B, C, P, G
    }

    public static String[] Divide_Phase_Summer(ConvertedTrace t) {
        //Phase: A B C G P
        int length = t.get_length();
        phase = new String[length];
        for (int i = 0; i < phase.length; i++) {
            phase[i] = phase_code.start.toString();
        }
        HashSet<phase_code> phase_stack = new HashSet<>();
        phase_stack.add(phase_code.start);
        int[][] matrix = t.get_Matrix();
        for (int index = 1; index < length; index++) {
            if (matrix[t.get_Activity_set().get("Airway")][index] == 1) {
                if ((!phase_stack.contains(phase_code.B)) && (!phase_stack.contains(phase_code.C)) && (!phase_stack.contains(phase_code.G) && (!phase_stack.contains(phase_code.P)))) {
                    phase[index] = phase_code.A.toString();
                    phase_stack.add(phase_code.A);
                } else {
                    phase[index] = phase[index - 1];
                }
            } else if (matrix[t.get_Activity_set().get("Breath")][index] == 1) {
                if ((!phase_stack.contains(phase_code.C)) && (!phase_stack.contains(phase_code.G) && (!phase_stack.contains(phase_code.P)))) {
                    phase[index] = phase_code.B.toString();
                    phase_stack.add(phase_code.B);
                } else {
                    phase[index] = phase[index - 1];
                }
            } else if (matrix[t.get_Activity_set().get("Circulation")][index] == 1) {
                if ((!phase_stack.contains(phase_code.G) && (!phase_stack.contains(phase_code.P)))) {
                    phase[index] = phase_code.C.toString();
                    phase_stack.add(phase_code.C);
                } else {
                    phase[index] = phase[index - 1];
                }
            } else if (matrix[t.get_Activity_set().get("GCS")][index] == 1) {
                if ((!phase_stack.contains(phase_code.P))) {
                    phase[index] = phase_code.G.toString();
                    phase_stack.add(phase_code.G);
                } else {
                    phase[index] = phase[index - 1];
                }
            } else if (matrix[t.get_Activity_set().get("Pupils")][index] == 1) {
                if (true) {
                    phase[index] = phase_code.P.toString();
                    phase_stack.add(phase_code.P);
                } else {
                    phase[index] = phase[index - 1];
                }
            } else {
                phase[index] = phase[index - 1];
            }
        }

        return phase.clone();
    }

    private static void Check_phase() {
    }
}
