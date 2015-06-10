/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.panayotis.gnuplot.GNUPlotException;
import com.panayotis.gnuplot.GNUPlotParameters;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.dataset.FileDataSet;
import com.panayotis.gnuplot.layout.StripeLayout;
import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.swing.JPlot;
import com.panayotis.gnuplot.terminal.ImageTerminal;
import com.panayotis.gnuplot.terminal.PostscriptTerminal;
import com.panayotis.gnuplot.terminal.SVGTerminal;
import com.panayotis.gnuplot.utils.Debug;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Math.sin;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * This Object is used to demonstrate JavaPlot library
 *
 * @author Prado Lima
 */
public class DemoJavaPlot {

    /**
     * @param args the command line arguments. First argument is the path of
     * gnuplot application
     */
    public static void main(String[] args) {
        String path = null;
        if (args.length > 0) {
            path = args[0];
        }

        if (path == null) {
            // gnuplot aplication
            path = "C:\\Program Files (x86)\\gnuplot\\bin\\gnuplot.exe";
        }

        //simple();
        //simple3D();
        //defaultTerminal(path);
        //EPSTerminal(path);
        //SVGTerminal(path);
        //JPlotTerminal(path);
        //serialization(defaultTerminal(path));
        //file();
        //demo();
        //demo2();
        //demo3();
        demo4();
    }

    /* This is a very simple plot to demonstrate JavaPlot graphs */
    private static void simple() {
        JavaPlot p = new JavaPlot();
        p.addPlot("sin(x)");
        p.plot();
    }

    /* This is a very simple plot to demonstrate JavaPlot 3d graphs */
    private static void simple3D() {
        JavaPlot p = new JavaPlot(true);
        p.addPlot("sin(x)*y");
        p.plot();
    }

    /* This demo code uses default terminal. Use it as reference for other javaplot arguments  */
    private static JavaPlot defaultTerminal(String gnuplotpath) {
        JavaPlot p = new JavaPlot(gnuplotpath);
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);

        p.setTitle("Default Terminal Title");
        p.getAxis("x").setLabel("X axis", "Arial", 20);
        p.getAxis("y").setLabel("Y axis");

        p.getAxis("x").setBoundaries(-30, 20);
        p.setKey(JavaPlot.Key.TOP_RIGHT);

        double[][] plot = {{1, 1.1}, {2, 2.2}, {3, 3.3}, {4, 4.3}};
        DataSetPlot s = new DataSetPlot(plot);
        p.addPlot(s);
        p.addPlot("besj0(x)*0.12e1");
        PlotStyle stl = ((AbstractPlot) p.getPlots().get(1)).getPlotStyle();
        stl.setStyle(Style.POINTS);
        stl.setLineType(NamedPlotColor.GOLDENROD);
        stl.setPointType(5);
        stl.setPointSize(8);
        p.addPlot("sin(x)");

        p.newGraph();
        p.addPlot("sin(x)");

        p.newGraph3D();
        double[][] plot3d = {{1, 1.1, 3}, {2, 2.2, 3}, {3, 3.3, 3.4}, {4, 4.3, 5}};
        p.addPlot(plot3d);

        p.newGraph3D();
        p.addPlot("sin(x)*sin(y)");

        p.setMultiTitle("Global test title");
        StripeLayout lo = new StripeLayout();
        lo.setColumns(9999);
        p.getPage().setLayout(lo);
        p.plot();

