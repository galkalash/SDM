package SDM.Utils;

import Engine.SDMInterface;
import Engine.SuperDuperMarketManager;
import User.UserManager;
import chat.ChatManager;

import javax.servlet.ServletContext;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";
    private static final String SDM_MANAGER_ATTRIBUTE_NAME = "SDMManager";

    private static final Object chatManagerLock = new Object();
    private static final Object userManagerLock = new Object();
    private static final Object sdmManagerLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static SDMInterface getSDMManager(ServletContext servletContext){
        synchronized (sdmManagerLock) {
            if (servletContext.getAttribute(SDM_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(SDM_MANAGER_ATTRIBUTE_NAME, new SuperDuperMarketManager(getUserManager(servletContext)));
            }
        }
        return (SDMInterface) servletContext.getAttribute(SDM_MANAGER_ATTRIBUTE_NAME);
    }

    public static ChatManager getChatManager(ServletContext servletContext) {
        synchronized (chatManagerLock) {
            if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
            }
        }
        return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
    }
}
