/**
 * 
 */

import java.awt.Color;
import java.awt.*;  
import javax.swing.*;

/**
 * @author Vuong
 *
 */
public class WordProcessorGui {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Word Processor");
		
		GridBagLayout grid = new GridBagLayout();
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		frame.setSize(500, 800);
		frame.setMinimumSize(frame.getSize());
		frame.setPreferredSize(frame.getSize());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(grid);
		//frame.setResizable(false);
		
		/*******************Menu********************/
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		JMenuItem openFile, saveFile;
		
		openFile = new JMenuItem("Open File and Process");
		saveFile = new JMenuItem("Save File");
		
		menu.add(openFile);
		menu.add(saveFile);
		
		menuBar.add(menu);
		
		frame.setJMenuBar(menuBar);
		
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
		
		JTextArea previewDisplay = new JTextArea(30, 40); 
		previewDisplay.setEditable(false);
		JScrollPane previewScroll = new JScrollPane(previewDisplay);
		
		JTextArea errorLogDisplay = new JTextArea(10, 40); 
		errorLogDisplay.setEditable(false);
		JScrollPane errorLogScroll = new JScrollPane(errorLogDisplay);
		
		previewScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		previewPanel.add(previewScroll);
		
		errorLogScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		errorLogPanel.add(errorLogScroll);
		
		/***********************************************/

	    gbc.gridx = 0;  
	    gbc.gridy = 0;
		frame.add(previewPanel, gbc);
	    gbc.gridx = 0;
	    gbc.gridy = 1;
		frame.add(errorLogPanel, gbc);
		
		frame.setVisible(true);
	}
	
}
