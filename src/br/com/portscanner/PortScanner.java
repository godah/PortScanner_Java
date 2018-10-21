/**
 * 
 */
package br.com.portscanner;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author luciano.lima
 *
 */
public class PortScanner {
	
	//portas conhecidas
	private static final int startSystemPorts = 0;
	private static final int endSystemPorts = 1023;
	
	//portas registradas
	private static final int startUserPorts = 1024;
	private static final int endUserPorts = 49151;
	
	//portas dinamicas
	private static final int startDynamicPorts = 49152;
	private static final int endDynamicPorts = 65535;
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Digite o numero do ip: ");
		String ip = scanner.nextLine();
		scanner.close();
		
		ThreadPoolExecutor poll = new ThreadPoolExecutor(5, 10, 1, TimeUnit.HOURS, new ArrayBlockingQueue<Runnable>(10));
		
		CheckPort systemPortScanner = new CheckPort(ip, startSystemPorts, endSystemPorts);
		CheckPort userPortScanner = new CheckPort(ip, startUserPorts, endUserPorts);
		CheckPort dynamicPortScanner = new CheckPort(ip, startDynamicPorts, endDynamicPorts);


		poll.submit(systemPortScanner);
		poll.submit(userPortScanner);
		poll.submit(dynamicPortScanner);
		poll.shutdown();
		
		try {
			char[] wait = {'|','/','-', '\\'};
			int i = 0;
			while(!poll.awaitTermination(1, TimeUnit.SECONDS)) {
				if(System.getProperty("os.name").equalsIgnoreCase("Windows")) {
					Runtime.getRuntime().exec("cls");
				} else {
					System.out.print("\033[H\033[2J");
				}
				System.out.println(wait[i++] + " Escaneando portas do ip: " + ip);
				if (i == 4)
					i = 0;
			}
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
				
		if(!systemPortScanner.getOpenPorts().isEmpty())
			System.out.println("### PORTAS CONHECIDAS ###");
		for (Integer porta : systemPortScanner.getOpenPorts()) {
			System.out.println("Porta ["+porta+"] aberta.");
		}

		if(!userPortScanner.getOpenPorts().isEmpty())
			System.out.println("### PORTAS REGISTRADAS ###");
		for (Integer porta : userPortScanner.getOpenPorts()) {
			System.out.println("Porta ["+porta+"] aberta.");
		}
		
		if(!dynamicPortScanner.getOpenPorts().isEmpty())
			System.out.println("### PORTAS DINAMICAS ###");
		for (Integer porta : dynamicPortScanner.getOpenPorts()) {
			System.out.println("Porta ["+porta+"] aberta.");
		}
	}

}
