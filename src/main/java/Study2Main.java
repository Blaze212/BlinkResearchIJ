import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by barto on 10/10/2016.
 */
public class Study2Main {
    String name = "Study2CombinedMarked.csv";
    String path = "C:\\Users\\barto\\OneDrive\\Documents\\School Fall 2016\\Research\\Study 2\\MarkedBlinks\\";
    String savelocation = "C:\\Users\\barto\\OneDrive\\Documents\\School Fall 2016\\Research\\Study 2\\MarkedBlinks\\" + name;

    ArrayList<Double> timestamps = new ArrayList<Double>();
    ArrayList<Double> leftPoints= new ArrayList<Double>();
    ArrayList<Double> rightPoints= new ArrayList<Double>();
    ArrayList<Double> markedPoints= new ArrayList<Double>();


    public static void main(String[] args) {
        // TODO Auto-generated method stub
       Study2Main m = new Study2Main();


    }

    Study2Main(){
        //combineStudy2();
        path += name;    // if reading only one file need this line
        readFile(path);
        countBlinks();
        countNotBlinks();
        //saveMarkedBlinks();
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
        String[] filenames = {
                "5SecRandomMarked",
                "5SecrandomV2Marked",
                "BlinkThenUpNoBlinkMarkedNOTE",
                "DownNoBlinksMarked",
                "DownWithBlinkMarked",
                "LeftNoBlinkMarked",
                "LeftWithBlinkMarked",
                "NormalBlinks5secMarked",
                "RegularBlinksV2Marked",
                "RightNoBlinkSpaceBlinkMarked",
                "RightWithBlinkMarked",
                "UDRLB5secMarked",
                "UpWithBlinksMarked"

        };

        for(int i=0;i<filenames.length;i++){
            filenames[i] = path + filenames[i] + ".csv";
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

    public void saveMarkedBlinks(){
        String line;
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
