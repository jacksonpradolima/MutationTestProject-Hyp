/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.terminal.ImageTerminal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Prado Lima
 */
public class GeneratePlot {

    public static void main(String[] args) throws IOException {
        plot2lines("Escore de Mutação", "Número de Casos de Teste",
                0.0, 1.0,
                0.0, 30.0,
                "NSGA", "SPEA2",
                "experiment/guizzo_cas/NSGAII/F2/100_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30/FUN_All.txt",
                "experiment/guizzo_cas/SPEA2/F2/200_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30/FUN_All.txt",
                "Fronteira de Pareto",
                System.getProperty("user.dir") + "/plots/plot.png", "png");
    }

    public static void plot2lines(
            String labelX, String labelY,
            double rangeStartX, double rangeEndX,
            double rangeStartY, double rangeEndY,
            String titlePointA, String titlePointB,
            String dataPathA, String dataPathB,
            String titleGraph,
            String pathSave, String fileExtension) throws IOException {

        PlotStyle styleDeleted = new PlotStyle();
        styleDeleted.setStyle(Style.POINTS);
        styleDeleted.setLineType(NamedPlotColor.RED);

        PlotStyle styleExist = new PlotStyle();
        styleExist.setStyle(Style.POINTS);
        styleExist.setLineType(NamedPlotColor.BLACK);

        // Read data file A
        InstanceReader readerA = new InstanceReader(dataPathA);
        readerA.open();
        double[][] valuesA = readerA.readDoubleMatrix(" ", true);
        readerA.close();

        DataSetPlot setDeleted = new DataSetPlot(valuesA);
        setDeleted.setPlotStyle(styleDeleted);
        setDeleted.setTitle(titlePointA);

        // Read data file B
        InstanceReader readerB = new InstanceReader(dataPathB);
        readerB.open();
        double[][] valuesB = readerB.readDoubleMatrix(" ", true);
        readerB.close();

        DataSetPlot setExist = new DataSetPlot(valuesB);
        setExist.setPlotStyle(styleExist);
        setExist.setTitle(titlePointB);

        ImageTerminal png = new ImageTerminal();
        File file = new File(pathSave);
        try {
            file.createNewFile();
            png.processOutput(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            System.err.print(ex);
        } catch (IOException ex) {
            System.err.print(ex);
        }

        JavaPlot p = new JavaPlot();
        p.setTerminal(png);
        //p.set("size", "square");

        p.getAxis("x").setLabel(labelX);
        p.getAxis("y").setLabel(labelY);
        p.getAxis("x").setBoundaries(rangeStartX, rangeEndX);
        p.getAxis("y").setBoundaries(rangeStartY, rangeEndY);
        p.addPlot(setDeleted);
        p.addPlot(setExist);
        p.setTitle(titleGraph);
        //p.newGraph3D();
        p.plot();

        try {
            ImageIO.write(png.getImage(), fileExtension, file);
        } catch (IOException ex) {
            System.err.print(ex);
        }
    }
}
