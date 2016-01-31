


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.zip.*;
import java.util.Enumeration;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.*;
import org.xml.sax.SAXException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;

import javax.swing.JRadioButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.UIManager;
import javax.swing.SpringLayout;
import javax.swing.JToolBar;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

@SuppressWarnings("serial")
public class osdep extends JFrame {

	private JPanel contentPane;
	private JTextField txtEnter;
	private JTextField resultsName;
	private Node root = new Node(null, "full_tree");
	private ArrayList<PackageHit> packs = new ArrayList<PackageHit>();
	private JPanel panel_7;
	private JTextField txtBaseDirectoryTo;
	private boolean changesMade = false;
	protected static void help() {
			System.out.println("\n     __________OSDEP: Open Source Dependency Extraction Program__________\n");
			System.out.print("Usage: java OSDEP.osdep <filepath_to_scan> [-pdept] [-pL] [-pl] [-pT] [-pt] [-xz | -xu]\n");
			System.out.println();
			System.out.println("\t-pl\toutputs a simple sorted list of all possible *interesting* packages [default]");
			System.out.println("\t-pL\toutputs the list with all file paths containing this package");

			System.out.println("\t-pt\tprints the tree with problematic folders only");
			System.out.println("\t-pT\tprints the entire tree with files");

			System.out.println("\t-pdept\tprints only the dependency packages in a tree form. [default]");

			System.out.println("\t-xu\tdeletes the expanded zips and leaves folder as-is [default]");
			System.out.println("\t-xz\tdeletes the zipped folders and leaves the unzipped versions");
			
			System.out.println();
			System.exit(0);
	}

	public static void main(String[] args) throws Exception{

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					osdep frame = new osdep();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	} // end main
	
