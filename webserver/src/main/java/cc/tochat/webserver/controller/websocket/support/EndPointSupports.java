package cc.tochat.webserver.controller.websocket.support;

import com.openthinks.easyweb.context.WebContexts;

public final class EndPointSupports {

	public final static <E, F extends IEndPointSession, T extends IEndPointSupported<E, F>> T lookup(Class<T> clzz) {
		return WebContexts.get().lookup(clzz);
	}

}
