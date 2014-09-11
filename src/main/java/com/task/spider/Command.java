package com.task.spider;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.io.OutputStream;
import java.io.PrintWriter;

public class Command {
    //Выводим помощь
    public static void printHelp (
            final Options options,
            final int printedRowWidth,
            final String header,
            final String footer,
            final int spacesBeforeOption,
            final int spacesBeforeOptionDescription,
            final boolean displayUsage,
            final OutputStream out) {
        final String commandLineSyntax = "Spider.jar";
        final PrintWriter writer = new PrintWriter(out);
        final HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(writer, printedRowWidth, commandLineSyntax,
                                     header, options, spacesBeforeOption, 
                                     spacesBeforeOptionDescription, footer, 
                                     displayUsage);
        writer.flush();
    }

    //Создает объекты для комманд и возравщает их в объекте Options.
    public static Options getOption() {
        Options spiderOption = new Options();

        Option dephth = new Option("d", "dephth", true, "Dephth 0-10");
        dephth.setArgs(1);
        dephth.setArgName("-d N <site_url> or --dephth=N <site_url>");

        spiderOption.addOption(dephth);

        Option help = new Option("h", "help", false, "Help");
        dephth.setArgs(0);
        dephth.setArgName("-h or --help");

        spiderOption.addOption(help);

        return spiderOption;
    }

    //Метод обрабатывает комманду --dephth=N возвращает число N
    public static Integer getDephth(String s) {
        int i = s.lastIndexOf('=');
        s = s.substring(i + 1);
        return Integer.parseInt(s);
    }


}
