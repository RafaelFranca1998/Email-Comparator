package com.comparator.verify;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;

import com.comparator.main.CheckErrorWindow;

public class CheckError {

	private static int counter;
	private int progress;

	private ArrayList<String> dominios;
	private ArrayList<String> email;
	private ArrayList<String> invalidEmail;
	private ArrayList<String> validEmail;

	public CheckError(ArrayList<String> email, ArrayList<String> dominios) {
		this.dominios = new ArrayList<>(dominios);
		this.email = new ArrayList<>(email);
		invalidEmail = new ArrayList<>(email);
		validEmail = new ArrayList<>();
	}

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
}
