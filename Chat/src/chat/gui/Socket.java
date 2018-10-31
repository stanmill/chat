/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.gui;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author stanleypena
 */
public class Socket {

    	private InetAddress myAddress;
	private int port;
	private DatagramSocket socket = null;
	private ConcurrentLinkedQueue<DatagramPacket> messageQueue = new ConcurrentLinkedQueue<DatagramPacket>();
	private ChatScreen obj;
        
        
        public Socket(int port, ChatScreen obj) {
            
		this.port = port;
                this.obj = obj;
		try {
			this.myAddress = InetAddress.getLocalHost();
			this.socket = new DatagramSocket(port, this.myAddress);
		} catch (SocketException | UnknownHostException e) {
			System.exit(-1);
		}
		
		Thread receiveThread = new Thread(
				new Runnable() {
					public void run() {
						receiveThread();
					}
				});
		receiveThread.setName("Receive Thread For Port = " + this.port);
		receiveThread.start();
	}
	
	
	public void receiveThread() {
		
		do {
			byte[] inBuffer = new byte[1024];
			for ( int i = 0 ; i < inBuffer.length ; i++ ) {
				inBuffer[i] = ' ';
			}

			DatagramPacket inPacket = new DatagramPacket(inBuffer, inBuffer.length);
			
			try {
				// this is a blocking call
				socket.receive(inPacket);
			} catch(IOException e) {
				System.exit(-1);
			}
			
			String message = new String(inPacket.getData());
			System.out.println("ReceiveThread - Message on port = " + this.port + 
					" message = " + message + "\n" +
					" From IP = " + inPacket.getAddress() + 
					" From Port = " + inPacket.getPort());
			messageQueue.add(inPacket);
                        obj.getChatTextField().append("them: "+message +"\n");
                        
		} while(true);
	}
	
	
	public DatagramPacket receive() {
		return messageQueue.poll();
	}
	
	
	public void send(String s, InetAddress destinationIP, int destinationPort) {
		
		byte[] outBuffer;
		
		try {
			outBuffer = s.getBytes();
			DatagramPacket outPacket = new DatagramPacket(outBuffer, s.length());
			outPacket.setAddress(destinationIP);
			outPacket.setPort(destinationPort);
			socket.send(outPacket);
		} catch (IOException e) {
			System.exit(-1);
		}
	}
}
