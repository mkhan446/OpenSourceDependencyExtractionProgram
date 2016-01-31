

import java.util.*;
import java.text.*;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Node{

	// fields set at runtime 
	private Node parent;
	private ArrayList<Node> children;
	private DefaultMutableTreeNode alias;
	private String name;
	private String path;
	private String org;
	private String pkg; 
	private String status = "OK"; 
	private PackageHit pack;
	public String treeType; // added 20140730
	public boolean isDirectory = false;


	// Additional fields
	private String comment;
	private String license; 

	

	public Node() {
	}

	public Node(Node parent, ArrayList<Node> children, String name, String path) {
		this.parent = parent;
		this.children = children;
		this.alias = new DefaultMutableTreeNode(name);
		this.name = name;
		this.path = path;
		this.pack = new PackageHit(name);
		this.treeType = (parent == null || parent.treeType == null) ? name : parent.treeType; //added new 20140730


		if (this.children == null) {
			this.children = new ArrayList<Node>();
		}
	}

	public Node(Node parent, String name, boolean isDirectory) {
		this.parent = parent;
		this.name = name;
		this.alias = new DefaultMutableTreeNode(name);
		this.path = (this.parent == null) ? this.name : this.parent.getPath() + "/" + this.name;
		this.treeType = (parent == null || parent.treeType == null) ? name : parent.treeType; //added new 20140730
		this.pack = new PackageHit(name);
		this.isDirectory = isDirectory;

		if (!treeType.contains("dependency") || !treeType.contains("dep")) //added new 20140730
			setStatus(this.parent);

		if (this.children == null) {
			this.children = new ArrayList<Node>();
		}
	}

	public Node(Node parent, String name) {
		this.parent = parent;
		this.name = name;
		this.alias = new DefaultMutableTreeNode(name);
		this.path = (this.parent == null) ? this.name : this.parent.getPath() + "/" + this.name;
		this.treeType = (parent == null || parent.treeType == null) ? name : parent.treeType;
		this.pack = new PackageHit(name);
	
		if (!treeType.contains("dependency") || !treeType.contains("dep")) //added new 20140730
			setStatus(this.parent);

		if (this.children == null) {
			this.children = new ArrayList<Node>();
		}
	}

	// Accessory functions

	protected boolean hasChildren() {
		return (this.children == null) ? false : (!this.children.isEmpty());

	}

	protected int numChildren() {
		if (!this.children.isEmpty()) {
			return this.children.size();
		} 
		return -1;
	}

	/********** FIND/DEL: NOT YET IMPLEMENTED *************/
	public Node findDepthFirst(Node parent, String name, String path) {

		if (this.name.contains(name)) {
			return this;
		} else if (this.path.contains(path)) {
			return this;
		} else {
			if (!children.isEmpty()) {
				for (int i=0; i<children.size(); i++) {
					Node node = findDepthFirst(this, name, path);
					if (node != null) {
						return node;
					}
				}
			}
		}
		return null;
	}

	public Node findBreadthFirst(String name) {
		return null;
	}

	public void delete(Node node) {
		//TODO. Remember to delete the entire subtree.
		
	}
	/*********** Children ***********/

	protected boolean containsChild(Node child) {
		boolean doesContain = false;

		for (Node node : this.children) {
			doesContain = (child.equals(node)) ? true : false;
			if (doesContain == true) break;
		}
		return doesContain;
	}

	protected boolean addChild(Node child) {
		if (this.children == null) {
			this.children = new ArrayList<Node>();
		}
		this.getAlias().add(child.getAlias());
		return this.children.add(child);
	}

	protected boolean removeChild(Node child) {
		if (this.children != null) {
			this.getAlias().remove(child.getAlias());
			return this.children.remove(child);
		}
		return false;
	}

	protected ArrayList<Node> getChildren() {
		return this.children;
	}
	/************* Path **************/

	protected String getPath() {
		return this.path;
	}

	/************* Pkg **************/

	protected String getPkg() {
		return this.pkg;
	}

	/************* Name **************/

	protected void setName(String name) {
		if (this.name != null) {
			// TODO return error: the name has already been defined
		}
	 	this.name = name;
	 	this.pack.setName(name);
	}

	protected String getName() {
		return this.name;
	}
	
	protected DefaultMutableTreeNode getAlias(){
		return this.alias;
	}
	
	protected PackageHit getPackageHit(){
		return pack;
	}


	// Additional Functionalities

	/************* Comments **************/

	//TODO this functionality is not yet implemented

	protected void addComment(String comment) {


	 	if (this.comment == null) {
	 		this.comment = comment;
	 	} else {
	 		Date dNow = new Date( );
      		SimpleDateFormat ft = new SimpleDateFormat ("'['yyyy/MM/dd']' ");

      		// for testing
      		System.out.println("Current Date: " + ft.format(dNow));

	 		this.comment += " " + ft.format(dNow) + comment;
	 	}
	}

	protected String getComment() {
		return this.comment;
	}

	/************* License **************/

	protected void setLicense(String license) {
	 	this.license = license;
	}

	protected String getLicense() {
		return this.license;
	}

	/************ package detection algorithm right here **************/

	protected void setStatus(Node parent) {

		String warning = "WARNING";

		if (this.name.contains("ibm")) {
			this.status = "OK";
		} 

		else if (this.getName().equals("lib")) {
			//System.out.println("["+this.getPath() + "] contains:");
			if (this.parent!=null && this.parent.getName()!=null)
				this.pkg = this.parent.getName() + "lib";

		}

		else if (this.parent != null && 
				this.parent.getName().equals("lib") && 
				this.getName().contains(".")){


			this.status = warning;
			this.pkg = this.getName();

		}


		else if (!this.getName().contains("ibm") && this.isDirectory && 
			(this.getName().contains(".") && this.getName().charAt(0) != '.')){

			this.status = warning + " NEW";
			this.pkg = this.getName();


		}

		else if (this.parent != null && !this.getName().contains("ibm") &&	((this.parent.getName().equals("org") || this.parent.getName().equals("com")) && (!this.getName().equals("apache") && !this.getName().equals("eclipse"))  )) {

			this.status = warning  + " NEW ORG: " + this.getName();
			
		} 



		else if (this.parent != null && this.parent.parent != null && 
				 !this.parent.getName().contains("ibm") &&
				 (
				 (this.parent.parent.getName().equals("org") || this.parent.parent.getName().equals("com")) &&
				 (!this.getName().equals("features") && !this.getName().equals("plugins"))
				 )) {

			this.org = this.parent.parent.getName()+"."+this.parent.getName();
			this.status = warning + " NEW: "+ this.org + "."+ this.getName();
			this.pkg = this.org+"."+this.getName();

		} 

		if (this.parent != null && 
			this.parent.getName().equals("lib")) {
			//System.out.println("\t"+this.getName());
		}




	}


	protected String getStatus() {
		return this.status;
	}

	/************* Print **************/

	protected void print() {
		//System.out.println("---------------------------------------------------------");
		//System.out.println("Printing tree... please wait ...\n");
        print("", true);
    }
    protected void printStatus() {
    	//System.out.println("---------------------------------------------------------");
    	//System.out.println("Printing tree... please wait ...\n");
        printStatus("", true);
    }
    
    protected void printStatus(String status) {
    	//System.out.println("---------------------------------------------------------");
    	//System.out.println("Printing tree with status ["+ status + "]... please wait ...\n");
    	printStatus("", true, status);
    }


    private void print(String prefix, boolean isTail) {
       // System.out.println(prefix + (isTail ? "└── " : "├── ") + this.getName());
        if (children.size()!= 0){
	        for (int i = 0; i < children.size() - 1; i++) {
	            children.get(i).print(prefix + (isTail ? "    " : "│   "), false);
	        }
	        if (children.size() >= 1) {
	            children.get(children.size() - 1).print(prefix + (isTail ?"    " : "│   "), true);
	        }
    	}
    }

    private void printStatus(String prefix, boolean isTail) {
       // System.out.println(prefix + (isTail ? "└── " : "├── ") + this.getName() + " ["+this.status+"]");
        if (children.size()!= 0){
	        for (int i = 0; i < children.size() - 1; i++) {
	            children.get(i).printStatus(prefix + (isTail ? "    " : "│   "), false);
	        }
	        if (children.size() >= 1) {
	            children.get(children.size() - 1).printStatus(prefix + (isTail ?"    " : "│   "), true);
	        }
    	}
    }

    private void printStatus(String prefix, boolean isTail, String status) {
    	// NOTE: status is a dummy parameter. this function will print anything with the status not being OK.
    	if (!this.status.contains("OK")) {
    		//System.out.println(prefix + (isTail ? "└── " : "├── ") + this.getName() + " ["+this.status+"]");

    		if (children.size()!= 0){
	        for (int i = 0; i < children.size() - 1; i++) {
	            children.get(i).printStatus(prefix + (isTail ? "    " : "│   "), false, status);
	        }
	        if (children.size() >= 1) {
	            children.get(children.size() - 1).printStatus(prefix + (isTail ?"    " : "│   "), true, status);
	        }
    	}
    	} else {
    		
    		if (children.size()!= 0){
    			//System.out.println(prefix + (isTail ? "└── " : "├── ") + this.getName());
		        for (int i = 0; i < children.size() - 1; i++) {
		            children.get(i).printStatus(prefix + (isTail ? "    " : "│   "), false, status);
		        }
		        if (children.size() >= 1) {
		            children.get(children.size() - 1).printStatus(prefix + (isTail ?"    " : "│   "), true, status);
		        }
    	}
    	}
    	
        
    }//end print status

    /************* collect packages and print **************/
    /* you can collect new packages using any node as your base node, potentially
    */

    private HashMap<String, ArrayList<Node>> collectNewPkgs(HashMap<String,ArrayList<Node>> map) {

		if (children.size()!=0) {

			for (int i = 0; i < children.size(); i++) {
				//Insert
				Node child = children.get(i);
				if (child.pkg != null && !child.status.equals("OK")) {
					if (!map.containsKey(child.pkg)) {
						map.put(child.pkg, new ArrayList<Node>());
					} 
					map.get(child.pkg).add(child);
				}

				//recurse
				map = child.collectNewPkgs(map);
				
			}

		}
		return map;
	}

	protected HashMap<String, ArrayList<Node>> aggregateNewPkgs() {
		//create an map of pkg name to list of paths
		HashMap<String, ArrayList<Node>> map = new HashMap<String, ArrayList<Node>>();
		return collectNewPkgs(map);
	}

	protected ArrayList<String> printNewPkgsConcise() {
		//System.out.println("---------------------------------------------------------");
		//System.out.println("Collecting new packages... please wait ...\n");
		//call aggergate newPkgs
		HashMap<String, ArrayList<Node>> map = aggregateNewPkgs();

		ArrayList<String> keys = new ArrayList<String>(map.keySet());
		
    	Collections.sort(keys);
//    	for (String key : keys) {
//			if (!key.equals("lib")) System.out.println(key);
//		}
    	return keys;

		//iterate over keys, as it is a set
		


	}

	protected void printNewPkgsElaborate() {
		//System.out.println("---------------------------------------------------------");
		//System.out.println("Collecting new packages with details... please wait ...\n");
		HashMap<String, ArrayList<Node>> map = aggregateNewPkgs();

		//iterate over keys to get values
		for (Map.Entry<String, ArrayList<Node>> entry : map.entrySet()) {
			String key = entry.getKey();
		    if (!key.equals("lib")){
			    ArrayList<Node> nodelist = entry.getValue();
			    
			    System.out.println(key);
			    for (Node n : nodelist) {
			    	System.out.println("\t"+n.getPath());
			    }
			    System.out.println();
			}

		}

	}



}