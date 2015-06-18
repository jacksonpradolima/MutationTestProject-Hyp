/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.util.MetricsUtil;

/**
 *
 * @author thaina
 */
public class Normalization {

    public static void main(String[] args) throws IOException {

        List<String> instances = new ArrayList<>();

        instances.add("fourballs");
        instances.add("guizzo_cas");
        instances.add("guizzo_james");
        instances.add("guizzo_save");
        instances.add("guizzo_weatherstation");
        instances.add("mid");
        instances.add("trityp");

        List<String> algorithms = new ArrayList<>();
        algorithms.add("HHIBEA");
        algorithms.add("HHNSGAIII");
        algorithms.add("HHNSGAII");
        algorithms.add("HHSPEA2");
        algorithms.add("R-HHSPEA2");
        algorithms.add("R-HHNSGAII");
        algorithms.add("R-HHIBEA");
        algorithms.add("R-HHNSGAIII");
        //algorithms.add("IBEA");
        //algorithms.add("NSGAIII");

        for (String instance : instances) {
            List<String> instancesKruskal = new ArrayList<>();
            instancesKruskal.add(instance);
            List<Path> paths = ManageResults.getPaths(instancesKruskal, algorithms);
            for (Path path : paths) {
                read(path.toString(), instance);
            }
        }
    }

    public static void read(String path, String instance) throws IOException {
        MetricsUtil metricsUtil = new MetricsUtil();
        for (int i = 0; i < 30; i++) {
            String fun = path + "/FUN_" + i;
            SolutionSet solutions = metricsUtil.readSolutionSet(fun);
            write(solutions, fun, instance);
        }
        String funAll = path + "/FUN_All";
        SolutionSet solutions = metricsUtil.readSolutionSet(funAll);
        write(solutions, funAll, instance);
    }

    public static void write(SolutionSet solutions, String fun, String instance) throws IOException {
        FileWriter writer = new FileWriter(fun);
        Scanner scanner = new Scanner(new File("instances/" + instance + ".txt"));
        int numberOfTestCases = Integer.valueOf(scanner.nextLine());
        for (int i = 0; i < solutions.size(); i++) {
            double mutationScore = (double) solutions.get(i).getObjective(1) * (double) numberOfTestCases;
            writer.write(solutions.get(i).getObjective(0) + " " + mutationScore + "\n");
        }
        writer.close();
        scanner.close();
    }

}
