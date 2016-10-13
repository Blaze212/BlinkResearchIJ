import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by barto on 10/10/2016.
 */
public class Study2Main {
    String name = "Study2CombinedAll.csv";
    String saveName = "";
    String path = "C:\\Users\\barto\\OneDrive\\Documents\\School Fall 2016\\Research\\Study 2\\Combined\\";
    String savelocation = "C:\\Users\\barto\\OneDrive\\Documents\\School Fall 2016\\Research\\Study 2\\Combined\\" + name;

    ArrayList<Double> timestamps = new ArrayList<Double>();
    ArrayList<Double> leftPoints= new ArrayList<Double>();
    ArrayList<Double> rightPoints= new ArrayList<Double>();
    ArrayList<Double> markedPoints= new ArrayList<Double>();

    int BLINk_RANGE = 700; // window to look for blink in
    int SMOOTH = 10;


    public static void main(String[] args) {
        // TODO Auto-generated method stub
       Study2Main m = new Study2Main();


    }

    Study2Main(){
        ///path += name;    // if reading only one file need this line
        //readFile(path);

        /*combine a list of files
        //combineStudy2();
        //saveMarkedBlinks();
        */

       // XXX // Next step combine the 2 combined files, then create NN INp

         //Create NN-Input from file
        path += name;
        readFile(path);
        filterColissions();
        markWindows();
        verify();
        ArrayList<String> lines = makeNNLines();
        saveName = path.substring(0,path.length()-4) + "NN-Input" + BLINk_RANGE + "ms.csv";
        saveNNInputFile(lines);


        /* Smooth a file
        path+=name;
        readFile(path);
        average(SMOOTH);
        savelocation = path.substring(0,path.length()-4) + "Smooth10.csv";
        saveMarkedBlinks();
        */

    }

    /* OBSOLETE
    Used to fix points that were off by 1000
    public void fixRightPoints(){
        for(int i=0;i<timestamps.size();i++){
            rightPoints.set(i,rightPoints.get(i)/1000);
        }
    }
    */
    // combines individual files into one long file
    public void combineStudy2(){
        /*String[] filenames = {
                "5SecRandomMarked",
                "5SecrandomV2Marked",
                "BlinkThenUpNoBlinkMarked",  //(NOTE) for Blinks
                "DownNoBlinksMarked",
                "DownWithBlinkMarked",
                "LeftNoBlinkMarked",
                "LeftWithBlinkMarked",
                "NormalBlinks5secMarked",
                "RegularBlinksV2Marked",
                "RightNoBlinkSpaceBlinkMarked",
                "RightWithBlinkMarked",
                "UpWithBlinksMarked",
                "UDRLB5secMarked"             // Only for Not Blinks

        };


        for(int i=0;i<filenames.length;i++){
            filenames[i] = path + filenames[i] + "NotBlink.csv";
            readFile(filenames[i]);
        }
        */

        String[] filenames = {"C:\\Users\\barto\\OneDrive\\Documents\\School Fall 2016\\Research\\Study 2\\Combined\\Study2CombinedMarked.csv",
                "C:\\Users\\barto\\OneDrive\\Documents\\School Fall 2016\\Research\\Study 2\\Combined\\Study2CombinedMarkedNotBlinks.csv"};

        for(int i=0;i<filenames.length;i++){
            readFile(filenames[i]);
        }



    }

