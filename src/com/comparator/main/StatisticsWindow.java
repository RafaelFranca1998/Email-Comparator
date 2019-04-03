package com.comparator.main;

import javax.swing.JFrame;

import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;

public class StatisticsWindow {
	//Declara��o dos componentes
	private JFrame frame;
	private CheckErrorWindow checkError;
	private JTextArea textArearStatistcs;
	private JLabel lblResultadoFinal;

	public StatisticsWindow(CheckErrorWindow checkError) {
		this.checkError = checkError; //inst�ncia objeto com as informa��es.
		initialize();
		frame.setVisible(true);
	}

	private void initialize() {
		//Inicializa��o dos componentes
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
		
		textArearStatistcs.append("Emails v�lidos: "+checkError.getValidEmail().size()
				+"/"+(int)emailSize+"\n"); //exibe na textArea o total de emails v�lidos.
		
		textArearStatistcs.append("Emails Inv�lidos: "+String.valueOf(checkError.getInvalidEmail().size())
		+"/"+(int)emailSize+"\n");//exibe na textArea o total de emails inv�lidos.
		
		textArearStatistcs.append("Total de Emails: "
		+String.valueOf(checkError.getEmail().size())+"\n");//exibe na textArea o total de emails.
	
		double valor = checkError.getInvalidEmail().size();//obtem o total de emails inv�lidos.

		double d = (valor*100)/emailSize; // c�lculo da porcentagem de emails inv�lidos.
		
		textArearStatistcs.append("Porcentagem de Emails Inv�lidos: "+d+"% \n");
		
		double valor2 = checkError.getValidEmail().size();//obtem o total de emails v�lidos.
		
		double h = (valor2*100)/emailSize; // c�lculo da porcentagem de emails inv�lidos.
		
		textArearStatistcs.append("Porcentagem de Emails V�lidos: "+h+"% \n");
	}
}
