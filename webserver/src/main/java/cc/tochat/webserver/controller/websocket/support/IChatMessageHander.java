/**
 * 
 */
package cc.tochat.webserver.controller.websocket.support;

import cc.tochat.webserver.model.message.LoginMessage;
import cc.tochat.webserver.model.message.LogoutMessage;

/**
 * @author daile
 *
 */
public interface IChatMessageHander extends IMessageHander {
	
	public void processLogin(LoginMessage loginMessage);

	public void processLogout(LogoutMessage logoutMessage);
}