	/**
	 * Create the frame.
	 */
	public osdep() {
		
		setTitle("OSDEP");
		setBackground(Color.DARK_GRAY);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if(changesMade){
					Object[] options = {"Save",
		                    "Don't Save",
		                    "Cancel"};
					int value = JOptionPane.showOptionDialog(osdep.this, "Do you want to save your changes?", "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
					if(value == JOptionPane.YES_OPTION){
						try {
							XMLwriter.createXML(packs, resultsName.getText(), txtEnter.getText());
							changesMade = false;
						} catch (ParserConfigurationException | TransformerException e3) {
							// TODO Auto-generated catch block
							e3.printStackTrace();
						}
					}
					else if(value == JOptionPane.NO_OPTION) dispose();
				}else{
					dispose();
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		setBounds(20, 20, 596, 441);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		final JFileChooser fc_files = new JFileChooser();
		fc_files.setFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
		fc_files.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		final JFileChooser fc_text = new JFileChooser();
		fc_text.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
		fc_text.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setForeground(Color.CYAN);
		menuBar.setBackground(Color.DARK_GRAY);
		contentPane.add(menuBar, BorderLayout.NORTH);
		
		JMenu mnNewMenu = new JMenu("File");
		mnNewMenu.setBackground(Color.DARK_GRAY);
		mnNewMenu.setForeground(Color.CYAN);
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Open...");
		mntmNewMenuItem.setBackground(Color.DARK_GRAY);
		mntmNewMenuItem.setForeground(Color.CYAN);
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Save XML");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					XMLwriter.createXML(packs, resultsName.getText(), txtEnter.getText());
					changesMade = false;
				} catch (ParserConfigurationException | TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		mntmNewMenuItem_1.setBackground(Color.DARK_GRAY);
		mntmNewMenuItem_1.setForeground(Color.CYAN);
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Save XML As...");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				fc_files.setCurrentDirectory(fc.getCurrentDirectory());
				int fc_return = fc_files.showSaveDialog(osdep.this);
				
				
				if(fc_return == JFileChooser.APPROVE_OPTION){
					File file = fc_files.getSelectedFile();
					try {
						XMLwriter.createXML(packs, file.getAbsolutePath(), txtEnter.getText());
						changesMade = false;
						//System.out.println()
					} catch (ParserConfigurationException
							| TransformerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		mntmNewMenuItem_2.setBackground(Color.DARK_GRAY);
		mntmNewMenuItem_2.setForeground(Color.CYAN);
		mnNewMenu.add(mntmNewMenuItem_2);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Exit");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		JMenuItem mntmExportAsText = new JMenuItem("Export As Text File");
		mntmExportAsText.setBackground(Color.DARK_GRAY);
		mntmExportAsText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final JFrame frame = new JFrame("Export As Text File");
				frame.setSize(1000, 800);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);

				final JTextArea textArea = new JTextArea("");
				textArea.setEditable(false);
				
				String output = "";

				for (int i = 0; i < packs.size(); i++) {
					output += "Name: \t" + packs.get(i).getName();
					output += "\nDesignation: \t" + packs.get(i).getDesig();

					if (packs.get(i).getAnnot() != "") output += "\nAnnotation: \t" + packs.get(i).getAnnot();

					for (int j = 0; j < packs.get(i).getNumberOfFilepaths(); j++)
						output += "\nFilepath-" + (j + 1) + ": \t" + packs.get(i).getFilepaths().get(j).toString();

					output += "\n\n";
				}
				
				textArea.setText(output);

				JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

				JPanel bottomPanel = new JPanel();

				JButton saveButton = new JButton("Save As");
				JButton closeButton = new JButton("Close");

				bottomPanel.add(saveButton);
				bottomPanel.add(closeButton);

				frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
				frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
				frame.setVisible(true);

				saveButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						fc_text.setCurrentDirectory(fc_files.getCurrentDirectory());
						int fc_return = fc_text.showSaveDialog(frame);
						
						
						if(fc_return == JFileChooser.APPROVE_OPTION){
							final String name = fc_text.getSelectedFile().getAbsolutePath();
							
							try {
								String fileName = name;
								if(!fileName.contains(".txt")) fileName += ".txt";
								
								File file = new File(fileName);
								BufferedWriter output = new BufferedWriter(new FileWriter(file));

								for (int i = 0; i < packs.size(); i++) {
									output.write("Name: \t" + packs.get(i).getName());
									output.write(System.getProperty("line.separator").toString());
									output.write("Designation: \t" + packs.get(i).getDesig());

									if (packs.get(i).getAnnot() != "") {
										output.write(System.getProperty("line.separator").toString());
										output.write("Annotation: \t" + packs.get(i).getAnnot());
									}

									for (int j = 0; j < packs.get(i).getNumberOfFilepaths(); j++) {
										output.write(System.getProperty("line.separator").toString());
										output.write("Filepath-" + (j + 1) + ": \t" + packs.get(i).getFilepaths().get(j).toString());
									}
									
									output.write(System.getProperty("line.separator").toString());
									output.write(System.getProperty("line.separator").toString());
								}

								output.close();
								//System.out.println()
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

						frame.setVisible(false);
						frame.dispose();
					}
				});
				
				closeButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						frame.setVisible(false);
						frame.dispose();
					}
				});
			}
		});
		mntmExportAsText.setForeground(Color.CYAN);
		mnNewMenu.add(mntmExportAsText);
		mntmNewMenuItem_3.setBackground(Color.DARK_GRAY);
		mntmNewMenuItem_3.setForeground(Color.CYAN);
		mnNewMenu.add(mntmNewMenuItem_3);
		
		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new EmptyBorder(5,0,0,0));
		tabbedPane.setBackground(Color.DARK_GRAY);
		tabbedPane.setForeground(Color.CYAN);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setForeground(Color.CYAN);
		tabbedPane.addTab("Scan", null, panel, "Run a new scan");
		panel.setBackground(Color.DARK_GRAY);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.DARK_GRAY);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.DARK_GRAY);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.DARK_GRAY);
		
		final JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.DARK_GRAY);
		panel_4.setBorder(new TitledBorder(null, "List Settings", TitledBorder.CENTER, TitledBorder.TOP, null, Color.CYAN));
		
		JPanel panel_5 = new JPanel();
		panel_5.setBackground(Color.DARK_GRAY);
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Tree Settings", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 255, 255)));
		
		JLabel lblBinaryScanTool = new JLabel("Open Source Dependency Extraction Program");
		lblBinaryScanTool.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblBinaryScanTool.setForeground(Color.CYAN);
		lblBinaryScanTool.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel panel_9 = new JPanel();
		panel_9.setLayout(null);
		panel_9.setBackground(Color.DARK_GRAY);
		
		txtBaseDirectoryTo = new JTextField();
		txtBaseDirectoryTo.setText("Base Directory to Scan:");
		txtBaseDirectoryTo.setColumns(10);
		txtBaseDirectoryTo.setBounds(12, 13, 389, 22);
		txtBaseDirectoryTo.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				if(txtBaseDirectoryTo.getText().equals("Base Directory to Scan:")){
					txtBaseDirectoryTo.setText(null);
					}
			}
			
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		panel_9.add(txtBaseDirectoryTo);
		
		JButton button = new JButton("Browse");
		button.setForeground(Color.CYAN);
		button.setBackground(Color.DARK_GRAY);
		button.setBounds(413, 12, 97, 25);
		button.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				int fc_return = fc.showOpenDialog(osdep.this);
				
				if(fc_return == JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();
					txtBaseDirectoryTo.setText(file.getAbsolutePath());
				}
			}
		});
		panel_9.add(button);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
								.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
								.addComponent(lblBinaryScanTool, GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
								.addComponent(panel_9, GroupLayout.PREFERRED_SIZE, 539, GroupLayout.PREFERRED_SIZE)
								.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE))
							.addContainerGap())
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(10)
							.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 237, GroupLayout.PREFERRED_SIZE)
							.addGap(40))))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblBinaryScanTool)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_9, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addGap(20))
		);
		
		txtEnter = new JTextField();
		txtEnter.setBounds(12, 13, 389, 22);
		txtEnter.setText("Project Home Directory:");
		txtEnter.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				if(txtEnter.getText().equals("Project Home Directory:")){
					txtEnter.setText(null);
					}
			}
			
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
				
			}
			
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
				
			}
		});
		panel_1.add(txtEnter);
		txtEnter.setColumns(10);
		
		resultsName = new JTextField();
		resultsName.setBounds(12, 13, 389, 22);
		resultsName.setText("Scan Results Filename:");
		resultsName.setColumns(10);
		resultsName.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				if(resultsName.getText().equals("Scan Results Filename:")){
					resultsName.setText(null);
					}
			}
			
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		panel_2.add(resultsName);
		
