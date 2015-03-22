/**
Class: MyWordCount
@author: Vikramjit Sandhu
This file reads all the files stored in directory wc_input
and counts the occurrence of each word and prints the word and its
frequency in a file wc_result in directory wc_output. It also creates
another file med_result in directory wc_output which contains the
running median of the number of words in each file in directory 
wc_input
*/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Pattern;


public class MyWordCount {
	//heap to store the smallest 50% of the values
	private static MaxHeap lowHeap = new MaxHeap();
	
	//heap to store the greatest 50% of the values
	private static MinHeap highHeap = new MinHeap();
	
	//the instance of the running median class which keeps
	//a track of the running median
	private static final RunningMedian rm = new RunningMedian();
	
	//the location of the input directory relative to this file location
	private static String inputPath = "..//wc_input";
	//the location of the output directory relative to this file location
	private static String outputPath = "..//wc_output";

	public static void main(String[] args) {
		//open the input file folder
		final File folder = new File(inputPath);
		
		//list of file names
		ArrayList<String> fileNames = new ArrayList<String>();
		
		//list of file input streams created from the file names in the
		//input directory
		ArrayList<InputStream> fileStreams = new ArrayList<InputStream>();
		String currentFileName = null;
		
		System.out.println("Processing. Please wait");
		
		//read and store all file names in the input directory
		for (final File fileEntry : folder.listFiles()) {
			currentFileName = inputPath+"//"+fileEntry.getName();
			fileNames.add(currentFileName);
	    	}
		
		//sort the file names by name
		Collections.sort(fileNames);
		
		//create a file input stream from a file name and store it
		for(String fileName : fileNames){
			try{
				fileStreams.add(new FileInputStream(fileName));
			}catch(FileNotFoundException e){
				
			}
		}
		
		//create an enumeration of file input streams. The enumeration preserves the order of file names
		//during creation unless a new one gets added (which does not in this scenario)
		Enumeration<InputStream> enu = (Enumeration<InputStream>) Collections.enumeration(fileStreams);
		//compress all file input streams into one stream so that the files may be read
		//sequentially
		SequenceInputStream combinedStreams = new SequenceInputStream(enu);
		
		//this hashtable stores every unique word read from the files and also stores
		//the number of times the word occurs in all the files in directory wc_input
		Hashtable<String, Integer> allWords = new Hashtable<String, Integer>();
		
		//Output stream for creating file med_result.txt
		PrintWriter out = null;

		try{
			out = new PrintWriter(outputPath+"//med_result.txt", "UTF-8");
			
			//get the hashtable containing the words and their frequencies
			allWords = readFile(combinedStreams, allWords, rm, lowHeap, highHeap, out);
		}catch(Exception e){
			
		}finally{
			out.close();
		}
		
		//get the key i.e. words in the hastable
		ArrayList<String> sortedWords = new ArrayList<String>(allWords.keySet());
		//sort the words by alphabet
		Collections.sort(sortedWords);
		
		//output stream to write to file wc_result in directory wc_output
		PrintWriter writer = null;
		try{
			writer = new PrintWriter(outputPath+"//wc_result.txt", "UTF-8");
			for(String word : sortedWords){
				writer.println(word+"  "+allWords.get(word));
			}
		}
		catch(Exception e1){
			System.out.println("exception while writing file "+e1);
		}finally{
			writer.close();
		}

		System.out.println("Finished writing to files");
	}
	
	/**
		this function counts the frequency of each word in the input stream and stores and returns
		the hashtable containing that information
		it also keeps a track of the running median
		
		@param:
		InputStream: the input stream of all the files chained sequentially
		allWords: the hashtable in which words and their frequencies will be stored
		rm: an instance if the RunningMedian class
		lowHeap: a MaxHeap keeping a track of the smallest 50% of the values
		highHeap: a MiHeap keeping track of the greatest 50% of the values
		
		@return: hashtable of words and their frequencies
	*/
	public static Hashtable<String, Integer> readFile(SequenceInputStream inputStream, Hashtable<String, Integer> allWords, 
            RunningMedian rm, MaxHeap lowHeap, MinHeap highHeap, PrintWriter writer){	
		
		//a Scanner object will be used so that files are read line by line i.e.
		//the entire file is not kept in memory and the line is discarded after being processed
		Scanner sc = null;
		try {
			//create the scanner from the chained input stream
		    sc = new Scanner(inputStream, "UTF-8");
		    int count = 0;
			
			//while there are lines to be read form the input stream
		    while (sc.hasNextLine()) {
				
				//read the line and remove leading and trailing blank spaces
		        String line = sc.nextLine().trim();
			//System.out.println("the trimmed line is "+line);	
				//remove all instances of characters that are not alphabets but leave apostrophe and 
				//hyphens
		        String [] wordsInLine = line.replaceAll("[^a-zA-Z'\\- ]", " ").toLowerCase().split("\\s+");
		        int len = wordsInLine.length;
		        //if the line read in is a blank line, pass 0 length
		        if(wordsInLine.length == 1){
		        	if(wordsInLine[0].trim().length() == 0)
		        		len = 0;
		        }
				
				//calculate the running median and write it to file med_result
			//System.out.println("length passed is: "+len);
		        writer.println(rm.calculateRunningMedian(len, lowHeap, highHeap));
			
				//create a mapping of the words and their frequencies
		        allWords = createWordMapping(allWords, wordsInLine);
		    }
		    if (sc.ioException() != null)
		        throw sc.ioException();
		} 
		catch(IOException e){
			System.out.println("exception in function block "+e);
		}
		finally {
			try {
			    if (inputStream != null) 
			        inputStream.close();
			}
		    catch(IOException e)		    {
		    	System.out.println("exception in finally block "+e);
		    }
		    if (sc != null) 
		        sc.close();
		}
		return allWords;
		
	}
	
	/**
		This function creates a mapping of each word and its frequency of occurrence
		@param:
		allWords: the hashtable in which a unique word is stored as the key and its frequency as the values
		words: the line form each file currently being read
		@return:
		the hashtable in which a unique word is stored as the key and its frequency as the values
	*/
	public static Hashtable<String, Integer> createWordMapping(Hashtable<String, Integer> allWords, String[] words){
		String word = null;
		//for each word in the line
		for(int i = 0; i < words.length; i++){
			//convert word to lower case
			word = words[i].toLowerCase();
			//remove any hyphens and apostrophe
			word = word.replaceAll("[-']+","");
			if(word!=null && word.trim().length()!=0){
				//if the word has not occurred before, store it 
				//in the hashtable with 0 value
				if(!allWords.containsKey(word))
					allWords.put(word, 0);
				//increase the frequency of the word by 1
				allWords.put(word, allWords.get(word)+1);
			}
		}
		return allWords;
	}

}
