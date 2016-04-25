package cc.tochat.webserver.test;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

public class PEchoServerAppConfig implements ServerApplicationConfig {

	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> arg0) {
		return arg0;
	}

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> arg0) {
		Set<ServerEndpointConfig> configs = new HashSet<ServerEndpointConfig>();
		ServerEndpointConfig sec = ServerEndpointConfig.Builder.create(PEchoServer.class, "/pecho").build();
		configs.add(sec);
		return configs;
	}
}
