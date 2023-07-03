/*
 * Programmer: Richard Caraballo
 * Description: This program creates an array of 2 million random numbers
 * 				which are added first by a single thread, and then by
 * 				two threads in parallel. Their sum and the time taken
 * 				to calculate the sum are displayed.
 */

package cen3024;

public class Main{
	
	//used to sum the numbers in the single thread
	static int singleSum = 0;
	//used to sum the numbers in parallelThread1 and parallelThread2
	static int parallelSum = 0;
	
	//array used for holding 2 million randomly-generated numbers
	static int[] numArray;
	
	public static void main(String[] args) {
		
		//will be used to determine how long each process takes
		long startTime;
		long endTime;
		
		//fills numArray with randomly-generated numbers from 1-10
		numArray = new int[2000000];
		for(int i = 0; i < 2000000; i++) {
			numArray[i] = (int)Math.ceil(Math.random() * 10);
		}
		
		
		//runs a single thread which adds up the 2 million numbers in numArray
		Thread singleSumThread = new Thread(new Runnable() {
			public void run() {
				for(int i = 0; i < 2000000; i++) {
					singleSum += numArray[i];
				}
			}
		});
		startTime = System.nanoTime();
		singleSumThread.start();
		
		//join() allows singleSumThread to finish before the print statement displays the sum
		try {
			singleSumThread.join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		};
		
		endTime = System.nanoTime();
		System.out.println("The sum of the single thread is: " + singleSum);
		System.out.println("The time taken for the single thread was: " + (endTime - startTime) + " nanoseconds");
		
		
		
		//runs two threads at once which both add their sums of 2 million total numbers
		Thread parallelThread1 = new Thread(new Runnable() {
			public void run() {
				parallelSum();
			}
		});
		parallelThread1.setName("Parallel Thread 1");
		startTime = System.nanoTime();
		parallelThread1.start();
		
		Thread parallelThread2 = new Thread(new Runnable() {
			public void run() {
				parallelSum();
			}
		});
		parallelThread2.setName("Parallel Thread 2");
		parallelThread2.start();
		
		//makes both parallel threads wait to finish before their sum is displayed
		try {
			parallelThread1.join();
			parallelThread2.join();	
		}catch(InterruptedException e) {
			e.printStackTrace();
		};
			
		endTime = System.nanoTime();
		System.out.println("The sum of the two parallel threads is: " + parallelSum);
		System.out.println("The time taken for the parallel threads was: " + (endTime - startTime) + " nanoseconds");
	}	
	
	//used to make sure all numbers from the parallel threads are added before the print statement
	//displays their total sum by using the synchronized keyword
	public static synchronized void parallelSum() {
		for(int i = 0; i < 1000000; i++) {
			//each thread is named so Parallel Thread 1 adds the first half of the array and
			//Parallel Thread 2 adds the second half
			if(Thread.currentThread().getName() == "Parallel Thread 1") {
				parallelSum += numArray[i];
			}
			if(Thread.currentThread().getName() == "Parallel Thread 2") {
				parallelSum += numArray[i + 1000000];
			}
		}
	}

}
