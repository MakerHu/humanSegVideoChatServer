package humanSegServer;
//
//import java.net.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.io.*;
//
//public class MultiTCPServer {
//	static ServerSocket serverSocket = null;
//	static int count = 1; // ����ͬ�Ŀͻ���ͳһ���
//	private int clientID;
//
//	private List<ObjectOutput> objectOutputList = new ArrayList<>();
//
//	// ���췽��
//	public MultiTCPServer() {
//		new Thread(new ServerRunnable()).start();
//	}
//
//	class ClientRunnable implements Runnable {
//
//		Socket clientSocket = null; // ���߳���صĿͻ��׽���
//
//		private ClientRunnable(Socket clientSoc) {
//			clientID = count++;
//			System.out.println("�ͻ��� " + clientID + " �Ѿ�������");
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
//				// ��ȡSocket��Ӧ��������
//				objectInput = new ObjectInputStream(clientSocket.getInputStream());
//				Object o;
//				while ((o = objectInput.readObject()) != null) {
////					if (o instanceof String) {
////
////					}else {
////						System.out.println("�յ���Ϣ[from client " + clientID + "]: " + o);
////					}
//
////					System.out.println("�յ���Ϣ[from client " + clientID + "]: " + (String)o);
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
//				// ���������˿ڵķ������׽���
//				serverSocket = new ServerSocket(5050);
//				// Socket s = serverSocket.accept();
//				while (true) {
//					// �ȴ�����
//					Socket s = serverSocket.accept();
//					// һ����client�����ϣ������̣߳�֮��ȴ���һ������
//					Thread serverThread = new Thread(new ClientRunnable(s));
//			        serverThread.start();
//
//					// new MultiTCPServer(serverSocket.accept()).start();
//				}
//			} catch (IOException e) {
//				System.out.println("������ֹ��" + e);
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

	static int count = 1; // ����ͬ�Ŀͻ���ͳһ���
	private int clientID;
	private List<ObjectOutput> objectOutputList = new ArrayList<>();


	public MultiTCPServer() {
		new Thread(new serverRunnable()).start();
	}

	// ʹ���̴߳���ͻ�����
	class clientRunnable implements Runnable {
		Socket clientSocket = null; // ���߳���صĿͻ��׽���
		// ������ͳ�ƿͻ�������
		private int thisClientID;

		public clientRunnable(Socket clientSoc) {
			clientID = count++;
			this.thisClientID=clientID;
			System.out.println("�ͻ��� " + clientID + " �Ѿ�������");
			clientSocket = clientSoc;
		}

		public void run() {
			// TODO �Զ����ɵķ������

			ObjectOutput objectOutput = null;
			ObjectInput objectInput = null;

			try {

				objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());

				objectOutputList.add(objectOutput);
				System.out.println(objectOutputList.size());

				// ��ȡSocket��Ӧ��������
				objectInput = new ObjectInputStream(clientSocket.getInputStream());
				Object o;
				while ((o = objectInput.readObject()) != null){
//					System.out.println("�յ���Ϣ[from client " + thisClientID + "]: " + o.toString());
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
				// TODO �Զ����ɵ� catch ��
				System.out.println("Client " + thisClientID + " logout 2 ");
				objectOutputList.remove(objectOutput);
//				e.printStackTrace();
			}
		}
	}

	/*
	 * ���������������߳�
	 */
	class serverRunnable implements Runnable {

		public void run() {
			// TODO �Զ����ɵķ������
			System.out.println("Server started...");

			try {
				// ���������˿ڵķ������׽���
				serverSocket = new ServerSocket(5050);
				// Socket s = serverSocket.accept();
				while (true) {
					// �ȴ�����
					Socket s = serverSocket.accept();
					// һ����client�����ϣ������̣߳�֮��ȴ���һ������
					Thread thread = new Thread(new clientRunnable(s));
					thread.start();

					// new MultiTCPServer(serverSocket.accept()).start();
				}
			} catch (IOException e) {
				System.out.println("������ֹ��" + e);
				// serverSocket.close();
				System.exit(1);
			}
		}

	}

}

