package admin.listener;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Application started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            System.out.println("Shutting down MySQL cleanup thread...");
            AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Exception e) {
            System.err.println("Error shutting down MySQL cleanup thread: " + e.getMessage());
        }
    }
}
