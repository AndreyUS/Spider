package com.task.spider;

import org.apache.commons.cli.Options;

public class Message {
    public static void errorUrl() {
        System.out.println("Url is not valid.");
    }

    public static void errorDephth() {
        System.out.println("Dephth only 0 - 10. Try again.");
    }

    public static void showHelp(Options options) {
        Command.printHelp(
                options,
                80,
                "Commands",
                "-- HELP --",
                3,
                5,
                true,
                System.out
        );
    }

    public static void unrecognizedOption() {
        System.out.println("Unrecognized Command. Call -h or --help");
    }

    public static void parseException() {
        System.out.println("Parse Exception. Call -h or --help");
    }

    public static void errorLogConfig() {
        System.out.println("Can`t read logging.properties. Please check file.");
    }

    public static void startDownload(String url) {
        System.out.println("Start download file: " + url);
    }

    public static void malformedUrl(String url) {
        System.out.println("Malformed URL: " + url);
    }

    public static void fileNotFound(String url) {
        System.out.println("File not found: " + url);
    }

    public static void ioException(String url) {
        System.out.println("IOException save file: " + url);
    }

    public static void fileIsExists(String url) {
        System.out.println("Error file is exists: " + url);
    }

    public static void fileDone(String url) {
        System.out.println("Done! File save: " + url);
    }
}
