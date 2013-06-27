package auxtestlib;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class that runs a command and captures its output.
 */
public class CommandRunner { // NOPMD (TooManyMethods)
	/**
	 * Class that represents the output of a command.
	 */
	public static class CommandOutput {
		/**
		 * Output generated by the command.
		 */
		public String output;

		/**
		 * Error messages generated.
		 */
		public String error;

		/**
		 * Output generated by the command (in bytes).
		 */
		public byte outputBytes[];

		/**
		 * Output written by the commandin the stderr (in bytes).
		 */
		public byte errorBytes[];

		/**
		 * Program's exit code.
		 */
		public int exitCode;

		/**
		 * Has the program timed out?
		 */
		public boolean timedOut;
	}

	/**
	 * Creates a new instance.
	 */
	public CommandRunner() {
		/*
		 * Nothing to do.
		 */
	}

	/**
	 * Runs a command and captures the output. This method will return
	 * immediately (the process keeps running in the background). The process
	 * can me monitored and accessed through the {@link ProcessInterface} class.
	 * 
	 * @param cmds the command and its arguments
	 * @param directory the directory where the command should bd executed
	 * @param limit execution time limit (in seconds)
	 * 
	 * @return an interface to control the process
	 * 
	 * @throws IOException failed to launch the process
	 */
	public ProcessInterface runCommandAsync(String[] cmds, File directory,
			int limit) throws IOException {
		if (cmds == null) {
			throw new IllegalArgumentException("cmds == null");
		}

		if (directory == null) {
			throw new IllegalArgumentException("directory == null");
		}

		if (limit <= 0) {
			throw new IllegalArgumentException("limit <= 0");
		}

		ProcessBuilder pb = new ProcessBuilder(cmds);
		pb.directory(directory);
		Process p = pb.start();
		return ProcessInterface.makeProcessInterface(p, limit);
	}

	/**
	 * This method is a shortcut for the
	 * {@link #runCommandAsync(String[], File, int)}. It will invoke the
	 * command, wait for it to run and returns the command's output.
	 * 
	 * @param cmds the command and its arguments
	 * @param directory the directory where the command should bd executed
	 * @param limit execution time limit (in seconds)
	 * 
	 * @return the commands output in the stdout
	 * 
	 * @throws IOException failed to launch the process
	 */
	public CommandOutput runCommand(String cmds[], File directory, int limit)
			throws IOException {
		ProcessInterface pi = runCommandAsync(cmds, directory, limit);

		/*
		 * Wait until the process dies.
		 */
		while (pi.isRunning()) {
			try {
				Thread.sleep(ProcessInterface.PROCESS_POLLING);
			} catch (InterruptedException e) {
				/*
				 * We'll ignore this.
				 */
			}
		}

		return pi.getOutput();
	}

	/**
	 * Thread that keeps reading an input stream and saves the output. The
	 * thread will automatically stop when the stream is closed.
	 */
	static class Capturer extends Thread {
		/**
		 * The input stream.
		 */
		private final InputStream inputStream;

		/**
		 * Buffer where the text is kept.
		 */
		private final StringBuffer result;

		/**
		 * Data read from the stream (without text conversion).
		 */
		private final ByteArrayOutputStream resultBytes;

		/**
		 * Creates and starts the thread.
		 * 
		 * @param is the stream to read
		 */
		Capturer(InputStream is) {
			assert is != null;

			inputStream = is;
			result = new StringBuffer();
			resultBytes = new ByteArrayOutputStream();
			start();
		}

		@Override
		public void run() {
			int read;
			try {
				while ((read = inputStream.read()) != -1) {
					synchronized (this) {
						resultBytes.write(read);
						result.append((char) read);
					}
				}
			} catch (IOException e) {
				/*
				 * We'll ignore I/O exceptions.
				 */
			}
		}

		/**
		 * Obtains a copy of the captured text.
		 * 
		 * @return the text
		 */
		synchronized String getText() {
			return result.toString();
		}

		/**
		 * Obtains a copy of the captured bytes.
		 * 
		 * @return the captured bytes
		 */
		synchronized byte[] getBytes() {
			return resultBytes.toByteArray();
		}
	}

	/**
	 * Interface provided to access the process while it is running.
	 */
	public static class ProcessInterface {
		/**
		 * Polling interval to check whether the process has finished (in
		 * milliseconds).
		 */
		private static final int PROCESS_POLLING = 200;

		/**
		 * The process itself.
		 */
		private final Process process;

		/**
		 * Is the process still running?
		 */
		private boolean running;

		/**
		 * What was the exit code for the process?
		 */
		private int exitCode;

		/**
		 * Stdout capturer.
		 */
		private final Capturer out;

		/**
		 * Stderr capturer.
		 */
		private final Capturer err;

