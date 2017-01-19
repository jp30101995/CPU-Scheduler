
package cpuscheduler;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.text.*;

/**
 * A simple panel for showing min/mean/max for any quantifier.
 */
class StatsPanel extends JPanel{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final static int width=170,height=80;

    JLabel Turn,Response,Wait,Tvalue,Rvalue,Wvalue;

    StatsPanel(){
    }

    StatsPanel(String title){
	//TitledBorder tBorder =  BorderFactory.createTitledBorder(title);
	//setBorder( tBorder);
	setLayout(new GridLayout(0,2));

	Turn =  new JLabel("Turn");
	Tvalue = new JLabel(""+0);
    Response  = new JLabel("Response");
	Rvalue =  new JLabel(""+0);
	Wait = new JLabel("Wait");
	Wvalue  = new JLabel(""+0);

	add(Turn);
	add(Tvalue);
	add(Response);
	add(Rvalue);
	add(Wait);
	add(Wvalue);

	setSize(width,height);
	setMinimumSize(new Dimension(width,height));
    }

    
    /**
     * Update the statistic 
     */
    public void setStats(double Turn,double Response, double Wait){
	NumberFormat nf = NumberFormat.getInstance();
	nf.setMaximumFractionDigits(2);
	nf.setMinimumFractionDigits(2);
	nf.setGroupingUsed(false);

	Tvalue.setText(Double.toString(Turn));
	Rvalue.setText(Double.toString(Response));
	Wvalue.setText(Double.toString(Wait));
    }


     public Dimension getMinimumSize(){
 	return new Dimension(width,height);
     }

     public Dimension getPreferredSize(){
 	return new Dimension(width,height);
     }
   
}
