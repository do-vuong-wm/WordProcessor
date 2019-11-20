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
 * @author Vuong
 *
 */

@SuppressWarnings("serial")
public class WordProcessorGui extends JFrame implements ActionListener{
	
	private JMenuItem openFile, saveFile;
	private JTextArea previewDisplay, errorLogDisplay;
	private File processFile;
	
	WordProcessorGui(){
		
		GridBagLayout grid = new GridBagLayout();
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		setTitle("Word Processor");
		setSize(660, 800);
		setMinimumSize(getSize());
		setPreferredSize(getSize());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		        String filepath= processFile.getPath();
		        // Call processing function in here on File object then display new File object
		        try {
		        BufferedReader br=new BufferedReader(new FileReader(filepath));    
		        String s1="",s2="";                         
		        while((s1=br.readLine()) != null) {
		        s2+=s1+"\n";    
		        }
		        previewDisplay.setText(s2);    
		        br.close();
		        }catch(Exception ex) {
		        	ex.printStackTrace();
		        }                 
		    } 
		}else if(e.getSource() == saveFile) {
		    JFileChooser fc = new JFileChooser();
		    int i = fc.showSaveDialog(this);
		    if(i == JFileChooser.APPROVE_OPTION) { 
		    	try {
		    		if(processFile != null)
		    			Files.copy(processFile.toPath(), fc.getSelectedFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
		    	}catch(Exception ex) {
		        	ex.printStackTrace();
		        }  
		    }
		}
	}
	
	public static void main(String[] args) {
		WordProcessorGui wpg = new WordProcessorGui();
	}
	
}
