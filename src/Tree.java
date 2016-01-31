


import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.util.Enumeration;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.io.*;

class Tree {
	public static final int UNKNOWN = -1;

    /** File type Java class */
    public static final int CLASSFILE = 0xcafebabe;

    /** File type ZIP archive */
    public static final int ZIPFILE = 0x504b0304;

    /** File type GZIP compressed Data */
    public static final int GZFILE = 0x1f8b0000;

    /** File type Pack200 archive */
    public static final int PACK200FILE = 0xcafed00d;

    protected static boolean deleteUnzipped = true;
    protected static boolean deleteZipped = false;

    protected static boolean printDependencyTree = true;

	protected static boolean printTreeElaborate = false;
	protected static 	boolean printTreeConcise = false;
	protected static 	boolean printPkgElaborate = false;
	protected static 	boolean printPkgConcise = true;


	public Tree() {
	}//default constructor

	private static boolean ignoreFolder(String foldername) {
		
		String foldername_lowercase = foldername.toLowerCase();

		switch (foldername_lowercase) {
			case "samples": return true;
			case "sample": return true;
			case "tests": return true;
			case "test": return true;
			case "docs": return true;
			case "doc": return true;
			case "examples": return true;
			case "example": return true;
			case "temps": return true;
			case "temp": return true;
			case "tutorials": return true;
			case "tutorial": return true;
			case "images": return true;
			case "img": return true;
			case "icons": return true;
			default: return false;
		}
		
	}

	public static Node buildTree(Node root, File folder) throws Exception{
		File[] directory = folder.listFiles();
	
		
		for (File file : directory) {
			//if zip, extract zip
			
	
				if (file.isDirectory() ) {
					if (!ignoreFolder(file.getName())){
						////////////RECURSIVE CALL/////////
			    		//create root node for current directory
			    		Node node = new Node(root, file.getName(), true);
			    		root.addChild(node);
			    		root.getAlias().add(node.getAlias());
			    		//RECURSE
			    		//System.out.println("[DIR] "+ file.getCanonicalPath());
			    		node = buildTree(node, file);
					}
		    		
		    		
				}  else {
					try {
						
						FileInputStream fis = new FileInputStream(file);
						InputStream is = fis;
						FileSig fileSig = new FileSig(is);
						int type = fileSig.getType();
						
						if (type == ZIPFILE) {
						
				      	
				    	UnzipAll unzip = new UnzipAll();
				    	File f = UnzipAll.unzip(file);
				    	
						
			        	if (f.isDirectory()) {
			        		if (!ignoreFolder(f.getName())) {
	
				        		////////////RECURSIVE CALL/////////
				        		//create root node for current directory
				        		
					        		Node node = new Node(root, f.getName(), true);
					        		root.addChild(node);
					        		root.getAlias().add(node.getAlias());
					        		//RECURSE
					        		node = buildTree(node, f);
	
			        		}
			        		
			        		
	
			        	} else if (printTreeElaborate) {
			        		
			        		Node node = new Node(root, f.getName());
							root.addChild(node);
							root.getAlias().add(node.getAlias());
			        	}
	
				    	if (f.exists() && deleteUnzipped) {
				    		FileUtils.forceDelete(f);
	
				    	} else if (file.exists() && deleteZipped) {
				    		FileUtils.forceDelete(file);
				    	}
					  
						} else {
							if (printTreeElaborate) {
								//System.out.println(file.getCanonicalPath());
								Node node = new Node(root, file.getName());
								root.addChild(node);
								root.getAlias().add(node.getAlias());
							}
							
						}
					    is.close();
						fis.close(); 
					} catch (IOException e) {}
	
				} //end if-else
			
				
		
		}//end-for (all files in directory)
	
		
		return root;
	};
	
	public static Node buildDependencyTree(Node root, Node depRoot) {
		
		if (root.hasChildren()) {
			for (int i=0; i < root.getChildren().size(); i++ ) {
				Node child = root.getChildren().get(i);
	
				if (child.getPkg() != null){
	
	
	
					Node node = new Node(depRoot, child.getPkg(), true);
					depRoot.getAlias().add(node.getAlias());
	
	
					node = buildDependencyTree(child, node);
	
	
					if (!depRoot.containsChild(node)){
						if (!depRoot.addChild(node)) System.out.println("[WARNING] Tried to add ["+ node.getName()+"] to dependency tree. failed.");
					}
						
	
				} else depRoot = buildDependencyTree(child, depRoot);
				
			}
		} 
		
		
	
		return depRoot;

		
	}

}