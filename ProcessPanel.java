
package cpuscheduler;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.geom.*;
class ProcessPanel extends JPanel{
    Process proc;
    static final int PPWIDTH  = 15;
    static final int PPHEIGHT = 130;
    static final int BARHEIGHT = 110;
    Color burstColor,
	initBurstColor=Color.cyan,
	unarrivedColor,
	lblColor;
    JLabel priLbl,priLbl1;
    static boolean showHidden=false;
    ProcessPanel(){
	proc = new Process();
	initPanel();
    }
    ProcessPanel( Process p){
	proc = p;
	initPanel();
    }
    void initPanel(){
	setAlignmentX(Component.LEFT_ALIGNMENT);
	setLayout(new BorderLayout());
	priLbl = new JLabel(""+ (int)proc.getPID());
	priLbl1 = new JLabel(""+(int)proc.getPriorityWeight());
	setSize(PPWIDTH,PPHEIGHT);
	setBackground(Color.white);
	setOpaque(true);
	add(priLbl,"South");
	add(priLbl1,"North");
    }
    public void paintComponent(Graphics g){
	super.paintComponent(g);
	if ( proc.isFinished() == true){
	    setVisible(true);
		g.setColor(Color.black);
		g.drawRect(0,BARHEIGHT-(int)proc.getInitBurstTime(),PPWIDTH-5,(int)proc.getInitBurstTime());
		
	}
	else {
	    DrawBursts(g);
	}
    }
    void DrawBursts(Graphics g){
	int initBurstHeight=0,burstHeight=0;
	int width=0;
	initBurstHeight = (int) proc.getInitBurstTime();
	burstHeight = (int) proc.getBurstTime();
	width  = (int) PPWIDTH-5; // off by one error in swing?
	lblColor = ( proc.isArrived()  ? Color.black :
		     (showHidden ? Color.darkGray : Color.white) );
	initBurstColor = ( proc.isArrived() ? Color.black : Color.yellow );
	burstColor  = (proc.isArrived() ) ? 
	    (proc.isActive() == true ? Color.green : Color.orange ):
	    (showHidden ? Color.darkGray : Color.white) ;

	priLbl.setForeground( lblColor );
	priLbl1.setForeground(lblColor);
	if( proc.isArrived() ){
	    g.setColor(initBurstColor);
	    g.drawRect(0,BARHEIGHT-initBurstHeight,width,initBurstHeight);
	    g.setColor(burstColor);
	    g.fillRect(1,BARHEIGHT-burstHeight+1,width-1,burstHeight-1);
		
	}
	else if( showHidden ){
	    g.setColor(initBurstColor);
	    g.drawRect(0,BARHEIGHT-initBurstHeight,width,initBurstHeight);
	}
	
   }
    public Process getProc() {return proc;}
    public void setProc(Process  v) {this.proc = v;}
    public Dimension getPreferredSize(){
	return ( new Dimension(PPWIDTH,PPHEIGHT));
    }
    public static boolean getShowHidden() {return showHidden;}
    public static void setShowHidden(boolean  v) { showHidden = v;}
} // ENDS  ProcessPanel

