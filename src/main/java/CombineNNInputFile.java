import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class CombineNNInputFile {
	int BLINK_RANGE = 150;
	String path = "C:\\Users\\barto\\BlinkResearch\\Marked-Video-Files\\BuildingBlocks\\BartonStudy2Marked.csv";
	ArrayList<Double> timestamps = new ArrayList<Double>();
	ArrayList<Double> leftPoints= new ArrayList<Double>();;
	ArrayList<Double> rightPoints= new ArrayList<Double>();;
	ArrayList<Integer> blinks= new ArrayList<Integer>();;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CombineNNInputFile cif = new CombineNNInputFile();
	}

	@SuppressWarnings("unused")
	public CombineNNInputFile(){
		if(true == false){
			readBlinksFile("C:\\Users\\barto\\BlinkResearch\\Marked-Video-Files\\BartonStudy2NN.csv");
			readBlinksFile("C:\\Users\\barto\\BlinkResearch\\Marked-Video-Files\\BartonStudy3NN.csv");
			readBlinksFile("C:\\Users\\barto\\BlinkResearch\\Marked-Video-Files\\BartonStudy4NN.csv");
			writeToFile();
		}else{
			readBlinksFile(path);
			//verify();
			removeZeroes();
			//verify();
			reduceToOnesAndZeroes();
			verify();
			//writeToFileBlinkRange();
			writeToFile();
			//writeToFileBlinkRangewithTimeRange();
		}


	}

	public void verify(){
		int ONES = 0;
		int TWOS = 0;
		int ZEROES = 0;
		int THREES = 0;
		int ELEVEN = 0;
		int TWELVE = 0;
		for(int i=0;i<blinks.size();i++){
			if(blinks.get(i)==0)
				ZEROES++;
			if(blinks.get(i)==1)
				ONES++;
			if(blinks.get(i)==2)
				TWOS++;
			if(blinks.get(i)==3)
				THREES++;
			if(blinks.get(i)==11)
				ELEVEN++;
			if(blinks.get(i)==22)
				TWELVE++;
		}
		System.out.println("Zeroes: " + ZEROES);
		System.out.println("Ones: " + ONES);
		System.out.println("Twos: " + TWOS);
		System.out.println("Threes: " + THREES);
		System.out.println("Eleven (One): " + ELEVEN);
		System.out.println("Twenty-Two (Two) : " + TWELVE);

		System.out.println("---");

	}
	public void removeZeroes(){
		int index = 0;
		ArrayList<Double> rv = new ArrayList<Double>();
		for(int i=0;i<blinks.size();i++){
			if(blinks.get(i)==1){
				for(int k=i-BLINK_RANGE/2;k<i+BLINK_RANGE/2;k++){
					blinks.set(k, 11);
					index = k+1;
				}
				i = index;	
			}
			if(blinks.get(i)==2){
				for(int k=i-BLINK_RANGE/2;k<i+BLINK_RANGE/2;k++){
					blinks.set(k, 22);
					index = k+1;
				}
				i = index;	
			}

		}

	}
	public void reduceToOnesAndZeroes() {
		int index = 0;
		ArrayList<Double> rv = new ArrayList<Double>();
		for (int i = 0; i < blinks.size(); i++) {
			if (blinks.get(i) == 11) {
				blinks.set(i, 1);
			}
			else if (blinks.get(i) == 22) {
				blinks.set(i, 0);
			}
			else if (blinks.get(i) == 0) {
				blinks.set(i, 3);
			}
		}
	}

	/// XXX Either broken here in the write method or in the reduceTOZeros()....
	public void writeToFileBlinkRange(){

		String line = "";
		try(Writer writer = new BufferedWriter(new OutputStreamWriter 
				(new FileOutputStream("BartonStudy2-3-4NN-Input-150ms-Time.csv"), StandardCharsets.UTF_8))){
			writer.write("Left," + "Right," + "Mark \n");
			System.out.println(blinks.size());
			for(int i=1;i<blinks.size();i++){
				double old = blinks.get(i-1);
				double current = blinks.get(i);
				if(old != current){//label and newline
					line += blinks.get(i-1).intValue() + "\n";
					if(old != 3) {
							if(goodLine(line))
								writer.write(line);
					}

					line = "";
				}
				if(old == current && current != 3){ // write line
					line += leftPoints.get(i) + "," + rightPoints.get(i) + ",";
				}
				if(old == current && current == 3){ // do nothing
				}
				
					if(i % 100000 ==0 )
					System.out.println(i);
					
				}
			
			System.out.println("Saved");
		}catch (Exception e){
			System.out.println("failed to save " + e.getMessage());
		}

		System.out.println("Saved");
	}
	
	public boolean goodLine(String line){
		String[] pieces = line.split(",");
		int len = pieces.length;
		System.out.println("LENGTH" + len);
		if(pieces.length==299)
			return true;
		return false;
	}

	public void writeToFile(){

		String line;
		try(Writer writer = new BufferedWriter(new OutputStreamWriter 
				(new FileOutputStream("BartonStudy2-3-4NN-Input-WithTimes.csv"), StandardCharsets.UTF_8))){
			writer.write("Left," + "Right," + "Mark,Time \n");

			for(int i=0;i<blinks.size();i++){

				line = String.format(leftPoints.get(i) + "," + rightPoints.get(i) + ","
						+ (blinks.get(i).intValue())+"," +timestamps.get(i)+"\n");
				writer.write(line);
			}
			System.out.println("Saved");
		}catch (Exception e){
			System.out.println("failed here " + e.getMessage());
		}

		System.out.println("Saved");
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

			timestamps.add(Double.parseDouble(pieces[0]));
			leftPoints.add(Double.parseDouble(pieces[1]));
			rightPoints.add(Double.parseDouble(pieces[2]));
			blinks.add(Double.valueOf(pieces[3]).intValue());


			//System.out.printf("P: %s %s %s \n", pieces[0], pieces[1], pieces[2]);
		}
	}
	
	public void readCombinedBlinksFile(String path){
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

			leftPoints.add(Double.parseDouble(pieces[0]));
			rightPoints.add(Double.parseDouble(pieces[1]));
			blinks.add(Double.valueOf(pieces[2]).intValue());


			//System.out.printf("P: %s %s %s \n", pieces[0], pieces[1], pieces[2]);
		}
	}
}

