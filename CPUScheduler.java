
package cpuscheduler;
import java.util.*;
import java.io.*;
import java.text.*;
public class CPUScheduler{
    public static final int FCFS       = 1;
    public static final int SJF        = 2;
    public static final int PRIORITY   = 3;
    public static final int ROUNDROBIN = 4;
    static final int DEF_PROC_COUNT=50;
	public static int QuantumCounter=0;
	public static int count=0;
    long currentTime=0;
    long idle=0;
    long busy=0;
    long quantum=10;
    long quantumCounter=quantum;
    long turnCounter=0;
    int procsIn=0;
    int procsOut=0;
    boolean preemptive=false;
    boolean priority=false;
    int algorithm=this.FCFS;
    Vector allProcs   = new Vector(DEF_PROC_COUNT);
    Vector jobQueue   = new Vector(DEF_PROC_COUNT);
    Vector readyQueue = new Vector(DEF_PROC_COUNT);
    Process activeJob = null;
    int     activeIndex = 0;
    int minWait=0,maxWait=0;
    double meanWait=0.0,sDevWait=0.0;
    int minResponse=0,maxResponse=0;
    double meanResponse=0.0,sDevResponse=0.0;
    int minTurn=0,maxTurn=0;
    double meanTurn=0.0,sDevTurn=0.0;
    CPUScheduler(){
    }
    CPUScheduler(File filename){
	activeJob = null;
	Process proc = null;
	String s = null;
	long b=0,d=0,p=0;
	try{
	    BufferedReader input = new BufferedReader(new FileReader(filename));
  	    while( (s = input.readLine()) != null ){
  	        StringTokenizer st = new StringTokenizer(s);
  	        b = Long.parseLong(st.nextToken());
  	        d = Long.parseLong(st.nextToken());
  	        p = Long.parseLong(st.nextToken());
  	        proc = new Process(b,d,p);
  	        allProcs.add(proc);
 	    }
	    
	}
	catch(FileNotFoundException fnfe){}
	catch(IOException ioe){}
    LoadJobQueue(allProcs);
    }
    void Schedule(){
	switch( algorithm ){
	case FCFS :
	    RunFCFS(readyQueue);
	    break;
	case SJF :
	    RunSJF(readyQueue);
	    break;
	case PRIORITY :
	    RunPriority(readyQueue);
	    break;
	case ROUNDROBIN :
	    RunRoundRobin(readyQueue);
	    break;
	default:
	    System.out.println("Not a valid scheduling algorithm");
	    break;
	}
	Dispatch();
    }
    
	void Dispatch(){
	Process p=null;
	activeJob.executing(currentTime);
	for(int i=0 ; i < readyQueue.size() ; ++i){
	    p = (Process) readyQueue.get(i);
	    if( p.getPID() !=  activeJob.getPID() ){
		p.waiting(currentTime);
	    }
	}
	for(;QuantumCounter%10==0;QuantumCounter++){
		System.out.println(QuantumCounter/10+" - >"+activeIndex);
	}
	
	
    }
    
	void RunFCFS(Vector jq){
	Process p;
	try {
	    if(  busy == 0 || activeJob.getBurstTime() == 0 ){
		activeJob = findEarliestJob(jq);
		activeIndex = jq.indexOf(activeJob);
	    }
	}
	catch( NullPointerException e){
	}
    }
	
	void RunSJF(Vector jq){
	Process p;
	try {
	    if(  busy == 0 || activeJob.isFinished() || preemptive == true ){
		activeJob = findShortestJob(jq);
		activeIndex = jq.indexOf(activeJob);
	    }
	}
	catch( NullPointerException e){
	}
    }

    void RunPriority(Vector jq){
	try {
	    if( busy == 0 || activeJob.isFinished() || preemptive == true ){
		activeJob = findLoftiestJob(jq);
		activeIndex = jq.indexOf(activeJob);
	    }
	}
	catch( NullPointerException e){
	}
    }

