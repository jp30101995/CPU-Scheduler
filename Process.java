
package cpuscheduler;
import java.io.*;
public class Process{
    long PID=0;     
    static long nextPID = 0; 
    long burst=0;   
    long initBurst=0;
    long delay=0;   
    long priority=0;
    long arrival=0; 
    long start=0;   
    long finish=0;  
    long wait=0;   
    long response=0; 
    long lifetime = 0;
    boolean arrived = false;
    boolean started=false;
    boolean finished=false;
    boolean active = false;
    Process(){
	nextPID++;
	PID = nextPID;
	burst = (long) (Math.random() * 99 + 1 );
	initBurst = burst;
	delay = (long) (Math.random() * 70 );
	priority = (long) Math.round((Math.random() * 9));
    }
    Process(long b, long d, long p){
	nextPID++;
	PID = nextPID;
	burst = b;
	initBurst = burst;
	delay = d;
	priority = p;
    }
    public  synchronized void executing(long timeNow){
	
	active=true;

	if( timeNow == arrival ){
	    arrived = true;
	}
	    
	if( burst == initBurst){
	    started  = true;
	    start    = timeNow;
	    response = start - arrival;
	}
	    
	burst--;
	lifetime++;
	    
	if( burst == 0){
	    finished = true;
	    finish = timeNow;
	}
    }
    public synchronized void waiting(long timeNow){
 	active=false;
	lifetime++;
	wait++;
	if( timeNow == arrival ){
	    arrived = true;
	}
    }
    public long getResponseTime() {return response;}
    public long getWaitTime() {return wait;}
    public long getFinishTime() {return finish;}
    public long getStartTime() {return start;}
    public void setStartTime(long  v) {this.start = v;}
    public long getArrivalTime() {return arrival;}
    public void setArrivalTime(long  v) {this.arrival = v;}
    public long getPriorityWeight() {return priority;}
    public long getDelayTime() {return delay;}
    public long getBurstTime() {return burst;}
    public long getInitBurstTime() {return initBurst;}
    public long getPID() {return PID;}
    public long getLifetime(){ return lifetime; };
    public void restore(){
	burst = initBurst;
	lifetime = 0;
	response = 0;
	start    = 0;
	wait     = 0;
	active   = false;
	started  = false;
	finished = false;
	arrived  = false;
    }
    public boolean isActive() {return active;}
    public boolean isFinished() {return finished;}
    public boolean isStarted() {return started;}
    public boolean isArrived() { return arrived; }
	public void print(){
	System.out.println( PID + " ");
    }


} 

 
