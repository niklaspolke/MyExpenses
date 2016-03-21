package vu.de.npolke.myexpenses.servlets.util;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import vu.de.npolke.myexpenses.services.DAOFactory;
import vu.de.npolke.myexpenses.services.SystemDAO;

@WebListener
public class SessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		SystemDAO systemDAO = (SystemDAO) DAOFactory.getDAO(null);
		systemDAO.checkpoint();
	}
}
