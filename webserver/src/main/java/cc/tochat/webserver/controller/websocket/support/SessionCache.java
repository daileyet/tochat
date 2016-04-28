package cc.tochat.webserver.controller.websocket.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SessionCache<T extends IEndPointSession> {
	private final Map<String, T> allSessionCache; //session id <=> session
	private final Map<String, SessionGroup<T>> groupCache; // group id <=> group session

	public SessionCache() {
		allSessionCache = new ConcurrentHashMap<String, T>();
		groupCache = new ConcurrentHashMap<String, SessionGroup<T>>();
	}

	public void add(T session) {
		allSessionCache.put(session.getId(), session);
		SessionGroup<T> group = groupCache.get(session.getGroup());
		if (group == null) {
			group = new SessionGroup<T>();
			groupCache.put(session.getGroup(), group);
		}
		group.add(session);
	}

	public SessionGroup<T> getSessionGroup(ChatSession chatSession) {
		SessionGroup<T> group = groupCache.get(chatSession.getGroup());
		if (group == null) {
			group = new SessionGroup<T>();
		}
		return group;
	}

	public void remove(ChatSession chatSession) {
		getSessionGroup(chatSession).remove(chatSession);
		allSessionCache.remove(chatSession.getId());
	}

	public void removeGroup(ChatSession chatSession) {
		SessionGroup<T> group = getSessionGroup(chatSession);
		for (T t : group) {
			allSessionCache.remove(t.getId());
		}
		groupCache.remove(group.getId());
	}
}