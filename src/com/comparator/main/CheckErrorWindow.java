package com.comparator.main;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;

import javax.swing.JFrame;

import javax.swing.JProgressBar;
import javax.swing.JLabel;
import java.awt.Font;

public class CheckErrorWindow {

	private JFrame frmVerificando;
	private JProgressBar progressBar;
	private JProgressBar progressBarTotal;
	private JLabel lblTotal;
	private JLabel lblProgress;

	private int counter; // contadores de progresso.
	private int progress1;
	private int progress2;
	private int taskProgress;

	private Thread t;// Threads
	private Thread t2;
	private Thread t3;

	private ArrayList<String> dominios; // lista de dominios
	private ArrayList<String> email; // Emails
	private ArrayList<String> invalidEmail; // emails inválidos
	private ArrayList<String> validEmail; // emails válidos

	/**
	 * construtor da classe.
	 * 
	 * @param email
	 *            Emails a serem verificados.
	 * @param dominios
	 *            Dominios para comparação.
	 */
	public CheckErrorWindow(ArrayList<String> email, ArrayList<String> dominios) {
		this.dominios = new ArrayList<>(dominios);
		this.email = new ArrayList<>(email);
		invalidEmail = new ArrayList<>(email);
		validEmail = new ArrayList<>();
		initialize();
	}

	/**
	 * Desenha tela.
	 */
	private void initialize() {

		new Thread(new Runnable() {
			public void run() {
				//Configurações da tela
				frmVerificando = new JFrame();
				frmVerificando.setTitle("Verificando...");
				frmVerificando.setBounds(100, 100, 450, 227);
				frmVerificando.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frmVerificando.getContentPane().setLayout(null);
				frmVerificando.setAutoRequestFocus(true);
				frmVerificando.setAlwaysOnTop(true);
				frmVerificando.setResizable(false);

				progressBar = new JProgressBar();
				progressBar.setValue(0);
				progressBar.setBounds(32, 161, 376, 14);
				progressBar.setMaximum(email.size());
				progressBar.setMinimum(0);

				frmVerificando.getContentPane().add(progressBar);

				JLabel lblComparandoEmailsCom = new JLabel("Comparando Emails com os dominios...");
				lblComparandoEmailsCom.setBounds(32, 47, 352, 14);
				frmVerificando.getContentPane().add(lblComparandoEmailsCom);

				JLabel lblExecutandoTarefa = new JLabel("Executando tarefa");
				lblExecutandoTarefa.setFont(new Font("Tahoma", Font.PLAIN, 20));
				lblExecutandoTarefa.setBounds(32, 11, 233, 25);
				frmVerificando.getContentPane().add(lblExecutandoTarefa);

				progressBarTotal = new JProgressBar();
				progressBarTotal.setMaximum(2);
				progressBarTotal.setBounds(32, 111, 376, 14);
				frmVerificando.getContentPane().add(progressBarTotal);

				lblTotal = new JLabel("Progresso(0/0)");
				lblTotal.setBounds(32, 86, 175, 14);
				frmVerificando.getContentPane().add(lblTotal);

				lblProgress = new JLabel("Verificando(0/0)");
				lblProgress.setBounds(32, 136, 204, 14);
				frmVerificando.getContentPane().add(lblProgress);
				frmVerificando.setVisible(true);

				checkDominio();

				Worker w = new Worker();
				w.jProgressBar1 = progressBar; // progressbar a ser atualizado.
				w.max = email.size();// tamanho do progressbar.
				t2 = new Thread(w);// instância nova Thread Passando a Classe Worker como instancia.
				t2.start();// inicia a thread.

				setOnCompleteListener(new CheckErrorWindow.OnCompleteListener() {// quando a tarefa for completa...
					@Override
					public void onComplete() {
						t.interrupt();// parar todas as threads...
						t2.interrupt();
						t3.interrupt();
						close();// esconder janela.
					}
				});
			}
		}).start();
	}

	// #####################################Metodos####################################################
	/**
	 * Compara os emails com os dominios cedidos.
	 */
	public void checkDominio() {
		validEmail = new ArrayList<>(); // lista de emails válidos a serem preenchidos.
		counter = 0; // contador de emails válidos
		taskProgress = 0; // progresso da tarefa
		progress1 = 0;// progresso da verificação
		progressBarTotal.setValue(taskProgress);
		lblTotal.setText("Progresso (" + (taskProgress + 1) + "/2)");

		for (String s : email) {// varredura na lista de emails
			progress1++;// acrescenta no progresso da verificação

			progressBar.setMaximum(email.size());// Configura progressbar
			progressBar.setValue(progress1);
			lblProgress.setText("Verificando (" + progress1 + "/" + email.size() + ")");

			String emailDomain = s.substring(s.indexOf("@") + 1);// separa o email do dominio
			for (int i = 0; i < dominios.size(); i++) {// varredura na lista de dominios
				String domain = dominios.get(i);
				if (emailDomain.equals(domain)) {// compara o email com os dominios, se for validado...
					counter++;// acrescenta no contador
					validEmail.add(s);// adiciona na lista de emails válidos
				}
			}
		}

		Worker w = new Worker();// inicia Thread para o progressbar.
		w.jProgressBar1 = progressBar;// pasa o progressbar que será atualizado
		w.max = validEmail.size(); // tamanho do progressbar
		Thread t2 = new Thread(w); // instância nova Thread Passando a Classe Worker como instancia.
		t2.start();// inicia a thread.

		separateEmail(validEmail);// separar emails
	}

