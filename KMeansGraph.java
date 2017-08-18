/*
Name: Danielle Lewis
Date: 05-04-16
Class: Data Mining
File: KMeansGraph.java
Description: Perform the K-Means clustering
	Alg. on 20 random points.
*/

import java.io.*;
import java.awt.*;
import java.net.*;
import java.lang.*;
import java.util.*;
import javax.swing.*;
import java.applet.*;
import javafx.scene.*;
import java.util.Random;
import java.util.Vector;
import java.awt.event.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import java.awt.geom.Point2D.*;
import javafx.scene.chart.XYChart;
import javafx.embed.swing.JFXPanel;
import java.awt.geom.Point2D.Double;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;

class KMeansGraph extends JFrame
					implements ActionListener
{
	Vector<Point2D> meanV;
	Vector<Vector<Point2D>> clusterV;
	int m;
	Container container;
	JButton initializeBT, nextBT;
	JLabel cLabel;
	JPanel buttonPanel;
	JFXPanel graphPanel;
	JSpinner cSpinner;

//==========================================================
public static void main(String[] args)
	{
	SwingUtilities.invokeLater(new Runnable()
		{
		@Override public void run()
			{
			new KMeansGraph();
			}
		});
	}// end of main()

//==========================================================
KMeansGraph()
{
System.out.println("Starting K-Means Graph Application...");
// Setiting up the GUI
initializeBT = new JButton("Initialize");
nextBT= new JButton("Next");
cLabel = new JLabel(" K = ");
buttonPanel = new JPanel(new FlowLayout());
graphPanel = new JFXPanel();
cSpinner   = new JSpinner(new SpinnerNumberModel(2,2,5,1));

initializeBT.setActionCommand ("INITIALIZE");
nextBT.setActionCommand ("NEXT");

initializeBT.addActionListener(this);
nextBT.addActionListener(this);

buttonPanel.add(cLabel);
buttonPanel.add(cSpinner);
buttonPanel.add(initializeBT);
buttonPanel.add(nextBT);

getRootPane().setDefaultButton(nextBT);
container = getContentPane();
container.add(graphPanel, BorderLayout.CENTER);
container.add(buttonPanel, BorderLayout.SOUTH);
setupMainFrame();
}// end of constructor

//==========================================================
void setupMainFrame()
{
// setting up the size, location, and visibility
System.out.println("Calling setupMainFrame()...");
Toolkit tk;
Dimension d;
tk= Toolkit.getDefaultToolkit();
d= tk.getScreenSize();
setSize(d.width/2, d.height/2);
setLocation(d.width/4, d.height/4);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setTitle("K-Means Graph Application");
setVisible(true);
}// end of setupmainFrame()

//==========================================================
public void actionPerformed(ActionEvent e)
{
if(e.getActionCommand().equals("INITIALIZE"))
	{
	doPointInitialization();
	drawChart();
	}
else if(e.getActionCommand().equals("NEXT"))
	{
	kMeansAlgorithm();
	drawChart();
	}
}// end of AP()

//==========================================================
void doPointInitialization()
{
// create the points
Point2D point;
Random random = new Random();
Vector<Point2D> cValue = new Vector<Point2D>();
int gSize = 20;
while(cValue.size() < gSize)
	{
	point = new Point2D.Double(random.nextInt(gSize), random.nextInt(gSize));
	if(cValue.contains(point) == false)
		cValue.add(point);
	}
meanV = new Vector<Point2D>();
m = (int)cSpinner.getValue();
while(meanV.size() < m)
	{
	point = cValue.get(random.nextInt(gSize));
	if(meanV.contains(point) == false)
		meanV.add(point);
	}
clusterV = new Vector<Vector<Point2D>>();
clusterV.add(cValue);
for(int v = 1; v < m; v++)
	{
	clusterV.add(new Vector<Point2D>());
	}

}// end of doPointInitialization()

//==========================================================
void drawChart()
{
// draw the graph with the points from above.
//Init Axises
final NumberAxis xAxis = new NumberAxis(0, 20, .5);
final NumberAxis yAxis = new NumberAxis(0, 20, .5);
Point2D point;

ScatterChart<Number, Number> scatChart = new ScatterChart<Number, Number>(xAxis, yAxis);
xAxis.setLabel("X-Values: ");
yAxis.setLabel("Y-Values: ");
scatChart.setTitle("K-Means Scatter Chart");

Vector <XYChart.Series> csVector;
csVector = new Vector<XYChart.Series>();

// setting up clusterij
XYChart.Series cSeries;
for(int i = 0; i < clusterV.size(); i++)
	{
	cSeries = new XYChart.Series();
	cSeries.setName("Cluster " + (i+1));
	for(int j = 0; j < clusterV.get(i).size(); j++)
		{
		point = clusterV.get(i).get(j);
		cSeries.getData().add(new XYChart.Data(point.getX(), point.getY()));
		}
	csVector.add(i, cSeries);
	}

// setting up the Meanc
XYChart.Series mSeries;
mSeries = new XYChart.Series();
mSeries.setName("Mean Clusters");
for(int c = 0; c < meanV.size(); c++)
	{
	point = meanV.get(c);
	mSeries.getData().add(new XYChart.Data(point.getX(), point.getY()));
	}

for(int n = 0; n < csVector.size(); n++)
	{
	scatChart.getData().add(csVector.get(n));
	}
scatChart.getData().add(mSeries);
Scene sc;
sc = new Scene(scatChart, 500, 400);
//sc = new Scene(scatChart, this.width, this.height);
graphPanel.setScene(sc);
}// end of drawChart()

//==========================================================
void kMeansAlgorithm()
{
// Implementing the K-Means alg
double minimumVal;
int minimumIndex;
Point2D tempMean;
double kMeans =0;
Point2D point;
for(int i = 0; i < clusterV.size(); i++)
	{
	for(int j = (clusterV.get(i).size()-1); j >= 0; j--)
		{
		point = clusterV.get(i).get(j);
		minimumVal = 100.0;
		minimumIndex = 0;
		for(int c = 0; c < meanV.size(); c++)
			{
			tempMean = meanV.get(c);
			kMeans = Math.sqrt(Math.pow(point.getX()-tempMean.getX(),2) + Math.pow(point.getY()-tempMean.getY(),2));
			if(minimumVal > kMeans)
				{
				minimumIndex = c;
				minimumVal = kMeans;
				}
			}
		if(minimumIndex != i)
			{
			clusterV.get(i).remove(j);
			clusterV.get(minimumIndex).add(point);
			}
		}
	}

double xMeanAverage;
double yMeanAverage;

for(int k = 0; k < meanV.size(); k++)
	{
	xMeanAverage = 0.0;
	yMeanAverage = 0.0;
	for(int l = 0; l < clusterV.get(k).size(); l++)
		{
		point = clusterV.get(k).get(l);
		xMeanAverage = xMeanAverage + point.getX();
		yMeanAverage = yMeanAverage + point.getY();
		}
	tempMean = new Point2D.Double((xMeanAverage/clusterV.get(k).size()), (yMeanAverage/clusterV.get(k).size()));
	meanV.set(k, tempMean);
	}

}// end of kMeansAlgorithm()

}// end of K-Means-GraphClass