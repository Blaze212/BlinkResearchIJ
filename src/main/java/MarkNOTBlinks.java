import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class MarkNOTBlinks {
	int BLINK_RANGE = 150;
	String path = "C:\\Users\\barto\\BlinkResearch\\Marked-Video-Files\\BartonStudy2-3-4-Input.csv";
	double[] timestamps;
	double[] leftPoints;
	double[] rightPoints;
	double[] blinks;


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MarkNOTBlinks mnb = new MarkNOTBlinks();
		
			
	}
	public MarkNOTBlinks(){
		readBlinksFile(path);
		MarkNotBlinks();
		writeToFile();
	}
	
	public void MarkNotBlinks(){
		int count = 0;
		for(int i=0;i<blinks.length;i++){
			if(blinks[i]==1){
				count++;
			}
		}
		System.out.println("Count: " + count);
		int numNotBlinks = 0;
		boolean allZeros = true;
		int tries = 0;
		
		while(numNotBlinks < count * 3 && tries < 9999999){
			// pads 300 at begining and end because need a 300ms window to mark for a blink
			int randIndex = (int) (BLINK_RANGE + (Math.random() * (blinks.length - (BLINK_RANGE*2))));
			if(blinks[randIndex] == 0){
				for(int i=1; i<=BLINK_RANGE/2;i++){
					if(blinks[randIndex-i] != 0 || blinks[randIndex+i] != 0){
						allZeros = false;
					}
					
				}
				if(allZeros){
					blinks[randIndex] = 2;
					count++;
					tries = 0;
				}else{
					tries++;
					if(tries% 1000000 == 0)
						System.out.println(tries  + " count: " + count);
				}
				// all 0's with in RANGE of blink 50 ms or 300 ms? need to pick.
			}
					// then mark a 2 else contnue until enough not blinks marked
			
		}
		System.out.println("Not Blinks: " + count);
	}
	
	public void writeToFile(){
		
		String line;
		try(Writer writer = new BufferedWriter(new OutputStreamWriter 
				(new FileOutputStream("BartonStudy2-3-4-M.csv"), StandardCharsets.UTF_8))){
			writer.write("Time, " + "Left," + "Right," + "Mark \n");
			
			for(int i=0;i<blinks.length;i++){

				line = String.format(leftPoints[i] + "," + rightPoints[i] + "," + blinks[i]+ "\n");
				writer.write(line);
			}
			System.out.println("Saved");
		}catch (Exception e){
			System.out.println("failed here " + e.getMessage() + "  " + e.getCause());
		}

		System.out.println("Saved");
	}
	

	public void readBlinksFile(String path){
		File f = new File(path);
		Scanner scanner = null;
		//ArrayList<Double> timesAL = new ArrayList<Double>();
		ArrayList<Double> blinksAL = new ArrayList<Double>();
		ArrayList<Double> leftAL = new ArrayList<Double>();
		ArrayList<Double> rightAL = new ArrayList<Double>();
		
		

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

			//timesAL.add(Double.parseDouble(pieces[0]));
			leftAL.add(Double.parseDouble(pieces[0]));
			rightAL.add(Double.parseDouble(pieces[1]));
			blinksAL.add(Double.parseDouble(pieces[2]));


			//System.out.printf("P: %s %s %s \n", pieces[0], pieces[1], pieces[2]);
		}
		//timestamps= new double[timesAL.size()];
		blinks = new double[blinksAL.size()];
		leftPoints = new double[leftAL.size()];
		rightPoints = new double[rightAL.size()];

		for(int i=0;i<blinksAL.size();i++){
			//timestamps[i]= timesAL.get(i);
			blinks[i]= blinksAL.get(i);
			leftPoints[i] = leftAL.get(i);
			rightPoints[i] = rightAL.get(i);
		}


	}
}
