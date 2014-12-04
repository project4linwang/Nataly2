package jaist.info.aspectj.nataly2.tools;


import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;

public class SerialXMLUtil {

	public static void save(Object theGraph,String filename) throws IOException{
			XMLEncoder os=new XMLEncoder(
					new BufferedOutputStream(
							new FileOutputStream(filename)));
			os.writeObject(theGraph);
			os.close();
		
		
		
	}
	public static Object load(String filename) throws IOException{
		Object result=null;
		XMLDecoder imp = new XMLDecoder(
				new BufferedInputStream(new FileInputStream(filename)));
		result=imp.readObject();		
		imp.close();
		return result;
	}
}
