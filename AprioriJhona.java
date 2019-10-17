
/* Apriori Algorithm
 * The program takes two inputs during runtime, minimum support and minimum confidence
 * There are 4 txt databases used "1.txt", "2.txt", "3.txt", "4.txt"
 * Candidate itemsets that are generated at each stage are visible in the console along with their support values
 * The itemsets that have support greater than or equal to minimum support are written into text file "out.txt"
 * The rules generated are written into text file "rules.txt"
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.io.FileOutputStream;

public class AprioriJhona {

	private static BufferedReader br1;

	// function to write to a file
	public static void write2(String fileName, String str) throws IOException {

		FileOutputStream outputStream = new FileOutputStream(fileName, true);
		byte[] strToBytes = str.getBytes();
		outputStream.write(strToBytes);

		outputStream.close();
	}

	public static void main(String[] args) {

		Vector<String> products = new Vector<String>(); // Vector products used to store itemsets of size 1
		Vector<String> products2 = new Vector<String>(); // Vector used to store itemsets of size 2
		Vector<String> products5 = new Vector<String>(); // Vector used to store eliminated itemsets that dont satisfy
															// minimum support
		Vector<String> products3 = new Vector<String>(); // Vector used to store the splitted items of size 2
		String outputfilename = "out.txt"; // file that displays itemsets that satisfy minimum support
		String rulesfilename = "rules.txt"; // file that displays the rules generated from candidate itemsets

		// User enters the minimum support and confidence
		System.out.println("Enter Min support and Min confidence");
		float minSup;
		float minCon;

		// create a scanner object to take in the minimum support and confidence values
		Scanner in = new Scanner(System.in);
		minSup = Integer.parseInt(in.nextLine());
		minCon = Integer.parseInt(in.nextLine());
		minSup = minSup / 100;
		minCon = minCon / 100;
		System.out.println("Min support is: " + minSup);
		System.out.println("Min confidence is : " + minCon);

		in.close(); // close the scanner object

		try {

			// Create a file stream object to read from the datasets file
			FileInputStream fstream = new FileInputStream("2.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;

			// Initialize the total number of transactions to 0
			int totalcount = 0;

			// Initialize an array of strings to contain the items in supermarket
			String[] items = new String[] { "Bread", "Milk", "Diaper", "Beer", "Eggs", "Coke", "Sugar", "Coffee",
					"Yogurt", "Butter" };

			// Initialize the count for each item to 0
			float[] countitems = new float[10];
			for (int i = 0; i < countitems.length; i++)
				countitems[i] = 0;

			// read line by line from "1.txt"
			while ((strLine = br.readLine()) != null) {

				totalcount++; // increase the count of transactions
				for (int i = 0; i < items.length; i++) {
					if (strLine.contains(items[i]))
						countitems[i]++; // increase the count of item
				}

			}

			fstream.close(); // close the filestream object

			// calculate the minimum support for each item and display the itemsets of size
			// 1

			System.out.println("****************Generating candidate itemsets of size 1 ******************\n");
			float[] eachminsup = new float[10];

			for (int i = 0; i < items.length; i++) {

				eachminsup[i] = countitems[i] / totalcount; // calculates the support of each item

				// compares support of item with minimum support
				if (eachminsup[i] > minSup) {
					write2(outputfilename, items[i] + " minimum support: " + eachminsup[i] + "\n");
					System.out.println(items[i] + " minimum support: " + eachminsup[i] + "\n");
					products.add(items[i]);
				}
			}

			System.out.println("****************Generating candidate itemsets of size 2 ******************\n");

			// for two more items in itemsets
			Vector<String[]> productsjoined = new Vector<String[]>(); // contains candidate itemsets of size 2
			Vector<String[]> productsjoinedjon = new Vector<String[]>(); // contains candidate itemsets of size 3

			// Create candidate itemsets of size 2
			for (int i = 0; i < products.size(); i++) {

				for (int j = i + 1; j < products.size(); j++) {
					String[] joined = { "", "" };

					joined[0] = products.get(i);
					joined[1] = products.get(j);
					if (joined[0] != joined[1]) {
						productsjoined.add(joined);
					}

				}
			}

			// Calculating the minimum support for the candidate itemsets of size 2
			for (int i = 0; i < productsjoined.size(); i++) {
				fstream = new FileInputStream("5.txt");
				br = new BufferedReader(new InputStreamReader(fstream));
				float joincount = 0, denomcount = 0;
				while ((strLine = br.readLine()) != null) {

					if (strLine.contains(productsjoined.get(i)[0]) && strLine.contains(productsjoined.get(i)[1]))
						joincount++;
					// to calculate confidence we need support of numerator
					if (strLine.contains(productsjoined.get(i)[0]))
						denomcount++;

				}

				float minsup = joincount / totalcount; // calculate support of each itemset
				float minconf = joincount / denomcount; // calculate confidence of each itemset

				System.out.println(
						productsjoined.get(i)[0] + " " + productsjoined.get(i)[1] + " minimum support: " + minsup);
				if (minsup > minSup) {
					write2(outputfilename, productsjoined.get(i)[0] + " " + productsjoined.get(i)[1]
							+ " minimum support: " + minsup + "\n");
					// write into rules file the support and confidence of itemsets of size 2
					write2(rulesfilename, productsjoined.get(i)[0] + "=>" + productsjoined.get(i)[1]
							+ " minimum support: " + minsup + " minimum confidence " + minconf + "\n");
					products2.add(productsjoined.get(i)[0] + " " + productsjoined.get(i)[1]);

				}
				// add the itemsets that dont satisfy minimum support to another vector
				// products5
				else {
					products5.add(productsjoined.get(i)[0] + " " + productsjoined.get(i)[1]);

				}

			}

			// Converting the itemsets into an vector array of strings
			Vector<String[]> productsjoinedelim = new Vector<String[]>();

			// convert the eliminated itemsets from string to a Vector
			for (int i = 0; i < products5.size(); i++) {

				String[] result = products5.get(i).split(" ");

				String[] joined1 = { "", "" };
				joined1[0] = result[0];
				joined1[1] = result[1];
				if (joined1[0] != joined1[1]) {
					productsjoinedelim.add(joined1);
				}

			}

			System.out.println("****************Generating candidate itemsets of size 3 ******************\n");

			// Splitting the itemsets of size 2 to generte candidate itemsets of size 3
			for (int j = 0; j < products2.size(); j++) {
				String[] result1 = products2.get(j).split(" ");
				for (int x = 0; x < 2; x++) {
					// splitting the elements to form itemsets of size 3
					if (!products3.contains(result1[x]))
						products3.add(result1[x]);

				}
			}

			// Generating itemsets of size 3 from the split array
			Vector<String[]> productsjoined4 = new Vector<String[]>();
			for (int i = 0; i < products3.size(); i++) {

				for (int j = i + 1; j < products3.size(); j++) {

					for (int k = i + 2; k < products3.size(); k++) {

						String[] joined = { "", "", "" };

						joined[0] = products3.get(i);
						joined[1] = products3.get(j);
						joined[2] = products3.get(k);
						if (joined[0] != joined[1] && joined[1] != joined[2] && joined[0] != joined[2]) {
							System.out.println(joined[0] + " " + joined[1] + " " + joined[2]);

							productsjoined4.add(joined);

						}

					}
				}

			}

			/*
			 * The formed itemsets of size 3 are written into a text file "6.txt" The
			 * itemsets that have rejected pairs of two are eliminated
			 */
			for (int i = 0; i < productsjoined4.size(); i++) {
				write2("6.txt", productsjoined4.get(i)[0] + " " + productsjoined4.get(i)[1] + " "
						+ productsjoined4.get(i)[2] + "\n");
			}
			try {
				// Eliminating itemsets of size 3 that contain eliminated itemsets of size 2
				fstream = new FileInputStream("6.txt");
				br = new BufferedReader(new InputStreamReader(fstream));
				Boolean jon = false;
				String[] joinedjon = { "", "", "" };

				while ((strLine = br.readLine()) != null) {
					for (int i = 0; i < productsjoinedelim.size(); i++) {

						if (strLine.contains(productsjoinedelim.get(i)[0])
								&& strLine.contains(productsjoinedelim.get(i)[1])) {
							jon = true;
						}
					}

					// Only those itemsets that dont have the rejected pairs are put into
					// productsjoinedjon
					if (jon == false) {
						String[] result = strLine.split(" ");
						for (int i = 0; i < 3; i++) {
							joinedjon[i] = result[i];
						}
						productsjoinedjon.add(joinedjon);
					}
				}

				// intializing the count of itemsets to 0
				int len = productsjoinedjon.size();
				float[] joincount = new float[len];
				for (int i = 0; i < len; i++) {
					joincount[i] = 0;
				}
				float[] minsup = new float[len];
				// Calculating the minimum support for itemsets of size 3 and writing into
				// output file "out.txt"
				for (int i = 0; i < productsjoinedjon.size(); i++) {
					fstream = new FileInputStream("5.txt");
					br = new BufferedReader(new InputStreamReader(fstream));

					while ((strLine = br.readLine()) != null) {

						// System.out.println(strLine);
						if (strLine.contains(productsjoinedjon.get(i)[0])
								&& strLine.contains(productsjoinedjon.get(i)[1])
								&& strLine.contains(productsjoinedjon.get(i)[2])) {
							joincount[i]++;

						}

					}

					// calculating the minimum support of each itemset of size 3
					minsup[i] = joincount[i] / totalcount;

					System.out.println(productsjoinedjon.get(i)[0] + " " + productsjoinedjon.get(i)[1] + " "
							+ productsjoinedjon.get(i)[2] + " minimum support: " + minsup[i]);
					if (minsup[i] > minSup) {
						write2(outputfilename, productsjoinedjon.get(i)[0] + " " + productsjoinedjon.get(i)[1] + " "
								+ productsjoinedjon.get(i)[2] + " minimum support: " + minsup[i] + "\n");
						write2(rulesfilename, productsjoinedjon.get(i)[0] + "," + productsjoinedjon.get(i)[1] + "=>"
								+ productsjoinedjon.get(i)[2] + " minimum support: " + minsup[i] + "\n");
						write2(rulesfilename, productsjoinedjon.get(i)[1] + "," + productsjoinedjon.get(i)[2] + "=>"
								+ productsjoinedjon.get(i)[0] + " minimum support: " + minsup[i] + "\n");
						write2(rulesfilename, productsjoinedjon.get(i)[0] + "," + productsjoinedjon.get(i)[2] + "=>"
								+ productsjoinedjon.get(i)[1] + " minimum support: " + minsup[i] + "\n");
						write2(rulesfilename, productsjoinedjon.get(i)[0] + "=>" + productsjoinedjon.get(i)[1] + ","
								+ productsjoinedjon.get(i)[2] + " minimum support: " + minsup[i] + "\n");
						write2(rulesfilename, productsjoinedjon.get(i)[1] + "=>" + productsjoinedjon.get(i)[0] + ","
								+ productsjoinedjon.get(i)[2] + " minimum support: " + minsup[i] + "\n");
						write2(rulesfilename, productsjoinedjon.get(i)[2] + "=>" + productsjoinedjon.get(i)[0] + ","
								+ productsjoinedjon.get(i)[1] + " minimum support: " + minsup[i] + "\n");

						products2.add(productsjoinedjon.get(i)[0] + " " + productsjoinedjon.get(i)[1] + " "
								+ productsjoinedjon.get(i)[2]);

					}
				}

			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
