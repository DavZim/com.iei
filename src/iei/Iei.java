package iei;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Iei {

	public static void main (String[] args) throws IOException {
		ArrayList<String> groups = new ArrayList<String>(); //String[] groups;
		ArrayList<LocalDate> dates = new ArrayList<LocalDate>(); //Calendar[] dates
		ArrayList<Boolean> indepEvents = new ArrayList<Boolean>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");//("dd/MM/yyyy");
		LocalDate lastIndepEvent = LocalDate.parse("2000-01-01", formatter);//("01/01/2000", formatter);
		LocalDate date;

		// 1. read data by line
		String csvFile = "C:/Users/david/OneDrive/Documents/bid_contest_codecats_java/dataset_big.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));

			line = br.readLine(); // read header (and discard the values)
			
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] rowEl = line.split(cvsSplitBy);
				//				group = rowEl[1];
				groups.add(rowEl[1]);
				date = LocalDate.parse(rowEl[2].replace("\"", ""), formatter);
				dates.add(date);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		


		// 2. Analyse Events
		final long startTime = System.currentTimeMillis();

		int indepCounter = 0;
		// The first element (at 0) is per definition an independent event
		indepEvents.add(true);
		lastIndepEvent = dates.get(0);
		indepCounter++;
		
		for (int r = 1; r < dates.size(); r++) {
			
			long daysBetween = ChronoUnit.DAYS.between(lastIndepEvent, dates.get(r));

			if (groups.get(r).equals(groups.get(r - 1))) {
				if (daysBetween > 60){
					indepEvents.add(true);
					indepCounter++;
					lastIndepEvent = dates.get(r);
				} else {
					indepEvents.add(false);
				}
				
			} else {
				// new event
				indepEvents.add(true);
				indepCounter++;
				lastIndepEvent = dates.get(r);
			}
		}
		
		final long endTime = System.currentTimeMillis();

		System.out.println("Total of " + indepCounter + " independent events found!");

		// 3. Write Out File
		String csvFileOut = "C:/Users/david/OneDrive/Documents/bid_contest_codecats_java/dataset_big_java.csv";
		String lineOut = "target,date,independent\n";
		File fileOut = new File(csvFileOut);
		// if the file doesnt exist create new file
		if (!fileOut.exists()) {
			try {
				fileOut.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		FileWriter fw = new FileWriter(fileOut.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(lineOut);

		for (int r = 0; r < dates.size(); r++){
			lineOut = groups.get(r) + "," + dates.get(r) + "," + indepEvents.get(r) + "\n";
			bw.write(lineOut);

		}
		bw.close();
		System.out.println("Total execution time [milliseconds]: " + (endTime - startTime) );
	}
}
