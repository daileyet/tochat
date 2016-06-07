package cc.tochat.webserver.dao;

import java.util.List;

import cc.tochat.webserver.model.Room;

public interface ChannelDao {
	public List<Room> list(int count, Long offset);
}
