import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


public class PackageHit {
	
	private JPanel container;
	private JPanel panel;
	private JLabel name;
	private JRadioButton nA;
	private JRadioButton nI;
	private JRadioButton I;
	private String designation = "notAnalyzed";
	private JTextField annot;
	private ArrayList<String> filepaths;
	private String annotation = "";
	private Border padding = BorderFactory.createEmptyBorder(5, 5, 5, 5);
	
	PackageHit(String n){
		
		filepaths = new ArrayList<String>();
		
		container = new JPanel();
		container.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1), new LineBorder(new Color(0, 0, 0), 2, true)));
		container.setBounds(12, 13, 997, 38);
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		
		panel = new JPanel();
		container.add(panel);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel_7 = new JPanel();
		panel.add(panel_7);
		panel_7.setLayout(new BorderLayout(0, 0));
		
		name = new JLabel(n);
		name.setBorder(padding);
		name.setFont(new Font("Tahoma", Font.BOLD, 15));
		panel_7.add(name, BorderLayout.CENTER);
		
		JPanel panel_8 = new JPanel();
		panel.add(panel_8);
		
		nA = new JRadioButton("notAnalyzed");
		nA.setSelected(true);
		panel_8.add(nA);
		
		nI = new JRadioButton("notInteresting");
		panel_8.add(nI);
		
		I = new JRadioButton("Interesting");
		panel_8.add(I);
		
		JPanel panel_9 = new JPanel();
		panel.add(panel_9);
		panel_9.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_2 = new JLabel("Annotation:");
		lblNewLabel_2.setBorder(padding);
		panel_9.add(lblNewLabel_2, BorderLayout.WEST);
		
		annot = new JTextField();
		panel_9.add(annot, BorderLayout.CENTER);
		annot.setColumns(10);
		
	}
	
	protected String getName(){
		return name.getText();
	}
	
	protected void setName(String n){
		name.setText(n);
	}
	
	protected String getDesig(){
		return designation;
	} 
	
	protected void setDesig(String n){
		if(n.equals("notAnalyzed")){
			nA.setSelected(true);
			nI.setSelected(false);
			I.setSelected(false);
		}
		else if(n.equals("notInteresting")){
			nI.setSelected(true);
			I.setSelected(false);
			nA.setSelected(false);
		}
		else if(n.equals("Interesting")){
			I.setSelected(true);
			nI.setSelected(false);
			nA.setSelected(false);
		}
		designation = n;
	}
	
	protected String getAnnot(){
		return annotation;
	}
	
	protected void setAnnot(String a){
		annotation = a;
	}
	
	protected void setTextField(String a){
		annot.setText(a);
	}
	
	protected ArrayList<String> getFilepaths(){
		return filepaths;
	}
	
	protected int getNumberOfFilepaths(){
		return filepaths.size();
	}
	
	protected void addFilepath(String f){
		filepaths.add(f);
	}
	
	protected void addFilepathGUI(){
		container.setBounds(12, 13, 997, (38+(20*filepaths.size())));
		
		for(int i=0; i<filepaths.size(); i++){
			JPanel newPanel = new JPanel();
			newPanel.setLayout(new BorderLayout(0, 0));
			
			JLabel newLabel = new JLabel(filepaths.get(i));
			newLabel.setBorder(padding);
			newPanel.add(newLabel, BorderLayout.CENTER);
			container.add(newPanel);
		}
		
		panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 0, 0)));
	}
	
	
	protected JRadioButton getCheckBox(String n){
		if(n.equals("na")){
			return nA;
		}
		else if(n.equals("ni")){
			return nI;
		}
		else{
			return I;
		}
	}
	
	protected JTextField getTextField(){
		return annot;
	}
	
	protected JPanel getPanel(){
		return container;
	}
	
}