	/**
	 * Separa os emails válidos dos inválidos.
	 * 
	 * @param validList
	 *            Lista com emails válidos.
	 */
	private void separateEmail(ArrayList<String> validList) {
		taskProgress = 1;// contador de progresso
		progressBarTotal.setValue(taskProgress);// seta o progressbar
		for (String s : validList) {
			lblProgress.setText("Verificando (" + progress2 + "/" + validList.size() + ")");
			progress2++;
			invalidEmail.remove(s);// remove emails válidos e cia uma lista só com inválidos.
		}
		System.out.println(invalidEmail.size() + " Emails inválidos");
		taskProgress = 2;
		lblTotal.setText("Progresso (" + (taskProgress + 1) + "/2)");
		onCompleteListener.onComplete();// informa ao listener que a tarefa foi completa.
		frmVerificando.setVisible(false);// esconde a tela mas mantem a instância na memória.
	}

	/**
	 * Exporta o resultado da verificação.
	 */
	public void exportCvs() {
		try {
			FileDialog fileDialog = new FileDialog(new Frame(), "Save", FileDialog.SAVE);// Abre janela para selecionar
																							// o local a ser salvo.
			fileDialog.setFilenameFilter(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".csv");
				}
			});
			fileDialog.setFile("Resultado.csv");
			fileDialog.setVisible(true);

			BufferedWriter br; // Buffer de escrita.
			if (fileDialog.getFile().endsWith(".csv")) {// checa se o usuário alterou a extensão do arquivo.
				br = new 
						BufferedWriter(new FileWriter(fileDialog.getDirectory() 
										+ fileDialog.getFile()));// inicializa o buffer com o caminho escolhido.
			} else {
				fileDialog
				.setFile(fileDialog.getFile() + ".csv");// adiciona a extensão no final do arquivo caso tenha sido alterada.
				br = new BufferedWriter(new FileWriter(fileDialog.getDirectory() + fileDialog.getFile()));
			}

			StringBuilder sb = new StringBuilder();

			// Acrescenta strings do array ao stringbuilder.
			for (String s : validEmail) {
				sb.append("'");
				sb.append(s);
				sb.append("' \n");
			}

			br.write(sb.toString());// escreve o arquivo no caminho escolhido.
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ####################################Threads do Progressbar###############################
	/**
	 * Classe para atualizar o Progressbar, implementa metodos da classr
	 * {@link Runnable}.
	 * 
	 * @author Rafael
	 *
	 */
	class Worker implements Runnable {
		public JProgressBar jProgressBar1;
		public int max;

		public void run() {
			while (jProgressBar1.getValue() != max) {// loop enquanto o progressbar não atinge o máximo
				try {
					Thread.sleep(1);
					// Atualiza o ProgressBar
					if (taskProgress == 0) { // verifica estágio do proceso de verificação e atualiza o progressbar
												// correto.
						jProgressBar1.setValue(progress1);
					} else if (taskProgress == 1) {
						jProgressBar1.setValue(progress2);
					} else {
						break;
					}
				} catch (InterruptedException ex) {
					break;
				}
			}
		}
	}

	/**
	 * Fecha a janela e interrompe threads que podem estar em execução.
	 */
	public void close() {
		t.interrupt();
		t2.interrupt();
		t3.interrupt();
		progressBar = null;
		progressBarTotal = null;
		dominios = null;
		email = null;
		invalidEmail = null;
		validEmail = null;
		frmVerificando.dispose();
	}

	// ############################Getter and Setters####################################
	/**
	 * Obtem o contador de Emails válidos.
	 * @return Um inteiro com os emails válidos.
	 */
	public int getCounter() {
		return counter;
	}
	/**
	 * Lista de emails inválidos.
	 * @return um arraylist com os emails inválidos.
	 */
	public ArrayList<String> getInvalidEmail() {
		return invalidEmail;
	}

	/**
	 * lista de dominios.
	 * @return um arraylist com a lista de dominios.
	 */
	public ArrayList<String> getDominios() {
		return dominios;
	}
	/**
	 * Obtem lista de emails completa.
	 * @return arraylist com emails.
	 */
	public ArrayList<String> getEmail() {
		return email;
	}
	/**
	 * lista com emails validados.
	 * @return arraylist com emails válidos.
	 */
	public ArrayList<String> getValidEmail() {
		return validEmail;
	}

	// #####################Listener################################################
	private OnCompleteListener onCompleteListener;
	
	/**
	 * Listener da classe.
	 * @param onCompleteListener
	 */
	public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
		this.onCompleteListener = onCompleteListener; // listener da classe.
	}
	
	/**
	 * interface a ser inplementada toda vez que o setOnCompleteListener for usado.
	 * @author Rafael
	 *
	 */
	public interface OnCompleteListener {// interface a ser executada.
		public void onComplete();
	}
}
