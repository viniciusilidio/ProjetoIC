
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Servidor implements Runnable {

	public static int PORTA = 12345;
	private static ServerSocket servidor;

	public Servidor() {
		System.out.println("Iniciando servidor ...");
	}

	public void executa() {
		Scanner s;

		try {
			servidor = new ServerSocket(PORTA);
			System.out.printf("Porta %d aberta\n", PORTA);
			while (true) {

				/*
				 * Espera um cliente se conectar ao servidor, cria um objeto
				 * para este cliente e lança uma thread para tratar as mensagens
				 * enviadas por ele
				 */

				Socket c = servidor.accept();
				s = new Scanner(c.getInputStream());
				
				Thread t = new Thread(this);
				t.start();
			}
		} catch (BindException e) {
			System.out.printf("Porta %d ocupada\n", PORTA);
		} catch (IOException e) {
			// Não faz nada caso a porta esteja sendo usada
		}
	}
	
	public void run () {
		
	}

	public static void main(String[] args) {
		Servidor server = new Servidor();
		server.executa();
	}
}