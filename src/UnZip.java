

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
 
 
/****************** NOT USED *******************/

public class UnZip
{
    List<String> fileList;
    /*
     * Unzip it
     * @param zipFile input zip file
     * @param output zip file output folder
     */
    public File unZipIt(String zipFile, String outputFolder){
 
     byte[] buffer = new byte[1024];
     //create output directory is not exists
    File folder = new File(outputFolder);
    if(!folder.exists()){
        folder.mkdirs();
    }

    File unzipped = folder;
 
     try{
 
    	//get the zip file content
    	ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
    	//get the zipped file list entry
    	ZipEntry ze = zis.getNextEntry();
 
    	while(ze!=null){

            String fileName = ze.getName();
            File newFile = new File(outputFolder + File.separator + fileName);

            System.out.println("Unpacking: "+ newFile.getCanonicalFile());
            if (ze.isDirectory()) newFile.mkdirs();
            else
            {
 
            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
           File parent = new File(newFile.getParent());
           if (!parent.exists()) {
                //System.out.print("Making parent directory: "+ parent.getName()+"... ");
                boolean didMkdir = parent.mkdirs();
                System.out.print(didMkdir+"\n");

           } 
            
            FileOutputStream fos = new FileOutputStream(newFile);             
            
            int len;
            while ((len = zis.read(buffer)) > 0) {
       		   fos.write(buffer, 0, len);
            }
 
            fos.close();

            }
            zis.closeEntry();   
            ze = zis.getNextEntry();

    	}
 
        
    	zis.close();
 
    	System.out.println("...Done.");
 
    }catch(IOException ex){
       ex.printStackTrace(); 
    }
    return unzipped;
   }//end unzipit  
}