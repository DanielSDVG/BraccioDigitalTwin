package utils;

import pubsub.PubService;

import java.io.PrintStream;

/**
 * @author Daniel Pérez - University of Málaga
 * A collection of utility methods for printing text to the console.
 */
@SuppressWarnings("unused")
public class DTLogger {

    /**
     * Prints a message to the standard output.
     * @param msg The message to print.
     */
    public static void info(String msg) {
        log(System.out, "DT-INFO", msg);
    }

    /**
     * Prints a warning message to the standard error output.
     * @param msg The message to print.
     */
    public static void warn(String msg) {
        log(System.err, "DT-WARN", msg);
    }

    /**
     * Prints an error message to the standard error output.
     * @param msg The message to print.
     */
    public static void error(String msg) {
        log(System.err, "DT-ERR", msg);
    }

    /**
     * Prints an error message to the standard error output.
     * @param msg The message to print.
     */
    public static void error(String msg, Exception exception) {
        log(System.err, "DT-ERR", msg, exception);
    }

    /**
     * Prints a message to the standard output.
     * @param tag A tag to prepend to the message.
     * @param msg The message to print.
     */
    public static void info(String tag, String msg) {
        log(System.out, "DT-INFO:" + tag, msg);
    }

    /**
     * Prints a warning message to the standard error output.
     * @param tag A tag to prepend to the message.
     * @param msg The message to print.
     */
    public static void warn(String tag, String msg) {
        log(System.err, "DT-WARN:" + tag, msg);
    }

    /**
     * Prints a message to the standard output.
     * @param service A PubService whose channel name to use as a tag to be prepended to the message.
     * @param msg The message to print.
     */
    public static void info(PubService service, String msg) {
        log(System.out, service.getChannel(), msg);
    }

    private static void log(PrintStream stream, String tag, String msg) {
        log(stream, tag, msg, null);
    }
    private static synchronized void log(PrintStream stream, String tag, String msg, Exception exception) {
        stream.println("[" + tag + "] " + msg);
        if (exception != null) {
            exception.printStackTrace();
        }
    }

}