//		final JRadioButton rdbtnNewRadioButton_5 = new JRadioButton("Delete Unzipped Folders");
//		rdbtnNewRadioButton_5.setToolTipText("Deletes the expanded zips and leaves the folder as-is. [DEFAULT]");
//		rdbtnNewRadioButton_5.setSelected(true);
//		final JRadioButton rdbtnNewRadioButton_6 = new JRadioButton("Delete Zipped Folders");
//		rdbtnNewRadioButton_6.setToolTipText("Deletes the zipped folders and leaves the unzipped version.");
//		
//		rdbtnNewRadioButton_5.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if(rdbtnNewRadioButton_5.isSelected()){
//					Tree.deleteUnzipped=true;
//					Tree.deleteZipped=false;
//					rdbtnNewRadioButton_6.setSelected(false);
//				}
//				else{
//					Tree.deleteUnzipped=false;
//				}
//			}
//		});
//		
//		rdbtnNewRadioButton_5.setForeground(Color.CYAN);
//		rdbtnNewRadioButton_5.setBackground(Color.DARK_GRAY);
//		panel_6.add(rdbtnNewRadioButton_5);
//		
//		rdbtnNewRadioButton_6.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if(rdbtnNewRadioButton_6.isSelected()){
//					Tree.deleteZipped=true;
//					Tree.deleteUnzipped=false;
//					rdbtnNewRadioButton_5.setSelected(false);
//				}
//				else{
//					Tree.deleteZipped=false;
//				}
//			}
//		});
		
		
//		rdbtnNewRadioButton_6.setForeground(Color.CYAN);
//		rdbtnNewRadioButton_6.setBackground(Color.DARK_GRAY);
//		panel_6.add(rdbtnNewRadioButton_6);
		panel_5.setLayout(new BoxLayout(panel_5, BoxLayout.Y_AXIS));
		
//		final JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("Concise Tree");
//		rdbtnNewRadioButton_2.setToolTipText("Outputs a tree containing problematic folders only.");
//		rdbtnNewRadioButton_2.setForeground(Color.CYAN);
//		rdbtnNewRadioButton_2.setBackground(Color.DARK_GRAY);
//		panel_5.add(rdbtnNewRadioButton_2);
		
		final JRadioButton rdbtnNewRadioButton_4 = new JRadioButton("Dependency Tree");
		rdbtnNewRadioButton_4.setToolTipText("Outputs a tree containing only the dependency packages. [DEFAULT]");
		rdbtnNewRadioButton_4.setSelected(true);
		rdbtnNewRadioButton_4.setForeground(Color.CYAN);
		rdbtnNewRadioButton_4.setBackground(Color.DARK_GRAY);
		panel_5.add(rdbtnNewRadioButton_4);
		
		final JRadioButton rdbtnNewRadioButton_3 = new JRadioButton("Full Tree with Files");
		rdbtnNewRadioButton_3.setToolTipText("Outputs the full tree with all the files.");
		rdbtnNewRadioButton_3.setForeground(Color.CYAN);
		rdbtnNewRadioButton_3.setBackground(Color.DARK_GRAY);
		panel_5.add(rdbtnNewRadioButton_3);
		
//		rdbtnNewRadioButton_2.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if(rdbtnNewRadioButton_2.isSelected()){
//					Tree.printTreeConcise=true;
//					Tree.printTreeElaborate=false;
//					Tree.printDependencyTree=false;
//					rdbtnNewRadioButton_3.setSelected(false);
//					rdbtnNewRadioButton_4.setSelected(false);
//				}
//				else{
//					Tree.printTreeConcise=false;
//				}
//			}
//		});
		
		rdbtnNewRadioButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnNewRadioButton_4.isSelected()){
					Tree.printDependencyTree=true;
					Tree.printTreeConcise=false;
					Tree.printTreeElaborate=false;
					//rdbtnNewRadioButton_2.setSelected(false);
					rdbtnNewRadioButton_3.setSelected(false);
				}
				else{
					Tree.printDependencyTree=false;
				}
			}
		});
		
		rdbtnNewRadioButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnNewRadioButton_3.isSelected()){
					Tree.printTreeElaborate=true;
					Tree.printTreeConcise=false;
					Tree.printDependencyTree=false;
					//rdbtnNewRadioButton_2.setSelected(false);
					rdbtnNewRadioButton_4.setSelected(false);
				}
				else{
					Tree.printTreeElaborate=false;
				}
			}
		});
		
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.Y_AXIS));
		
		final JRadioButton rdbtnNewRadioButton = new JRadioButton("Concise List");
		rdbtnNewRadioButton.setToolTipText("Outputs a concise list of all the possible third party packages. [DEFAULT]");
		final JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Full List with File Paths");
		rdbtnNewRadioButton_1.setToolTipText("Outputs a list of all the file paths which contain possible third party packages. ");
		
		rdbtnNewRadioButton.setSelected(true);
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(rdbtnNewRadioButton.isSelected()){
					Tree.printPkgConcise=true;
					Tree.printPkgElaborate=false;
					rdbtnNewRadioButton_1.setSelected(false);
					
				}
				else{
					Tree.printPkgConcise=false;
					if(!rdbtnNewRadioButton_1.isSelected() && !rdbtnNewRadioButton.isSelected()){
						panel_4.setToolTipText("");
					}
				}
			}
		});
		rdbtnNewRadioButton.setForeground(Color.CYAN);
		rdbtnNewRadioButton.setBackground(Color.DARK_GRAY);
		panel_4.add(rdbtnNewRadioButton);
		
		
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(rdbtnNewRadioButton_1.isSelected()){
					Tree.printPkgElaborate=true;
					Tree.printPkgConcise=false;
					rdbtnNewRadioButton.setSelected(false);
				}
				else{
					Tree.printPkgElaborate=false;
				}
			}
		});
		rdbtnNewRadioButton_1.setForeground(Color.CYAN);
		rdbtnNewRadioButton_1.setBackground(Color.DARK_GRAY);
		panel_4.add(rdbtnNewRadioButton_1);
		
		final JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane_1.setForeground(Color.CYAN);
		tabbedPane_1.setBackground(Color.DARK_GRAY);
		tabbedPane.addTab("Analyze", null, tabbedPane_1, null);
		
		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		tabbedPane_1.addTab("Package View", null, scrollPane, null);
		
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BorderLayout(0, 0));
		scrollPane.setColumnHeaderView(headerPanel);
		
		
		JLabel label = new JLabel("Interesting Packages");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD, 21));
		headerPanel.add(label, BorderLayout.NORTH);
		
		final JLabel numOfHits = new JLabel("0 hits");
		numOfHits.setHorizontalAlignment(SwingConstants.CENTER);
		numOfHits.setFont(new Font("Tahoma", Font.BOLD, 15));
		headerPanel.add(numOfHits, BorderLayout.EAST);
		
		panel_7 = new JPanel();
		scrollPane.setViewportView(panel_7);
		final GridBagLayout gbl_panel_7 = new GridBagLayout();
		gbl_panel_7.columnWidths = new int[]{0, 0};
		gbl_panel_7.rowHeights = new int[]{0, 0, 0};
		gbl_panel_7.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_7.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_7.setLayout(gbl_panel_7);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.getVerticalScrollBar().setUnitIncrement(16);
		tabbedPane_1.addTab("Tree View", null, scrollPane_1, null);
		
		JLabel lblTree = new JLabel("File Tree");
		lblTree.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTree.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane_1.setColumnHeaderView(lblTree);
		
		JPanel panel_8 = new JPanel();
		scrollPane_1.setViewportView(panel_8);
		panel_8.setLayout(new BorderLayout(0, 0));
		
		final DefaultMutableTreeNode top = new DefaultMutableTreeNode("root");
		JTree tree_1 = new JTree(top);
		panel_8.add(tree_1);
		
		ChangeListener changedTab = new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
			        int index = ((JTabbedPane) arg0.getSource()).getSelectedIndex();
			        if(index == 0){
			        	setBounds(20, 20, 596, 442);
			        }
			        else{
			        	setBounds(20, 20, 1500, 1000);
			        }
			        
				
			}
			
		};
		tabbedPane.addChangeListener(changedTab);
		
