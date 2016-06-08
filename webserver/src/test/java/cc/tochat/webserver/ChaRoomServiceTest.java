package cc.tochat.webserver;

import java.util.Date;
import java.util.List;

import cc.tochat.webserver.model.Room;
import cc.tochat.webserver.model.message.ActionMessage;
import cc.tochat.webserver.model.message.FetchRoomListMessage;
import cc.tochat.webserver.service.ChatRoomService;
import cc.tochat.webserver.service.impl.ChatRoomServiceImpl;

public class ChaRoomServiceTest {
	ChatRoomService service = new ChatRoomServiceImpl();

	public void testActionMessageEncode() {

		FetchRoomListMessage message = new FetchRoomListMessage();
		message.setTimestamp(new Date().getTime());
		message.setCount(5);
		message.setFrom("Dailey");
		message.setOffset(0);
		List<Room> rooms = service.getRooms(message.getCount(), message.getOffset());
		message.setContent(rooms);

		System.out.println(message.encode());
	}

	public void testActionMessageDecode() {

		FetchRoomListMessage message = new FetchRoomListMessage();
		message.setTimestamp(new Date().getTime());
		message.setCount(5);
		message.setFrom("Dailey");
		message.setOffset(0);
		List<Room> rooms = service.getRooms(message.getCount(), message.getOffset());
		message.setContent(rooms);

		String encodeStr = message.encode();

		ActionMessage message2 = new FetchRoomListMessage();
		message2 = message2.decode(encodeStr);

		String encodeStr2 = message2.encode();
		System.out.println(encodeStr2);
		System.out.println(encodeStr.equals(encodeStr2));
	}

	public static void main(String[] args) {
		ChaRoomServiceTest test = new ChaRoomServiceTest();
		test.testActionMessageEncode();
		test.testActionMessageDecode();
	}
}
