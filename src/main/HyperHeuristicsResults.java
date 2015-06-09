/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import experiment.HyperHeuristicType;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import util.ManageResults;

/**
 *
 * @author Prado Lima
 */
public class HyperHeuristicsResults {

    public static void main(String[] args) throws IOException, FileNotFoundException, InterruptedException {

        List<String> instances = new ArrayList<>();

        //instances.add("find");
        //instances.add("fourballs");
        instances.add("guizzo_cas");
        //instances.add("guizzo_james");
        //instances.add("guizzo_save");
        //instances.add("guizzo_weatherstation");
        //instances.add("mid");
        //instances.add("trityp");

        List<String> algorithms = new ArrayList<>();
        //algorithms.add(HyperHeuristicType.HHIBEA.toString());
        algorithms.add(HyperHeuristicType.HHNSGAII.toString());
        //algorithms.add(HyperHeuristicType.HHNSGAIII.toString());
        //algorithms.add(HyperHeuristicType.HHSPEA2.toString());

        int numberOfObjectives = 5;
        int numberOfExecutions = 30; 

        ManageResults manageResults = new ManageResults();

        manageResults.calculateHypervolumeResults(instances, algorithms, numberOfObjectives, numberOfExecutions);
        //calculateKruskalWallisForTuning(instances, algorithms, numberOfExecutions);
        //calculateKruskalWallisForAlgorithms(instances, algorithms);
        //calculatePFTrue(instances, algorithms, numberOfExecutions);
    }
}