        return p;
    }

    /* This demo code creates a EPS file on home directory */
    private static JavaPlot EPSTerminal(String gnuplotpath) {
        JavaPlot p = new JavaPlot();

        PostscriptTerminal epsf = new PostscriptTerminal(System.getProperty("user.home")
                + System.getProperty("file.separator") + "output.eps");
        epsf.setColor(true);
        p.setTerminal(epsf);

        p.setTitle("Postscript Terminal Title");
        p.addPlot("sin (x)");
        p.addPlot("sin(x)*cos(x)");
        p.newGraph();
        p.addPlot("cos(x)");
        p.setTitle("Trigonometric functions -1");
        p.setMultiTitle("Trigonometric functions");
        p.plot();
        return p;
    }

    /* This demo code displays plot on screen using image terminal */
    private static JavaPlot JPlotTerminal(String gnuplotpath) {
        JPlot plot = new JPlot();
        plot.getJavaPlot().addPlot("sqrt(x)/x");
        plot.getJavaPlot().addPlot("x*sin(x)");
        plot.plot();

        JFrame f = new JFrame();
        f.getContentPane().add(plot);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        return plot.getJavaPlot();
    }

    /* This demo code displays plot on screen using SVG commands (only b&w) */
    private static JavaPlot SVGTerminal(String gnuplotpath) {
        JavaPlot p = new JavaPlot();
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);

        SVGTerminal svg = new SVGTerminal();
        p.setTerminal(svg);

        p.setTitle("SVG Terminal Title");
        p.addPlot("x+3");
        p.plot();

        try {
            JFrame f = new JFrame();
            f.getContentPane().add(svg.getPanel());
            f.pack();
            f.setLocationRelativeTo(null);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
        } catch (ClassNotFoundException ex) {
            System.err.println("Error: Library SVGSalamander not properly installed?");
        }

        return p;
    }

    private static void serialization(JavaPlot p) {
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream("koko.lala"));
            out.writeObject(p.getParameters());

            in = new ObjectInputStream(new FileInputStream("koko.lala"));
            JavaPlot q = new JavaPlot((GNUPlotParameters) in.readObject());
            q.plot();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /* This is a simple plot to demonstrate file datasets */
    private static void file() {
        try {
            JavaPlot p = new JavaPlot();
            p.addPlot(new FileDataSet(new File("lala")));
            p.plot();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void demo() {
        JavaPlot p = new JavaPlot();
        double tab[][];

        tab = new double[2][2];
        tab[0][0] = 0.0000;
        tab[0][1] = 2.0000;
        tab[1][0] = 1.0000;
        tab[1][1] = 6.0000;
        PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.LINES);
        DataSetPlot s = new DataSetPlot(tab);
        myPlotStyle.setLineWidth(1);
        DataSetPlot testDataSetPlot = new DataSetPlot(tab);
        //DataSetPlot.setPlotStyle(myPlotStyle);
        s.setPlotStyle(myPlotStyle);
        //p.newGraph();
        p.addPlot(s);
        p.newGraph();
        p.plot();
    }

    private static void demo2() {
        JavaPlot testPlot = new JavaPlot(); // (create graph style)

        PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.LINES);
        myPlotStyle.setLineWidth(2);

        double[][] myArray = new double[100][2];
        for (int j = 0; j < 100; j++) {
            myArray[j][0] = j + 1;     // x mean
            myArray[j][1] = sin(j);  // y mean
        }

        // Create dataset for graph on the basis of array
        DataSetPlot testDataSetPlot = new DataSetPlot(myArray);

        // set graph style
        testDataSetPlot.setPlotStyle(myPlotStyle);
        // set graph title
        testPlot.setTitle("Title test");

        testPlot.addPlot(testDataSetPlot);
        //plot title
        testPlot.setTitle("test");
        try {
            testPlot.plot();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    private static void demo3() {
        JavaPlot testPlot = new JavaPlot();

        PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.LINES);
        myPlotStyle.setLineWidth(2);

        File testFile = new File("test.txt");
        FileDataSet testFileDataSet;
        DataSetPlot testDataSetPlot;

        try {
            testFileDataSet = new FileDataSet(testFile);
            testDataSetPlot = new DataSetPlot(testFileDataSet);

            testDataSetPlot.setPlotStyle(myPlotStyle);
            testDataSetPlot.setTitle("test");

            testPlot.addPlot(testDataSetPlot);
            testPlot.setTitle("Title test");
            testPlot.plot();
        } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException | GNUPlotException ex) {
            System.out.println(ex.toString());
        }
    }

    public static void demo4() {
        double[][] values = new double[3][2];
        values[0][0] = 0.1;
        values[0][1] = 0.3;
        values[1][0] = 0.4;
        values[1][1] = 0.3;
        values[2][0] = 0.5;
        values[2][1] = 0.5;

        double[][] values2 = new double[3][2];
        values2[0][0] = 0.2;
        values2[0][1] = 0.0;
        values2[1][0] = 0.7;
        values2[1][1] = 0.1;
        values2[2][0] = 0.6;
        values2[2][1] = 0.5;

        PlotStyle styleDeleted = new PlotStyle();
        styleDeleted.setStyle(Style.POINTS);
        styleDeleted.setLineType(NamedPlotColor.GRAY80);

        PlotStyle styleExist = new PlotStyle();
        styleExist.setStyle(Style.POINTS);
        styleExist.setLineType(NamedPlotColor.BLACK);

        DataSetPlot setDeleted = new DataSetPlot(values);
        setDeleted.setPlotStyle(styleDeleted);
        setDeleted.setTitle("deleted EMs");

        DataSetPlot setExist = new DataSetPlot(values2);
        setExist.setPlotStyle(styleExist);
        setExist.setTitle("remaining EMs");

        ImageTerminal png = new ImageTerminal();
        File file = new File(System.getProperty("user.dir") + "/plots/plot.png");
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

        p.getAxis("x").setLabel("yield");
        p.getAxis("y").setLabel("biomass");
        p.getAxis("x").setBoundaries(0.0, 1.0);
        p.getAxis("y").setBoundaries(0.0, 1.0);
        p.addPlot(setDeleted);
        p.addPlot(setExist);
        p.setTitle("remaining EMs");
        p.plot();

        try {
            ImageIO.write(png.getImage(), "png", file);
        } catch (IOException ex) {
            System.err.print(ex);
        }
    }
}
