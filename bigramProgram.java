import java.io.*;
import java.util.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Comparator;

public class bigramProgram {
	public Hashtable<String, Integer> testTable = new Hashtable<String, Integer>();
	public Hashtable<String, Integer> firstTable = new Hashtable<String, Integer>();
	public Hashtable<String, Integer> secondTable = new Hashtable<String, Integer>();
	public Hashtable<String, Integer> thirdTable = new Hashtable<String, Integer>();

	public Hashtable<String, Double> testFreqTable = new Hashtable<String, Double>();
	public Hashtable<String, Double> firstFreqTable = new Hashtable<String, Double>();
	public Hashtable<String, Double> secondFreqTable = new Hashtable<String, Double>();
	public Hashtable<String, Double> thirdFreqTable = new Hashtable<String, Double>();

	public Map<String, Double> testOrderedFreq;
	public Map<String, Double> firstOrderedFreq;
	public Map<String, Double> secondOrderedFreq;
	public Map<String, Double> thirdOrderedFreq;

	public Map<String, Double> testRankedTable = testOrderedFreq;
	public Map<String, Double> firstRankedTable = firstOrderedFreq;
	public Map<String, Double> secondRankedTable = secondOrderedFreq;
	public Map<String, Double> thirdRankedTable = thirdOrderedFreq;

	public ArrayList<String> testTop = new ArrayList<String>();

	double testTotalBigrams;
	double testBigramCount;
	double testResult;
	double testOverallFreq;

	double firstTotalBigrams;
	double firstBigramCount;
	double firstResult;
	double firstOverallFreq;

	double secondTotalBigrams;
	double secondBigramCount;
	double secondResult;
	double secondOverallFreq;

	double thirdTotalBigrams;
	double thirdBigramCount;
	double thirdResult;
	double thirdOverallFreq;

	double testCompareVal = 0;

	double firstCompareVal = 0;
	double firstCompareResult = 0;

	double secondCompareVal = 0;
	double secondCompareResult = 0;

	double thirdCompareVal = 0;
	double thirdCompareResult = 0;

	public static int counter = 0;

	public static boolean visitedTest = false;
	public static boolean visitedFirst = false;
	public static boolean visitedSecond = false;
	public static boolean visitedThird = false;

	String fileInput = "";
	String firstString = "";
	String secondString = "";
	String thirdString = "";

	public static void main(String[] args) {
		bigramProgram bg = new bigramProgram();

		while (true) {
			int answer;
			Scanner userInput = new Scanner(System.in);

			do {
				System.out.println("\n1: Enter training file\n2: Enter test file ");

				while (!userInput.hasNextInt()) // if the input doesn't have an integer
				{
					System.out.print("\nInvalid input \nAnswer: "); // print invalid input
					userInput.next(); // get new input
				}
				answer = userInput.nextInt();
			} while (answer < 1 || answer > 2);

			switch (answer) {
			case 1:
				counter++;

				if (counter == 1) {
					bg.training();
					bg.firstOrderedFrequency();
					bg.firstRanked();
				}

				else if (counter == 2) {
					bg.training();
					bg.secondOrderedFrequency();
					bg.secondRanked();
				}

				else if (counter == 3) {
					bg.training();
					bg.thirdOrderedFrequency();
					bg.thirdRanked();
				}

				else if (counter == 4) {
					System.out.println();
					System.out.println("Program now needs to restart");
				}

				break;

			case 2:
				bg.readTest();

				bg.testOrderedFrequency();
				bg.testRanked();

				if (visitedTest == false) {
					bg.testCompare();
				}

				if (counter == 1 && visitedFirst == false) {
					bg.firstCompare();
				}

				else if (counter == 2 && visitedFirst == false && visitedSecond == false) {
					bg.firstCompare();
					bg.secondCompare();
				}

				else if (counter == 3 && visitedFirst == false && visitedSecond == false && visitedThird == false) {
					bg.firstCompare();
					bg.secondCompare();
					bg.thirdCompare();
				}

				else if (counter == 2 && visitedSecond == false) {
					bg.secondCompare();
				}

				else if (counter == 3 && visitedSecond == false && visitedThird == false) {
					bg.secondCompare();
					bg.thirdCompare();
				}

				else if (counter == 3 && visitedThird == false) {
					bg.thirdCompare();
				}

				bg.predict();

				if (counter == 3 && visitedThird == true) {
					System.out.println();
					System.out.println("Program now needs to restart");
					System.exit(0);
				}
				break;
			}
		}
	}

