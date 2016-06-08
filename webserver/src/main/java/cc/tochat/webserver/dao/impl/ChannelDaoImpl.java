package cc.tochat.webserver.dao.impl;

import java.util.List;

import cc.tochat.webserver.dao.ChannelDao;
import cc.tochat.webserver.model.Room;

import com.openthinks.libs.sql.dhibernate.Session;
import com.openthinks.libs.sql.dhibernate.support.query.Query;
import com.openthinks.libs.sql.dhibernate.support.query.impl.DirectFilter;

public class ChannelDaoImpl extends DhibernateHelper implements ChannelDao {

	@Override
	public List<Room> list(int count, Long offset) {
		Session session = getSession();
		Query<Room> query = session.createQuery(Room.class);
		query.addFilter(new DirectFilter().filter(" limit ?,? ").params(new Object[] { offset, count }));
		return query.execute();
	}
}
