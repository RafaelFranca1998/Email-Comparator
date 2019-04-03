package com.comparator.main;

import javax.swing.JFrame;

import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;

public class StatisticsWindow {
	//Declaração dos componentes
	private JFrame frame;
	private CheckErrorWindow checkError;
	private JTextArea textArearStatistcs;
	private JLabel lblResultadoFinal;

	public StatisticsWindow(CheckErrorWindow checkError) {
		this.checkError = checkError; //instância objeto com as informações.
		initialize();
		frame.setVisible(true);
	}

	private void initialize() {
		//Inicialização dos componentes
		frame = new JFrame("Estatisticas");
		frame.setBounds(100, 100, 353, 281);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		textArearStatistcs = new JTextArea();
		textArearStatistcs.setColumns(2);
		textArearStatistcs.setEditable(false);;		
		textArearStatistcs.setBounds(10, 35, 318, 198);
		frame.getContentPane().add(textArearStatistcs);
		
		lblResultadoFinal = new JLabel("Resultado Final");
		lblResultadoFinal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblResultadoFinal.setBounds(10, 11, 144, 13);
		frame.getContentPane().add(lblResultadoFinal);
		//################################################################################################################
		
		double emailSize = checkError.getEmail().size(); // obtem numero total de emails
		
		textArearStatistcs.append("Emails válidos: "+checkError.getValidEmail().size()
				+"/"+(int)emailSize+"\n"); //exibe na textArea o total de emails válidos.
		
		textArearStatistcs.append("Emails Inválidos: "+String.valueOf(checkError.getInvalidEmail().size())
		+"/"+(int)emailSize+"\n");//exibe na textArea o total de emails inválidos.
		
		textArearStatistcs.append("Total de Emails: "
		+String.valueOf(checkError.getEmail().size())+"\n");//exibe na textArea o total de emails.
	
		double valor = checkError.getInvalidEmail().size();//obtem o total de emails inválidos.

		double d = (valor*100)/emailSize; // cálculo da porcentagem de emails inválidos.
		
		textArearStatistcs.append("Porcentagem de Emails Inválidos: "+d+"% \n");
		
		double valor2 = checkError.getValidEmail().size();//obtem o total de emails válidos.
		
		double h = (valor2*100)/emailSize; // cálculo da porcentagem de emails inválidos.
		
		textArearStatistcs.append("Porcentagem de Emails Válidos: "+h+"% \n");
	}
}