//		final JDialog progress = new JDialog(osdep.this, "Scanning");
//		progress.setLayout(new GridBagLayout());
//		progress.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
//		GridBagConstraints bag = new GridBagConstraints();
//		bag.insets = new Insets(4, 4, 4, 4);
//		bag.weightx = 1;
//		bag.gridy = 0;
//		progress.add(new JLabel("Scanning Package..."), bag);
//		
//		final JProgressBar progressBar = new JProgressBar();
//		progressBar.setBounds(25, 40, 231, 25);
//		bag.gridy = 1;
//		progressBar.setIndeterminate(true);
//		progress.add(progressBar, bag);
//		progress.pack();
//		progress.setLocationRelativeTo(osdep.this);
		
		
		
//		final SwingWorker<JPanel, Void> pkgConcise = new SwingWorker<JPanel, Void>(){
//
//			@Override
//			protected JPanel doInBackground() throws Exception {
//				packs.clear();
//				panel_7.removeAll();
//				JPanel temp = new JPanel();
//				temp.setLayout(gbl_panel_7);
//				
//				final HashMap<String, ArrayList<Node>> map = root.aggregateNewPkgs();
//				final ArrayList<String> list = new ArrayList<String>(map.keySet());
//		    	Collections.sort(list);
//				
//				GridBagConstraints c = new GridBagConstraints();
//				
//				for(int i = 0; i<list.size(); i++){
//					
//					
//					
//					final PackageHit pack = new PackageHit(list.get(i));
//					
//					if (!list.get(i).equals("lib")){
//					 ArrayList<Node> entries = map.get(list.get(i));
//					    for (Node n : entries) {
//					    	pack.addFilepath(n.getPath());
//					    }
//					}
//					
//					if(Tree.printPkgElaborate) pack.addFilepathGUI();
//					
//					c.gridx = 1;
//					c.gridy = i;	//set constraints
//					c.weightx = 1;
//					c.gridwidth = 1;
//					c.gridheight = 1;
//					c.fill = GridBagConstraints.HORIZONTAL;
//					c.anchor = GridBagConstraints.FIRST_LINE_START;
//				   // c.insets = new Insets(3,3,3,3);	
//					if(i != list.size()-1){
//						c.weighty = 0;
//					}else{
//						c.weighty = 1;
//					}
//					
//					
//					pack.getCheckBox("na").addActionListener(new ActionListener() {
//						
//						@Override
//						public void actionPerformed(ActionEvent arg0) {
//							changesMade = true;
//							if(!pack.getCheckBox("na").isSelected()){
//								pack.getCheckBox("na").setSelected(true);
//							}
//							pack.getCheckBox("ni").setSelected(false);
//							pack.getCheckBox("i").setSelected(false);
//							pack.setDesig("notAnalyzed");
//						}
//					});
//					pack.getCheckBox("ni").addActionListener(new ActionListener() {
//						
//						@Override
//						public void actionPerformed(ActionEvent arg0) {
//							changesMade = true;
//							if(!pack.getCheckBox("ni").isSelected()){
//								pack.getCheckBox("ni").setSelected(true);
//							}
//							pack.getCheckBox("na").setSelected(false);
//							pack.getCheckBox("i").setSelected(false);
//							pack.setDesig("notInteresting");
//						}
//					});
//					pack.getCheckBox("i").addActionListener(new ActionListener() {
//						
//						@Override
//						public void actionPerformed(ActionEvent arg0) {
//							changesMade = true;
//							if(!pack.getCheckBox("i").isSelected()){
//								pack.getCheckBox("i").setSelected(true);
//							}
//							pack.getCheckBox("na").setSelected(false);
//							pack.getCheckBox("ni").setSelected(false);
//							pack.setDesig("Interesting");
//						}
//					});
//					
//					pack.getTextField().getDocument().addDocumentListener(new DocumentListener() {
//						
//						@Override
//						public void removeUpdate(DocumentEvent e) {
//							// TODO Auto-generated method stub
//							changesMade = true;
//							pack.setAnnot(pack.getTextField().getText());
//						}
//						
//						@Override
//						public void insertUpdate(DocumentEvent e) {
//							// TODO Auto-generated method stub
//							changesMade = true;
//							pack.setAnnot(pack.getTextField().getText());
//						}
//						
//						@Override
//						public void changedUpdate(DocumentEvent e) {
//							changesMade = true;
//							pack.setAnnot(pack.getTextField().getText());
//						}
//					});
//					
//					packs.add(pack);
//					
//					temp.add(pack.getPanel(), c);								
//					temp.revalidate();
//					temp.repaint();
//				}
//				
//				XMLwriter.createXML(packs, resultsName.getText(), txtEnter.getText());
//				
//				return temp;
//				
//				}
//				public void done() {
//			        
//			        try {
//			             panel_7 = get();
//			             scrollPane.setViewportView(panel_7);
//			             tabbedPane.setSelectedIndex(1);
//			        } catch (InterruptedException ignore) {}
//			        catch (java.util.concurrent.ExecutionException e) {
//			            String why = null;
//			            Throwable cause = e.getCause();
//			            if (cause != null) {
//			                why = cause.getMessage();
//			            } else {
//			                why = e.getMessage();
//			            }
//			            System.err.println("Error retrieving file: " + why);
//			        }
//			
//			
//				}
//			
//		};
		
