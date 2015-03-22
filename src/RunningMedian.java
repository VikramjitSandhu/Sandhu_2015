/**
Class: RunningMedian
@author: Vikramjit Sandhu
This class maintains the running median for a series of natural numbers
passed. It does this by maintaining two heaps a low and a high heap.
The low heap stores the smallest 50% of the numbers and the high heap stores
the greatest 50% of the numbers. The difference between the size of the two heaps
is never greater than 1.
*/
public class RunningMedian {
	/**
		this function returns the running median
		@param: value: the new value passed
				lowHeap: the heap storing the smallest 50% of the values
				highHeap: the heap storing the largest 50% of the values
	*/
	public float calculateRunningMedian(int value, MaxHeap lowHeap, MinHeap highHeap){
		
		//if both the heaps are empty, we can store the value in any heap.
		//I chose to store in the low heap
		if(lowHeap.isEmpty() && highHeap.isEmpty()){
			lowHeap.add(value);
			//the median is the only value passed
			//System.out.println("0: "+ (float)lowHeap.peek());
			return((float)lowHeap.peek());
		}
		else{
			//if the new value passed is in smaller than the largest value in the
			//min heap, then it must go in the low heap.
			if(value < lowHeap.peek()){
				lowHeap.add(value);
				//if after adding a value to the low heap, the heaps become unbalanced,
				//remove the maximum value from the low heap and add it to the high heap
				if((lowHeap.size() - highHeap.size()) > 1){
					highHeap.add(lowHeap.removeMax());
				}
			}
			//if the new value passed is in greater than the smallest value in the
			//high heap, then it must go in the high heap.
			else {
				highHeap.add(value);
				//if after adding a value to the high heap, the heaps become unbalanced,
				//remove the minimum value from the high heap and add it to the low heap
				if((highHeap.size() - lowHeap.size()) > 1){
					lowHeap.add(highHeap.removeMin());
				}
			}
			
			//if the number of elements in the high and low heaps are equal
			//i.e. there have been an even number of word counts passed,
			//then the median is the average of the maximum in the low and 
			//minimum in the high heap
			if((lowHeap.size()+highHeap.size())%2 == 0){
							//System.out.println("1: "+ ((float)(lowHeap.peek() + highHeap.peek())/2));
				return ((float)(lowHeap.peek() + highHeap.peek())/2);
			}
			//if the low heap has more elements, the median is the maximum
			//of the low heap
			else if(lowHeap.size() > highHeap.size()){
				//System.out.println("2: "+(float)lowHeap.peek());
				return((float)lowHeap.peek());
			}
			//if the high heap has more elements, the median is the minimum
			//of the high heap
			else{
				//System.out.println("3: "+(float)highHeap.peek());
				return((float)highHeap.peek());
			}
		}
	}

}
