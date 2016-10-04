import org.math.plot.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class AnalyzeRawBlinks {
	String filePath = "C:\\Users\\barto\\BlinkResearch\\Marked-Video-Files\\BartonEyesSpacedFastEnd1.xls";
	double[] timestamps;
	double[] leftPoints;
	double[] rightPoints;

	
	Plot2DPanel plot = new Plot2DPanel();
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AnalyzeRawBlinks data = new AnalyzeRawBlinks();

		
	}
	

	public AnalyzeRawBlinks(){
		readFile(filePath);
		Plot2DPanel panel = new Plot2DPanel();
		double[] shortTime = new double[6000];
		double[] shortLeft = new double[6000];
		double[] shortRight = new double[6000];
		double[] shortLeftN = new double[6000];
		double[] shortRightN = new double[6000];
		
		
		for(int i=0;i<6000;i++){
			shortTime[i]=timestamps[i];
			shortLeftN[i]=leftPoints[i];
			if(i % 100 > 50){
			
			shortLeft[i]=leftPoints[i];
			shortRight[i]=rightPoints[i];
			}
		}
		
		//double[] diff = combine("subtract",leftPoints,rightPoints);
		//double[] abs = combine("abs",leftPoints,rightPoints);
		//double[] absDiff = combine("absDiff",leftPoints,rightPoints);
		
		//processData(shortTime,shortLeft,shortRight)
		panel.addLinePlot("Left", Color.red, shortTime, shortLeft);
		panel.addLinePlot("Right", Color.blue, shortTime, shortLeftN);
		//panel.addLinePlot("Right", Color.orange, timestamps, absDiff);
		
		JFrame  frame= new JFrame("Histogram");
		frame.setContentPane(panel);
		frame.setSize(500, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public AnalyzeRawBlinks(String path, boolean display){
		readFile(path);
		Plot2DPanel panel = new Plot2DPanel();
		
		//double[] diff = combine("subtract",leftPoints,rightPoints);
		//double[] abs = combine("abs",leftPoints,rightPoints);
		//double[] absDiff = combine("absDiff",leftPoints,rightPoints);
		
		//processData(shortTime,shortLeft,shortRight)
		
		if(display){
			panel.addLinePlot("Left", Color.red, timestamps, leftPoints);
			panel.addLinePlot("Right", Color.blue, timestamps, rightPoints);
			//panel.addLinePlot("Right", Color.orange, timestamps, absDiff);

			JFrame  frame= new JFrame("Histogram");
			frame.setContentPane(panel);
			frame.setSize(500, 600);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
	}

	/* Combines the Left and Right Arrays:
	 * Always Left first then Right eye
	 * subtract
	 * abs = sum abs(a) + abs(b)
	 * absDiff = abs(a) - abs(b)
	 *  
	 */
	/*
	public double[] combine(String key, double[] a, double[] b){
		double[] rv = new double[a.length];
		switch (key){
			case "subtract":
				for(int i=0;i<a.length;i++)
					rv[i] = a[i] - b[i];
				break;
			case "abs":
				for(int i=0;i<a.length;i++){
					double absA = Math.abs(a[i]);
					double absB = Math.abs(b[i]);
					rv[i] = absA + absB;
				}
				break;
			case "absDiff":
				for(int i=0;i<a.length;i++){
					double absA = Math.abs(a[i]);
					double absB = Math.abs(b[i]);
					rv[i] = absA - absB;
				}
				break;
				
				
		}
		return rv;
		
	}
	*/
	
	public void readFile(String path){
		File f = new File(path);
		Scanner scanner = null;
		ArrayList<Double> time = new ArrayList<Double>();
		ArrayList<Double> left = new ArrayList<Double>();
		ArrayList<Double> right = new ArrayList<Double>();
		
		try {
			scanner = new Scanner(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scanner.nextLine(); //skips the headers
		
		while(scanner.hasNextLine()) {
			String thisInfoString = scanner.nextLine();
			String[] pieces = thisInfoString.split("\t");
			
			time.add(Double.parseDouble(pieces[0]));
			left.add(Double.parseDouble(pieces[1]));
			right.add(Double.parseDouble(pieces[2]));
			
			//System.out.printf("P: %s %s %s \n", pieces[0], pieces[1], pieces[2]);
		}
		timestamps= new double[time.size()];
		leftPoints = new double[time.size()];
		rightPoints= new double[time.size()];
		for(int i=0;i<time.size();i++){
			timestamps[i]= time.get(i);
			leftPoints[i]= left.get(i);
			rightPoints[i] = right.get(i)*1000;
		}
		
		//System.out.println(time);
		//System.out.println(left);
		//System.out.println(right);
		//System.out.println(timestamps[4]);
		//System.out.println(leftPoints[4]);
		//System.out.println(rightPoints[2354]);
		
		
	}

	
}
