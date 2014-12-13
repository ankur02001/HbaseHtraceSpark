  // ****************************************************************************
  // Group 4: Ankur Pandey , Tushar Gupta , Nupur Bhonge , Tushar Bhatia , Nisha Choudhary 
  //  REFERENCE: http://www.object-refinery.com/jfreechart/guide.html                     *
  // ****************************************************************************

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.io.*;
import java.util.StringTokenizer;

public class BarChartDemo extends ApplicationFrame {

    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
    public BarChartDemo(final String title) throws Exception {

        super(title);
        try{
        final CategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);
         }catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }

    }

    /**
     * Returns a sample dataset.
     * 
     * @return The dataset.
     */
    private CategoryDataset createDataset()  throws Exception{

	    // row keys...
	    final String series1 = "Gets";
	    final String series2 = "RecoverableZookeeper.exists";
	    final String series3 = "RecoverableZookeeper.getData";
	    final String series4 = "ClientService.Get";
	    final String series5 = "HFileReaderV2.readBlock";


	    // column keys...
	    final String category1 = "Gets";
	    final String category2 = "RecoverableZookeeper.exists";
	    final String category3 = "RecoverableZookeeper.getData";
	    final String category4 = "ClientService.Get";
	    final String category5 = "HFileReaderV2.readBlock";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	FileReader in = null;
	BufferedReader br1=null;
	String TOKEN = ":";
	try{
	
	// getting data from data analysis
        in = new FileReader("/teaching/14f-cis655/proj-dtracing/DataAnalysis/Data_Analysis.txt");
	br1 = new BufferedReader(in);
	String line1;
	boolean hasChildNode = false;
	int getCount=0 , RecCount=0,RecDataCount=0,clientCount=0,Hfilecount=0;
        while ((line1 = br1.readLine()) != null) {
		StringBuffer result = new StringBuffer();
		String parentID = "";
		StringTokenizer str1 = new StringTokenizer(line1, TOKEN);
		String ss = str1.nextToken();
		String ss1 = str1.nextToken();
		double yValue = Double.parseDouble(ss1);
		if(ss.equals("Gets")){
			dataset.addValue(yValue, series1, category1);
		}
		if(ss.equals("RecoverableZookeeper.exists")){
			dataset.addValue(yValue, series2, category2);
		}
		if(ss.equals("RecoverableZookeeper.getData")){
			dataset.addValue(yValue, series3, category3);
		}
		if(ss.equals("ClientService.Get")){
			dataset.addValue(yValue, series4, category4);
		}
		if(ss.equals("HFileReaderV2.readBlock")){
			dataset.addValue(yValue, series5, category5);
		}
	}
        } finally {
                        if (in != null) {
                                in.close();
                        }
                }

        return dataset;
        
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private JFreeChart createChart(final CategoryDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
            "Mean Deviation in Processing Time",         // chart title
            "Functions ->",               // domain axis label
            "Time (msec) ->",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
     
        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
       // rangeAxis.setStandardTickUnits(0.5);
         rangeAxis.setTickUnit(new NumberTickUnit(0.25));
        // disable bar outlines...
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        
        // set up gradient paints for series...
        final GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f, Color.blue, 
            0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.green, 
            0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp2 = new GradientPaint(
            0.0f, 0.0f, Color.red, 
            0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp3 = new GradientPaint(
            0.0f, 0.0f, Color.orange,
            0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp4 = new GradientPaint(
            0.0f, 0.0f, Color.black,
            0.0f, 0.0f, Color.lightGray
        );

	renderer.setSeriesPaint(0, gp0);
	renderer.setSeriesPaint(1, gp1);
	renderer.setSeriesPaint(2, gp2); 
	renderer.setSeriesPaint(3, gp3);
	renderer.setSeriesPaint(4, gp4);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        // OPTIONAL CUSTOMISATION COMPLETED.
        
        return chart;
        
    }
    

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {
	    try{
		    final BarChartDemo demo = new BarChartDemo("Bar Chart Demo");
		    demo.pack();
		    RefineryUtilities.centerFrameOnScreen(demo);
		    demo.setVisible(true);
	    }catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
	    }

    }

}