	/**
	 * readTest method scans the users input which is a text file then it removes
	 * anything that isn't a character and, html tags and their contents split the
	 * file into words and run a while loop, while there are still tokens get the
	 * current word lenght and set it to two characters to use as bigrams put the
	 * bigrams into a table, if bigram is already in table increase the value for
	 * loop to iterate through the bigram table set the frequency to be the bigram
	 * table size and get the total amount of bigrams calculate internal frequency
	 * by totalBigrams / frequency put the internal frequency into a new table with
	 * the corresponding bigrams
	 **/
	public void readTest() {
		try {
			Scanner userInput = new Scanner(System.in);

			System.out.println("\nEnter test file to predict the language of: ");
			String input = userInput.nextLine();

			File testFile = new File(input);
			Scanner scan = new Scanner(testFile);

			while (scan.hasNext()) {
				String read = scan.nextLine();

				String noHTML = read.replaceAll("<!--.*?-->", "").replaceAll("<[^>]+>", "");
				String finalString = noHTML.replaceAll("[^\\p{L}]+", " ");

				// tokenizer used to cut the file into words
				StringTokenizer token = new StringTokenizer(finalString);

				while (token.hasMoreTokens()) {
					String currentWord = token.nextToken().toLowerCase();
					String currentBigram;

					for (int i = 0; i < currentWord.length(); i += 1) {
						// if current word has at least 2 chracters
						if (currentWord.length() >= (i + 2)) {
							// create substring of word to 2 characters
							currentBigram = currentWord.substring(i, i + 2);

							// if bigram is already in table increase value
							if (testTable.containsKey(currentBigram)) {
								Integer prevValue = testTable.get(currentBigram);
								testTable.put(currentBigram, prevValue + 1);
							}
							// else put bigram in table
							else {
								testTable.put(currentBigram, new Integer(1));
							}
						}
					}
				}
			}
			System.out.println("\nTEST TABLE:");

			for (String keys : testTable.keySet()) {
				double freq = testTable.size();
				double totalBigrams = testTable.get(keys);

				double internalFrequency = totalBigrams / freq + 1;

				System.out.println(
						keys + " [bigram(s)]: " + testTable.get(keys) + " [internal freq]: " + internalFrequency);

				testFreqTable.put(keys, internalFrequency);
			}

			System.out.println("\nTEST FREQ:");
			System.out.println(testTable.size());
		} catch (Exception e) {
			System.out.println("read file error " + e);
		}
	}