		/**
		 * Has the program timed out?
		 */
		private boolean timedOut;

		/**
		 * Listeners of the process interface.
		 */
		private final List<ProcessInterfaceListener> listeners;

		/**
		 * Creates a new interface for the process. These objects are linked to
		 * their respective runners.
		 * 
		 * @param process the process that is running
		 * @param limit the time limit to run the program (in seconds).
		 * 
		 * @return the process interface
		 */
		private static ProcessInterface makeProcessInterface(Process process,
				int limit) {
			return new ProcessInterface(process, limit);
		}

		/**
		 * Creates a new interface for the process. These objects are linked to
		 * their respective runners.
		 * 
		 * @param process the process that is running
		 * @param limit the time limit to run the program (in seconds).
		 */
		private ProcessInterface(Process process, int limit) {
			assert process != null;

			this.process = process;
			running = true;
			exitCode = 0;
			out = new Capturer(process.getInputStream());
			err = new Capturer(process.getErrorStream());
			listeners = new ArrayList<>();
			timedOut = false;

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					synchronized (ProcessInterface.this) {
						updateState();
						if (!running) {
							cancel();
						}
					}
				}
			}, PROCESS_POLLING, PROCESS_POLLING);

			final TimerTask timeoutTask = new TimerTask() {
				@Override
				public void run() {
					synchronized (this) {
						if (killProcess()) {
							timedOut = true;
						}
					}
				}
			};

			addProcessInterfaceListener(new ProcessInterfaceListener() {
				@Override
				public void processFinished(ProcessInterface process) {
					timeoutTask.cancel();
				}
			});

			timer.schedule(timeoutTask, limit * 1000);
		}

		/**
		 * Adds a listener to the process interface.
		 * 
		 * @param listener the listener
		 */
		public synchronized void addProcessInterfaceListener(
				ProcessInterfaceListener listener) {
			if (listener == null) {
				throw new IllegalArgumentException("listener == null");
			}

			listeners.add(listener);
		}

		/**
		 * Removes a listener from the process interface.
		 * 
		 * @param listener the listener
		 */
		public synchronized void removeProcessInterfaceListener(
				ProcessInterfaceListener listener) {
			if (listener == null) {
				throw new IllegalArgumentException("listener == null");
			}

			listeners.remove(listener);
		}

		/**
		 * Updates the state of the process. Since the
		 * <code>java.lang.Process</code> class doesn't provide any way of
		 * observing its state, we must probe regularly. This method should be
		 * called for that purpose.
		 */
		private synchronized void updateState() {
			if (!running) {
				return;
			}

			try {
				exitCode = process.exitValue();
				running = false;
				for (ProcessInterfaceListener l : new ArrayList<>(
						listeners)) {
					l.processFinished(this);
				}
			} catch (IllegalThreadStateException e) {
				/*
				 * Process is still running.
				 */
			}
		}

		/**
		 * Requests the process to be killed (if it is running).
		 * 
		 * @return was the process killed (<code>true</code>) or was it already
		 * dead (<code>false</code>)?
		 */
		public synchronized boolean killProcess() {
			if (!running) {
				return false;
			}

			process.destroy();
			while (true) {
				try {
					process.exitValue();
					break;
				} catch (IllegalThreadStateException e) {
					/*
					 * The process is still running.
					 */
				}
			}

			updateState();
			assert !running;
			return true;
		}

		/**
		 * Determines whether the process is still running.
		 * 
		 * @return is the process running?
		 */
		public synchronized boolean isRunning() {
			return running;
		}

		/**
		 * Obtains the output of the command. Can only be invoked if the process
		 * has been stopped.
		 * 
		 * @return the output
		 * 
		 * @throws IllegalStateException if the process is still running
		 */
		public synchronized CommandOutput getOutput() {
			if (running) {
				throw new IllegalStateException("Process still running.");
			}

			CommandOutput co = new CommandOutput();

			co.exitCode = exitCode;
			co.output = out.getText();
			co.error = err.getText();
			co.outputBytes = out.getBytes();
			co.errorBytes = err.getBytes();
			co.timedOut = timedOut;
			return co;
		}

		/**
		 * Obtains the text currently written to the stdout by the process.
		 * 
		 * @return the text written
		 */
		public synchronized String getOutputText() {
			return out.getText();
		}

		/**
		 * Obtains the text currently written to the stderr by the process.
		 * 
		 * @return the text written
		 */
		public synchronized String getErrorText() {
			return err.getText();
		}
	}

	/**
	 * Interface implemented by classes that observe a process interface.
	 */
	interface ProcessInterfaceListener {
		/**
		 * The process has finished.
		 * 
		 * @param process the process
		 */
		void processFinished(ProcessInterface process);
	}
}