//		final SwingWorker<Node, Void> treeBuilder = new SwingWorker<Node, Void>(){
//
//			@Override
//			protected Node doInBackground() throws Exception {
//				root = null;
//				Tree.deleteUnzipped = true;
//				File bin = new File(txtBaseDirectoryTo.getText());
//				Node filetree = new Node(null, resultsName.getText());
//					//System.out.println("\nGenerating tree... please wait ...\n");
//					try {
//						filetree = Tree.buildTree(filetree, bin);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				
//				return filetree;
//			}
//				public void done() {
//			        
//			        try {
//			            root = get();
//			            
//						if (Tree.printPkgConcise) pkgConcise.run();
//						
//						
//						if (Tree.printPkgElaborate) pkgConcise.run();
//						
//						//if (Tree.printTreeConcise) root.printStatus("WARNING"); // changed this to print anything that doesn't say OK
//				
//						if (Tree.printTreeElaborate) top.add(root.getAlias());
//				
//						if (Tree.printDependencyTree) {
//							Node dependency_tree = new Node(null, "dependency_tree");
//							dependency_tree = Tree.buildDependencyTree(root, dependency_tree);
//							top.add(dependency_tree.getAlias());
//						}
//						//progress.setVisible(false);
//			        } catch (InterruptedException ignore) {}
//			        catch (java.util.concurrent.ExecutionException e) {
//			            String why = null;
//			            Throwable cause = e.getCause();
//			            if (cause != null) {
//			                why = cause.getMessage();
//			            } else {
//			                why = e.getMessage();
//			            }
//			            System.err.println("Error retrieving file1: " + why);
//			        }
//			
//			
//				}
//			
//		};
		