	/**
		*
		*
		*
	**/
	public void training() {
		try {
			Scanner userInput = new Scanner(System.in);

			System.out.println("\nEnter training file: ");
			fileInput = userInput.nextLine();

			File input = new File(fileInput);
			Scanner scan = new Scanner(input);

			bigramProgram bg = new bigramProgram();

			while (scan.hasNext() && counter == 1) {
				firstString = fileInput;
				String read = scan.nextLine();

				String noHTML = read.replaceAll("<!--.*?-->", "").replaceAll("<[^>]+>", "");
				String finalString = noHTML.replaceAll("[^\\p{L}]+", " ");

				// tokenizer used to cut the file into words
				StringTokenizer token = new StringTokenizer(finalString);

				while (token.hasMoreTokens()) {
					String currentWord = token.nextToken().toLowerCase();
					String currentBigram;

					for (int i = 0; i < currentWord.length(); i += 1) {
						// if current word has at least 2 chracters
						if (currentWord.length() >= (i + 2)) {
							// create substring of word to 2 characters
							currentBigram = currentWord.substring(i, i + 2);

							// if bigram is already in table increase value
							if (firstTable.containsKey(currentBigram)) {
								Integer prevValue = firstTable.get(currentBigram);
								firstTable.put(currentBigram, prevValue + 1);
							}
							// else put bigram in table
							else {
								firstTable.put(currentBigram, new Integer(1));
							}
						}
					}
				}
			}

			if (counter == 1) {
				for (String keys : firstTable.keySet()) {
					double freq = firstTable.size();
					double totalBigrams = firstTable.get(keys);

					double internalFrequency = totalBigrams / freq + 1;

					System.out.println(
							keys + " [bigram(s)]: " + firstTable.get(keys) + " [internal freq]: " + internalFrequency);

					firstFreqTable.put(keys, internalFrequency);
				}
			}

			while (scan.hasNext() && counter == 2) {
				secondString = fileInput;
				String read = scan.nextLine();

				String noHTML = read.replaceAll("<!--.*?-->", "").replaceAll("<[^>]+>", "");
				String finalString = noHTML.replaceAll("[^\\p{L}]+", " ");

				// tokenizer used to cut the file into words
				StringTokenizer token = new StringTokenizer(finalString);

				while (token.hasMoreTokens()) {
					String currentWord = token.nextToken().toLowerCase();
					String currentBigram;

					for (int i = 0; i < currentWord.length(); i += 1) {
						// if current word has at least 2 chracters
						if (currentWord.length() >= (i + 2)) {
							// create substring of word to 2 characters
							currentBigram = currentWord.substring(i, i + 2);

							// if bigram is already in table increase value
							if (secondTable.containsKey(currentBigram)) {
								Integer prevValue = secondTable.get(currentBigram);
								secondTable.put(currentBigram, prevValue + 1);
							}
							// else put bigram in table
							else {
								secondTable.put(currentBigram, new Integer(1));
							}
						}
					}
				}
			}

			if (counter == 2) {
				for (String keys : secondTable.keySet()) {
					double freq = secondTable.size();
					double totalBigrams = secondTable.get(keys);

					double internalFrequency = totalBigrams / freq + 1;

					System.out.println(
							keys + " [bigram(s)]: " + secondTable.get(keys) + " [internal freq]: " + internalFrequency);

					secondFreqTable.put(keys, internalFrequency);
				}
			}

			while (scan.hasNext() && counter == 3) {
				thirdString = fileInput;
				String read = scan.nextLine();

				String noHTML = read.replaceAll("<!--.*?-->", "").replaceAll("<[^>]+>", "");
				String finalString = noHTML.replaceAll("[^\\p{L}]+", " ");

				// tokenizer used to cut the file into words
				StringTokenizer token = new StringTokenizer(finalString);

				while (token.hasMoreTokens()) {
					String currentWord = token.nextToken().toLowerCase();
					String currentBigram;

					for (int i = 0; i < currentWord.length(); i += 1) {
						// if current word has at least 2 chracters
						if (currentWord.length() >= (i + 2)) {
							// create substring of word to 2 characters
							currentBigram = currentWord.substring(i, i + 2);

							// if bigram is already in table increase value
							if (thirdTable.containsKey(currentBigram)) {
								Integer prevValue = thirdTable.get(currentBigram);
								thirdTable.put(currentBigram, prevValue + 1);
							}
							// else put bigram in table
							else {
								thirdTable.put(currentBigram, new Integer(1));
							}
						}
					}
				}
			}

			if (counter == 3) {
				for (String keys : thirdTable.keySet()) {
					double freq = thirdTable.size();
					double totalBigrams = thirdTable.get(keys);

					double internalFrequency = totalBigrams / freq + 1;

					System.out.println(
							keys + " [bigram(s)]: " + thirdTable.get(keys) + " [internal freq]: " + internalFrequency);

					thirdFreqTable.put(keys, internalFrequency);
				}
			}
		} catch (Exception e) {
			System.out.println("read first file error " + e);
		}
	}