    public void readFile(String path) {
        // LEFT, RIGHT, MARK, TIME          <-------ORDER------->

        File f = new File(path);
        Scanner scanner = null;
        ArrayList<Double> time = new ArrayList<Double>();
        ArrayList<Double> left = new ArrayList<Double>();
        ArrayList<Double> right = new ArrayList<Double>();
        ArrayList<Double> marks = new ArrayList<Double>();

        try {
            scanner = new Scanner(f);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        scanner.nextLine(); //skips the headers

        while (scanner.hasNextLine()) {
            String thisInfoString = scanner.nextLine();
            String[] pieces = thisInfoString.split(",");

            left.add(Double.parseDouble(pieces[0]));
            right.add(Double.parseDouble(pieces[1]));
            marks.add(Double.parseDouble(pieces[2]));
            time.add(Double.parseDouble(pieces[3]));

        }

        timestamps.addAll(time);
        leftPoints.addAll(left);
        rightPoints.addAll(right);
        markedPoints.addAll(marks);

    }

    /*
    The middle part of a Blink or NotBlink has been marked by a 1 or a 2

    This method marks BLINK_RANGE / 2 on either side of the initial mark as a 1 or 2
    - This is a prep-step in preparing the data for the NN.
     */
    public void markWindows(){
        int index = 0;
        for(int i=0;i<markedPoints.size();i++){
            if(markedPoints.get(i)==1){
                for(int k=i-BLINk_RANGE/2;k<=i+BLINk_RANGE/2-1;k++){
                    markedPoints.set(k,1.0);
                    index = k+1;
                }
                i = index;
            }
            if(markedPoints.get(i)==2){
                for(int k=i-BLINk_RANGE/2;k<=i+BLINk_RANGE/2-1;k++){
                    markedPoints.set(k,2.0);
                    index = k+1;
                }
                i = index;
            }
        }

    }
    /* Smoothing function:
        Given x[i], x[i] = Average of previous @avg elements

      */

    public  void average(int avg){
        double[] currentL = new double[avg];
        double[] currentR = new double[avg];
        for(int i =0;i<timestamps.size();i++){
            currentL[i%avg] = leftPoints.get(i);
            currentR[i%avg] = rightPoints.get(i);

            if(i>10){
                double curAvgL = 0;
                double curAvgR = 0;
                for(int j=0;j<10;j++){
                    curAvgL += currentL[j];
                    curAvgR += currentR[j];
                }
                // round to 7 significant digits
                curAvgL = Math.round(curAvgL / avg * 10000000) / 10000000.0;
                curAvgR = Math.round(curAvgR / avg * 10000000) / 10000000.0;
                leftPoints.set(i,curAvgL);
                rightPoints.set(i, curAvgR);

            }
        }
    }

    public void filterColissions(){
        int count = -10000;
        boolean rv = true;
        for(int i=0;i<timestamps.size();i++){
            if(markedPoints.get(i) != 0) {
                if (count < BLINk_RANGE) {
                    System.out.println("Space between marks: " + count + ", " + i);
                    markedPoints.set(i,0.0);
                }
                count = 0;
            }
            if(markedPoints.get(i) == 0)
                count++;
        }
    }

    public void verify(){
        int ONES = 0;
        int TWOS = 0;
        int ZEROES = 0;

        for(int i=0;i<markedPoints.size();i++){
            if(markedPoints.get(i)==0)
                ZEROES++;
            if(markedPoints.get(i)==1)
                ONES++;
            if(markedPoints.get(i)==2)
                TWOS++;

        }
        System.out.println("Zeroes: " + ZEROES);
        System.out.println("Ones: " + ONES + " should be: " + ONES/700.0);
        System.out.println("Twos: " + TWOS + " should be: " + TWOS/700.0);


        System.out.println("---");

    }


    public ArrayList<String> makeNNLines(){
        double current = -1;
        double prev = -1;
        String line = "";
        ArrayList<String> lines = new ArrayList<String>();
        double[] glasses = new double[100];

        for(int i=1; i<timestamps.size();i++){
            prev = markedPoints.get(i-1);
            current = markedPoints.get(i);
            glasses[i%100] = markedPoints.get(i);

            //Starts marking a set
            if(prev == 0 && (current == 1 || current ==2)){
                line += leftPoints.get(i) + "," + rightPoints.get(i) + ",";
            }
            // continues through a marked set
            if((prev == 1 && current == 1) || (prev == 2 && current == 2)){
                line += leftPoints.get(i) + "," + rightPoints.get(i) + ",";
            }
            // ends a marked set, writes label to end of line
            if((prev == 1 || prev == 2) && current ==0){
                //Blink
                if(prev == 1) {
                    line += markedPoints.get(i - 1).intValue();
                }
                // Not Blink  -- NN need 0 -> X for labels, cannot start at 1.
                if(prev == 2){
                    line += 0;
                }
                lines.add(line);
                line = "";
            }
        }

        return lines;
    }

    public void saveNNInputFile(ArrayList<String> lines) {
        // Line = L,R ^ BLINK_RANGE, MARK
        try (Writer writer = new BufferedWriter(new OutputStreamWriter
                (new FileOutputStream(saveName), StandardCharsets.UTF_8))) {


            for (int i = 0; i < lines.size(); i++) {

                String line = lines.get(i);
                writer.write(line +"\n");
            }
            System.out.println("Saved");
        } catch (Exception e) {
            System.out.println("failed here " + e.getMessage());
        }

        System.out.println("Saved");
    }
    public void saveMarkedBlinks(){
        String line;
        String NNSaveLoc = path +"NN-Input-" + BLINk_RANGE + "ms.csv";
        try(Writer writer = new BufferedWriter(new OutputStreamWriter
                (new FileOutputStream(savelocation), StandardCharsets.UTF_8))){
            writer.write("Left," + "Right," + "Mark,Time \n");

            for(int i=0;i<timestamps.size();i++){

                line = String.format(leftPoints.get(i) + "," + rightPoints.get(i) + ","
                        + markedPoints.get(i)+"," +timestamps.get(i)+"\n");
                writer.write(line);
            }
            System.out.println("Saved");
        }catch (Exception e){
            System.out.println("failed here " + e.getMessage());
        }

        System.out.println("Saved");
    }

    public void countBlinks(){
        int count = 0;
        for(int i=0;i<timestamps.size();i++){
            if(markedPoints.get(i)==1)
                count++;
        }
        System.out.println("Num blinks: " + count);
    }
    public void countNotBlinks(){
        int count = 0;
        for(int i=0;i<timestamps.size();i++){
            if(markedPoints.get(i)==2)
                count++;
        }
        System.out.println("Num not blinks: " + count);
    }
}
