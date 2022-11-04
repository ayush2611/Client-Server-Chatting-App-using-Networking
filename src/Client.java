import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.dnd.Autoscroll;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class Client extends JFrame {

	Socket socket;
	BufferedReader br;
	PrintWriter out;

	
	//Declare Component
	private JLabel heading = new JLabel("Client Area");
	private JTextArea messageArea = new JTextArea();
	private JTextField messageInput= new JTextField();
	private Font font = new Font("Roboto",Font.PLAIN,20); 
	
	
	public Client() {
		try {
			socket = new Socket("127.0.0.1", 7777);
			System.out.println("Connection Done");
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());

		
			createGUI();
			handleEvents();
			startReading();
//			startWriting();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private void handleEvents() {
		messageInput.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==10) {
//					System.out.println("You've pressed enter button");
					String contentToSend= messageInput.getText();
					messageArea.append("Me :"+contentToSend+"\n");
					out.println(contentToSend);
					out.flush();
					messageInput.setText("");
					messageInput.requestFocus();
				}
				
			}
		});
		
	}
	
	private void createGUI() {
		
		//gui code
		this.setTitle("Client Messanger");
		this.setSize(400,600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//coding for component
		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
		
//		heading.setIcon(new ImageIcon());
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		messageInput.setHorizontalAlignment(SwingConstants.CENTER);
		messageArea.setEditable(false);
		
		//frame ka layout set krenge
		this.setLayout(new BorderLayout());
		
		//adding the components to frame
		this.add(heading,BorderLayout.NORTH);
		JScrollPane jScrollPane= new JScrollPane(messageArea);
		jScrollPane.setAutoscrolls(true);
//		this.add(messageArea,BorderLayout.CENTER);
		this.add(jScrollPane, BorderLayout.CENTER);
		this.add(messageInput,BorderLayout.SOUTH);
		this.setVisible(true);
	}

	public void startReading() {
		// data read karke deta rhega
		// lambda function
		Runnable r1 = () -> {
			System.out.println("Reader started..");
			while (true) {
				try {
					String msg = br.readLine();
					if (msg.equals("exit")) {
						System.out.println("Server terminated the chat");
						JOptionPane.showMessageDialog(this,"Server Terminated the chat");
						messageInput.setEnabled(false);
						socket.close();
						break;
					}
//					System.out.println("Server:" + msg);
					messageArea.append("Server:" + msg+"\n");
				} catch (Exception e) {
					System.exit(0); 
				}

			}

		};
		new Thread(r1).start();
	}

	public void startWriting() {
		// thread data user lega and send krega client tak
		Runnable r2 = () -> {
			System.out.println("Writer started..");
			while (true) {
				try {
					BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
					String content = br1.readLine();
					out.println(content);
					out.flush();

				} catch (Exception e) {
					System.exit(0); 
				}
			}

		};
		new Thread(r2).start();
	}

	public static void main(String[] args) {
		System.out.println("This is client...");
		new Client();
	}

}
