/**
 * 
 */
package br.com.portscanner;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luciano.lima
 *
 */
public class CheckPort implements Runnable{

	String ip;
	int portStart, portEnd;
	List<Integer> openPorts;
	
	public CheckPort(String ip, int portStart, int portEnd) {
		super();
		this.ip = ip;
		this.portStart = portStart;
		this.portEnd = portEnd;
		openPorts = new ArrayList<>();
	}
	
	public void run() {
		for(int i = portStart; i < portEnd; i++) {
			Socket socket;
			try {
				socket = new Socket(ip, i);
				socket.close();
				openPorts.add(i);
			} catch (IOException e) {
			}
			
			if (i%20 == 0) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public List<Integer> getOpenPorts(){
		return openPorts;
	}
}
