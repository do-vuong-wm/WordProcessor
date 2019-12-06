/**
 * 
 */

import java.awt.*; 
import java.awt.event.*;  
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*; 


/**
 * @author Vuong, Tsz
 *
 */

@SuppressWarnings("serial")
public class WordProcessorGui extends JFrame implements ActionListener{
	
	private JMenuItem openFile, saveFile;
	private JTextArea previewDisplay, errorLogDisplay;
	private File processFile;
	private String processedString;
	private String errorString;
	
    public static List<String> splitString(String msg, int lineSize, char indentation) {
        List<String> res = new ArrayList<>();

        Pattern p = Pattern.compile("\\b.{1," + (lineSize-1) + "}\\b\\W?");
        Matcher m = p.matcher(msg);
        
        while(m.find()) {
        		//System.out.println(m.group()+ '/');
        		if(indentation == 'b')
                	res.add("          " + m.group().trim());
                else
                	res.add(m.group().trim());
        }
        return res;
    }
	
	WordProcessorGui(){
		
		GridBagLayout grid = new GridBagLayout();
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		setTitle("Word Processor");
		setSize(700, 880);
		setMinimumSize(getSize());
		setPreferredSize(getSize());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(grid);
		setResizable(true);
		
		/*******************Menu********************/
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		
		openFile = new JMenuItem("Open File and Process");
		saveFile = new JMenuItem("Save File");
		
		// Add listener
		openFile.addActionListener(this);
		saveFile.addActionListener(this);
		
		menu.add(openFile);
		menu.add(saveFile);
		
		menuBar.add(menu);
		
		setJMenuBar(menuBar);
		
		/*****************Panels*******************/
		
		JPanel previewPanel, errorLogPanel;
		previewPanel = new JPanel();
		errorLogPanel = new JPanel();
		previewPanel.setBorder(BorderFactory.createTitledBorder("Preview"));
		errorLogPanel.setBorder(BorderFactory.createTitledBorder("Error Log"));
		
		/******************Text Area********************/
		Font font = new Font("monospaced", Font.PLAIN, 12);
		previewDisplay = new JTextArea(30, 80);
		previewDisplay.setMargin(new Insets(10,10,10,10));
		previewDisplay.setLineWrap(true);
		previewDisplay.setFont(font);
		previewDisplay.setForeground(Color.black);
		//previewDisplay.setEditable(false);
		JScrollPane previewScroll = new JScrollPane(previewDisplay);
		
		errorLogDisplay = new JTextArea("Error: An Error", 10, 80); 
		errorLogDisplay.setMargin(new Insets(10,10,10,10));
		errorLogDisplay.setLineWrap(true);
		errorLogDisplay.setFont(font);
		errorLogDisplay.setForeground(Color.red);
		errorLogDisplay.setEditable(false);
		JScrollPane errorLogScroll = new JScrollPane(errorLogDisplay);
		
		previewScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		previewScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		previewPanel.add(previewScroll,gbc);
		
		errorLogScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		errorLogScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		errorLogPanel.add(errorLogScroll,gbc);
		
		/***********************************************/
		//gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
	    gbc.gridx = 0;  
	    gbc.gridy = 0;
		add(previewPanel, gbc);
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
	    gbc.gridx = 0;
	    gbc.gridy = 1;
		add(errorLogPanel, gbc);
		
		pack();
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {    
		if(e.getSource() == openFile) {
		    JFileChooser fc = new JFileChooser();
		    int i = fc.showOpenDialog(this);    
		    if(i == JFileChooser.APPROVE_OPTION) { 
		        processFile = fc.getSelectedFile(); // this is a File object   
		        //String filepath = processFile.getPath();
		        try {
		        	processedString = "";
		        	errorString = "";
			        processing();
			        previewDisplay.setText(processedString); 
			        errorLogDisplay.setText(errorString);
			        //br.close();
		        }catch(Exception ex) {
		        	ex.printStackTrace();
		        }                 
		    } 
		}else if(e.getSource() == saveFile) {
		    JFileChooser fc = new JFileChooser();
		    int i = fc.showSaveDialog(this);
		    if(i == JFileChooser.APPROVE_OPTION) { 
		    	try {
		    	        FileWriter fw = new FileWriter(fc.getSelectedFile());
		    	        fw.write(processedString);
		    	        fw.close();
		    	}catch(Exception ex) {
		        	ex.printStackTrace();
		        }  
		    }
		}
	}
	
    public void processing() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(processFile));
		String oneLine;
        int lineNumber = 0;
		boolean end = false;
		//int index;

        //flags: Justification; Spacing; Indentation; Column
        String flags = "lsn1-"; //default (80 chars/line):  left(l); single space(s); no indentations(n); one column(1) 