	/**
		*
		*
		*
	**/
	public void testOrderedFrequency() {
		testOrderedFreq = sortedTest(testFreqTable);

		System.out.println();

		for (Entry<String, Double> entry : testOrderedFreq.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}

	/**
		*
		*
		*
	**/
	public static Map<String, Double> sortedTest(Map<String, Double> testFreqTable) {
		LinkedList<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(testFreqTable.entrySet());

		Collections.sort(list, new Comparator<Entry<String, Double>>() {
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Entry<String, Double> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	/**
		*
		*
		*
	**/
	public void firstOrderedFrequency() {
		System.out.println();
		firstOrderedFreq = sortedFirst(firstFreqTable);

		System.out.println();

		for (Entry<String, Double> entry : firstOrderedFreq.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}

	/**
		*
		*
		*
	**/
	public static Map<String, Double> sortedFirst(Map<String, Double> firstFreqTable) {
		LinkedList<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(firstFreqTable.entrySet());

		Collections.sort(list, new Comparator<Entry<String, Double>>() {
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Entry<String, Double> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	/**
		*
		*
		*
	**/
	public void secondOrderedFrequency() {
		System.out.println();
		secondOrderedFreq = sortedSecond(secondFreqTable);

		System.out.println();

		for (Entry<String, Double> entry : secondOrderedFreq.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}

	/**
		*
		*
		*
	**/
	public static Map<String, Double> sortedSecond(Map<String, Double> secondFreqTable) {
		LinkedList<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(secondFreqTable.entrySet());

		Collections.sort(list, new Comparator<Entry<String, Double>>() {
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Entry<String, Double> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	/**
		*
		*
		*
	**/
	public void thirdOrderedFrequency() {
		System.out.println();
		thirdOrderedFreq = sortedthird(thirdFreqTable);

		System.out.println();

		for (Entry<String, Double> entry : thirdOrderedFreq.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}

	/**
		*
		*
		*
	**/
	public static Map<String, Double> sortedthird(Map<String, Double> thirdFreqTable) {
		LinkedList<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(thirdFreqTable.entrySet());

		Collections.sort(list, new Comparator<Entry<String, Double>>() {
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Entry<String, Double> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	/**
		*
		*
		*
	**/
	public void testRanked() {
		testRankedTable = testOrderedFreq;
		try {
			double preVal = 0;
			double count = 0;

			for (String keys : testOrderedFreq.keySet()) {
				double currentVal = testOrderedFreq.get(keys);

				if (currentVal > preVal) {
					count++;

					testRankedTable.put(keys, count);
				}
			}
			System.out.println(testRankedTable);
		} catch (Exception e) {
			System.out.println("test ranked error: " + e);
		}
	}

	/**
		*
		*
		*
	**/
	public void firstRanked() {
		firstRankedTable = firstOrderedFreq;
		try {
			double preVal = 0;
			double count = 0;

			for (String keys : firstOrderedFreq.keySet()) {
				double currentVal = firstOrderedFreq.get(keys);

				if (currentVal > preVal) {
					count++;

					firstRankedTable.put(keys, count);
				}
			}
		} catch (Exception e) {
			System.out.println("first ranked error: " + e);
		}
	}

	/**
		*
		*
		*
	**/
	public void secondRanked() {
		secondRankedTable = secondOrderedFreq;
		try {
			double preVal = 0;
			double count = 0;

			for (String keys : secondOrderedFreq.keySet()) {
				double currentVal = secondOrderedFreq.get(keys);

				if (currentVal > preVal) {
					count++;

					secondRankedTable.put(keys, count);
				}
			}
		} catch (Exception e) {
			System.out.println("second ranked error: " + e);
		}
	}

	/**
		*
		*
		*
	**/
	public void thirdRanked() {
		thirdRankedTable = thirdOrderedFreq;
		try {
			double preVal = 0;
			double count = 0;

			for (String keys : thirdOrderedFreq.keySet()) {
				double currentVal = thirdOrderedFreq.get(keys);

				if (currentVal > preVal) {
					count++;

					thirdRankedTable.put(keys, count);
				}
			}
		} catch (Exception e) {
			System.out.println("test ranked error: " + e);
		}
	}

	/**
		* 
		* 
		*  
	**/
	public void testCompare() {
		try {
			visitedTest = true;
			Set set = testOrderedFreq.keySet();
			Iterator<String> iter = set.iterator();

			final int MAX = 1;
			int i = 0;

			while (iter.hasNext() && i < 20) {
				String sum = "";
				int counter = 1;

				while (counter <= MAX && iter.hasNext()) {
					sum += iter.next();
					counter++;
				}
				testTop.add(sum);
				i++;
			}
			System.out.println();
			System.out.println(testTop);
			System.out.println();

			for (Map.Entry m : testFreqTable.entrySet()) {
				if (testTop.contains(m.getKey())) {
					testCompareVal = (Double) m.getValue() + testCompareVal;
					System.out.println(m.getKey() + " [test internal freq]: " + m.getValue());
				}
			}
		} catch (Exception e) {
			System.out.println("eng compare error: " + e);
		}
	}

	/**
		* 
		* 
		* 
	**/
	public void firstCompare() {
		try {
			visitedFirst = true;
			System.out.println("\n[Compare with " + firstString + "]");
			for (Map.Entry m : firstRankedTable.entrySet()) {
				if (testTop.contains(m.getKey())) {
					System.out.println(m.getKey() + " [" + firstString + " internal ranking]: " + m.getValue());
				}
			}

			System.out.println();

			for (Map.Entry m : firstFreqTable.entrySet()) {
				if (testTop.contains(m.getKey())) {
					firstCompareVal = (Double) m.getValue() + firstCompareVal;
					System.out.println(m.getKey() + " [" + firstString + " internal freq]: " + m.getValue());
				}
			}
		} catch (Exception e) {
			System.out.println("first compare error: " + e);
		}
	}

	/**
		*
		* 
		*
	**/
	public void secondCompare() {
		try {
			visitedSecond = true;
			System.out.println("\n[Compare with " + secondString + "]");
			for (Map.Entry m : secondRankedTable.entrySet()) {
				if (testTop.contains(m.getKey())) {
					System.out.println(m.getKey() + " [" + secondString + " internal ranking]: " + m.getValue());
				}
			}

			System.out.println();

			for (Map.Entry m : secondFreqTable.entrySet()) {
				if (testTop.contains(m.getKey())) {
					secondCompareVal = (Double) m.getValue() + secondCompareVal;
					System.out.println(m.getKey() + " [" + secondString + " internal freq]: " + m.getValue());
				}
			}
		} catch (Exception e) {
			System.out.println("second compare error: " + e);
		}
	}

	/**
		*
		*
		*
	**/
	public void thirdCompare() {
		try {
			visitedThird = true;
			System.out.println("\n[Compare with " + thirdString + "]");
			for (Map.Entry m : thirdRankedTable.entrySet()) {
				if (testTop.contains(m.getKey())) {
					System.out.println(m.getKey() + " [" + thirdString + " internal ranking]: " + m.getValue());
				}
			}

			System.out.println();

			for (Map.Entry m : thirdFreqTable.entrySet()) {
				if (testTop.contains(m.getKey())) {
					thirdCompareVal = (Double) m.getValue() + thirdCompareVal;
					System.out.println(m.getKey() + " [" + thirdString + " internal freq]: " + m.getValue());
				}
			}
		} catch (Exception e) {
			System.out.println("third compare error: " + e);
		}
	}

	/**
		*
		*
		*
	**/
	public void predict() {
		for (Map.Entry m : testFreqTable.entrySet()) {
			firstCompareResult = firstCompareVal - testCompareVal;
		}
		System.out.println("\n" + firstString + " Total Freq: " + firstCompareVal);
		System.out.println("TestFile Total Freq: " + testCompareVal);
		System.out.println("Calculated Frequency: " + firstCompareResult);

		for (Map.Entry m : testFreqTable.entrySet()) {
			secondCompareResult = secondCompareVal - testCompareVal;
		}
		System.out.println("\n" + secondString + " Total Freq: " + secondCompareVal);
		System.out.println("TestFile Total Freq: " + testCompareVal);
		System.out.println("Calculated Frequency: " + secondCompareResult);

		for (Map.Entry m : testFreqTable.entrySet()) {
			thirdCompareResult = thirdCompareVal - testCompareVal;
		}
		System.out.println("\n" + thirdString + " Total Freq: " + thirdCompareVal);
		System.out.println("TestFile Total Freq: " + testCompareVal);
		System.out.println("Calculated Frequency: " + thirdCompareResult);

		if (firstCompareResult > secondCompareResult && firstCompareResult > thirdCompareResult) {
			System.out.println("\nTest file language predicition: " + firstString);
		}

		else if (secondCompareResult > firstCompareResult && secondCompareResult > thirdCompareResult) {
			System.out.println("\nTest file language predicition: " + secondString);
		}

		else if (thirdCompareResult > firstCompareResult && thirdCompareResult > secondCompareResult) {
			System.out.println("\nTest file language predicition: " + thirdString);
		}

		else if (firstCompareResult == secondCompareResult && firstCompareResult == thirdCompareResult
				&& thirdCompareResult == secondCompareResult) {
			System.out.println("\nUnable to predict language");
		}
	}
}