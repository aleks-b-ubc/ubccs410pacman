import java.net.*;


public class Node {
int id;
String ip;
boolean coord=false;

public Node(){
	try {
		this.ip = InetAddress.getLocalHost().getHostAddress();
		this.id = (Integer.parseInt(removeChar(ip, '.')));
	} catch (UnknownHostException e) {
		System.out.println("cannot resolve ip");
		e.printStackTrace();
	}
}

public static String removeChar(String s, char c) {
	   String r = "";
	   for (int i = 0; i < s.length(); i ++) {
	      if (s.charAt(i) != c) r += s.charAt(i);
	      }
	   return r;
	}

}
