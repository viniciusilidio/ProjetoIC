
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente implements Runnable {

	private Socket cliente;
	private String host;
	private PrintStream output;
	private String nickname;


	public Socket getCliente() {
		return cliente;
	}

	public String getNickname() {
		return nickname;
	}

	public Cliente(String host, String nickname) {
		this.host = host;
		this.nickname = nickname;

		try {
			cliente = new Socket(this.host, Servidor.PORTA);
			output = new PrintStream(cliente.getOutputStream());
			Thread tc = new Thread(this);
			tc.start();

		} catch (UnknownHostException e) {
			System.out.println("Servidor não existe");
		} catch (IOException e) {
			// Não faz nada caso o servidor não exista
		}

	}

	public void enviarMensagens(String nickname, String msg) {
		if (output == null)
			System.out.println("Servidor desconectado");
		else
			output.println("PROTOCOLO");
	}

	@Override
	public void run() {
		// Envia o protocolo para o servidor criar a visualização
	}
	
	public static void main(String[] args) {
		Cliente client = new Cliente("127.0.0.1", "BOT1");
		Thread tc = new Thread(client);
		tc.start();
		
		/*String[] command = { "bash", "-c",
				"raspistill -w 500 -h 500 -q 100 -t 1 -n -o imagem" + j };
		ProcessBuilder probuilder = new ProcessBuilder(command);

		probuilder.directory(new File("//home//pi//Desktop//"));

		Process process = probuilder.start();

		// Read out dir output
		/*
		 * InputStream is = process.getInputStream(); 
		 * InputStreamReader isr = new InputStreamReader(is); BufferedReader br = new
		 * BufferedReader(isr); String line;
		 * System.out.printf("Output of running %s is:\n",
		 * Arrays.toString(command)); while ((line = br.readLine()) != null)
		 * { System.out.println(line); }
		 

		// Wait to get exit value
		try {
			int exitValue = process.waitFor();
			System.out.println("\n\nExit Value is " + exitValue);
			String origem = "//home//pi//Desktop//imagem" + j;
			String destino = "//home//pi//Desktop//imagem" + j
					+ "Processada.jpg";
			picture(origem, destino);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
}