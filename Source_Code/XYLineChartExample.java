  // ****************************************************************************
  // Group 4: Ankur Pandey , Tushar Gupta , Nupur Bhonge , Tushar Bhatia , Nisha Choudhary 
  //  REFERENCE: http://www.object-refinery.com/jfreechart/guide.html                     *
  // ****************************************************************************
  
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.*;
import java.util.StringTokenizer;

/**
 * This program demonstrates how to draw XY line chart with XYDataset
 * using JFreechart library.
 * @author www.codejava.net
 *
 */
public class XYLineChartExample extends JFrame {

	public XYLineChartExample() {
		super("XY Line Chart Example with JFreechart");
		try{
		JPanel chartPanel = createChartPanel();
		add(chartPanel, BorderLayout.CENTER);
		
		setSize(640, 480);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
                 }catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }

	}
	
	private JPanel createChartPanel() throws Exception{
		String chartTitle = "Objects Movement Chart";
		String xAxisLabel = "X";
		String yAxisLabel = "Y";
		XYDataset dataset = null ;
		try{
		dataset = createDataset();
                 }catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        } 
          
		JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, 
				xAxisLabel, yAxisLabel, dataset);
		
//		boolean showLegend = false;
//		boolean createURL = false;
//		boolean createTooltip = false;
//		
//		JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, 
//				xAxisLabel, yAxisLabel, dataset, 
//				PlotOrientation.HORIZONTAL, showLegend, createTooltip, createURL);

		customizeChart(chart);

		// saves the chart as an image files
		File imageFile = new File("XYLineChart.png");
		int width = 640;
		int height = 480;

		try {
			ChartUtilities.saveChartAsPNG(imageFile, chart, width, height);
		} catch (IOException ex) {
			System.err.println(ex);
		}

		return new ChartPanel(chart);
	}

	private XYDataset createDataset() throws Exception{
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series1 = new XYSeries("Gets");
		XYSeries series2 = new XYSeries("RecoverableZookeeper.exists");
		XYSeries series3 = new XYSeries("RecoverableZookeeper.getData");
		XYSeries series4 = new XYSeries("ClientService.Get");
		XYSeries series5 = new XYSeries("HFileReaderV2.readBlock");
                 FileReader in = null;
                BufferedReader br1=null; 
               try{
		String TOKEN = ":";
		
		// getting data from Htrace_out .
		in = new FileReader("/teaching/14f-cis655/proj-dtracing/DataAnalysis/Htrace_out.txt");
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
			int  yValue = Integer.parseInt(ss1);
			if(ss.equals("Gets")){
				series1.add(++getCount, yValue);
			}
			if(ss.equals("RecoverableZookeeper.exists")){
				series2.add(++RecCount, yValue);
			}
			if(ss.equals("RecoverableZookeeper.getData")){
				series3.add(++RecDataCount, yValue);
			}
			if(ss.equals("ClientService.Get")){
				series4.add(++clientCount, yValue);
			}
			if(ss.equals("HFileReaderV2.readBlock")){
				series5.add(++Hfilecount, yValue);
			}
		}
              } finally {
                        if (in != null) {
                                in.close();
                        }
                }
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);
		dataset.addSeries(series4);
		dataset.addSeries(series5);
		return dataset;
	}

	private void customizeChart(JFreeChart chart) {
		XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		// sets paint color for each series
		renderer.setSeriesPaint(0, Color.RED);
		renderer.setSeriesPaint(1, Color.GREEN);
		renderer.setSeriesPaint(2, Color.YELLOW);
                renderer.setSeriesPaint(3, Color.BLUE);
                renderer.setSeriesPaint(4, Color.BLACK);

		// sets thickness for series (using strokes)
		renderer.setSeriesStroke(0, new BasicStroke(4.0f));
		renderer.setSeriesStroke(1, new BasicStroke(3.0f));
		renderer.setSeriesStroke(2, new BasicStroke(2.0f));
		renderer.setSeriesStroke(3, new BasicStroke(2.0f));
                renderer.setSeriesStroke(4, new BasicStroke(2.0f));
 
		// sets paint color for plot outlines
		plot.setOutlinePaint(Color.BLUE);
		plot.setOutlineStroke(new BasicStroke(2.0f));
		
		// sets renderer for lines
		plot.setRenderer(renderer);
		
		// sets plot background
		plot.setBackgroundPaint(Color.DARK_GRAY);
		
		// sets paint color for the grid lines
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);
		
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.BLACK);
		
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new XYLineChartExample().setVisible(true);
			}
		});
	}
}
