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
import javax.swing.SwingWorker;

import com.comparator.verify.CheckError;

import javax.swing.JLabel;
import java.awt.Font;

public class CheckErrorWindow {

	private JFrame frame;
	private JProgressBar progressBar;
	private JProgressBar progressBarTotal;
	private CheckError error;

	/**
	 * Create the application.
	 */
	public CheckErrorWindow(ArrayList<String> email, ArrayList<String> dominios) {
		this.dominios = new ArrayList<>(dominios);
		this.email = new ArrayList<>(email);
		invalidEmail = new ArrayList<>(email);
		validEmail = new ArrayList<>();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		new Thread(new Runnable() {
			public void run() {

				frame = new JFrame();
				frame.setBounds(100, 100, 450, 300);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.getContentPane().setLayout(null);

				progressBar = new JProgressBar();
				progressBar.setValue(0);
				progressBar.setBounds(32, 216, 376, 14);
				progressBar.setMaximum(email.size());
				progressBar.setMinimum(0);

				frame.getContentPane().add(progressBar);

				JLabel lblComparandoEmailsCom = new JLabel("Comparando Emails com os dominios...");
				lblComparandoEmailsCom.setBounds(41, 168, 352, 14);
				frame.getContentPane().add(lblComparandoEmailsCom);

				JLabel lblExecutandoTarefa = new JLabel("Executando tarefa");
				lblExecutandoTarefa.setFont(new Font("Tahoma", Font.PLAIN, 20));
				lblExecutandoTarefa.setBounds(42, 42, 233, 25);
				frame.getContentPane().add(lblExecutandoTarefa);

				progressBarTotal = new JProgressBar();
				progressBarTotal.setBounds(32, 193, 376, 14);
				frame.getContentPane().add(progressBarTotal);
				frame.setVisible(true);
				ProgressBarPainter p = new ProgressBarPainter();
				p.jProgressBar1 = progressBar; // Fill in with the bar you want painted
				Thread t = new Thread(p);
				t.start();

				Worker w = new Worker();
				w.jProgressBar1 = progressBarTotal;
				Thread t2 = new Thread(w);
				t2.start();
			}
		}).start();

		worker.execute();
	}

	private static int counter;
	private int progress1;
	private int progress2;
	private int taskProgress;

	private ArrayList<String> dominios;
	private ArrayList<String> email;
	private ArrayList<String> invalidEmail;
	private ArrayList<String> validEmail;

	private int progress;

	SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

		@Override
		protected Void doInBackground() throws Exception {
			checkDominio();
			return null;
		}

	};

	public void checkDominio() {
		validEmail = new ArrayList<>();
		counter = 0;
		progress = 0;

		for (String s : email) {
			progress++;
			String emailDomain = s.substring(s.indexOf("@") + 1);
			for (int i = 0; i < dominios.size(); i++) {
				String domain = dominios.get(i);
				if (emailDomain.equals(domain)) {
					counter++;
					validEmail.add(s);
				}
			}
		}

		System.out.println(email.size() + " Tamanho da Lista");
		System.out.println(dominios.size() + " Tamanho Dominios");

		separateEmail(validEmail);
		System.out.println(counter + " Emails Válidos");
		// System.out.println(validEmail + " Válidos");
	}

	private void separateEmail(ArrayList<String> validList) {
		for (String s : validList) {
			invalidEmail.remove(s);
		}
		System.out.println(invalidEmail.size() + " Emails inválidos");

		onCompleteListener.onComplete();
	}

	public void exportCvs() {
		try {
			FileDialog fileDialog = new FileDialog(new Frame(), "Save", FileDialog.SAVE);
			fileDialog.setFilenameFilter(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".csv");
				}
			});
			fileDialog.setFile("Resultado.csv");
			fileDialog.setVisible(true);
			BufferedWriter br;
			if (fileDialog.getFile().endsWith(".csv")) {
				br = new BufferedWriter(new FileWriter(fileDialog.getDirectory() + fileDialog.getFile()));
			} else {
				fileDialog.setFile(fileDialog.getFile() + ".csv");
				br = new BufferedWriter(new FileWriter(fileDialog.getDirectory() + fileDialog.getFile()));
			}

			StringBuilder sb = new StringBuilder();

			// Append strings from array
			for (String element : validEmail) {
				sb.append("'");
				sb.append(element);
				sb.append("' \n");
			}

			br.write(sb.toString());
			br.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	class Worker implements Runnable {
		public JProgressBar jProgressBar1;

		public void run() {
			int x = 0;
			while (jProgressBar1.getValue() != jProgressBar1.getMaximum()) {
				try {
					// Do some work
					Thread.sleep(50);
					// Update bar
					jProgressBar1.setValue(x++);
				} catch (InterruptedException ex) {
					break;
				}
			}
		}
	}

	class ProgressBarPainter implements Runnable {
		public JProgressBar jProgressBar1;

		public void run() {
			while (true) {
				try {
					Thread.sleep(50);
					jProgressBar1.repaint();
				} catch (InterruptedException ex) {
					break;
				}
			}
		}
	}

	public int getCounter() {
		return counter;
	}

	public ArrayList<String> getInvalidEmail() {
		return invalidEmail;
	}

	public ArrayList<String> getDominios() {
		return dominios;
	}

	public ArrayList<String> getEmail() {
		return email;
	}

	public ArrayList<String> getValidEmail() {
		return validEmail;
	}

	public int getProgress() {
		return progress;
	}

	private OnCompleteListener onCompleteListener;

	public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
		this.onCompleteListener = onCompleteListener;
	}

	public interface OnCompleteListener {
		public void onComplete();
	}

	public CheckError getError() {
		return error;
	}

}
