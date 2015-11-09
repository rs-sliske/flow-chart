package uk.sliske.util.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import uk.sliske.util.Constants;

public class FileSaver {

	private PrintWriter	file;


	public FileSaver(String fileName, String path) {
		String directory = Constants.USER_PATH;
		File tempFile = new File(directory);
		File temp2 = new File(tempFile, path);
		temp2.mkdirs();
		createFile(temp2.getAbsolutePath() +"\\"+ fileName);
	}

	private final void createFile(String s) {
		try {
			file = new PrintWriter(s);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		if (file != null) {
			System.out.println("file " + s + " created");
		}
	}
	
	public void empty(){
		
	}

	public void append(String s) {
		file.append(s);
	}
	
	public void appendf(String s,Object...objects ) {
		file.append(String.format(s, objects));
	}

	public void appendln(String s) {
		append(s);
		append("\n");
	}

	public void close() {
		file.close();

	}

}
