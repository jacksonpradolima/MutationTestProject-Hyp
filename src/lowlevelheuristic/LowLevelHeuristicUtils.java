/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lowlevelheuristic;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import jmetal.util.PseudoRandom;

/**
 *
 * @author Prado Lima
 */
public class LowLevelHeuristicUtils {

    public LowLevelHeuristicUtils() {
        this.lowLevelHeuristics = new ArrayList<>();
    }

    private final List<LowLevelHeuristic> lowLevelHeuristics;
    private FileWriter lowLevelHeuristicsRankWriter;
    private FileWriter lowLevelHeuristicsTimeWriter;
    
    public List<LowLevelHeuristic> getLowLevelHeuristics()
    {
        return this.lowLevelHeuristics;
    }

    public LowLevelHeuristic addLowLevelHeuristic(HashMap<String, Object> parameters) {
        LowLevelHeuristic lowLevelHeuristic = new LowLevelHeuristic(parameters);
        if (!lowLevelHeuristics.contains(lowLevelHeuristic)) {
            lowLevelHeuristics.add(lowLevelHeuristic);
            return lowLevelHeuristic;
        } else {
            return null;
        }
    }

    public LowLevelHeuristic removeLowLevelHeuristic(String name) {
        for (int i = 0; i < lowLevelHeuristics.size(); i++) {
            LowLevelHeuristic lowLevelHeuristic = lowLevelHeuristics.get(i);
            if (lowLevelHeuristic.getName().equals(name)) {
                return lowLevelHeuristics.remove(i);
            }
        }
        return null;
    }

    public void clearLowLeverHeuristics() {
        lowLevelHeuristics.clear();
    }

    public void clearLowLeverHeuristicsValues() {
        LowLevelHeuristic.clearAllStaticValues();
        for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
            lowLevelHeuristic.clearAllValues();
        }
    }

    public int[] getLowLevelHeuristicsNumberOfTimesApplied() {
        int[] allTimesApplied = new int[lowLevelHeuristics.size()];
        for (int i = 0; i < lowLevelHeuristics.size(); i++) {
            LowLevelHeuristic lowLevelHeuristic = lowLevelHeuristics.get(i);
            allTimesApplied[i] = lowLevelHeuristic.getNumberOfTimesApplied();
        }
        return allTimesApplied;
    }

    public int getLowLevelHeuristicsSize() {
        return lowLevelHeuristics.size();
    }

    public void setLowLevelHeuristicsRankPath(String path) throws IOException {
        if (lowLevelHeuristicsRankWriter != null) {
            lowLevelHeuristicsRankWriter.close();
        }
        lowLevelHeuristicsRankWriter = new FileWriter(path);
    }

    public void setLowLevelHeuristicsTimePath(String path) throws IOException {
        if (lowLevelHeuristicsTimeWriter != null) {
            lowLevelHeuristicsTimeWriter.close();
        }
        lowLevelHeuristicsTimeWriter = new FileWriter(path);
    }

    public void closeLowLevelHeuristicsRankPath() throws IOException {
        if (lowLevelHeuristicsRankWriter != null) {
            lowLevelHeuristicsRankWriter.close();
        }
    }

    public LowLevelHeuristic getApplyingHeuristic(String heuristicFunction, Comparator<LowLevelHeuristic> comparator) {
        if (heuristicFunction.equals(LowLevelHeuristic.RANDOM) || comparator == null) {
            return lowLevelHeuristics.get(PseudoRandom.randInt(0, lowLevelHeuristics.size() - 1));
        } else {
            List<LowLevelHeuristic> allLowLevelHeuristics = new ArrayList<>(lowLevelHeuristics);
            Collections.sort(allLowLevelHeuristics, comparator);
            List<LowLevelHeuristic> applyingHeuristics = new ArrayList<>();

            //Find the best tied heuristics
            Iterator<LowLevelHeuristic> iterator = allLowLevelHeuristics.iterator();
            LowLevelHeuristic heuristic;
            LowLevelHeuristic nextHeuristic = iterator.next();
            do {
                heuristic = nextHeuristic;
                applyingHeuristics.add(heuristic);
            } while (iterator.hasNext() && comparator.compare(heuristic, nextHeuristic = iterator.next()) == 0);

            return applyingHeuristics.get(PseudoRandom.randInt(0, applyingHeuristics.size() - 1));
        }
    }
}