		while (((oneLine = br.readLine()) != null) && (end != true)) {
            lineNumber = lineNumber + 1;
            
            if(oneLine.isEmpty()) {
            	
            	processedString += "\n";
            	
            }else {	
            
				if (oneLine.charAt(0) == '-') {
					
					if(oneLine.length() != 1) {
						
	                //read a flag
	                if (oneLine.charAt(1) == 'r') {
	                    //right justify
	                    flags = flags.replace('l','r');
	                    flags = flags.replace('c','r');
	                    flags = flags.replace('t','r');
	
	                } else if (oneLine.charAt(1) == 'c') {
	                    //center (right and left)
	                    flags = flags.replace('l','c');
	                    flags = flags.replace('r','c');
	                    flags = flags.replace('t','c');
	
	                } else if (oneLine.charAt(1) == 'l') {
	                    //left justify
	                    flags = flags.replace('r','l');
	                    flags = flags.replace('c','l');
	                    flags = flags.replace('t','l');
	
	                } else if (oneLine.charAt(1) == 't') {
	                    //title
	                    flags = flags.replace('-','t');
	                } else if (oneLine.charAt(1) == 'd') {
	                    //double space
	                    flags = flags.replace('s','d');
	
	                } else if (oneLine.charAt(1) == 's') {
	                    //single space
	                    flags = flags.replace('d','s');
	
	                } else if (oneLine.charAt(1) == 'i') {
	                    //indent on 1st line, 5 spaces, left only
						//the "left only" status will be check in the writing method  
	                    flags = flags.replace('n','i');
	                    flags = flags.replace('b','i');
	
	                } else if (oneLine.charAt(1) == 'b') {
	                    //indent multiple lines. 10 spaces
	                    flags = flags.replace('n','b');
	                    flags = flags.replace('i','b');
	
	                } else if (oneLine.charAt(1) == '2') {
	                    //2 columns, 35chs/10chs/35chs
	                    flags = flags.replace('1','2');
	
	                } else if (oneLine.charAt(1) == '1') {
	                    //1 column
	                    flags = flags.replace('2','1');
	
	                } else if (oneLine.charAt(1) == 'e') {
	                    //blank line
	                    //adding a blank line to the output file directly
	                	processedString += '\n';
	
	                } else if (oneLine.charAt(1) == 'n') {
	                    //remove indentation
	                    flags = flags.replace('i','n');
	                    flags = flags.replace('b','n');
	
	                } else {
	                    //case: invalid flag
	                    //print an error message in the error log
	                	errorString += "Error (Line number " + lineNumber + "): Invalid flag.\n";
						//end = true;
	                }
	                
					}else {
	                	errorString += "Error (Line number " + lineNumber + "): Must include flag after -.\n";
					}
	            } else {
	                end = writing(oneLine, flags, lineNumber);
	            } 
			
            }
			
		} //end of the while loop

