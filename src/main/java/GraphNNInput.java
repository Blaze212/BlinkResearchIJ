import org.math.plot.Plot2DPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by barto on 9/24/2016.
 */
public class GraphNNInput {
    int HIGH = 0;

    ArrayList<Double> timestamps = new ArrayList<Double>();
    ArrayList<Double> leftPoints= new ArrayList<Double>();;
    ArrayList<Double> rightPoints= new ArrayList<Double>();;
    ArrayList<Integer> blinks= new ArrayList<Integer>();;
    Plot2DPanel plot = new Plot2DPanel();

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        GraphNNInput g = new GraphNNInput();
    }

    public GraphNNInput() {
        readBlinksFile("C:\\Users\\barto\\OneDrive\\Documents\\School Fall 2016\\Research\\Study 2\\MaryKateBlinks\\FixedMaryKateBlinks.csv");
        expandMarked();
        display();
    }

    public void display(){
        if(HIGH ==0)
            HIGH = timestamps.size();

        Plot2DPanel panel = new Plot2DPanel();

        double[] leftPointsA = new double[HIGH];
        double[] rightPointsA = new double[HIGH];
        double[] timestampsA = new double[HIGH];
        for(int i=0;i<HIGH;i++){
            leftPointsA[i]=leftPoints.get(i);
            rightPointsA[i]=rightPoints.get(i);
            timestampsA[i] = timestamps.get(i);
        }
        //double[] rightblinks = makeBlinksOnlyRight();
        //double[] leftblinks = makeBlinksOnlyLeft();

        //panel.addLinePlot("Left", Color.red, timestampsA, leftPointsA);
        //panel.addLinePlot("Left", Color.red, timestampsA, rightblinks);
        panel.addLinePlot("Right", Color.blue, timestampsA, rightPointsA);
        //panel.addLinePlot("Right", Color.orange, timestamps, absDiff);

        JFrame frame= new JFrame("Histogram");
        frame.setContentPane(panel);
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void readBlinksFile(String path){
        File f = new File(path);
        Scanner scanner = null;

        try {
            scanner = new Scanner(f);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        scanner.nextLine(); //skips the headers

        while(scanner.hasNextLine()) {
            String thisInfoString = scanner.nextLine();
            String[] pieces = thisInfoString.split(",");

            timestamps.add(Double.parseDouble(pieces[2]));
            leftPoints.add(Double.parseDouble(pieces[0]));
            rightPoints.add(Double.parseDouble(pieces[1]));
            //blinks.add(Double.valueOf(pieces[3]).intValue());


            //System.out.printf("P: %s %s %s \n", pieces[0], pieces[1], pieces[2]);
        }
    }

    public double[] makeBlinksOnlyRight(){
        double[] rv = new double[blinks.size()];

        for(int i=0;i<HIGH;i++){
            if(blinks.get(i)==1) {
                rv[i] = rightPoints.get(i);
                System.out.println(i+" "+rv[i] + " " + rightPoints.get(i));
            }
            else
                rv[i]=0;

        }
        return rv;
    }
    public double[] makeBlinksOnlyLeft(){
        double[] rv = new double[blinks.size()];

        for(int i=0;i<HIGH;i++){
            if(blinks.get(i)==1)
                rv[i]=leftPoints.get(i);
            else
                rv[i]=0;
        }
        return rv;
    }

    public void expandMarked(){
        System.out.print("HERE"+ blinks.size());

        for(int i=0;i<blinks.size();i++){
            if(blinks.get(i)==1){
                for(int k=0;k<250;k++){
                    int ind = i+k;
                    int neg = i-k;
                    if(ind<blinks.size() && neg>0) {
                        blinks.set(ind, 1);
                        blinks.set(neg, 1);
                    }
                 if(k==249){
                     i+=249;
                 }
                }

            }

        }
    }

}
