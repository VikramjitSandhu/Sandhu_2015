Important Note: Please read the following two points:

***********************************************************************************************************************
1)
This program will read data from all files of type *.txt. If any temporary files of type *.txt~ are present, 
they will also be treated as text files and the program reads them too. I found that editing a file (say abcd.txt)in gedit 
in Ubuntu 12.04 results in creating a file of type abcd.txt~ and the program reads this as a text file also along with abcd.txt.
************************************************************************************************************************

2)
The program works by combining multiple file input streams (as asked by the requirements,)present in the wc_input directory 
into one stream and then reading line by line from that stream. The last line in each text must be terminated with the newline 
character. Since I developed the program in Windows, I added the newline explicitly in the input text files whereas in Ubuntu 
I did not have to add the newline. The application Gedit seems to do it for you. 
 
 ******More precisely if********:
 
 Cat file1.txt gives:
 abc def
 ghi jkl
 
 and Cat file2.txt gives:
 klu ijk
 lmnb opit
 
 then Cat file1.txt file2.txt should give
 
 abc def
 ghi jkl
 klu ijk
 lmnb opit
 
 The above shows the files correctly terminated by the EOF and the word count by the program for the 4 lines will be
 2 2 2 2. If however
 
 Cat file1.txt file2.txt gives
 
 abc def
 ghi jklklu ijk
 lmnb opit
 
 Then the files have not been correctly terminated by the EOF and the word count for the 3 lines will be 2 3 2.
 
 I have tested the program on Ubuntu 12.04. I am assuming that you have the jdk 1.7 version installed; if not, please 
 uncomment the line starting with 'sudo' (line 3) to install the necessary java environment.
 
DESIGN:
-------
The program reads all files from the wc_input directory and creates  combined input stream for all files.
To avoid keeping all lines in memory, the program reads the lines from the combined stream using a Scanner object. The program 
then discards all punctuation from the current line read except for apostrophe and hyphen. Please note that any word separated by
slash e.g. you/me will be treated as two separate word i.e. 'you' and 'me'. It the passes the count to the running median problem
which stores it in either of two heap data structures; one is a low heap storing the smallest 50% of the numbers and the other
is a high heap storing the greatest 50% of the numbers. Specifically, if the value read in is less than the maximum value in the 
low heap it is put in the low heap else it is put in the max heap. If the heaps become unbalanced, i.e. if the difference in the 
number of stored elements in the heaps exceeds 1, then the max(min) value is removed from the low(high) heap and put in the high(low)
heap to balance them. The running median at any time is the

a: if an even number of elements have been passed, it is the average of the sum of the max value in the low 
   heap and the min value in the high heap
b: if an odd number of elements have been passed then it is the maximum element in the low heap if it has one more 
   element than the high heap else it is the minimum element in the high heap. 

After reading in a line, the program checks to see if the word has occurred before and how many times. It then increments the
count of that word by one and stores it in a hashtable with each unique forming the key and its frequency of occurrence stored as a 
value against the key.

I have placed three text files in the input directory for testing purposes where I have tried to include various combinations of 
punctuations, spaces, blank lines to show how the program deals with them.
   