/**
 * 
 */
package cc.tochat.webserver.controller.websocket.support;


/**
 * @author daile
 *
 */
public interface IChatMessageHander extends IMessageHander {

	public void processLogin();

	public void processLogout();

	public void processSessionUser();
}
