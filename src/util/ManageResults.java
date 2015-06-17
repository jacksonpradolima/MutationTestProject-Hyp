/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import statistics.KruskalWallisTest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path; 
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.util.MetricsUtil;
import jmetal.util.NonDominatedSolutionList;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import statistics.HypervolumeCalculator;

/**
 *
 * @author Prado Lima
 */
public class ManageResults {
  
    /**
     * 
     * @param fun
     * @return the standard deviation
     */
    private static double getStandardDeviation(List<Double> fun) {
        double[] funArray = new double[fun.size()];
        for (int i = 0; i < funArray.length; i++) {
            funArray[i] = fun.get(i);
        }
        StandardDeviation sd = new StandardDeviation();
        return sd.evaluate(funArray);
    }

    /**
     * 
     * @param fun
     * @return return the average
     */
    private static double getAverage(List<Double> fun) {
        double[] funArray = new double[fun.size()];
        for (int i = 0; i < funArray.length; i++) {
            funArray[i] = fun.get(i);
        }
        Mean mean = new Mean();
        return mean.evaluate(funArray);
    }

    /**
     * 
     * @param instances
     * @param algorithms
     * @return the paths containing the results based on the instances and algorithms passed
     */
    static List<Path> getPaths(List<String> instances, List<String> algorithms) {
        final List<Path> paths = new ArrayList<>();
        for (String instance : instances) {
            for (String algorithm : algorithms) {
                String path = "experiment/" + instance + "/" + algorithm;

                try {
                    Path startPath = Paths.get(path);
                    Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                            //System.out.println("Dir: " + dir.toString());
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                            //System.out.println("File: " + file.toString());
                            if (!file.getFileName().toString().contains("KruskalWallisResults") && !file.getFileName().toString().contains("Hypervolume_Results")) {
                                Path currentPath = file.getParent();
                                
                                if (!paths.contains(currentPath)) {
                                    paths.add(currentPath);
                                }
                            }
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFileFailed(Path file, IOException e) {
                            return FileVisitResult.CONTINUE;
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return paths;
    }

    /**
     * Write the hypervolume results for each experiment passed
     * @param paths
     * @param numberOfObjectives
     * @param numberOfExecutions
     * @throws IOException 
     */
    private static void writeHypervolume(List<Path> paths, int numberOfObjectives, int numberOfExecutions) throws IOException {
        HypervolumeCalculator hypervolumeCalculator = new HypervolumeCalculator(numberOfObjectives);

        for (Path path : paths) {
            for (int i = 0; i < numberOfExecutions; i++) {
                if (!path.toString().endsWith("KruskalWallisResults") && !path.toString().endsWith("Hypervolume_Results")) {
                    hypervolumeCalculator.addParetoFront(path.toString() + "/FUN_" + i);
                }
            }
        }
        try (final FileWriter fileWriter = new FileWriter(paths.get(0).getParent().getParent().getParent().toString() + "/Hypervolume_Results")) {
            for (Path path : paths) {
                if (!path.toString().endsWith("KruskalWallisResults") && !path.toString().endsWith("Hypervolume_Results")) {
                    List<Double> allHypervolumes = new ArrayList<>();
                    for (int i = 0; i < numberOfExecutions; i++) {
                        try (FileWriter fileWriterIndividual = new FileWriter(path + "/Hypervolume_" + i)) {
                            double hypervolume = hypervolumeCalculator.execute(path.toString() + "/FUN_" + i);
                            fileWriterIndividual.write("" + hypervolume);
                            allHypervolumes.add(hypervolume);
                        }
                    }

                    try (final FileWriter fileWriter2 = new FileWriter(path + "/Hypervolume_Results")) {
                        //write the hypervolume average and standard deviation in a file for each algorithm
                        fileWriter2.write("Average: " + getAverage(allHypervolumes) + "\n");
                        fileWriter2.write("Standard Deviation: " + getStandardDeviation(allHypervolumes) + "\n");

                        //write the hypervolume average and standard deviation in a file common for all algorithms
                        fileWriter.write("Path: " + path.toString() + "\n");
                        fileWriter.write("Average: " + getAverage(allHypervolumes) + "\n");
                        fileWriter.write("Standard Deviation: " + getStandardDeviation(allHypervolumes) + "\n\n");
                        fileWriter.write("----------------------------------------------------------------------------------\n\n");
                    }
                }
            }
        }
    }

   /**
    * must be passed as parameters the directories with the experiments to be compared and the number of executions performed
    * @param directories
    * @param executions
    * @param archiveSuffix
    * @throws FileNotFoundException
    * @throws IOException
    * @throws InterruptedException 
    */
    private static void doKruskalWallisTest(List<Path> directories, int executions, String archiveSuffix) throws FileNotFoundException, IOException, InterruptedException {
        KruskalWallisTest kruskal = new KruskalWallisTest();
        HashMap<String, double[]> values = new HashMap<>();
        for (Path directory : directories) {
            double[] funArray = new double[executions];
            for (int i = 0; i < executions; i++) {
                String sCurrentLine;
                BufferedReader br = new BufferedReader(new FileReader(directory + "/" + archiveSuffix + i));
                while ((sCurrentLine = br.readLine()) != null) {
                    if (!"".equals(sCurrentLine)) {
                        funArray[i] = Double.parseDouble(sCurrentLine);
                        break;
                    }
                }
                br.close();
            }
            values.put(directory.toString(), funArray);
        }
        writeKruskalWallisTest(directories, kruskal.test(values));
    }

    private static void writeKruskalWallisTest(List<Path> directories, HashMap<String, HashMap<String, Boolean>> resultKruskal) throws IOException {
        if (!directories.isEmpty()) {
            Path parent = directories.get(0).getParent().getParent().getParent();
            File writtenFile = new File(parent + "/KruskalWallisResults_" + directories.get(0).getParent().getParent().getFileName());
            if (!writtenFile.exists()) {
                writtenFile.createNewFile();
            }
            FileWriter fw = new FileWriter(writtenFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < directories.size() - 1; i++) {
                for (int j = i + 1; j < directories.size(); j++) {
                    Boolean difference = resultKruskal.get(directories.get(i).toString()).get(directories.get(j).toString());
                    bw.write("Configs");
                    bw.newLine();
                    bw.write(directories.get(i).toString());
                    bw.newLine();
                    bw.write(directories.get(j).toString());
                    bw.newLine();
                    bw.write("Different? " + difference);
                    bw.newLine();
                    bw.newLine();
                    bw.write("------------------------------------------------------");
                    bw.newLine();
                    bw.newLine();
                }
            }
            bw.close();
            fw.close();
        }
    }

    private static void createPFTrue(List<Path> paths) throws FileNotFoundException, IOException {
        MetricsUtil metricsUtil = new MetricsUtil();
        NonDominatedSolutionList nonDominatedSolutionList = new NonDominatedSolutionList();

        //calculate PFtrue
        for (Path path : paths) {
            SolutionSet solutions = metricsUtil.readNonDominatedSolutionSet(path + "/FUN_All");
            for (int i = 0; i < solutions.size(); i++) {
                nonDominatedSolutionList.add(solutions.get(i));
            }
        }

        ExperimentUtil.removeRepeated(nonDominatedSolutionList);

        //read PFtrue
        double[][] trueFront = nonDominatedSolutionList.writeObjectivesToMatrix();

        //write PFtrue
        try (FileWriter writer = new FileWriter(paths.get(0).getParent().getParent().getParent() + "/Pareto.txt")) {
            writer.write(nonDominatedSolutionList.size() + "\n\n");

            //write PFknown
            for (Path path : paths) {
                int count = 0;
                double[][] solutions = metricsUtil.readFront(path + "/FUN_All");
                for (double[] solution : solutions) {
                    double distance = metricsUtil.distanceToClosedPoint(solution, trueFront);
                    if (distance == 0) {
                        count++;
                    }
                }
                writer.write(path.getParent().toString() + ": ");
                writer.write(solutions.length + " (" + count + ")\n\n");
            }
            writer.close();
        }
    }
    
     public static void calculateHypervolumeResults(List<String> instances, List<String> algorithms, int numberOfObjectives, int numberOfExecutions) throws IOException {
        for (String instance : instances) {
            List<String> instanceAux = new ArrayList<>();
            instanceAux.add(instance);
            List<Path> paths = ManageResults.getPaths(instanceAux, algorithms);
            //write the files to be read
            System.out.println("Instância: " + instance);
            for (Path path : paths) {
                System.out.println("Path: " + path);
            }
            System.out.println("--------------------------------------------------------------");
            ManageResults.writeHypervolume(paths, numberOfObjectives, numberOfExecutions);
        }
    }
     
    public static void calculateKruskalWallisForAlgorithms(List<String> instances, List<String> algorithms, int numberOfExecutions) throws IOException, InterruptedException {
        for (String instance : instances) {
            List<String> instancesKruskal = new ArrayList<>();
            instancesKruskal.add(instance);
            List<Path> paths = ManageResults.getPaths(instancesKruskal, algorithms);
            ManageResults.doKruskalWallisTest(paths, numberOfExecutions, "Hypervolume_");
        }
    }

    public static void calculateKruskalWallisForTuning(List<String> instances, List<String> algorithms, int numberOfExecutions) throws IOException, InterruptedException {
        for (String instance : instances) {
            List<String> instancesKruskal = new ArrayList<>();
            instancesKruskal.add(instance);
            for (String algorithm : algorithms) {
                List<String> algorithmsKruskal = new ArrayList<>();
                algorithmsKruskal.add(algorithm);
                List<Path> paths = ManageResults.getPaths(instancesKruskal, algorithmsKruskal);
                ManageResults.doKruskalWallisTest(paths, numberOfExecutions, "Hypervolume_");
            }
        }
    }

    public static void calculatePFTrue(List<String> instances, List<String> algorithms) throws IOException {
        for (String instance : instances) {
            List<String> instancesKruskal = new ArrayList<>();
            instancesKruskal.add(instance);
            List<Path> paths = ManageResults.getPaths(instancesKruskal, algorithms);
            ManageResults.createPFTrue(paths);
        }
    }
}