		br.close();
		
    }
    
    public boolean writing(String oneLine, String flags, int lineNumber) {
    	
		String newStr = "";
		String temp;
		String temp2;
		int indexStr;
		boolean end = false;
		
		List<String> wordblocks = null;
		ListIterator<String> iterator;
		
		if (flags.charAt(4) == 't') {
			System.out.println("here");
			//-t: centered, no justification (i.e. just centers 1 line)
			temp = oneLine;
			newStr = "";
			indexStr = 0;
			while (indexStr < temp.length()) {
				temp2 = temp.substring(indexStr, temp.length());
				if (temp2.length() > 80) {
					temp2 = temp2.substring(0, 80);
				}
				int numberOfSpaces = 80 - temp2.length();
				for (int i = 0; i < java.lang.Math.ceil(numberOfSpaces/2); i++) {
					newStr += ' ';
				}
				newStr += temp2;
				for (int i = 0; i < java.lang.Math.floor(numberOfSpaces/2); i++) {
					newStr += ' ';
				}
				indexStr += 80;
				newStr += '\n';
			}
			flags = flags.replace('t','-');
		}else {
		
		if(flags.charAt(3) == '1') {
			
			if(flags.charAt(2) == 'i') {
				wordblocks = splitString("XXXXX"+ oneLine, 80, flags.charAt(2));
			}
			if(flags.charAt(2) == 'b')
				wordblocks = splitString(oneLine, 70, flags.charAt(2));
			
			if(flags.charAt(2) == 'n')
				wordblocks = splitString(oneLine, 80, flags.charAt(2));

		}else if(flags.charAt(3) == '2') {
			if(flags.charAt(2) == 'i')
				wordblocks = splitString("XXXXX" + oneLine, 30, flags.charAt(2));
			if(flags.charAt(2) == 'b')
				wordblocks = splitString(oneLine, 25, flags.charAt(2));
			if(flags.charAt(2) == 'n')
				wordblocks = splitString(oneLine, 35, flags.charAt(2));
		}
				
		iterator = wordblocks.listIterator();
		
		int counter = 0;

        while (iterator.hasNext()) { 
        	String tempStr = iterator.next();
        	int tempStrLen = tempStr.length();
        	int addSpaces = 0;
        	
        	if(counter == 0) {
        		if(tempStrLen >= 5) {
	        		if(tempStr.substring(0, 5).equals("XXXXX")) {
	        			tempStr = new String(new char[5]).replace("\0", " ") + tempStr.substring(5, tempStrLen);
	        			if(flags.charAt(3) == '2')
	        				wordblocks.set(0, tempStr);
	        		}
        		}
        	}
        	
        	if(flags.charAt(0) == 'r') {
        	
	        	if(flags.charAt(3) == '1') {
	    				tempStr = tempStr.trim();
	    				int strLength = tempStr.length();
	    				int numberOfSpaces = 80 - strLength;
	    				
	    				if(numberOfSpaces > 0) {
	    					tempStr = new String(new char[numberOfSpaces]).replace("\0", " ") + tempStr;
	    				}
	        	}else if(flags.charAt(3) == '2') {
	    				tempStr = tempStr.trim();
	    				int strLength = tempStr.length();
	    				int numberOfSpaces = 35 - strLength;
	    				
	    				if(numberOfSpaces > 0) {
	    					tempStr = new String(new char[numberOfSpaces]).replace("\0", " ") + tempStr;
	    					wordblocks.set(counter, tempStr);
	    				}
	        	}
	        	
        	}else {
            	if(flags.charAt(3) == '1')
            		addSpaces = 80 - tempStrLen;
            	else if(flags.charAt(3) == '2')
            		addSpaces = 35 - tempStrLen;
            	
            	tempStr = tempStr + new String(new char[addSpaces]).replace("\0", " ");
            	if(flags.charAt(3) == '2') {
            		wordblocks.set(counter, tempStr);
            	}
        	}
        	
        	newStr += tempStr;
        	
			if(flags.charAt(1) == 's')
				newStr += '\n';
			else if(flags.charAt(1) == 'd')
				newStr += "\n\n";
			
			counter++;
        } 
		
		if(flags.charAt(3) == '2') {
    		int cols = wordblocks.size()/2;;
    		int extraCol = wordblocks.size()%2;
    		int anocounter = 0;
    		int othCounter = 0;
    		
    		newStr = "";
    		
    		if(extraCol == 1) {
    			othCounter = cols+1;
    		}else{
    			othCounter = cols;
    		}
    		
    		while (anocounter < cols) {
    			        		
        		if(extraCol == 1 && othCounter == cols) {
        			newStr += wordblocks.get(cols);
        		}else {
        			newStr += wordblocks.get(anocounter) + "          " + wordblocks.get(othCounter);
        		}
        		
    			if(flags.charAt(1) == 's')
    				newStr += '\n';
    			else if(flags.charAt(1) == 'd')
    				newStr += "\n\n";
    			
    			othCounter++;
    			anocounter++;
    		}
    		
    	}
		
		}

		processedString += newStr;
		
		return end;
		
    }
	
	public static void main(String[] args) {
		WordProcessorGui wpg = new WordProcessorGui();
	}
	
}

//if(flags.charAt(0) == 'c' && flags.charAt(2) != 'b') {
//	int countLeadingWS = 0;
//	int spacesBetween = 0;
//	int spacesBetweenMod = 0;
//	int spacesCount;
//	int totalSpaces;
//	int totalChars = 0;
//
//	for(int i = 0; i < tempStr.length(); i++) {
//		if(tempStr.charAt(i) == ' ')
//			countLeadingWS++;
//		else
//			break;
//	}
//	String[] words = tempStr.split("\\s+");
//	if(words.length > 0) {
//		for(int i = 0;  i < words.length; i++) {
//			totalChars += words[i].length();
//		}
//		
//		spacesCount = (80-totalChars-countLeadingWS);
//		totalSpaces = (words.length-1);
//		if(totalSpaces != 0) {
//			spacesBetween = (spacesCount)/totalSpaces;
//			spacesBetweenMod = (spacesCount)%totalSpaces;
//		}
//		tempStr = "";
//		for(int i = 0;  i < words.length; i++) {
//			if(i != words.length-1)
//				if(spacesBetweenMod == 0)
//					tempStr += words[i] + new String(new char[spacesBetween]).replace("\0", " ");
//				else {
//					tempStr += words[i] + new String(new char[spacesBetween+1]).replace("\0", " ");
//					spacesBetweenMod--;
//				}
//			else
//				tempStr += words[i];
//		}
//	}
//		tempStr = new String(new char[countLeadingWS]).replace("\0", " ") + tempStr;
//}