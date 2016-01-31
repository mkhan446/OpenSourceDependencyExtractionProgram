import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JCheckBox;

import java.awt.GridBagLayout;

import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;

import java.awt.Color;

import javax.swing.border.LineBorder;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.JProgressBar;
import javax.swing.BoxLayout;
import java.awt.Font;


public class testFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtAnnotation;
	private JPanel container;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public testFrame() {
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		contentPane.setBounds(12, 13, 997, 78);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		Border padding = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblName.setBorder(padding);
		panel_1.add(lblName, BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		
		JCheckBox checkBox = new JCheckBox("notAnalyzed");
		checkBox.setSelected(true);
		panel_2.add(checkBox);
		
		JCheckBox checkBox_1 = new JCheckBox("notInteresting");
		panel_2.add(checkBox_1);
		
		JCheckBox checkBox_2 = new JCheckBox("Interesting");
		panel_2.add(checkBox_2);
		
		JPanel panel_3 = new JPanel();
		panel.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		txtAnnotation = new JTextField();
		panel_3.add(txtAnnotation, BorderLayout.CENTER);
		txtAnnotation.setColumns(10);
		
		JLabel lblAnnotation = new JLabel("Annotation:");
		lblAnnotation.setBorder(padding);
		panel_3.add(lblAnnotation, BorderLayout.WEST);
		
		JPanel panel_4 = new JPanel();
		contentPane.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JLabel lblCkjsdhfkjhfdskkjhdsfkjhdsfkdoidsuhfoidshfkj = new JLabel("C://kjsdhfkjhfdsk/kjhdsfkjhdsfkd/oidsuhfoidshfkj");
		panel_4.add(lblCkjsdhfkjhfdskkjhdsfkjhdsfkdoidsuhfoidshfkj, BorderLayout.CENTER);
		
		JPanel panel_5 = new JPanel();
		contentPane.add(panel_5);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("kjdsfkjdhfkjhfdskjfds");
		panel_5.add(lblNewLabel, BorderLayout.NORTH);
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		
		container = new JPanel();
		container.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		container.setBounds(12, 13, 997, 35);
		container.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel_7 = new JPanel();
		container.add(panel_7);
		panel_7.setLayout(new BorderLayout(0, 0));
		
		JLabel name1 = new JLabel("name");
		Border padding1 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		name1.setBorder(padding1);
		panel_7.add(name1, BorderLayout.CENTER);
		
		JPanel panel_8 = new JPanel();
		container.add(panel_8);
		
		JCheckBox nA = new JCheckBox("notAnalyzed");
		nA.setSelected(true);
		panel_8.add(nA);
		
		JCheckBox nI = new JCheckBox("notInteresting");
		panel_8.add(nI);
		
		JCheckBox I = new JCheckBox("Interesting");
		panel_8.add(I);
		
		JPanel panel_9 = new JPanel();
		container.add(panel_9);
		panel_9.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_2 = new JLabel("Annotation:");
		lblNewLabel_2.setBorder(padding);
		panel_9.add(lblNewLabel_2, BorderLayout.WEST);
		
		JTextField annot = new JTextField();
		panel_9.add(annot, BorderLayout.CENTER);
		annot.setColumns(10);
	}
}
