package kickerstats.interfaces;

import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import kickerstats.domain.CouchdbConfig;

@WebServlet(urlPatterns = "/init", loadOnStartup = 1)
public class InitializeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private CouchdbConfig config;
	@Inject
	private StatsUpdaterBatch statsUpdaterBatch;
	private ScheduledExecutorService scheduler;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		setDbConfig(servletConfig.getServletContext());
		startUpdatingStats();
	}

	@Override
	public void destroy() {
		scheduler.shutdownNow();
	}

	protected void setDbConfig(ServletContext servletContext) {
		System.out.println("CONFIG IST: " + config);
		System.out.println("==> CONTEXT IST: " + servletContext);
		System.out.println("==> SERVLET CONTEXT IST: " + servletContext);
		Enumeration<String> enumeration = servletContext
				.getInitParameterNames();
		while (enumeration.hasMoreElements()) {
			System.out.println("==> ENUM IST: " + enumeration.nextElement());
		}

		config.setUser(servletContext.getInitParameter("dbuser"));
		config.setPassword(servletContext.getInitParameter("dbpassword"));
		config.setUrl(servletContext.getInitParameter("dburl"));
		config.setDbname(servletContext.getInitParameter("dbname"));
		System.out.println("CONFIG IST nach dem laden: " + config);
	}

	protected void startUpdatingStats() {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(statsUpdaterBatch, 0, 1, TimeUnit.DAYS);
	}

	private static class StatsUpdaterBatch implements Runnable {

		@Inject
		private StatsUpdater statsUpdater;

		@Override
		public void run() {
			statsUpdater.updateStats();
		}

	}

}
