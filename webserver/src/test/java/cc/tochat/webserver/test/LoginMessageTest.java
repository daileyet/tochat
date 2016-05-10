package cc.tochat.webserver.test;

import cc.tochat.webserver.model.User;
import cc.tochat.webserver.model.message.LoginMessage;

public class LoginMessageTest {

	public static void main(String[] args) {
		LoginMessage msg = new LoginMessage();
		User user = new User();
		user.setId("U1");
		user.setName("Dailey1");
		user.setEmail("dailey1@oracle.com");
		user.setPassword("1234");
		user.setAlias("d1");
		msg.setFrom("U1");
		msg.setRoom("1");
		msg.setUser(user);
		System.out.println(msg.encode());

		LoginMessage msg2 = (LoginMessage) msg.decode(msg.encode());

		System.out.println(msg2);
		System.out.println(msg2.getUser());
	}
}
