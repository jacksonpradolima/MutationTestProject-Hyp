/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem;

import jmetal.encodings.variable.Binary;
import util.InstanceReader;

/**
 *
 * @author Prado Lima
 */
public class MTProblemBase {

    private int[][] coverageMutantes;

    private int numberOfTestSuite;

    private int numberOfMutants;

    public MTProblemBase(int numberOfTestSuite, int numberOfMutants, int[][] coverageMutantes) {
        this.coverageMutantes = coverageMutantes;
        this.numberOfMutants = numberOfMutants;
        this.numberOfTestSuite = numberOfTestSuite;
    }

    public MTProblemBase(String filename) {
        readMutants(filename);
    }

    private void readMutants(String filename) {
        System.out.println("Nome do arquivo: " + filename);

        // Read Instance's file
        InstanceReader reader = new InstanceReader(filename);

        reader.open();
        this.numberOfTestSuite = reader.readInt();
        this.numberOfMutants = reader.readInt();
        this.coverageMutantes = reader.readIntMatrix(numberOfMutants, numberOfTestSuite, " ");
        reader.close();

        System.out.println("Numero de  casos de teste (produtos): " + this.numberOfTestSuite);
        System.out.println("Numero de mutantes: " + this.numberOfMutants);
    }

    public double getTestScore(Binary s) {
        return (double) (getNumberOfSelectedTestSuite(s) / this.numberOfTestSuite);
    }

    public double getMutantionScore(Binary s) {
        int deadMutants = getNumberOfDifferentKilledMutants(s); //dm(p,t)
        double totalMutants = numberOfMutants; //m(p)
        return (double) deadMutants / (double) totalMutants; //ms(p,t)
    }

    /**
     *
     * @param solution
     * @return Number of test cases (Products)
     */
    public double getNumberOfSelectedTestSuite(Binary solution) {
        if (solution == null) {
            throw new IllegalArgumentException("Solution cannot be null");
        }

        int total = 0;

        for (int i = 0; i < solution.getNumberOfBits(); i++) {
            if (solution.getIth(i)) {
                total++;
            }
        }

        return (double) total;
    }

    public int getNumberOfDifferentKilledMutants(Binary solution) {
        if (solution == null) {
            throw new IllegalArgumentException("Solution cannot be null");
        }

        int[] visited = new int[numberOfMutants];
        int total = 0;

        for (int i = 0; i < solution.getNumberOfBits(); i++) {
            if (solution.getIth(i)) {
                // Test Suite was selected by metaheurist
                for (int j = 0; j < numberOfMutants; j++) {
                    if (coverageMutantes[j][i] == 1 && visited[j] == 0) {
                        // Test Suit has not yet been visited
                        visited[j] = 1;
                        total++;
                    }
                }
            }
        }

        return total;
    }

    public int getNumberOfTestSuite() {
        return numberOfTestSuite;
    }

    public void setNumberOfTestSuite(int numberOfTestSuite) {
        this.numberOfTestSuite = numberOfTestSuite;
    }

    public int getNumberOfMutants() {
        return numberOfMutants;
    }

    public void setNumberOfMutants(int numberOfMutants) {
        this.numberOfMutants = numberOfMutants;
    }

    public boolean isKilled(int testSuite, int mutant) {
        return coverageMutantes[mutant][testSuite] == 1;
    }
}
