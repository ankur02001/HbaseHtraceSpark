package com.syracuse.aca;
/**
 * 
 * @author tushar
 *Code for InterThread communication.
 */

class Data{
	private int data;
	private boolean isAvailable=false;
	boolean bool=true;
	
	public synchronized void put(int content)
	{
		if(isAvailable)
		{
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		isAvailable=true;
		data=content;
		notify();
	}

	public synchronized int get()
	{
		if(!isAvailable)
		{
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		isAvailable=false;
		notify();
		return data;
	}

	
}


class Producer implements Runnable
{
   Data d;
   Thread producer;
   
   static long start_producer;
  // long stop_producer;
   
	public Producer(Data d) {
	this.d = d;
	start_producer=System.currentTimeMillis();
	System.out.println("Start producer:"+ start_producer);
	producer=new Thread(this,"Producer");
	producer.start();
}
   
   
	public Producer() {
		// TODO Auto-generated constructor stub
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(int i=0;i<2048;i++)
		{	d.put(i);
			System.out.println("Producer, put data : "+i);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		//stop_producer=System.currentTimeMillis();
		//System.out.println("Time taken by producer to produce elements: "+ (stop_producer-start_producer));
	}
	
	
	}

class Consumer implements Runnable
{
Data d;
Thread consumer;
long stop_consumer;

	public Consumer(Data d) {
	this.d = d;
	consumer=new Thread(this,"Consumer");
	consumer.start();
}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(int i=0;i<2048;i++){
		int val=d.get();
		System.out.println("Consumer, got data: "+val);
		}
		
		Producer p=new Producer();
		stop_consumer=System.currentTimeMillis();
		System.out.println("producer starts at: "+ p.start_producer +" ms");
		System.out.println("consumer stops at: "+ stop_consumer + " ms");
		long latency=stop_consumer-p.start_producer+100;
		System.out.println("overall latency to produce and consume is: "+latency +" ms");
		System.out.println("Communiaction bandwidth is: "+ (latency/8000) + " kb/sec");
	}
	
}


public class ThreadCommunication {
    public static void main(String[] args) {
    	Data d = new Data();
        Producer p=new Producer(d);
        Consumer c=new Consumer(d);
    }
}