import java.util.ArrayList;
import java.util.List;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.apache.commons.lang3.text.WordUtils;

import sun.awt.shell.ShellFolder;

/*
 * Shortcut Maker
 * 
 * This program, given a folder of shortcuts, creates a GUI to display the shortcuts.
 * 
 * @author Chris Coraggio
 * 
 * @version 08-24-2016
 * 
 */

class ShortcutMaker extends javax.swing.JFrame{
	
	private static final String PATH_TO_FOLDER = "C:\\Users\\Chris\\Desktop\\Junk";
	
	public ShortcutMaker(File directory){
		
		List<File> filesList = getFilesInDir(directory);
		int [] dimensions = getDimensions(filesList.size());
		
		setLayout(new GridLayout(dimensions[0], dimensions[1]));
		
		for(File file: filesList){
			add(getJButtonFromFile(file));
		}
		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	}

	public static ArrayList<File> getFilesInDir(File dir){
		
		ArrayList<File> returnArrayList = new ArrayList<File>();
		for(File file : dir.listFiles()){
			if(file.getName().endsWith(".lnk")){
				returnArrayList.add(file);
			}
		}
		return returnArrayList;
	}

	private static ImageIcon getImageIconFromFile(File file){
		//helper method - only called in getJButtonFromFile
		
		try{
			sun.awt.shell.ShellFolder shellFolder =
				sun.awt.shell.ShellFolder.getShellFolder(file);
			return new ImageIcon(shellFolder.getIcon(true));
		}catch(Exception err) {
			return new ImageIcon();
		}
	}
	
	private static String getFileNameFromFile(File file){
		//propercased and without suffix
		//helper method - only called in getJButtonFromFile
		
		return WordUtils.capitalize(file.getName().substring(0, file.getName().indexOf(".")));
	}
	
	public static JButton getJButtonFromFile(File file){
		
		JButton button = new javax.swing.JButton(getImageIconFromFile(file));
		button.setText(getFileNameFromFile(file));
		button.addActionListener(new java.awt.event.ActionListener() {
			
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					Runtime.getRuntime().exec("cmd /c START \"Title\" \"" + file.getAbsolutePath() + "\"");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		return button;
	}
	
	private int[] getDimensions(int numButtons){
		//dynamically create a neat rectangular display
		
		int [] dimensions = new int[2]; //length, width
		if(numButtons < 16){ //length=4 until wider than length
			dimensions[0] = 4;
			dimensions[1] = (int) Math.ceil((double)(numButtons / 4));
		}else{
			dimensions[0] = (int) Math.ceil(Math.sqrt(numButtons));
			dimensions[1] = (int) Math.floor(Math.sqrt(numButtons));
		}
		return dimensions;
	}
	
	public static void main(String [] args) {
		
		File directory = new File(PATH_TO_FOLDER);
		ShortcutMaker gui = new ShortcutMaker(directory);
		setAttributes(gui);
	}
	
	public static void setAttributes(ShortcutMaker sm){
		
		sm.setVisible(true);
		sm.setExtendedState(sm.getExtendedState() | JFrame.MAXIMIZED_BOTH); //make the GUI go full-screen
	}
}

