package edu.ucf.eel5780.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimLogger {
	
	private static final String LOGGER_NAME = "simulation";
	private static SimLogger singleton;
	
	public static SimLogger getInstance() {
		if(singleton == null) {
			singleton = new SimLogger();
		}
		return singleton;
	}
	
	private Logger logger;
	
	private SimLogger() {
		initializeFile();
	}

	private void initializeFile() {
		try {
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			//FileHandler fh = new FileHandler("log_" + sdf.format(new Date())+ ".log");
			FileHandler fh = new FileHandler("log.log");
			fh.setFormatter(new EventFormatter());
			logger = Logger.getLogger(LOGGER_NAME);
			logger.addHandler(fh);
			logger.setLevel(Level.FINE);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void severe(Exception ex) {
		info(ex.getMessage());
	}
	
	public void severe(String msg) {
		log(Level.SEVERE, msg);
	}
	
	public void warning(Exception ex) {
		info(ex.getMessage());
	}
	
	public void warning(String msg) {
		log(Level.WARNING, msg);
	}
	
	public void info(String msg) {
		log(Level.INFO, msg);
	}
	
	public void fine(String msg) {
		log(Level.FINE, msg);
	}

	public void finer(String msg) {
		log(Level.FINER, msg);
	}
	
	private void log(Level level, String msg) {
		logger.log(level, msg);
	}

}
