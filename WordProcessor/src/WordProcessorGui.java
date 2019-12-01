/**
 * 
 */

import java.awt.*;  
import java.awt.event.*;  
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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
	
	WordProcessorGui(){
		
		GridBagLayout grid = new GridBagLayout();
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		setTitle("Word Processor");
		setSize(680, 800);
		setMinimumSize(getSize());
		setPreferredSize(getSize());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(grid);
		setResizable(false);
		
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
//		JLabel preview, errorLog;
//		preview = new JLabel("Preview");
//		errorLog = new JLabel("Error Log");
		
		JPanel previewPanel, errorLogPanel;
		previewPanel = new JPanel();
		errorLogPanel = new JPanel();
		previewPanel.setBorder(BorderFactory.createTitledBorder("Preview"));
		errorLogPanel.setBorder(BorderFactory.createTitledBorder("Error Log"));
		
		/******************Text Area********************/
		Font font = new Font("Arial", Font.PLAIN, 12);
		
		previewDisplay = new JTextArea(30, 51);
		previewDisplay.setMargin(new Insets(10,10,10,10));
		previewDisplay.setLineWrap(true);
		previewDisplay.setFont(font);
		previewDisplay.setForeground(Color.black);
		//previewDisplay.setEditable(false);
		JScrollPane previewScroll = new JScrollPane(previewDisplay);
		
		errorLogDisplay = new JTextArea("Error: An Error", 10, 51); 
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
//			        BufferedReader br = new BufferedReader(new FileReader(filepath));    
//			        String s1 = "", s2 = "";                         
//			        while((s1 = br.readLine()) != null) {
//			        	s2 += s1 + "\n";
//			        }
			        // Call processing function in here on File object then display new File object
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
		    		//if(processFile != null) {
		    			//Files.copy(processFile.toPath(), fc.getSelectedFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
		    		//}
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
        String flags = "lsn1"; //default (80 chars/line):  left(l); single space(s); no indentations(n); one column(1) 

		while (((oneLine = br.readLine()) != null) && (end != true)) {
            lineNumber = lineNumber + 1;

			if (oneLine.charAt(0) == '-') {
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
                    flags = flags.replace('r','t');
                    flags = flags.replace('c','t');
                    flags = flags.replace('l','t');

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
                	flags += 'e';

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
            } else {
                end = writing(oneLine, flags, lineNumber);
            } 
		} //end of the while loop

		br.close();
		
    }
    
    public boolean writing(String oneLine, String flags, int lineNumber) {
		boolean end = false;
		int index;
		String temp;

		if (flags.charAt(0) != 'l') {
			//case: flag "-l" required for using flag "-i"
            //print an error message in the error log
            errorString += "Error (Line number " + lineNumber + "): flag -l is required for using flag -i.\n";
			//end = true;
		}

		if (end != true) {
			//checking each of the flags
        	//call each flag's related method to adjust the output

			//Justification
			if (flags.charAt(0) == 'l') {
				//left justify
				oneLine = String.format("%s", oneLine);

			} else if (flags.charAt(0) == 'r') {
				//right justify
				//oneLine = String.format("%s", oneLine);

			} else if (flags.charAt(0) == 'c') {
				//center (right and left)
				//???
			} else if (flags.charAt(0) == 't') {
				//title
				//???
			}

			//Spacing
			if (flags.charAt(1) == 's') {
				//single space
				//assume the input must be single space
				//do nothing
			} else if (flags.charAt(1) == 'd') {
				//double space
				temp = null;
				for (index = 0; index < oneLine.length(); index++) {
					temp = temp + oneLine.charAt(index);
					if ((index % 80 == 0) && (index != 0)) {
						temp = temp + "\n";
					}
				}
				oneLine = temp;
			}

			//Indentation
			if (flags.charAt(2) == 'n') {
				//remove indentation
				//do nothing
			} else if (flags.charAt(2) == 'i') {
				//indent on 1st line, 5 spaces, left only
				oneLine = "     " + oneLine;

			} else if (flags.charAt(2) == 'b') {
				//indent multiple lines. 10 spaces
				temp = null;
				for (index = 0; index < oneLine.length(); index++) {
					if (index % 80 == 0) {
						temp = "          " + temp;
					}
					temp = temp + oneLine.charAt(index);
				}
				oneLine = temp;
			}
			
			//Column
			if (flags.charAt(3) == '1') {
				//1 columns
				//assume the input must be 1 column
				//do nothing
			} else if (flags.charAt(3) == '2') {
				//2 columns, 35chs/10chs/35chs
				//twoColumn(oneLine);
			}

        	//Finally, write to a file 
			//or print it to the preview
			//appendToFile(address, oneLine);
			processedString += oneLine;
		}

		return end;
    }
	
	public static void main(String[] args) {
		WordProcessorGui wpg = new WordProcessorGui();
	}
	
}