    void RunRoundRobin(Vector jq){
	Process p=null;
	
	try {
	    if( busy == 0 || activeJob.isFinished() || quantumCounter == 0){
		activeJob = findNextJob(jq);
		activeIndex = jq.indexOf(activeJob);
		    quantumCounter = quantum;
			//QuantumCounter++;
	    }
	    quantumCounter--;
	}
	catch( NullPointerException e){
	}
    }


    Process findNextJob(Vector que){
	Process p = null , nextJob = null;
	int index=0;
	if( activeIndex >= (que.size() - 1) )
	    index = 0;
	else if( activeJob != null && activeJob.isFinished() ){
	    index = activeIndex;
	}
	else{
		index = (activeIndex + 1);
	}
	nextJob = (Process) que.get(index);
	return nextJob;
    }

    Process findShortestJob(Vector que){
	Process p=null,shortest=null;
	long time=0,shorttime=0;
	
	for(int i=0; i < que.size(); ++i){
	    p = (Process) que.get(i);
	    time = p.getBurstTime();
	    if( (time < shorttime) || (i == 0) ){
		shorttime = time;
		shortest = p;
	    }
	}
	return shortest;
    }
    Process findEarliestJob(Vector que){
	Process p=null,earliest=null;
	long time=0,arrTime=0;
	
	for(int i=0; i < que.size(); ++i){
	    p = (Process) que.get(i);
	    time = p.getArrivalTime();
	    if( (time < arrTime) || (i == 0) ){
		arrTime = time;
		earliest = p;
	    }
	}
	return earliest ;
    }
    Process findLoftiestJob(Vector que){
	Process p=null,loftiest=null;
	long priority=0, highest=0;
	
	for(int i=0; i < que.size(); ++i){
	    p = (Process) que.get(i);
	    priority = p.getPriorityWeight();
	    if( ( priority < highest ) || (i == 0) ){
		highest = priority;
		loftiest = p;
	    }
	}
	return loftiest;
    }
    private void harvestStats(){
	int allWaited=0,allResponded=0, allTurned=0;
//	int sDevWaited=0, sDevWaitedSquared=0;
//	int sDevTurned=0,sDevTurnedSquared=0;
//	int sDevResponded=0,sDevRespondedSquared=0;
	int startedCount=0,finishedCount=0;
	Process p=null;
	int i=0;

	for( i=0;i < allProcs.size();i++){
	    p = (Process) allProcs.get(i);
	    if(p.isStarted() ){
		startedCount++;
		int responded = (int) p.getResponseTime();
		allResponded += responded;
	    }
	}
	
	if( startedCount > 0){
	    meanResponse = ((double)allResponded) / ((double)startedCount);

	}
	else{
	    meanResponse = 0.0;
	}


	for( i=0;i < allProcs.size();i++){
	    p = (Process) allProcs.get(i);
	    
	    if( p.isFinished() ){
		finishedCount++;
		int waited = (int) p.getWaitTime();
		int turned = (int) p.getLifetime();
		allWaited += waited;
		allTurned += turned;
	    }
	}


	if( finishedCount > 0){
	    meanWait = (double)allWaited / (double)finishedCount;
	    meanTurn = (double)allTurned/(double)finishedCount;
	}
	else{
	    meanWait = 0.0;
	    meanTurn = 0.0;
	}

    }
    	
	public void printReadyQueue(){
	Process p;
	for(int i=0; i < readyQueue.size(); i++){
	    p = (Process) readyQueue.get(i);
	    p.print();
	    System.out.println("---------------");
	}
    }

    void LoadReadyQueue(){
	Process p;
	for(int i=0; i < jobQueue.size() ; i++){
	    p = (Process) jobQueue.get(i);
	    if( p.getArrivalTime() == currentTime){
		readyQueue.add(p);
		procsIn++;
	    }
	}
	
    }
    
    void PurgeReadyQueue(){
	Process p;
	for(int i=0; i < readyQueue.size(); i++){
	    p = (Process) readyQueue.get(i);
	    if( p.isFinished() == true ){
		readyQueue.remove(i);
		procsOut++;
	    }
	}
    }	
//system
    void PurgeJobQueue(){
	Process p;
	for(int i=0; i < jobQueue.size(); i++){
	    p = (Process) jobQueue.get(i);
	    if( p.isFinished() == true ){
		jobQueue.remove(i);
	    }
	}
    }
	
