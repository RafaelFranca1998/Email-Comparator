package com.comparator.main;

import java.awt.EventQueue;
import java.awt.FileDialog;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.comparator.main.CheckErrorWindow.OnCompleteListener;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Font;


public class MainWindow {
	// DECLARAÇÃO DE COMPONENTES****************************************************************************************
	public  JFrame frmVerificadorDeDominios;
	private JTextField textFieldEmail;
	private JTextField textFieldDominios;
	private JTextField txtInserirEmail;
	private JTextField txtInserirDominio;
	private JTable tableEmail;
	private JTable tableDominios;
	private CheckErrorWindow checkError;
	private static ArrayList<String> emailList;
	private static ArrayList<String> dominioList;
	private static ArrayList<String> resultList;
	private JTable tableResultado;

	private JButton btnStatistcs;
	private JButton btnExport;
	private JButton btEmail;
	private JButton btDominios;
	private JButton btnInserirEmail;
	private JButton btnVerificarDominios;
	private JButton btInserirDominio;
	
	/**
	 * Inicia a Aplicação
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmVerificadorDeDominios.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainWindow() {
		emailList = new ArrayList<>();
		dominioList = new ArrayList<>();
		resultList = new ArrayList<>();
		initialize();
	}

	/**
	 * Inicialização dos componentes da tela.
	 */
	private void initialize() {
		//Configurações da tela
		frmVerificadorDeDominios = new JFrame();
		frmVerificadorDeDominios.getContentPane().setBackground(Color.WHITE);
		frmVerificadorDeDominios.setTitle("Verificador de Dominios");
		frmVerificadorDeDominios.setBounds(100, 100, 850, 500);
		frmVerificadorDeDominios.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmVerificadorDeDominios.getContentPane().setLayout(null);
		//scrollpane
		
		JScrollPane scrollPaneDominios = new JScrollPane();
		scrollPaneDominios.setBounds(257, 98, 164, 181);
		frmVerificadorDeDominios.getContentPane().add(scrollPaneDominios);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(455, 98, 185, 181);
		frmVerificadorDeDominios.getContentPane().add(scrollPane);
		
		JScrollPane scrollPaneEmail = new JScrollPane();
		scrollPaneEmail.setBounds(24, 98, 203, 181);
		frmVerificadorDeDominios.getContentPane().add(scrollPaneEmail);
		
		//botões
		btEmail = new JButton("Escolher Arquivo");
		btEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String cvsPath = getPath();
				textFieldEmail.setText(cvsPath);
				emailList = new ArrayList<>();
				emailList = readCvs(cvsPath);
				DefaultTableModel modelo = (DefaultTableModel) tableEmail.getModel();
				modelo.setNumRows(0);
				for (String s : emailList) {
					modelo.addRow(new Object[] { s });
				}
			}
		});
		btEmail.setBounds(694, 391, 140, 23);
		frmVerificadorDeDominios.getContentPane().add(btEmail);

		btDominios = new JButton("Escolher Arquivo");
		btDominios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cvsPath = getPath();
				textFieldDominios.setText(cvsPath);
				dominioList = new ArrayList<>();
				dominioList = readCvs(cvsPath);
				updateDominioList();
			}
		});
		btDominios.setBounds(694, 425, 140, 23);
		frmVerificadorDeDominios.getContentPane().add(btDominios);
		
		btnInserirEmail = new JButton("Inserir");
		btnInserirEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (txtInserirEmail.getText().equals("")) {
						throw new Exception();
					}
					emailList.add(txtInserirEmail.getText());
					txtInserirEmail.setText("");
					updateEmailList();
				} catch (Exception a) {
					JOptionPane.showMessageDialog(tableDominios, "Erro ao adicionar, Tente novamente.",
							"Erro ao adicionar!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnInserirEmail.setBounds(72, 335, 89, 23);
		frmVerificadorDeDominios.getContentPane().add(btnInserirEmail);

		btnVerificarDominios = new JButton("Verificar Dominios");
		btnVerificarDominios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (emailList.size() <= 0 || dominioList.size() <= 0) { // checa se a lista está vazia.
						JOptionPane.showMessageDialog(tableDominios, "Lista vazia", "Erro",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					if (dominioList.get(0).contains("@"))
						throw new Exception();//checa se o dominio contem @.
					if (!emailList.get(0).contains("@"))
						throw new Exception();//checa se o email não contem @.
				
					checkError = null;
					checkError = new CheckErrorWindow(emailList, dominioList);//inicializa a classe de checagem.
					checkError.setOnCompleteListener(new OnCompleteListener() {//listener da classe.
						@Override
						public void onComplete() {
							resultList = checkError.getValidEmail();
							updateResultadoList();
							btnStatistcs.setEnabled(true);
							btnExport.setEnabled(true);
						}
					});
				} catch (Exception e2) {
					e2.printStackTrace();
					JOptionPane.showMessageDialog(tableEmail, "Algum dos arquivos Não é compativel!", "Erro",
							JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});
		btnVerificarDominios.setBounds(670, 99, 164, 58);
		frmVerificadorDeDominios.getContentPane().add(btnVerificarDominios);

		btnStatistcs = new JButton("Estatisticas");
		btnStatistcs.setEnabled(false);
		btnStatistcs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new StatisticsWindow(checkError); //abre a janela de estatisticas passando o objeto de checagem.
			}
		});
		btnStatistcs.setBounds(670, 168, 164, 67);
		frmVerificadorDeDominios.getContentPane().add(btnStatistcs);
		
		btnExport = new JButton("Exportar Resultado");
		btnExport.setEnabled(false);
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkError.exportCvs();//exporta o arquivo.
			}
		});
		btnExport.setBounds(670, 246, 164, 33);
		frmVerificadorDeDominios.getContentPane().add(btnExport);
		
		btInserirDominio = new JButton("Inserir");
		btInserirDominio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (txtInserirDominio.getText().equals("")) {
						throw new Exception();
					}
					dominioList.add(txtInserirDominio.getText());
					txtInserirDominio.setText("");
					updateDominioList();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(tableDominios, "Erro ao adicionar, Tente novamente.",
							"Erro ao adicionar!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btInserirDominio.setBounds(292, 335, 89, 23);
		frmVerificadorDeDominios.getContentPane().add(btInserirDominio);
		
		//tabelas
		
		tableDominios = new JTable();
		tableDominios.setModel(new DefaultTableModel(new Object[][] { { null }, { null }, { null }, { null }, { null },
				{ null }, { null }, { null }, { null }, { null }, }, new String[] { "Dominios" }) {
			/**
					 * 
					 */
					private static final long serialVersionUID = -7557510312156075286L;
			boolean[] columnEditables = new boolean[] { false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tableDominios.getColumnModel().getColumn(0).setResizable(false);
		scrollPaneDominios.setViewportView(tableDominios);
		
		tableEmail = new JTable();
		tableEmail.setModel(new DefaultTableModel(new Object[][] { { null }, { null }, { null }, { null }, { null },
				{ null }, { null }, { null }, { null }, { null }, { null }, }, new String[] { "Email" }) {
			/**
					 * 
					 */
					private static final long serialVersionUID = 9131615632798182147L;
			boolean[] columnEditables = new boolean[] { false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tableEmail.getColumnModel().getColumn(0).setResizable(false);
		scrollPaneEmail.setViewportView(tableEmail);
		tableEmail.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		tableResultado = new JTable();
		tableResultado.setModel(new DefaultTableModel(new Object[][] { { null }, { null }, { null }, { null }, { null },
				{ null }, { null }, { null }, { null }, { null }, }, new String[] { "Resultado" }));
		scrollPane.setViewportView(tableResultado);
		
		//TextFields
		textFieldEmail = new JTextField();
		textFieldEmail.setBounds(139, 392, 549, 20);
		frmVerificadorDeDominios.getContentPane().add(textFieldEmail);
		textFieldEmail.setColumns(10);

		textFieldDominios = new JTextField();
		textFieldDominios.setBounds(139, 426, 549, 20);
		frmVerificadorDeDominios.getContentPane().add(textFieldDominios);
		textFieldDominios.setColumns(10);

		txtInserirEmail = new JTextField();
		txtInserirEmail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						if (txtInserirEmail.getText().equals("")) {
							throw new Exception();
						}
						emailList.add(txtInserirEmail.getText());
						txtInserirEmail.setText("");
						updateEmailList();
					} catch (Exception a) {
						JOptionPane.showMessageDialog(tableDominios, "Erro ao adicionar, Tente novamente.",
								"Erro ao adicionar!", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		txtInserirEmail.setBounds(24, 304, 203, 20);
		frmVerificadorDeDominios.getContentPane().add(txtInserirEmail);
		txtInserirEmail.setColumns(10);

		txtInserirDominio = new JTextField();
		txtInserirDominio.addKeyListener(new KeyAdapter() { 
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						if (txtInserirDominio.getText().equals("")) {
							throw new Exception();
						}
						dominioList.add(txtInserirDominio.getText());
						txtInserirDominio.setText("");
						updateDominioList();
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(tableDominios, "Erro ao adicionar, Tente novamente.",
								"Erro ao adicionar!", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		txtInserirDominio.setColumns(10);
		txtInserirDominio.setBounds(257, 304, 164, 20);
		frmVerificadorDeDominios.getContentPane().add(txtInserirDominio);
		//itens do menu
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 844, 21);
		frmVerificadorDeDominios.getContentPane().add(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmReset = new JMenuItem("Restart");
		mntmReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainWindow newWindow = new MainWindow();
				newWindow.frmVerificadorDeDominios.setVisible(true);
				frmVerificadorDeDominios.dispose();
			}
		});
		mnFile.add(mntmReset);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmVerificadorDeDominios.dispose();
			}
		});
		mnFile.add(mntmExit);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmInstructions = new JMenuItem("Instructions");
		mnHelp.add(mntmInstructions);

		//Labels
		JLabel lblComparadorDeEmails = new JLabel("Comparador de E-mails");
		lblComparadorDeEmails.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblComparadorDeEmails.setBounds(288, 32, 255, 36);
		frmVerificadorDeDominios.getContentPane().add(lblComparadorDeEmails);
		
		JLabel lblListaDeEmails = new JLabel("Lista de E-mails");
		lblListaDeEmails.setBounds(35, 395, 100, 14);
		frmVerificadorDeDominios.getContentPane().add(lblListaDeEmails);

		JLabel lblListaDeDominios = new JLabel("Lista de dominios");
		lblListaDeDominios.setBounds(35, 429, 100, 14);
		frmVerificadorDeDominios.getContentPane().add(lblListaDeDominios);
		
		JLabel lblInserirEmail = new JLabel("Inserir Email");
		lblInserirEmail.setBounds(86, 290, 72, 14);
		frmVerificadorDeDominios.getContentPane().add(lblInserirEmail);
		
		JLabel lblInserirDominio = new JLabel("Inserir Dominio");
		lblInserirDominio.setBounds(292, 290, 89, 14);
		frmVerificadorDeDominios.getContentPane().add(lblInserirDominio);
		
		frmVerificadorDeDominios.setResizable(false);
	}
	
	/**
	 * Obtem caminho do arquivo
	 * @return Caminho do arquivo.
	 */
	private String getPath() {
		FileDialog fd = new FileDialog(new JFrame());//abre a janela de seleção
		fd.setVisible(true);
		
		File[] f = fd.getFiles();//obtem e armazena o arquivo
		String path = "";
		if (f.length > 0) {
			path = fd.getFiles()[0].getAbsolutePath(); // obtem o caminho do arquivo
			System.out.println(path);
		}
		return path;
	}
	/**
	 * Lê o arquivo local e armazena ele em uma váriavel;
	 * @param path
	 * @return Arraylist com os emails.
	 */
	private ArrayList<String> readCvs(String path) {

		String csvFile = path; // caminho do arquivo
		BufferedReader br = null; //buffer de leitura
		String line = "";
		String cvsSplitBy = ","; //separador
		String[] email = null;
		ArrayList<String> result = new ArrayList<>();

		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				// separa os dados do arquivo.
				email = line.split(cvsSplitBy);
				result.add(email[0]);
			}
			for (int i = 0; i < result.size(); i++) {
				result.set(i, result.get(i).replaceAll("'", "")); //remove aspas simples
			}
			return result;// retorna Arraylist
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * Atualiza tabela Email
	 */
	private void updateEmailList() {
		DefaultTableModel modelo = (DefaultTableModel) tableEmail.getModel();// obtem o modelo da tabela
		modelo.setNumRows(0);// zera a tabela
		for (String s : emailList) {// percorre a lista de emails
			modelo.addRow(new Object[] { s });//adiciona nova linha a tabela
		}
	}
	
	/**
	 * Atualiza tabela Dominio
	 */
	private void updateDominioList() {
		DefaultTableModel modelo = (DefaultTableModel) tableDominios.getModel();// obtem o modelo da tabela
		modelo.setNumRows(0);// zera a tabela
		for (String s : dominioList) {// percorre a lista de dominios
			modelo.addRow(new Object[] { s });//adiciona nova linha a tabela
		}
	}
	/**
	 * Atualiza tabela Resultado
	 */
	private void updateResultadoList() {
		DefaultTableModel modelo = (DefaultTableModel) tableResultado.getModel();// obtem o modelo da tabela
		modelo.setNumRows(0); // zera a tabela
		for (String s : resultList) { // percorre a lista de resultados
			modelo.addRow(new Object[] { s }); //adiciona nova linha a tabela
		}
	}

}
