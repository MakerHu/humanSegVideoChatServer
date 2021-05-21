package humanSegServer;
//
//import java.net.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.io.*;
//
//public class MultiTCPServer {
//	static ServerSocket serverSocket = null;
//	static int count = 1; // 给不同的客户端统一编号
//	private int clientID;
//
//	private List<ObjectOutput> objectOutputList = new ArrayList<>();
//
//	// 构造方法
//	public MultiTCPServer() {
//		new Thread(new ServerRunnable()).start();
//	}
//
//	class ClientRunnable implements Runnable {
//
//		Socket clientSocket = null; // 与线程相关的客户套接字
//
//		private ClientRunnable(Socket clientSoc) {
//			clientID = count++;
//			System.out.println("客户端 " + clientID + " 已经连接上");
//			clientSocket = clientSoc;
//		}
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			try {
//				ObjectOutput objectOutput = null;
//				ObjectInput objectInput = null;
//				objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
//
//				objectOutputList.add(objectOutput);
//				System.out.println(objectOutputList.size());
//
//				// 获取Socket对应的输入流
//				objectInput = new ObjectInputStream(clientSocket.getInputStream());
//				Object o;
//				while ((o = objectInput.readObject()) != null) {
////					if (o instanceof String) {
////
////					}else {
////						System.out.println("收到信息[from client " + clientID + "]: " + o);
////					}
//
////					System.out.println("收到信息[from client " + clientID + "]: " + (String)o);
////					objectOutput.writeObject("ECHO From Server: " + (String)o);
//
//					for(ObjectOutput oo : objectOutputList) {
//						if(!objectOutput.equals(oo)) {
//							oo.writeObject(o);
//						}
//					}
//
//				}
//
//				System.out.println("BYE, client " + clientID + " ! ");
//				objectOutput.close();
//				objectInput.close();
//				clientSocket.close();
//			} catch (IOException e) {
//				System.out.println("Client " + clientID + " Exception ! ");
//				e.printStackTrace();
//				// System.exit(1);
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
//
//	}
//
//
//	class ServerRunnable implements Runnable{
//
//		@Override
//		public void run() {
//			System.out.println("Server started...");
//
//			try {
//				// 创建侦听端口的服务器套接字
//				serverSocket = new ServerSocket(5050);
//				// Socket s = serverSocket.accept();
//				while (true) {
//					// 等待连接
//					Socket s = serverSocket.accept();
//					// 一旦有client连接上，启动线程，之后等待下一个连接
//					Thread serverThread = new Thread(new ClientRunnable(s));
//			        serverThread.start();
//
//					// new MultiTCPServer(serverSocket.accept()).start();
//				}
//			} catch (IOException e) {
//				System.out.println("服务终止：" + e);
//				// serverSocket.close();
//				System.exit(1);
//			}
//
//		}
//	}
//}









import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class MultiTCPServer {
	static ServerSocket serverSocket = null;

	static int count = 1; // 给不同的客户端统一编号
	private int clientID;
	private List<ObjectOutput> objectOutputList = new ArrayList<>();


	public MultiTCPServer() {
		new Thread(new serverRunnable()).start();
	}

	// 使用线程处理客户请求
	class clientRunnable implements Runnable {
		Socket clientSocket = null; // 与线程相关的客户套接字
		// 构造器统计客户连接数
		private int thisClientID;

		public clientRunnable(Socket clientSoc) {
			clientID = count++;
			this.thisClientID=clientID;
			System.out.println("客户端 " + clientID + " 已经连接上");
			clientSocket = clientSoc;
		}

		public void run() {
			// TODO 自动生成的方法存根

			ObjectOutput objectOutput = null;
			ObjectInput objectInput = null;

			try {

				objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());

				objectOutputList.add(objectOutput);
				System.out.println(objectOutputList.size());

				// 获取Socket对应的输入流
				objectInput = new ObjectInputStream(clientSocket.getInputStream());
				Object o;
				while ((o = objectInput.readObject()) != null){
//					System.out.println("收到信息[from client " + thisClientID + "]: " + o.toString());
					for (ObjectOutput oo : objectOutputList) {
						if (!objectOutput.equals(oo)) {
							oo.writeObject((byte[])o);
						}
					}
				}

				objectOutputList.remove(objectOutput);

				System.out.println("BYE, client " + thisClientID + " ! ");

				clientSocket.close();
			} catch (IOException e) {
				System.out.println("Client " + thisClientID + " logout 1 ");
				objectOutputList.remove(objectOutput);
//				e.printStackTrace();
				// System.exit(1);
			} catch (ClassNotFoundException e) {
				// TODO 自动生成的 catch 块
				System.out.println("Client " + thisClientID + " logout 2 ");
				objectOutputList.remove(objectOutput);
//				e.printStackTrace();
			}
		}
	}

	/*
	 * 服务器接收连接线程
	 */
	class serverRunnable implements Runnable {

		public void run() {
			// TODO 自动生成的方法存根
			System.out.println("Server started...");

			try {
				// 创建侦听端口的服务器套接字
				serverSocket = new ServerSocket(5050);
				// Socket s = serverSocket.accept();
				while (true) {
					// 等待连接
					Socket s = serverSocket.accept();
					// 一旦有client连接上，启动线程，之后等待下一个连接
					Thread thread = new Thread(new clientRunnable(s));
					thread.start();

					// new MultiTCPServer(serverSocket.accept()).start();
				}
			} catch (IOException e) {
				System.out.println("服务终止：" + e);
				// serverSocket.close();
				System.exit(1);
			}
		}

	}

}

