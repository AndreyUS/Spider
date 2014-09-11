package com.task.spider;

import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class Main {
    private static Logger log = Logger.getLogger(Spider.class.getName());

    public static void main(String[] args) {
        Options options = Command.getOption();
        try {
            //Подключаем логирование
            FileInputStream fis = new FileInputStream("logging.properties");
            LogManager.getLogManager().readConfiguration(fis);

            CommandLineParser cmdLinePosixParser = new PosixParser();
            CommandLine commandLine = cmdLinePosixParser.parse(options, args);
            //Проверяем введеные данные
            if (args.length > 0) {
                if (commandLine.hasOption("d")) {
                    if (args[0].equals("-d")) {
                        Integer dephth = Integer.parseInt(args[1]);
                        if (FileManager.isUrlCorrect(args[2])) {
                            log.info("Start work: " + args[2] + " Dephth = " + dephth);
                            new Spider().startWork(args[2], dephth);
                        } else {
                            Message.errorUrl();
                        }
                    } else {
                        Integer dephth = Command.getDephth(args[0]);
                        if (dephth >= 0 && dephth <= 10) {
                            if (FileManager.isUrlCorrect(args[1])) {
                                log.info("Start work: " + args[1] + " Dephth = " + dephth);
                                new Spider().startWork(args[1], dephth);
                            } else {
                                Message.errorUrl();
                            }
                        } else {
                            Message.errorDephth();
                        }
                    }
                } else if (commandLine.hasOption("h")) {
                    Message.showHelp(options);
                } else if (FileManager.isUrlCorrect(args[0])) {
                    log.info("Start work: " + args[0]);
                    new Spider().startWork(args[0]);
                } else {
                    Message.errorUrl();
                }
            } else {
                Message.showHelp(options);
            }
        } catch (UnrecognizedOptionException e) {
            Message.unrecognizedOption();
        } catch (IOException e) {
            Message.errorLogConfig();
        } catch (ParseException e) {
            Message.parseException();
        }
    }
}