package fr.upem.rmirest.bilmancamp;

import utils.*;
import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;

public class CommandLineParser {

	@Parameter
	private List<String> parameters = new ArrayList<>();

	@Parameter(names = { "-verbose" }, description = "Verbose mode actives server talking while performing tasks")
	private Integer verbose = 1;

	@Parameter(names = "-reset", description = "Reset database values")
	private boolean reset = false;

	@Parameter(names = "-debug", description = "Debug mode")
	private boolean debug = false;

	@Parameter(names = "-host", description = "specify an existing registry")
	private String host = Constants.LocalHost;

	@Parameter(names = "-port", description = "specify the port of the registry")
	private int port = Constants.DEFAULT_PORT;


	public Integer getVerbose() {
		return verbose;
	}

	public boolean isReset() {
		return reset;
	}

	public boolean isDebug() {
		return debug;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

}
