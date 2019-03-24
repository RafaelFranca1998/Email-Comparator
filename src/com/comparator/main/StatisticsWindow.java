package com.comparator.main;

import javax.swing.JFrame;

import com.comparator.verify.CheckError;

import javax.swing.JTextArea;

public class StatisticsWindow {

	private JFrame frame;
	private CheckErrorWindow checkError;

	/**
	 * Create the application.
	 */
	public StatisticsWindow(CheckErrorWindow checkError) {
		this.checkError = checkError;
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Estatisticas");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTextArea textArearStatistcs = new JTextArea();
		textArearStatistcs.setColumns(2);
		textArearStatistcs.setEditable(false);;		
		textArearStatistcs.setBounds(26, 11, 387, 222);
		frame.getContentPane().add(textArearStatistcs);
		
		textArearStatistcs.append("Emails válidos: "+checkError.getValidEmail().size()+"\n");
		textArearStatistcs.append("Emails Inválidos: "+String.valueOf(checkError.getInvalidEmail().size())+"\n");
		textArearStatistcs.append("Total de Emails: "+String.valueOf(checkError.getEmail().size())+"\n");
	}
}
