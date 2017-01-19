package cpuscheduler;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.net.*;
//nextCycle
public class CPUSchedulerFrame extends JFrame implements ActionListener {
    CPUScheduler cpu;
    JCheckBox startCB;
	//JScrollPane scrollBar;
 	JPanel ButtonPanel=new JPanel();
    StatsPanel responseSP;
    int frameNumber = -1;
    Timer timer;
    boolean frozen = true;
    JPanel contentPane, queuePanel, buttonPanel;
    String fileName="";
    
	
	
	public CPUSchedulerFrame(){
		    File fileName=new File("C:/Java/src/cpu/data/jimmy.dat");
	        cpu = new CPUScheduler(fileName);
	int delay = 100;
        timer = new Timer(delay,this);
        timer.setCoalesce(false); // don't combine queued events
	timer.setInitialDelay(0);
	setTitle("CPU Scheduler");
	setSize(790,390);
	setBackground(new Color(123,123,0));
	buildButtons();
	queuePanel = new JPanel();
	buildMenus();
	buildStatusPanels();
	fillQueuePanel();
	//buildFileDialog();
	updateReadouts();

	Container masterPanel = getContentPane() ;
	masterPanel.setLayout(new BoxLayout(masterPanel,BoxLayout.Y_AXIS));
	ButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	
	JPanel topRow = new JPanel();
	topRow.setLayout(new BorderLayout());
	topRow.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

	JPanel middleRow = new JPanel();
	middleRow.setLayout(new FlowLayout(FlowLayout.CENTER));
	middleRow.setBackground(Color.white);
	middleRow.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

	JPanel bottomRow = new JPanel();
	bottomRow.setLayout(new BoxLayout(bottomRow,BoxLayout.Y_AXIS));
	bottomRow.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	topRow.add(queuePanel,"North");
	

	responseSP.setBackground(Color.white);
	middleRow.add(responseSP);

	bottomRow.add(middleRow,"Center");
	bottomRow.add(startCB,"South");

	masterPanel.add(ButtonPanel);
	masterPanel.add(topRow);
	masterPanel.add(bottomRow);

	addWindowListener(
			  new WindowAdapter() {
				  public void windowClosing(WindowEvent e) { 
				      System.exit(0); 
				  } 
			      } 
			  );
	setVisible(true);
    }
    public void emptyQueuePanel(){
	queuePanel.removeAll();
    }

    public void resetQueuePanel(){
	ProcessPanel p;
	int num = queuePanel.getComponentCount();
	for(int i=0; i < num;i++){
	    p = (ProcessPanel) queuePanel.getComponent(i);
	    p.setVisible(true);
	}
    }

    public void fillQueuePanel(){
	Vector v = cpu.getJobs();
 	queuePanel.setBackground(Color.white);
 	queuePanel.setOpaque(true);
	FlowLayout flay = new FlowLayout(FlowLayout.LEFT);
	queuePanel.setLayout(flay);
	for( int i = 0; i < v.size() ; i++){
	    ProcessPanel p = new ProcessPanel( (Process) v.get(i) );
	    queuePanel.add(p,"Left");
	}
    }

//System
    void buildStatusPanels(){
	responseSP = new StatsPanel("Response");
	responseSP.setStats(0,0,0);
    }

    public void actionPerformed(ActionEvent e){
	if( e.getSource() == startCB ){
	    if(frozen == false){
		frozen = true;
		stopAnimation(); 
	    }
	    else{
		frozen = false;
		startAnimation();
	    }
	}
	else if( e.getSource() == timer ){
	    if ( cpu.nextCycle() == true ){
		updateReadouts();
	    }
	    else{
		stopAnimation();
		startCB.setSelected(false);
	    } 
		repaint();
	}
	else if( e.getActionCommand().equals("FCFS")){
	    cpu.setAlgorithm(CPUScheduler.FCFS);
	//	System.out.println("FCFS Algorithm");
	}
	else if( e.getActionCommand().equals("RoundRobin")){
	    cpu.setAlgorithm(CPUScheduler.ROUNDROBIN);
	}
	else if( e.getActionCommand().equals("SJF")){
	    cpu.setAlgorithm(CPUScheduler.SJF);
	}
	else if( e.getActionCommand().equals("Priority")){
	    cpu.setAlgorithm(CPUScheduler.PRIORITY);
	}
}
    public void start() {
        startAnimation();
    }
    public void stop() {
        stopAnimation();
    }
    public synchronized void startAnimation() {
        if (frozen) { 
        } else {
            if (!timer.isRunning()) {
                timer.start();
            }
        }
    }
    public synchronized void stopAnimation() {
        if (timer.isRunning()) {
            timer.stop();
        }
    }
    void updateReadouts(){
	responseSP.setStats( cpu.getMeanTurn(),
			     cpu.getMeanResponse(),
			     cpu.getMeanWait());
    }   
    void buildMenus(){

	JButton fcfsRB=new JButton("FCFS");
	fcfsRB.addActionListener(this);
	ButtonPanel.add(fcfsRB);
	JButton sjfRB=new JButton("SJF");
	sjfRB.addActionListener(this);
	ButtonPanel.add(sjfRB);
	JButton rrRB=new JButton("RoundRobin");
	rrRB.addActionListener(this);
	ButtonPanel.add(rrRB);
	JButton priRB=new JButton("Priority");
	priRB.addActionListener(this);
	ButtonPanel.add(priRB);
	this.add(ButtonPanel);
	
    }
    void buildButtons(){
	startCB = new JCheckBox();
	startCB.addActionListener(this);
    }
    
} 



