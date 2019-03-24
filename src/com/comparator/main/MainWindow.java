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
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JProgressBar;

public class MainWindow {
	// DECLARAÇÃO DE
	// COMPONENTES****************************************************************************************
	public static JFrame frmVerificadorDeDominios;
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

	/**
	 * Launch the application.
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

	/**
	 * Create the application.
	 */
	public MainWindow() {
		emailList = new ArrayList<>();
		dominioList = new ArrayList<>();
		resultList = new ArrayList<>();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmVerificadorDeDominios = new JFrame();
		frmVerificadorDeDominios.getContentPane().setBackground(Color.WHITE);
		frmVerificadorDeDominios.setTitle("Verificador de Dominios");
		frmVerificadorDeDominios.setBounds(100, 100, 850, 500);
		frmVerificadorDeDominios.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmVerificadorDeDominios.getContentPane().setLayout(null);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(139, 369, 549, 14);
		frmVerificadorDeDominios.getContentPane().add(progressBar);

		Button btEmail = new Button("Escolher Arquivo");
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

		JButton btDominios = new JButton("Escolher Arquivo");
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

		JLabel lblListaDeEmails = new JLabel("Lista de E-mails");
		lblListaDeEmails.setBounds(35, 395, 100, 14);
		frmVerificadorDeDominios.getContentPane().add(lblListaDeEmails);

		JLabel lblListaDeDominios = new JLabel("Lista de dominios");
		lblListaDeDominios.setBounds(35, 429, 100, 14);
		frmVerificadorDeDominios.getContentPane().add(lblListaDeDominios);

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

		JLabel lblInserirEmail = new JLabel("Inserir Email");
		lblInserirEmail.setBounds(86, 290, 72, 14);
		frmVerificadorDeDominios.getContentPane().add(lblInserirEmail);

		JButton btnInserirEmail = new JButton("Inserir");
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

		JLabel lblInserirDominio = new JLabel("Inserir Dominio");
		lblInserirDominio.setBounds(292, 290, 89, 14);
		frmVerificadorDeDominios.getContentPane().add(lblInserirDominio);

		JButton btInserirDominio = new JButton("Inserir");
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

		JScrollPane scrollPaneEmail = new JScrollPane();
		scrollPaneEmail.setBounds(24, 98, 203, 181);
		frmVerificadorDeDominios.getContentPane().add(scrollPaneEmail);

		tableEmail = new JTable();
		tableEmail.setModel(new DefaultTableModel(new Object[][] { { null }, { null }, { null }, { null }, { null },
				{ null }, { null }, { null }, { null }, { null }, { null }, }, new String[] { "Email" }) {
			boolean[] columnEditables = new boolean[] { false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tableEmail.getColumnModel().getColumn(0).setResizable(false);
		scrollPaneEmail.setViewportView(tableEmail);
		tableEmail.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		JScrollPane scrollPaneDominios = new JScrollPane();
		scrollPaneDominios.setBounds(257, 98, 164, 181);
		frmVerificadorDeDominios.getContentPane().add(scrollPaneDominios);

		tableDominios = new JTable();
		tableDominios.setModel(new DefaultTableModel(new Object[][] { { null }, { null }, { null }, { null }, { null },
				{ null }, { null }, { null }, { null }, { null }, }, new String[] { "Dominios" }) {
			boolean[] columnEditables = new boolean[] { false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tableDominios.getColumnModel().getColumn(0).setResizable(false);
		scrollPaneDominios.setViewportView(tableDominios);

		JButton btnVerificarDominios = new JButton("Verificar Dominios");
		btnVerificarDominios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (emailList.size() <= 0 || dominioList.size() <= 0) {
						JOptionPane.showMessageDialog(tableDominios, "Lista vazia", "Erro",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					System.out.println(dominioList.get(0).toString());
					if (dominioList.get(0).contains("@"))
						throw new Exception();
					if (!emailList.get(0).contains("@"))
						throw new Exception();
					checkError = new CheckErrorWindow(emailList, dominioList);
					checkError.setOnCompleteListener(new OnCompleteListener() {
						@Override
						public void onComplete() {
							updateResultadoList();
							btnStatistcs.setEnabled(true);
							btnExport.setEnabled(true);							
						}
					});
					resultList = checkError.getValidEmail();

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
				new StatisticsWindow(checkError);
			}
		});
		btnStatistcs.setBounds(670, 168, 164, 67);
		frmVerificadorDeDominios.getContentPane().add(btnStatistcs);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(455, 98, 185, 181);
		frmVerificadorDeDominios.getContentPane().add(scrollPane);

		tableResultado = new JTable();
		tableResultado.setModel(new DefaultTableModel(new Object[][] { { null }, { null }, { null }, { null }, { null },
				{ null }, { null }, { null }, { null }, { null }, }, new String[] { "Resultado" }));
		scrollPane.setViewportView(tableResultado);

		btnExport = new JButton("Exportar Resultado");
		btnExport.setEnabled(false);
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkError.exportCvs();
			}
		});
		btnExport.setBounds(670, 246, 164, 33);
		frmVerificadorDeDominios.getContentPane().add(btnExport);

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

		JLabel lblComparadorDeEmails = new JLabel("Comparador de E-mails");
		lblComparadorDeEmails.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblComparadorDeEmails.setBounds(288, 32, 255, 36);
		frmVerificadorDeDominios.getContentPane().add(lblComparadorDeEmails);

		JLabel lblProgresso = new JLabel("Progresso");
		lblProgresso.setBounds(35, 369, 100, 14);
		frmVerificadorDeDominios.getContentPane().add(lblProgresso);
		frmVerificadorDeDominios.setResizable(false);
	}

	private String getPath() {
		FileDialog fd = new FileDialog(new JFrame());
		fd.setVisible(true);
		File[] f = fd.getFiles();
		String path = "";
		if (f.length > 0) {
			path = fd.getFiles()[0].getAbsolutePath();
			System.out.println(path);
		}
		return path;
	}

	private ArrayList<String> readCvs(String path) {

		String csvFile = path;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String[] email = null;
		ArrayList<String> result = new ArrayList<>();

		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				// use comma as separator
				email = line.split(cvsSplitBy);

				result.add(email[0]);
				// System.out.println("email " + email[0]);
			}
			for (int i = 0; i < result.size(); i++) {
				result.set(i, result.get(i).replaceAll("'", ""));
			}
			return result;
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
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

	private void updateEmailList() {
		DefaultTableModel modelo = (DefaultTableModel) tableEmail.getModel();
		modelo.setNumRows(0);
		for (String s : emailList) {
			modelo.addRow(new Object[] { s });
		}
	}

	private void updateDominioList() {
		DefaultTableModel modelo = (DefaultTableModel) tableDominios.getModel();
		modelo.setNumRows(0);
		for (String s : dominioList) {
			modelo.addRow(new Object[] { s });
		}
	}

	private void updateResultadoList() {
		DefaultTableModel modelo = (DefaultTableModel) tableResultado.getModel();
		modelo.setNumRows(0);
		for (String s : resultList) {
			modelo.addRow(new Object[] { s });
		}
	}

}