    public void LoadJobQueue(Vector jobs){
	Process p;
	long arTime = 0;
	for(int i = 0 ; i < jobs.size() ; i++ ){
	    p  = (Process) jobs.get(i);
	    arTime += p.getDelayTime();
	    p.setArrivalTime(arTime);
	    jobQueue.add(p);
 	}
    }
    public boolean getPreemption() {return preemptive;}
    public void setPreemption(boolean  v) {this.preemptive = v;}
    public void setAlgorithm(int algo){ algorithm = algo;}
    public int getAlgorithm(){ return algorithm; }
    public long getIdleTime() {return idle;}
    public long getTotalTime() {return currentTime;}
    public long getBusyTime() {return busy;}
    public long getQuantum() {return quantum;}
    public void setQuantum(long  v) {this.quantum = v;}
    public boolean getPriority() {return priority;}
    public void setPriority(boolean  v) {this.priority = v;}
    public long getProcsOut() {return procsOut;}
    public long getProcsIn() {return  procsOut;}
    public double getLoad() { return ( (double)procsIn/(double)procsOut ) ; }
    public Process getActiveProcess( ){ return activeJob; }
 //   public void Simulate(){
//	while( nextCycle() );
 //   }
    public boolean nextCycle(){
	boolean moreCycles=false;
	if( jobQueue.isEmpty() ){
	    moreCycles = false;
	}
	else{
	    LoadReadyQueue();
	    moreCycles = true;
	    if( readyQueue.isEmpty() ){
		idle++;
	    }
	    else{
		Schedule();
		busy++;
		cleanUp();
	    }
	    currentTime++;
	}
	/*for(int i=0;i<readyQueue.size();i++){
		Process p=(Process)readyQueue.get(i);
		System.out.println(p.getPID()+" ");
	}*/
	QuantumCounter++;
	/*System.out.println(QuantumCounter+"-----------");
	*/harvestStats();
	return moreCycles;
    }
    void cleanUp(){
	PurgeJobQueue();
	PurgeReadyQueue();
    }
    public void restore(){
	Process p;

	activeJob = null;
	currentTime = 0;
	busy = 0;
	idle = 0;
	procsIn = 0;
	procsOut = 0;
	quantum = 10;
	quantumCounter = quantum;
	turnCounter = 0;

	minWait=0;
	meanWait=0;
	maxWait=0;
 	sDevWait=0;

	minResponse=0;
	meanResponse=0;
	maxResponse=0;
 	sDevResponse=0;

	minTurn=0;
	meanTurn=0;
	maxTurn=0;
 	sDevTurn=0;

	for(int i=0;i<allProcs.size();i++){
	    p = (Process) allProcs.get(i);
	    p.restore();
	}
	jobQueue.clear();
	readyQueue.clear();
	LoadJobQueue(allProcs);

    }

    public Vector getJobs()      { return allProcs; }
    public double getMeanWait()  { return meanWait; }
    public int getMinWait()      { return minWait;  }
    public int getMaxWait()      { return maxWait;  }
    public double getStdDevWait(){ return sDevWait; }
     public double getMeanResponse()  { return meanResponse; }
    public int getMinResponse()      { return minResponse;  }
    public int getMaxResponse()      { return maxResponse;  }
     public double getStdDevResponse(){ return sDevResponse; }
     public double getMeanTurn()  { return meanTurn; }
     public int getMinTurn()      { return minTurn;  }
    public int getMaxTurn()      { return maxTurn;  }
    public double getStdDevTurn(){ return sDevTurn; }
    public String getAlgorithmName(){
	String s="";
	switch( algorithm ){
	case FCFS :
	    s = "First come first serve";
	    break;
	case SJF :
	    s = "Shortest job first";
	    break;
	case PRIORITY :
	    s = "Priority Weighted";
	    break;
	case ROUNDROBIN :
	    s = "Round Robin";
	    break;
	default:
	    break;
	}
	return s;
    }

}
