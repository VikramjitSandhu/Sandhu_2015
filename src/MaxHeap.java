/**
Class: MaxHeap.java
@author: Vikramjit Sandhu

This class simulates a max heap with the elements stored in an array.
The maximum element of the heap is stored at index 1 (2nd position)of 
the array; The 1st position (index 0) does not play any part. This
Heap class is only able to store integer values as the number of words
in a line from a text file are always >= 0.
*/

import java.util.Arrays;


public class MaxHeap {
	
	//the default capacity of the array storing the heap
	private static final int defaultCapacity = 2;
	//the array storing the heap elements
	private int[] storage;
	//the current total number of elements stored in the heap
	private int numberOfElements;
	
	/**
	   Heap constructor. Construct the heap with an empty array
	   and initialize number of elements stored to 0
	*/
	public MaxHeap(){
		storage = new int[defaultCapacity];
		numberOfElements = 0;
	}
	
	/**
		function to add an element to the heap
		@param: the value to be added
		@return void
	*/
	public void add(int value){
		//if the array is full, allocate more space to
		//store the additional element
		if(numberOfElements >= storage.length - 1)
			storage = resize();
		
		//increase the number of elements stored in the array
		numberOfElements++;
		
		//store the element in the new position. The index of the
		//array where the new element is to be stored corresponds
		//to the current number of elements in the heap
		storage[numberOfElements] = value;
		
		//bubble up to maintain max heap property i.e.
		//storage[k] >= storage[2*k] and storage[k]>= storage[2*k+1]
		bubbleUp(numberOfElements);
	}
	
	/**
		this function restores the max heap property so that every
		parent  >= its children. i.e. storage[k] >= storage[2*k] 
		                           and storage[k]>= storage[2*k+1]
		@param: the element whic is at the wrong position
		@return:
	*/
	private void bubbleUp(int k){
		//while the new node is greater than its parent, swap
		//its position with the parent
		while( k > 1 && (storage[k] > storage[k/2]) ){
			swap(k,k/2);
			k = k/2;
		}
	}
	
	/**
		this function swaps two elements in the array
		at positions i and j
		@params: the two positions at which the elements need swapping
		@return void
	*/
	private void swap(int i, int j){
		int temp;
		temp = storage[i];
		storage[i] = storage[j];
		storage[j] = temp;
	}
	
	/**
		this function returns the first element in the
		array without removing it from the array
		@param:
		@return: the maximum element of the heap
	*/
	public int peek(){
		if(numberOfElements == 0)
			return -1;
		return storage[1];
	}
	
	/**
		this function returns the first element in the
		array after removing it from the array
		@param:
		@return: the maximum element of the heap
	*/
	public int removeMax(){
		
		//if there are no elements in the array
		//return -1: error condition
		if(numberOfElements == 0)
			return -1;
		
		//return the maximum value stored at index 1 of the array
		int minValue = storage[1];
		//after swapping, put the last element at the first position
		//this will violate the heap property
		storage[1] = storage[numberOfElements];
		//put -1 at the index where the element is removed from
		storage[numberOfElements] = -1;
		//reduce the number of elements by 1
		numberOfElements--;
		
		//bubble down to restore the heap property
		bubbleDown(1);
		//return the max value
		return minValue;
	}
	
	/**
		this function restores the max heap property so that every
		parent  >= its children. i.e. storage[k] >= storage[2*k] 
		                           and storage[k]>= storage[2*k+1]
		@param: the element which is at the wrong position
		@return:
	*/
	private void bubbleDown(int k){
		//index of child with which the node aka parent will be swapped
		//the parent is always swapped with the child having the
		//greater value
		int indexToSwapWith;
		
		while(2*k <= numberOfElements){
			//if the parent only has 1 child,
			//swap it if it is smaller than the child
			//else heap property is restored and break
			if(2*k == numberOfElements){
				if(storage[k] < storage[2*k])
					swap(k,2*k);
				break;
			}
			else{
				//the parent must be swapped with the child
				//having the greater value
				if(storage[2*k] > storage[2*k+1]){
					indexToSwapWith = 2*k;	
				}
				else{
					indexToSwapWith = 2*k+1;
				}
				//if the parent is greater than the child, then
				//heap property is restored and break
				if(storage[k] > storage[indexToSwapWith]){
					break;
				}
				//else swap parent with child
				swap(k, indexToSwapWith);
				//the new position of the node that may potentially
				//need to be swapped with its children
				k = indexToSwapWith;
			}
		}
	}
	
	/**
		this function is responsible for resizing the array
		when it runs out of space. It doubles the array length
		every time.
		@param:
		@return: an array with all the old elements and twice the size
	*/
	protected int[] resize() {
        return Arrays.copyOf(storage, storage.length * 2);
    }
	
	/**
		function to print the contents of the array
		representing the heap. Used for testing only
	*/
	public void printHeap(){
		for(int i = 0; i < storage.length; i++){
			System.out.print(storage[i]+" ");
		}
		System.out.println("");
	}
	
	/**
		function to check if heap is empty
		@param:
		@return: true or false corresponding to empty or
		non-empty heap respectively
	*/
	public boolean isEmpty(){
		return numberOfElements == 0;
	}
	
	/**
		function returning number of elemnts in the heap
		@param:
		@return: number of elements in the heap
	*/
	public int size(){
		return numberOfElements;
	}
	
}