//		final SwingWorker<JPanel, Void> populate = new SwingWorker<JPanel, Void>(){
//
//			@Override
//			protected JPanel doInBackground() throws Exception {
//				// TODO Auto-generated method stub
//				panel_7.removeAll();
//				JPanel temp = new JPanel();
//				temp.setLayout(gbl_panel_7);
//				
//				GridBagConstraints c = new GridBagConstraints();
//				
//				for(int i = 0; i<packs.size(); i++){
//					
//					final int j = i;
//					
//					c.gridx = 1;
//					c.gridy = i;	//set constraints
//					c.weightx = 1;
//					c.gridwidth = 1;
//					c.gridheight = 1;
//					c.fill = GridBagConstraints.HORIZONTAL;
//					c.anchor = GridBagConstraints.FIRST_LINE_START;
//				   // c.insets = new Insets(3,3,3,3);	
//					if(i != packs.size()-1){
//						c.weighty = 0;
//					}else{
//						c.weighty = 1;
//					}
//					
//					
//					packs.get(j).getCheckBox("na").addActionListener(new ActionListener() {
//						
//						@Override
//						public void actionPerformed(ActionEvent arg0) {
//							changesMade = true;
//							if(!packs.get(j).getCheckBox("na").isSelected()){
//								packs.get(j).getCheckBox("na").setSelected(true);
//							}
//							packs.get(j).getCheckBox("ni").setSelected(false);
//							packs.get(j).getCheckBox("i").setSelected(false);
//							packs.get(j).setDesig("notAnalyzed");
//						}
//					});
//					packs.get(j).getCheckBox("ni").addActionListener(new ActionListener() {
//						
//						@Override
//						public void actionPerformed(ActionEvent arg0) {
//							changesMade = true;
//							if(!packs.get(j).getCheckBox("ni").isSelected()){
//								packs.get(j).getCheckBox("ni").setSelected(true);
//							}
//							packs.get(j).getCheckBox("na").setSelected(false);
//							packs.get(j).getCheckBox("i").setSelected(false);
//							packs.get(j).setDesig("notInteresting");
//						}
//					});
//					packs.get(j).getCheckBox("i").addActionListener(new ActionListener() {
//						
//						@Override
//						public void actionPerformed(ActionEvent arg0) {
//							changesMade = true;
//							if(!packs.get(j).getCheckBox("i").isSelected()){
//								packs.get(j).getCheckBox("i").setSelected(true);
//							}
//							packs.get(j).getCheckBox("na").setSelected(false);
//							packs.get(j).getCheckBox("ni").setSelected(false);
//							packs.get(j).setDesig("Interesting");
//						}
//					});
//					
//					packs.get(j).getTextField().getDocument().addDocumentListener(new DocumentListener() {
//						
//						@Override
//						public void removeUpdate(DocumentEvent e) {
//							// TODO Auto-generated method stub
//							changesMade = true;
//							packs.get(j).setAnnot(packs.get(j).getTextField().getText());
//						}
//						
//						@Override
//						public void insertUpdate(DocumentEvent e) {
//							// TODO Auto-generated method stub
//							changesMade = true;
//							packs.get(j).setAnnot(packs.get(j).getTextField().getText());
//						}
//						
//						@Override
//						public void changedUpdate(DocumentEvent e) {
//							changesMade = true;
//							packs.get(j).setAnnot(packs.get(j).getTextField().getText());
//						}
//					});
//					
//					
//					temp.add(packs.get(i).getPanel(), c);								
//					temp.revalidate();
//					temp.repaint();
//				
//				
//			}
//				return temp;
//			}
//			
//			public void done(){
//			        
//			        try {
//			             panel_7 = get();
//			             scrollPane.setViewportView(panel_7);
//			             tabbedPane.setSelectedIndex(1);
//			        } catch (InterruptedException ignore) {}
//			        catch (java.util.concurrent.ExecutionException e) {
//			            String why = null;
//			            Throwable cause = e.getCause();
//			            if (cause != null) {
//			                why = cause.getMessage();
//			            } else {
//			                why = e.getMessage();
//			            }
//			            System.err.println("Error retrieving file: " + why);
//			        }
//			
//			
//				
//			}
//		};

		
		JButton btnNewButton_1 = new JButton("Scan");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
					//File treefile = new File(args[1]); //write tree to this file
				
				        //progress.setVisible(true);
				
				if(txtEnter.getText().equals("Project Home Directory:") | txtEnter.getText().equals("")){
					JOptionPane.showMessageDialog(osdep.this, "Please specify a Project Home Directory.","Missing Parameter", JOptionPane.ERROR_MESSAGE);
				}
				else if(resultsName.getText().equals("") | resultsName.getText().equals("Scan Results Filename:")){
					JOptionPane.showMessageDialog(osdep.this, "Please specify a Scan Results Filename.","Missing Parameter", JOptionPane.ERROR_MESSAGE);
				}
				else if(txtBaseDirectoryTo.getText().equals("")| txtBaseDirectoryTo.getText().equals("Base Directory to Scan:")){
					JOptionPane.showMessageDialog(osdep.this, "Please specify a Base Directory to Scan.","Missing Parameter", JOptionPane.ERROR_MESSAGE);
				}
				else{
					//treeBuilder.run();
					root = null;
					top.removeAllChildren();
					Tree.deleteUnzipped = true;
					File bin = new File(txtBaseDirectoryTo.getText());
					root = new Node(null, resultsName.getText());
						//System.out.println("\nGenerating tree... please wait ...\n");
						try {
							root = Tree.buildTree(root, bin);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if (Tree.printPkgConcise | Tree.printPkgElaborate){
							packs.clear();
							panel_7.removeAll();
//							JPanel temp = new JPanel();
							//temp.setLayout(gbl_panel_7);
							
							final HashMap<String, ArrayList<Node>> map = root.aggregateNewPkgs();
							final ArrayList<String> list = new ArrayList<String>(map.keySet());
					    	Collections.sort(list);
							
							GridBagConstraints c = new GridBagConstraints();
							
							for(int i = 0; i<list.size(); i++){
								
								
								
								final PackageHit pack = new PackageHit(list.get(i));
								
								if (!list.get(i).equals("lib")){
								 ArrayList<Node> entries = map.get(list.get(i));
								    for (Node n : entries) {
								    	pack.addFilepath(n.getPath());
								    }
								}
								
								if(Tree.printPkgElaborate) pack.addFilepathGUI();
								
								c.gridx = 1;
								c.gridy = i;	//set constraints
								c.weightx = 1;
								c.gridwidth = 1;
								c.gridheight = 1;
								c.fill = GridBagConstraints.HORIZONTAL;
								c.anchor = GridBagConstraints.FIRST_LINE_START;
							   // c.insets = new Insets(3,3,3,3);	
								if(i != list.size()-1){
									c.weighty = 0;
								}else{
									c.weighty = 1;
								}
								
								
								pack.getCheckBox("na").addActionListener(new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changesMade = true;
										if(!pack.getCheckBox("na").isSelected()){
											pack.getCheckBox("na").setSelected(true);
										}
										pack.getCheckBox("ni").setSelected(false);
										pack.getCheckBox("i").setSelected(false);
										pack.setDesig("notAnalyzed");
									}
								});
								pack.getCheckBox("ni").addActionListener(new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changesMade = true;
										if(!pack.getCheckBox("ni").isSelected()){
											pack.getCheckBox("ni").setSelected(true);
										}
										pack.getCheckBox("na").setSelected(false);
										pack.getCheckBox("i").setSelected(false);
										pack.setDesig("notInteresting");
									}
								});
								pack.getCheckBox("i").addActionListener(new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changesMade = true;
										if(!pack.getCheckBox("i").isSelected()){
											pack.getCheckBox("i").setSelected(true);
										}
										pack.getCheckBox("na").setSelected(false);
										pack.getCheckBox("ni").setSelected(false);
										pack.setDesig("Interesting");
									}
								});
								
								pack.getTextField().getDocument().addDocumentListener(new DocumentListener() {
									
									@Override
									public void removeUpdate(DocumentEvent e) {
										// TODO Auto-generated method stub
										changesMade = true;
										pack.setAnnot(pack.getTextField().getText());
									}
									
									@Override
									public void insertUpdate(DocumentEvent e) {
										// TODO Auto-generated method stub
										changesMade = true;
										pack.setAnnot(pack.getTextField().getText());
									}
									
									@Override
									public void changedUpdate(DocumentEvent e) {
										changesMade = true;
										pack.setAnnot(pack.getTextField().getText());
									}
								});
								
								packs.add(pack);
								
								panel_7.add(pack.getPanel(), c);								
								panel_7.revalidate();
								panel_7.repaint();
							}
							
							try {
								XMLwriter.createXML(packs, resultsName.getText(), txtEnter.getText());
							} catch (ParserConfigurationException
									| TransformerException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							numOfHits.setText(list.size()+" hits");
							scrollPane.setViewportView(panel_7);
				            tabbedPane.setSelectedIndex(1);
						}
						
						
