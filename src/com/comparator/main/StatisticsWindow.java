package com.comparator.main;

import javax.swing.JFrame;

import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;

public class StatisticsWindow {

	private JFrame frame;
	private CheckErrorWindow checkError;

	public StatisticsWindow(CheckErrorWindow checkError) {
		this.checkError = checkError;
		initialize();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame("Estatisticas");
		frame.setBounds(100, 100, 353, 281);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		JTextArea textArearStatistcs = new JTextArea();
		textArearStatistcs.setColumns(2);
		textArearStatistcs.setEditable(false);;		
		textArearStatistcs.setBounds(10, 35, 318, 198);
		frame.getContentPane().add(textArearStatistcs);
		
		JLabel lblResultadoFinal = new JLabel("Resultado Final");
		lblResultadoFinal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblResultadoFinal.setBounds(10, 11, 144, 13);
		frame.getContentPane().add(lblResultadoFinal);
		
		double emailSize = checkError.getEmail().size();
		
		textArearStatistcs.append("Emails válidos: "+checkError.getValidEmail().size()+"/"+(int)emailSize+"\n");
		textArearStatistcs.append("Emails Inválidos: "+String.valueOf(checkError.getInvalidEmail().size())+"/"+(int)emailSize+"\n");
		textArearStatistcs.append("Total de Emails: "+String.valueOf(checkError.getEmail().size())+"\n");
	
		double valor = checkError.getInvalidEmail().size();

		double d = (valor*100)/emailSize;
		
		textArearStatistcs.append("Porcentagem de Emails Inválidos: "+d+"% \n");
		
		double valor2 = checkError.getValidEmail().size();
		
		double h = (valor2*100)/emailSize;
		
		textArearStatistcs.append("Porcentagem de Emails Válidos: "+h+"% \n");
	}
}
