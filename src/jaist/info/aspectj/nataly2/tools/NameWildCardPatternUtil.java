package jaist.info.aspectj.nataly2.tools;



import jaist.info.aspectj.nataly2.metamodel.PCSignature;

import java.io.File;
import java.io.IOException;

/**
 * Class of tools which support for persist data.
 * @author suse-wl
 *
 */
public class NameWildCardPatternUtil {

	public static void writeNWPattern(PCSignature signature,String filepath,String pcname){
		String filename=getRelativeXMLFullFileName(filepath+pcname);
		try{
				
 			File f=new File(filepath);
 			if(!f.exists()){
 				f.mkdir();
 			}
 			SerialXMLUtil.save(signature, filename);
 		}
	    catch(IOException e){
 			System.out.println(e.toString());
 		}
	}
	public static PCSignature readNWPattern(String filepath,String pcname){
		String filename=getRelativeXMLFullFileName(filepath+pcname);
		PCSignature nwpattern=null;
		try {
			nwpattern = (PCSignature)SerialXMLUtil.load(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nwpattern;
	}
	private static String getRelativeXMLFullFileName(String advpath){
		StringBuilder fileNameBuilder = new StringBuilder(advpath);
		fileNameBuilder.append("#" + "sourcecode");
		fileNameBuilder.append("-nwpattern.xml");
		return fileNameBuilder.toString();
	}
}