//						if (Tree.printPkgElaborate) pkgConcise.run();
						
						//if (Tree.printTreeConcise) root.printStatus("WARNING"); // changed this to print anything that doesn't say OK
				
						if (Tree.printTreeElaborate) top.add(root.getAlias());
				
						if (Tree.printDependencyTree) {
							Node dependency_tree = new Node(null, "dependency_tree");
							dependency_tree = Tree.buildDependencyTree(root, dependency_tree);
							top.add(dependency_tree.getAlias());
						}
						
						
				}
						//progress.setVisible(false);
			}}
		);
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		btnNewButton_1.setForeground(Color.CYAN);
		btnNewButton_1.setBackground(Color.DARK_GRAY);
		panel_3.add(btnNewButton_1);
		
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fc_files.setCurrentDirectory(fc.getCurrentDirectory());
				int fc_return = fc_files.showOpenDialog(osdep.this);
				
				
				if(fc_return == JFileChooser.APPROVE_OPTION){
					File file = fc_files.getSelectedFile();
					try {
						ArrayList<PackageHit> temp = null;
						temp = XMLwriter.readXML(file.getAbsolutePath());
						if(temp == null){
							JOptionPane.showMessageDialog(osdep.this, "The selected XML is incompatible with this program. Please select an XML that has been previously created by this program.", "XML Error", JOptionPane.ERROR_MESSAGE);
							//packs = new ArrayList<PackageHit>();
						}else{
							top.removeAllChildren();
							packs.clear();
							packs = temp;
							//populate.run();
							panel_7.removeAll();
							panel_7.setLayout(gbl_panel_7);
							
							GridBagConstraints c = new GridBagConstraints();
							
							for(int i = 0; i<packs.size(); i++){
								
								final int j = i;
								
								packs.get(i).addFilepathGUI();
								
								c.gridx = 1;
								c.gridy = i;	//set constraints
								c.weightx = 1;
								c.gridwidth = 1;
								c.gridheight = 1;
								c.fill = GridBagConstraints.HORIZONTAL;
								c.anchor = GridBagConstraints.FIRST_LINE_START;
							   // c.insets = new Insets(3,3,3,3);	
								if(i != packs.size()-1){
									c.weighty = 0;
								}else{
									c.weighty = 1;
								}
								
								
								packs.get(j).getCheckBox("na").addActionListener(new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changesMade = true;
										if(!packs.get(j).getCheckBox("na").isSelected()){
											packs.get(j).getCheckBox("na").setSelected(true);
										}
										packs.get(j).getCheckBox("ni").setSelected(false);
										packs.get(j).getCheckBox("i").setSelected(false);
										packs.get(j).setDesig("notAnalyzed");
									}
								});
								packs.get(j).getCheckBox("ni").addActionListener(new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changesMade = true;
										if(!packs.get(j).getCheckBox("ni").isSelected()){
											packs.get(j).getCheckBox("ni").setSelected(true);
										}
										packs.get(j).getCheckBox("na").setSelected(false);
										packs.get(j).getCheckBox("i").setSelected(false);
										packs.get(j).setDesig("notInteresting");
									}
								});
								packs.get(j).getCheckBox("i").addActionListener(new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changesMade = true;
										if(!packs.get(j).getCheckBox("i").isSelected()){
											packs.get(j).getCheckBox("i").setSelected(true);
										}
										packs.get(j).getCheckBox("na").setSelected(false);
										packs.get(j).getCheckBox("ni").setSelected(false);
										packs.get(j).setDesig("Interesting");
									}
								});
								
								packs.get(j).getTextField().getDocument().addDocumentListener(new DocumentListener() {
									
									@Override
									public void removeUpdate(DocumentEvent e) {
										// TODO Auto-generated method stub
										changesMade = true;
										packs.get(j).setAnnot(packs.get(j).getTextField().getText());
									}
									
									@Override
									public void insertUpdate(DocumentEvent e) {
										// TODO Auto-generated method stub
										changesMade = true;
										packs.get(j).setAnnot(packs.get(j).getTextField().getText());
									}
									
									@Override
									public void changedUpdate(DocumentEvent e) {
										changesMade = true;
										packs.get(j).setAnnot(packs.get(j).getTextField().getText());
									}
								});
								
								
								panel_7.add(packs.get(i).getPanel(), c);								
								panel_7.revalidate();
								panel_7.repaint();
							}
							numOfHits.setText(packs.size()+" hits");
							scrollPane.setViewportView(panel_7);
				            tabbedPane.setSelectedIndex(1);
						}
					} catch (ParserConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		JButton btnAnalyze = new JButton("Analyze");
		btnAnalyze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				fc_files.setCurrentDirectory(fc.getCurrentDirectory());
				int fc_return = fc_files.showOpenDialog(osdep.this);
				
				
				if(fc_return == JFileChooser.APPROVE_OPTION){
					File file = fc_files.getSelectedFile();
					try {
						ArrayList<PackageHit> temp = null;
						temp = XMLwriter.readXML(file.getAbsolutePath());
						if(temp == null){
							JOptionPane.showMessageDialog(osdep.this, "The selected XML is incompatible with this program. Please select an XML that has been previously created by this program.", "XML Error", JOptionPane.ERROR_MESSAGE);
							//packs = new ArrayList<PackageHit>();
						}else{
							top.removeAllChildren();
							packs.clear();
							packs = temp;
							//populate.run();
							panel_7.removeAll();
							panel_7.setLayout(gbl_panel_7);
							
							GridBagConstraints c = new GridBagConstraints();
							
							for(int i = 0; i<packs.size(); i++){
								
								packs.get(i).addFilepathGUI();
								
								final int j = i;
								
								c.gridx = 1;
								c.gridy = i;	//set constraints
								c.weightx = 1;
								c.gridwidth = 1;
								c.gridheight = 1;
								c.fill = GridBagConstraints.HORIZONTAL;
								c.anchor = GridBagConstraints.FIRST_LINE_START;
							   // c.insets = new Insets(3,3,3,3);	
								if(i != packs.size()-1){
									c.weighty = 0;
								}else{
									c.weighty = 1;
								}
								
								
								packs.get(j).getCheckBox("na").addActionListener(new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changesMade = true;
										if(!packs.get(j).getCheckBox("na").isSelected()){
											packs.get(j).getCheckBox("na").setSelected(true);
										}
										packs.get(j).getCheckBox("ni").setSelected(false);
										packs.get(j).getCheckBox("i").setSelected(false);
										packs.get(j).setDesig("notAnalyzed");
									}
								});
								packs.get(j).getCheckBox("ni").addActionListener(new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changesMade = true;
										if(!packs.get(j).getCheckBox("ni").isSelected()){
											packs.get(j).getCheckBox("ni").setSelected(true);
										}
										packs.get(j).getCheckBox("na").setSelected(false);
										packs.get(j).getCheckBox("i").setSelected(false);
										packs.get(j).setDesig("notInteresting");
									}
								});
								packs.get(j).getCheckBox("i").addActionListener(new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changesMade = true;
										if(!packs.get(j).getCheckBox("i").isSelected()){
											packs.get(j).getCheckBox("i").setSelected(true);
										}
										packs.get(j).getCheckBox("na").setSelected(false);
										packs.get(j).getCheckBox("ni").setSelected(false);
										packs.get(j).setDesig("Interesting");
									}
								});
								
								packs.get(j).getTextField().getDocument().addDocumentListener(new DocumentListener() {
									
									@Override
									public void removeUpdate(DocumentEvent e) {
										// TODO Auto-generated method stub
										changesMade = true;
										packs.get(j).setAnnot(packs.get(j).getTextField().getText());
									}
									
									@Override
									public void insertUpdate(DocumentEvent e) {
										// TODO Auto-generated method stub
										changesMade = true;
										packs.get(j).setAnnot(packs.get(j).getTextField().getText());
									}
									
									@Override
									public void changedUpdate(DocumentEvent e) {
										changesMade = true;
										packs.get(j).setAnnot(packs.get(j).getTextField().getText());
									}
								});
								
								
								panel_7.add(packs.get(i).getPanel(), c);								
								panel_7.revalidate();
								panel_7.repaint();
							}
							numOfHits.setText(packs.size()+" hits");
							scrollPane.setViewportView(panel_7);
				            tabbedPane.setSelectedIndex(1);
						}
					} catch (ParserConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		btnAnalyze.setForeground(Color.CYAN);
		btnAnalyze.setBackground(Color.DARK_GRAY);
		panel_3.add(btnAnalyze);
		panel_2.setLayout(null);
		
		
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBackground(Color.DARK_GRAY);
		btnBrowse.setForeground(Color.CYAN);
		btnBrowse.setBounds(413, 12, 97, 25);
		btnBrowse.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				fc_files.setCurrentDirectory(fc.getCurrentDirectory());
				int fc_return = fc_files.showDialog(osdep.this, "Select");
				
				
				if(fc_return == JFileChooser.APPROVE_OPTION){
					File file = fc_files.getSelectedFile();
					resultsName.setText(file.getAbsolutePath());
				}
			}
		});
		panel_2.add(btnBrowse);
		panel_1.setLayout(null);
		
		JButton btnNewButton = new JButton("Browse");
		btnNewButton.setBackground(Color.DARK_GRAY);
		btnNewButton.setForeground(Color.CYAN);
		btnNewButton.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
				int fc_return = fc.showOpenDialog(osdep.this);
				
				if(fc_return == JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();
					txtEnter.setText(file.getAbsolutePath());
				}
			}
		});
		btnNewButton.setBounds(413, 12, 97, 25);
		panel_1.add(btnNewButton);
		panel.setLayout(gl_panel);
		
		
		
		
		
		
		
	}
}