/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.plot;

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
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import util.InstanceReader;

/**
 *
 * @author Prado Lima
 */
public class GeneratePlot {

    public static void main(String[] args) throws IOException {
        plotCasNSGAII();
        plotCasSPEA2();
        plotFourballsNSGAII();
        plotFourballsSPEA2();
        plotJamesNSGAII();
        plotJamesSPEA2();
        plotMidNSGAII();
        plotMidSPEA2();
        plotSaveNSGAII();
        plotSaveSPEA2();
        plotTritypNSGAII();
        plotTritypSPEA2();
        plotWeatherstationNSGAII();
        plotWeatherstationSPEA2();
    }

    public static void plotCasNSGAII() throws IOException {
        List<PlotPoint> plotPoints = new ArrayList<>();

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleHHNSGAII = new PlotStyle();
        plotStyleHHNSGAII.setStyle(Style.POINTS);
        plotStyleHHNSGAII.setLineWidth(2);
        plotStyleHHNSGAII.setLineType(NamedPlotColor.RED);

        PlotPoint plotPointHHNSGAII = new PlotPoint();
        plotPointHHNSGAII.setDataPath("experiment\\guizzo_cas\\HHNSGAII\\ChoiceFunction\\200-600-0.05\\FUN_ALL");
        plotPointHHNSGAII.setTitlePoint("HHNSGAII");
        plotPointHHNSGAII.setPlotStyle(plotStyleHHNSGAII);

        plotPoints.add(plotPointHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleRHHNSGAII = new PlotStyle();
        plotStyleRHHNSGAII.setStyle(Style.POINTS);
        plotStyleRHHNSGAII.setLineWidth(2);
        plotStyleRHHNSGAII.setLineType(NamedPlotColor.GREEN);

        PlotPoint plotPointRHHNSGAII = new PlotPoint();
        plotPointRHHNSGAII.setDataPath("experiment\\guizzo_cas\\R-HHNSGAII\\200-600\\FUN_ALL");
        plotPointRHHNSGAII.setTitlePoint("R-HHNSGAII");
        plotPointRHHNSGAII.setPlotStyle(plotStyleRHHNSGAII);

        plotPoints.add(plotPointRHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleNSGAII = new PlotStyle();
        plotStyleNSGAII.setStyle(Style.POINTS);
        plotStyleNSGAII.setLineType(NamedPlotColor.BLUE);

        PlotPoint plotPointNSGAII = new PlotPoint();
        plotPointNSGAII.setDataPath("experiment\\guizzo_cas\\NSGAII\\200_600_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30\\FUN_ALL_NORMALIZED");
        plotPointNSGAII.setTitlePoint("NSGAII");
        plotPointNSGAII.setPlotStyle(plotStyleNSGAII);

        //plotPoints.add(plotPointNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        plot2lines("Escore de Mutação", "Número de Casos de Teste",
                0.0, 1.0,
                0.0, 1.0,
                plotPoints,
                "Fronteira de Pareto",
                "/plots/cas_nsgaII.png", "png");
    }

    public static void plotCasSPEA2() throws IOException {
        List<PlotPoint> plotPoints = new ArrayList<>();

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleHHNSGAII = new PlotStyle();
        plotStyleHHNSGAII.setStyle(Style.POINTS);
        plotStyleHHNSGAII.setLineWidth(2);
        plotStyleHHNSGAII.setLineType(NamedPlotColor.RED);

        PlotPoint plotPointHHNSGAII = new PlotPoint();
        plotPointHHNSGAII.setDataPath("experiment\\guizzo_cas\\HHSPEA2\\ChoiceFunction\\200-600-0.05\\FUN_ALL");
        plotPointHHNSGAII.setTitlePoint("HHSPEA2");
        plotPointHHNSGAII.setPlotStyle(plotStyleHHNSGAII);

        plotPoints.add(plotPointHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleRHHNSGAII = new PlotStyle();
        plotStyleRHHNSGAII.setStyle(Style.POINTS);
        plotStyleRHHNSGAII.setLineWidth(2);
        plotStyleRHHNSGAII.setLineType(NamedPlotColor.GREEN);

        PlotPoint plotPointRHHNSGAII = new PlotPoint();
        plotPointRHHNSGAII.setDataPath("experiment\\guizzo_cas\\R-HHSPEA2\\200-600\\FUN_ALL");
        plotPointRHHNSGAII.setTitlePoint("R-HHSPEA2");
        plotPointRHHNSGAII.setPlotStyle(plotStyleRHHNSGAII);

        plotPoints.add(plotPointRHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleNSGAII = new PlotStyle();
        plotStyleNSGAII.setStyle(Style.POINTS);
        plotStyleNSGAII.setLineType(NamedPlotColor.BLUE);

        PlotPoint plotPointNSGAII = new PlotPoint();
        plotPointNSGAII.setDataPath("experiment\\guizzo_cas\\SPEA2\\200_600_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30\\FUN_ALL_NORMALIZED");
        plotPointNSGAII.setTitlePoint("SPEA2");
        plotPointNSGAII.setPlotStyle(plotStyleNSGAII);

        //plotPoints.add(plotPointNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        plot2lines("Escore de Mutação", "Número de Casos de Teste",
                0.0, 1.0,
                0.0, 1.0,
                plotPoints,
                "Fronteira de Pareto",
                "/plots/cas_spea2.png", "png");
    }

    public static void plotFourballsNSGAII() throws IOException {
        List<PlotPoint> plotPoints = new ArrayList<>();

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleHHNSGAII = new PlotStyle();
        plotStyleHHNSGAII.setStyle(Style.POINTS);
        plotStyleHHNSGAII.setLineWidth(2);
        plotStyleHHNSGAII.setLineType(NamedPlotColor.RED);

        PlotPoint plotPointHHNSGAII = new PlotPoint();
        plotPointHHNSGAII.setDataPath("experiment\\fourballs\\HHNSGAII\\ChoiceFunction\\200-600-0.00005\\FUN_ALL");
        plotPointHHNSGAII.setTitlePoint("HHNSGAII");
        plotPointHHNSGAII.setPlotStyle(plotStyleHHNSGAII);

        plotPoints.add(plotPointHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleRHHNSGAII = new PlotStyle();
        plotStyleRHHNSGAII.setStyle(Style.POINTS);
        plotStyleRHHNSGAII.setLineWidth(2);
        plotStyleRHHNSGAII.setLineType(NamedPlotColor.GREEN);

        PlotPoint plotPointRHHNSGAII = new PlotPoint();
        plotPointRHHNSGAII.setDataPath("experiment\\fourballs\\R-HHNSGAII\\200-600\\FUN_ALL");
        plotPointRHHNSGAII.setTitlePoint("R-HHNSGAII");
        plotPointRHHNSGAII.setPlotStyle(plotStyleRHHNSGAII);

        plotPoints.add(plotPointRHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleNSGAII = new PlotStyle();
        plotStyleNSGAII.setStyle(Style.POINTS);
        plotStyleNSGAII.setLineType(NamedPlotColor.BLUE);

        PlotPoint plotPointNSGAII = new PlotPoint();
        plotPointNSGAII.setDataPath("experiment\\fourballs\\NSGAII\\200_600_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30\\FUN_ALL_NORMALIZED");
        plotPointNSGAII.setTitlePoint("NSGAII");
        plotPointNSGAII.setPlotStyle(plotStyleNSGAII);

        //plotPoints.add(plotPointNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        plot2lines("Escore de Mutação", "Número de Casos de Teste",
                0.0, 1.0,
                0.0, 1.0,
                plotPoints,
                "Fronteira de Pareto",
                "/plots/fourballs_nsgaII.png", "png");
    }

    public static void plotFourballsSPEA2() throws IOException {
        List<PlotPoint> plotPoints = new ArrayList<>();

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleHHNSGAII = new PlotStyle();
        plotStyleHHNSGAII.setStyle(Style.POINTS);
        plotStyleHHNSGAII.setLineWidth(2);
        plotStyleHHNSGAII.setLineType(NamedPlotColor.RED);

        PlotPoint plotPointHHNSGAII = new PlotPoint();
        plotPointHHNSGAII.setDataPath("experiment\\fourballs\\HHSPEA2\\ChoiceFunction\\200-600-0.0005\\FUN_ALL");
        plotPointHHNSGAII.setTitlePoint("HHSPEA2");
        plotPointHHNSGAII.setPlotStyle(plotStyleHHNSGAII);

        plotPoints.add(plotPointHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleRHHNSGAII = new PlotStyle();
        plotStyleRHHNSGAII.setStyle(Style.POINTS);
        plotStyleRHHNSGAII.setLineWidth(2);
        plotStyleRHHNSGAII.setLineType(NamedPlotColor.GREEN);

        PlotPoint plotPointRHHNSGAII = new PlotPoint();
        plotPointRHHNSGAII.setDataPath("experiment\\fourballs\\R-HHSPEA2\\200-600\\FUN_ALL");
        plotPointRHHNSGAII.setTitlePoint("R-HHSPEA2");
        plotPointRHHNSGAII.setPlotStyle(plotStyleRHHNSGAII);

        plotPoints.add(plotPointRHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleNSGAII = new PlotStyle();
        plotStyleNSGAII.setStyle(Style.POINTS);
        plotStyleNSGAII.setLineType(NamedPlotColor.BLUE);

        PlotPoint plotPointNSGAII = new PlotPoint();
        plotPointNSGAII.setDataPath("experiment\\fourballs\\SPEA2\\200_600_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30\\FUN_ALL_NORMALIZED");
        plotPointNSGAII.setTitlePoint("SPEA2");
        plotPointNSGAII.setPlotStyle(plotStyleNSGAII);

        //plotPoints.add(plotPointNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        plot2lines("Escore de Mutação", "Número de Casos de Teste",
                0.0, 1.0,
                0.0, 1.0,
                plotPoints,
                "Fronteira de Pareto",
                "/plots/fourballs_spea2.png", "png");
    }

    public static void plotJamesNSGAII() throws IOException {
        List<PlotPoint> plotPoints = new ArrayList<>();

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleHHNSGAII = new PlotStyle();
        plotStyleHHNSGAII.setStyle(Style.POINTS);
        plotStyleHHNSGAII.setLineWidth(2);
        plotStyleHHNSGAII.setLineType(NamedPlotColor.RED);

        PlotPoint plotPointHHNSGAII = new PlotPoint();
        plotPointHHNSGAII.setDataPath("experiment\\guizzo_james\\HHNSGAII\\ChoiceFunction\\200-600-0.05\\FUN_ALL");
        plotPointHHNSGAII.setTitlePoint("HHNSGAII");
        plotPointHHNSGAII.setPlotStyle(plotStyleHHNSGAII);

        plotPoints.add(plotPointHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleRHHNSGAII = new PlotStyle();
        plotStyleRHHNSGAII.setStyle(Style.POINTS);
        plotStyleRHHNSGAII.setLineWidth(2);
        plotStyleRHHNSGAII.setLineType(NamedPlotColor.GREEN);

        PlotPoint plotPointRHHNSGAII = new PlotPoint();
        plotPointRHHNSGAII.setDataPath("experiment\\guizzo_james\\R-HHNSGAII\\200-600\\FUN_ALL");
        plotPointRHHNSGAII.setTitlePoint("R-HHNSGAII");
        plotPointRHHNSGAII.setPlotStyle(plotStyleRHHNSGAII);

        plotPoints.add(plotPointRHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleNSGAII = new PlotStyle();
        plotStyleNSGAII.setStyle(Style.POINTS);
        plotStyleNSGAII.setLineType(NamedPlotColor.BLUE);

        PlotPoint plotPointNSGAII = new PlotPoint();
        plotPointNSGAII.setDataPath("experiment\\guizzo_james\\NSGAII\\200_600_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30\\FUN_ALL_NORMALIZED");
        plotPointNSGAII.setTitlePoint("NSGAII");
        plotPointNSGAII.setPlotStyle(plotStyleNSGAII);

        //plotPoints.add(plotPointNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        plot2lines("Escore de Mutação", "Número de Casos de Teste",
                0.0, 1.0,
                0.0, 1.0,
                plotPoints,
                "Fronteira de Pareto",
                "/plots/james_nsgaII.png", "png");
    }

    public static void plotJamesSPEA2() throws IOException {
        List<PlotPoint> plotPoints = new ArrayList<>();

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleHHNSGAII = new PlotStyle();
        plotStyleHHNSGAII.setStyle(Style.POINTS);
        plotStyleHHNSGAII.setLineWidth(2);
        plotStyleHHNSGAII.setLineType(NamedPlotColor.RED);

        PlotPoint plotPointHHNSGAII = new PlotPoint();
        plotPointHHNSGAII.setDataPath("experiment\\guizzo_james\\HHSPEA2\\ChoiceFunction\\200-600-0.05\\FUN_ALL");
        plotPointHHNSGAII.setTitlePoint("HHSPEA2");
        plotPointHHNSGAII.setPlotStyle(plotStyleHHNSGAII);

        plotPoints.add(plotPointHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleRHHNSGAII = new PlotStyle();
        plotStyleRHHNSGAII.setStyle(Style.POINTS);
        plotStyleRHHNSGAII.setLineWidth(2);
        plotStyleRHHNSGAII.setLineType(NamedPlotColor.GREEN);

        PlotPoint plotPointRHHNSGAII = new PlotPoint();
        plotPointRHHNSGAII.setDataPath("experiment\\guizzo_james\\R-HHSPEA2\\200-600\\FUN_ALL");
        plotPointRHHNSGAII.setTitlePoint("R-HHSPEA2");
        plotPointRHHNSGAII.setPlotStyle(plotStyleRHHNSGAII);

        plotPoints.add(plotPointRHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleNSGAII = new PlotStyle();
        plotStyleNSGAII.setStyle(Style.POINTS);
        plotStyleNSGAII.setLineType(NamedPlotColor.BLUE);

        PlotPoint plotPointNSGAII = new PlotPoint();
        plotPointNSGAII.setDataPath("experiment\\guizzo_james\\SPEA2\\200_600_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30\\FUN_ALL_NORMALIZED");
        plotPointNSGAII.setTitlePoint("SPEA2");
        plotPointNSGAII.setPlotStyle(plotStyleNSGAII);

        //plotPoints.add(plotPointNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        plot2lines("Escore de Mutação", "Número de Casos de Teste",
                0.0, 1.0,
                0.0, 1.0,
                plotPoints,
                "Fronteira de Pareto",
                "/plots/james_spea2.png", "png");
    }

    public static void plotSaveNSGAII() throws IOException {
        List<PlotPoint> plotPoints = new ArrayList<>();

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleHHNSGAII = new PlotStyle();
        plotStyleHHNSGAII.setStyle(Style.POINTS);
        plotStyleHHNSGAII.setLineWidth(2);
        plotStyleHHNSGAII.setLineType(NamedPlotColor.RED);

        PlotPoint plotPointHHNSGAII = new PlotPoint();
        plotPointHHNSGAII.setDataPath("experiment\\guizzo_save\\HHNSGAII\\ChoiceFunction\\200-600-0.05\\FUN_ALL");
        plotPointHHNSGAII.setTitlePoint("HHNSGAII");
        plotPointHHNSGAII.setPlotStyle(plotStyleHHNSGAII);

        plotPoints.add(plotPointHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleRHHNSGAII = new PlotStyle();
        plotStyleRHHNSGAII.setStyle(Style.POINTS);
        plotStyleRHHNSGAII.setLineWidth(2);
        plotStyleRHHNSGAII.setLineType(NamedPlotColor.GREEN);

        PlotPoint plotPointRHHNSGAII = new PlotPoint();
        plotPointRHHNSGAII.setDataPath("experiment\\guizzo_save\\R-HHNSGAII\\200-600\\FUN_ALL");
        plotPointRHHNSGAII.setTitlePoint("R-HHNSGAII");
        plotPointRHHNSGAII.setPlotStyle(plotStyleRHHNSGAII);

        plotPoints.add(plotPointRHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleNSGAII = new PlotStyle();
        plotStyleNSGAII.setStyle(Style.POINTS);
        plotStyleNSGAII.setLineType(NamedPlotColor.BLUE);

        PlotPoint plotPointNSGAII = new PlotPoint();
        plotPointNSGAII.setDataPath("experiment\\guizzo_save\\NSGAII\\200_600_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30\\FUN_ALL_NORMALIZED");
        plotPointNSGAII.setTitlePoint("NSGAII");
        plotPointNSGAII.setPlotStyle(plotStyleNSGAII);

        //plotPoints.add(plotPointNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        plot2lines("Escore de Mutação", "Número de Casos de Teste",
                0.0, 1.0,
                0.0, 1.0,
                plotPoints,
                "Fronteira de Pareto",
                "/plots/save_nsgaII.png", "png");
    }

    public static void plotSaveSPEA2() throws IOException {
        List<PlotPoint> plotPoints = new ArrayList<>();

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleHHNSGAII = new PlotStyle();
        plotStyleHHNSGAII.setStyle(Style.POINTS);
        plotStyleHHNSGAII.setLineWidth(2);
        plotStyleHHNSGAII.setLineType(NamedPlotColor.RED);

        PlotPoint plotPointHHNSGAII = new PlotPoint();
        plotPointHHNSGAII.setDataPath("experiment\\guizzo_save\\HHSPEA2\\ChoiceFunction\\200-600-0.05\\FUN_ALL");
        plotPointHHNSGAII.setTitlePoint("HHSPEA2");
        plotPointHHNSGAII.setPlotStyle(plotStyleHHNSGAII);

        plotPoints.add(plotPointHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleRHHNSGAII = new PlotStyle();
        plotStyleRHHNSGAII.setStyle(Style.POINTS);
        plotStyleRHHNSGAII.setLineWidth(2);
        plotStyleRHHNSGAII.setLineType(NamedPlotColor.GREEN);

        PlotPoint plotPointRHHNSGAII = new PlotPoint();
        plotPointRHHNSGAII.setDataPath("experiment\\guizzo_save\\R-HHSPEA2\\200-600\\FUN_ALL");
        plotPointRHHNSGAII.setTitlePoint("R-HHSPEA2");
        plotPointRHHNSGAII.setPlotStyle(plotStyleRHHNSGAII);

        plotPoints.add(plotPointRHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleNSGAII = new PlotStyle();
        plotStyleNSGAII.setStyle(Style.POINTS);
        plotStyleNSGAII.setLineType(NamedPlotColor.BLUE);

        PlotPoint plotPointNSGAII = new PlotPoint();
        plotPointNSGAII.setDataPath("experiment\\guizzo_save\\SPEA2\\200_600_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30\\FUN_ALL_NORMALIZED");
        plotPointNSGAII.setTitlePoint("SPEA2");
        plotPointNSGAII.setPlotStyle(plotStyleNSGAII);

        //plotPoints.add(plotPointNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        plot2lines("Escore de Mutação", "Número de Casos de Teste",
                0.0, 1.0,
                0.0, 1.0,
                plotPoints,
                "Fronteira de Pareto",
                "/plots/save_spea2.png", "png");
    }

    public static void plotWeatherstationNSGAII() throws IOException {
        List<PlotPoint> plotPoints = new ArrayList<>();

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleHHNSGAII = new PlotStyle();
        plotStyleHHNSGAII.setStyle(Style.POINTS);
        plotStyleHHNSGAII.setLineWidth(2);
        plotStyleHHNSGAII.setLineType(NamedPlotColor.RED);

        PlotPoint plotPointHHNSGAII = new PlotPoint();
        plotPointHHNSGAII.setDataPath("experiment\\guizzo_weatherstation\\HHNSGAII\\ChoiceFunction\\200-600-0.005\\FUN_ALL");
        plotPointHHNSGAII.setTitlePoint("HHNSGAII");
        plotPointHHNSGAII.setPlotStyle(plotStyleHHNSGAII);

        plotPoints.add(plotPointHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleRHHNSGAII = new PlotStyle();
        plotStyleRHHNSGAII.setStyle(Style.POINTS);
        plotStyleRHHNSGAII.setLineWidth(2);
        plotStyleRHHNSGAII.setLineType(NamedPlotColor.GREEN);

        PlotPoint plotPointRHHNSGAII = new PlotPoint();
        plotPointRHHNSGAII.setDataPath("experiment\\guizzo_weatherstation\\R-HHNSGAII\\200-600\\FUN_ALL");
        plotPointRHHNSGAII.setTitlePoint("R-HHNSGAII");
        plotPointRHHNSGAII.setPlotStyle(plotStyleRHHNSGAII);

        plotPoints.add(plotPointRHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleNSGAII = new PlotStyle();
        plotStyleNSGAII.setStyle(Style.POINTS);
        plotStyleNSGAII.setLineType(NamedPlotColor.BLUE);

        PlotPoint plotPointNSGAII = new PlotPoint();
        plotPointNSGAII.setDataPath("experiment\\guizzo_weatherstation\\NSGAII\\200_600_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30\\FUN_ALL_NORMALIZED");
        plotPointNSGAII.setTitlePoint("NSGAII");
        plotPointNSGAII.setPlotStyle(plotStyleNSGAII);

        //plotPoints.add(plotPointNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        plot2lines("Escore de Mutação", "Número de Casos de Teste",
                0.0, 1.0,
                0.0, 1.0,
                plotPoints,
                "Fronteira de Pareto",
                "/plots/weatherstation_nsgaII.png", "png");
    }

    public static void plotWeatherstationSPEA2() throws IOException {
        List<PlotPoint> plotPoints = new ArrayList<>();

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleHHNSGAII = new PlotStyle();
        plotStyleHHNSGAII.setStyle(Style.POINTS);
        plotStyleHHNSGAII.setLineWidth(2);
        plotStyleHHNSGAII.setLineType(NamedPlotColor.RED);

        PlotPoint plotPointHHNSGAII = new PlotPoint();
        plotPointHHNSGAII.setDataPath("experiment\\guizzo_weatherstation\\HHSPEA2\\ChoiceFunction\\200-600-0.005\\FUN_ALL");
        plotPointHHNSGAII.setTitlePoint("HHSPEA2");
        plotPointHHNSGAII.setPlotStyle(plotStyleHHNSGAII);

        plotPoints.add(plotPointHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleRHHNSGAII = new PlotStyle();
        plotStyleRHHNSGAII.setStyle(Style.POINTS);
        plotStyleRHHNSGAII.setLineWidth(2);
        plotStyleRHHNSGAII.setLineType(NamedPlotColor.GREEN);

        PlotPoint plotPointRHHNSGAII = new PlotPoint();
        plotPointRHHNSGAII.setDataPath("experiment\\guizzo_weatherstation\\R-HHSPEA2\\200-600\\FUN_ALL");
        plotPointRHHNSGAII.setTitlePoint("R-HHSPEA2");
        plotPointRHHNSGAII.setPlotStyle(plotStyleRHHNSGAII);

        plotPoints.add(plotPointRHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleNSGAII = new PlotStyle();
        plotStyleNSGAII.setStyle(Style.POINTS);
        plotStyleNSGAII.setLineType(NamedPlotColor.BLUE);

        PlotPoint plotPointNSGAII = new PlotPoint();
        plotPointNSGAII.setDataPath("experiment\\guizzo_weatherstation\\SPEA2\\200_600_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30\\FUN_ALL_NORMALIZED");
        plotPointNSGAII.setTitlePoint("SPEA2");
        plotPointNSGAII.setPlotStyle(plotStyleNSGAII);

        //plotPoints.add(plotPointNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        plot2lines("Escore de Mutação", "Número de Casos de Teste",
                0.0, 1.0,
                0.0, 1.0,
                plotPoints,
                "Fronteira de Pareto",
                "/plots/weatherstation_spea2.png", "png");
    }

    public static void plotMidNSGAII() throws IOException {
        List<PlotPoint> plotPoints = new ArrayList<>();

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleHHNSGAII = new PlotStyle();
        plotStyleHHNSGAII.setStyle(Style.POINTS);
        plotStyleHHNSGAII.setLineWidth(2);
        plotStyleHHNSGAII.setLineType(NamedPlotColor.RED);

        PlotPoint plotPointHHNSGAII = new PlotPoint();
        plotPointHHNSGAII.setDataPath("experiment\\mid\\HHNSGAII\\ChoiceFunction\\200-600-0.05\\FUN_ALL");
        plotPointHHNSGAII.setTitlePoint("HHNSGAII");
        plotPointHHNSGAII.setPlotStyle(plotStyleHHNSGAII);

        plotPoints.add(plotPointHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleRHHNSGAII = new PlotStyle();
        plotStyleRHHNSGAII.setStyle(Style.POINTS);
        plotStyleRHHNSGAII.setLineWidth(2);
        plotStyleRHHNSGAII.setLineType(NamedPlotColor.GREEN);

        PlotPoint plotPointRHHNSGAII = new PlotPoint();
        plotPointRHHNSGAII.setDataPath("experiment\\mid\\R-HHNSGAII\\200-600\\FUN_ALL");
        plotPointRHHNSGAII.setTitlePoint("R-HHNSGAII");
        plotPointRHHNSGAII.setPlotStyle(plotStyleRHHNSGAII);

        plotPoints.add(plotPointRHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleNSGAII = new PlotStyle();
        plotStyleNSGAII.setStyle(Style.POINTS);
        plotStyleNSGAII.setLineType(NamedPlotColor.BLUE);

        PlotPoint plotPointNSGAII = new PlotPoint();
        plotPointNSGAII.setDataPath("experiment\\mid\\NSGAII\\200_600_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30\\FUN_ALL_NORMALIZED");
        plotPointNSGAII.setTitlePoint("NSGAII");
        plotPointNSGAII.setPlotStyle(plotStyleNSGAII);

        //plotPoints.add(plotPointNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        plot2lines("Escore de Mutação", "Número de Casos de Teste",
                0.0, 1.0,
                0.0, 1.0,
                plotPoints,
                "Fronteira de Pareto",
                "/plots/mid_nsgaII.png", "png");
    }

    public static void plotMidSPEA2() throws IOException {
        List<PlotPoint> plotPoints = new ArrayList<>();

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleHHNSGAII = new PlotStyle();
        plotStyleHHNSGAII.setStyle(Style.POINTS);
        plotStyleHHNSGAII.setLineWidth(2);
        plotStyleHHNSGAII.setLineType(NamedPlotColor.RED);

        PlotPoint plotPointHHNSGAII = new PlotPoint();
        plotPointHHNSGAII.setDataPath("experiment\\mid\\HHSPEA2\\ChoiceFunction\\200-600-0.05\\FUN_ALL");
        plotPointHHNSGAII.setTitlePoint("HHSPEA2");
        plotPointHHNSGAII.setPlotStyle(plotStyleHHNSGAII);

        plotPoints.add(plotPointHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleRHHNSGAII = new PlotStyle();
        plotStyleRHHNSGAII.setStyle(Style.POINTS);
        plotStyleRHHNSGAII.setLineWidth(2);
        plotStyleRHHNSGAII.setLineType(NamedPlotColor.GREEN);

        PlotPoint plotPointRHHNSGAII = new PlotPoint();
        plotPointRHHNSGAII.setDataPath("experiment\\mid\\R-HHSPEA2\\200-600\\FUN_ALL");
        plotPointRHHNSGAII.setTitlePoint("R-HHSPEA2");
        plotPointRHHNSGAII.setPlotStyle(plotStyleRHHNSGAII);

        plotPoints.add(plotPointRHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleNSGAII = new PlotStyle();
        plotStyleNSGAII.setStyle(Style.POINTS);
        plotStyleNSGAII.setLineType(NamedPlotColor.BLUE);

        PlotPoint plotPointNSGAII = new PlotPoint();
        plotPointNSGAII.setDataPath("experiment\\mid\\SPEA2\\200_600_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30\\FUN_ALL_NORMALIZED");
        plotPointNSGAII.setTitlePoint("SPEA2");
        plotPointNSGAII.setPlotStyle(plotStyleNSGAII);

        //plotPoints.add(plotPointNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        plot2lines("Escore de Mutação", "Número de Casos de Teste",
                0.0, 1.0,
                0.0, 1.0,
                plotPoints,
                "Fronteira de Pareto",
                "/plots/mid_spea2.png", "png");
    }

    public static void plotTritypNSGAII() throws IOException {
        List<PlotPoint> plotPoints = new ArrayList<>();

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleHHNSGAII = new PlotStyle();
        plotStyleHHNSGAII.setStyle(Style.POINTS);
        plotStyleHHNSGAII.setLineWidth(2);
        plotStyleHHNSGAII.setLineType(NamedPlotColor.RED);

        PlotPoint plotPointHHNSGAII = new PlotPoint();
        plotPointHHNSGAII.setDataPath("experiment\\trityp\\HHNSGAII\\ChoiceFunction\\200-600-0.05\\FUN_ALL");
        plotPointHHNSGAII.setTitlePoint("HHNSGAII");
        plotPointHHNSGAII.setPlotStyle(plotStyleHHNSGAII);

        plotPoints.add(plotPointHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleRHHNSGAII = new PlotStyle();
        plotStyleRHHNSGAII.setStyle(Style.POINTS);
        plotStyleRHHNSGAII.setLineWidth(2);
        plotStyleRHHNSGAII.setLineType(NamedPlotColor.GREEN);

        PlotPoint plotPointRHHNSGAII = new PlotPoint();
        plotPointRHHNSGAII.setDataPath("experiment\\trityp\\R-HHNSGAII\\200-600\\FUN_ALL");
        plotPointRHHNSGAII.setTitlePoint("R-HHNSGAII");
        plotPointRHHNSGAII.setPlotStyle(plotStyleRHHNSGAII);

        plotPoints.add(plotPointRHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleNSGAII = new PlotStyle();
        plotStyleNSGAII.setStyle(Style.POINTS);
        plotStyleNSGAII.setLineType(NamedPlotColor.BLUE);

        PlotPoint plotPointNSGAII = new PlotPoint();
        plotPointNSGAII.setDataPath("experiment\\trityp\\NSGAII\\200_600_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30\\FUN_ALL_NORMALIZED");
        plotPointNSGAII.setTitlePoint("NSGAII");
        plotPointNSGAII.setPlotStyle(plotStyleNSGAII);

        //plotPoints.add(plotPointNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        plot2lines("Escore de Mutação", "Número de Casos de Teste",
                0.0, 1.0,
                0.0, 1.0,
                plotPoints,
                "Fronteira de Pareto",
                "/plots/trityp_nsgaII.png", "png");
    }

    public static void plotTritypSPEA2() throws IOException {
        List<PlotPoint> plotPoints = new ArrayList<>();

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleHHNSGAII = new PlotStyle();
        plotStyleHHNSGAII.setStyle(Style.POINTS);
        plotStyleHHNSGAII.setLineWidth(2);
        plotStyleHHNSGAII.setLineType(NamedPlotColor.RED);

        PlotPoint plotPointHHNSGAII = new PlotPoint();
        plotPointHHNSGAII.setDataPath("experiment\\trityp\\HHSPEA2\\ChoiceFunction\\200-600-0.05\\FUN_ALL");
        plotPointHHNSGAII.setTitlePoint("HHSPEA2");
        plotPointHHNSGAII.setPlotStyle(plotStyleHHNSGAII);

        plotPoints.add(plotPointHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleRHHNSGAII = new PlotStyle();
        plotStyleRHHNSGAII.setStyle(Style.POINTS);
        plotStyleRHHNSGAII.setLineWidth(2);
        plotStyleRHHNSGAII.setLineType(NamedPlotColor.GREEN);

        PlotPoint plotPointRHHNSGAII = new PlotPoint();
        plotPointRHHNSGAII.setDataPath("experiment\\trityp\\R-HHSPEA2\\200-600\\FUN_ALL");
        plotPointRHHNSGAII.setTitlePoint("R-HHSPEA2");
        plotPointRHHNSGAII.setPlotStyle(plotStyleRHHNSGAII);

        plotPoints.add(plotPointRHHNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        PlotStyle plotStyleNSGAII = new PlotStyle();
        plotStyleNSGAII.setStyle(Style.POINTS);
        plotStyleNSGAII.setLineType(NamedPlotColor.BLUE);

        PlotPoint plotPointNSGAII = new PlotPoint();
        plotPointNSGAII.setDataPath("experiment\\trityp\\SPEA2\\200_600_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30\\FUN_ALL_NORMALIZED");
        plotPointNSGAII.setTitlePoint("SPEA2");
        plotPointNSGAII.setPlotStyle(plotStyleNSGAII);

        //plotPoints.add(plotPointNSGAII);

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        plot2lines("Escore de Mutação", "Número de Casos de Teste",
                0.0, 1.0,
                0.0, 1.0,
                plotPoints,
                "Fronteira de Pareto",
                "/plots/trityp_spea2.png", "png");
    }

    public static void runExample() throws IOException {
        plot2lines("Escore de Mutação", "Número de Casos de Teste",
                0.0, 1.0,
                0.0, 30.0,
                "NSGA", "SPEA2",
                "experiment/guizzo_cas/NSGAII/F2/100_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30/FUN_All.txt",
                "experiment/guizzo_cas/SPEA2/F2/200_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30/FUN_All.txt",
                "Fronteira de Pareto",
                "/plots/plot.png", "png");
    }

    private static void plot2lines(
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

    private static void plot2lines(
            String labelX, String labelY,
            double rangeStartX, double rangeEndX,
            double rangeStartY, double rangeEndY,
            List<PlotPoint> plotPoints,
            String titleGraph,
            String pathSave, String fileExtension) throws IOException {

        System.out.println("Salvando o arquivo em: " + pathSave);
        ImageTerminal png = new ImageTerminal();       
        File file = new File(System.getProperty("user.dir") + pathSave);
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

        for (PlotPoint plotPoint : plotPoints) {
            // Read data file A
            InstanceReader reader = new InstanceReader(plotPoint.getDataPath());
            reader.open();
            double[][] values = reader.readDoubleMatrix(" ", true);
            reader.close();

            DataSetPlot dataSet = new DataSetPlot(values);
            dataSet.setPlotStyle(plotPoint.getPlotStyle());
            dataSet.setTitle(plotPoint.getTitlePoint());

            p.addPlot(dataSet);
        }

        p.setTitle(titleGraph);
        //p.newGraph3D();
        p.plot();

        try {
            ImageIO.write(png.getImage(), fileExtension, file);
            System.out.println("Arquivo salvo com sucesso.");
        } catch (IOException ex) {
            System.err.print(ex);
        }
    }
}
