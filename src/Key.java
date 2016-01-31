
public class Key {
	private String name;
	private String filepath;
	
	Key(String n, String f){
		name = n;
		filepath = f;
	}
	
	protected void setName(String n){
		name = n;
	}
	
	protected String getName(){
		return name;
	}
	
	protected void setFilepath(String f){
		filepath = f;
	}
	
	protected String getFilepath(){
		return filepath;
	}
}
