

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2Utils;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

/****************** NOT USED *******************/

public class unzipFunction {

	/*public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		File file = new File("altgraph-0.11.tar.gz");
		UnZipAllTypes(file);
		
	}*/
	
	public static File UnZipAllTypes(File zipped_type_unknown) throws IOException{
		
		//detect the file type
		
		String extension = "";
		
		int i = zipped_type_unknown.getName().lastIndexOf('.');
		if (i > 0) {
		    extension = zipped_type_unknown.getName().substring(i+1);
		}
		
		
		//based on the file type, apply the appropriate unzipping function
		
		if(extension.equals("zip")){ //this block extracts zip files

			 byte[] buffer = new byte[1024];
			 
		     try{
		 
		    	//create output directory is not exists
		    	File root = new File(zipped_type_unknown.getName().substring(0, i));
		    	if(!root.exists()){
		    		root.mkdir();
		    	}
		 
		    	//get the zip file content
		    	ZipInputStream zis = 
		    		new ZipInputStream(new FileInputStream(zipped_type_unknown));
		    	//get the zipped file list entry
		    	ZipEntry ze = zis.getNextEntry();
		 
		    	while(ze!=null){
		 
		    	   String fileName = ze.getName();
		    	   
		    	   if(ze.isDirectory()){
		    		   File newFile = new File(root + "/" + fileName);
		    		   newFile.mkdir();
		    		   ze = zis.getNextEntry();
		    		   continue;
		    	   }
		    	   else{
		    	   File newFile = new File(root + "/" + fileName);
		           System.out.println("file unzip : "+ newFile.getAbsoluteFile());
		            
		           	new File(newFile.getParent()).mkdirs();
		 
		            FileOutputStream fos = new FileOutputStream(newFile);             
		 
		            int len;
		            while ((len = zis.read(buffer)) > 0) {
		       		fos.write(buffer, 0, len);
		            }
		 
		            fos.close();   
		            ze = zis.getNextEntry();
		    	   }
		    	}
		 
		        zis.closeEntry();
		    	zis.close();
		    	
		    	
		 
		    	System.out.println("Done");
		    	return root;
		    }catch(IOException ex){
		       ex.printStackTrace(); 
		    }
			   
		}
		else if(extension.equals("gz")){ //this block extracts tar.gz files
			final int BUFFER = 2048;
			FileInputStream fin = new FileInputStream(zipped_type_unknown);
			BufferedInputStream in = new BufferedInputStream(fin);
			GzipCompressorInputStream gzIn = new GzipCompressorInputStream(in);
			TarArchiveInputStream tarIn = new TarArchiveInputStream(gzIn);
			
			TarArchiveEntry entry = null;
				  //Read the tar entries using the getNextEntry method 
				
			
				File root = new File(zipped_type_unknown.getName().substring(0, i));
					if(!root.exists()){
						root.mkdir();
					}
				 
				  while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
				 
				   System.out.println("Extracting: " + entry.getName());
				 
				   //If the entry is a directory, create the directory
				 
				   if (entry.isDirectory()) {
				 
				    File newFile = new File(root + "/" + entry.getName());
				    newFile.mkdirs();
				   }
				   //if the entry is a file, decompress the file
				   else {
				    int count;
				    byte data[] = new byte[BUFFER];
				 
				    FileOutputStream fos = new FileOutputStream(root + "/" + entry.getName());
			     BufferedOutputStream dest = new BufferedOutputStream(fos,
				      BUFFER);
				    while ((count = tarIn.read(data, 0, BUFFER)) != -1) {
			     dest.write(data, 0, count);
				    }
				    dest.close();
			   }
				  }
				 
				  tarIn.close();
				  System.out.println("Done");
				  
				  return root;
				 }
		else if(extension.equals("bz2")){ //this block extracts bz2 files
			
			System.out.println("Extracting: " + zipped_type_unknown.getName());
			
			final int buffersize = 2048;
			
			FileInputStream fin = new FileInputStream(zipped_type_unknown);
			BufferedInputStream in = new BufferedInputStream(fin);
			
			File root = new File(zipped_type_unknown.getName().substring(0, i));
			
			FileOutputStream out = new FileOutputStream(root);
			BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(in);
			
			
			final byte[] buffer = new byte[buffersize];
			int n = 0;
			
			
			while (-1 != (n = bzIn.read(buffer))) {
			    out.write(buffer, 0, n);
			}
			out.close();
			bzIn.close();
			
			System.out.println("Done");
			return root;
		}
		else{ //if the format is not recognized
			System.out.println("The format of " + zipped_type_unknown + " cannot be decompressed.");
			return zipped_type_unknown;
		}
		return zipped_type_unknown;
		
	}
}
