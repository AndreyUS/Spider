package com.task.spider;


import org.apache.commons.io.FileUtils;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileManager {

    public static boolean createDir(String name) {
        name = name.replaceAll("\\?", "");
        File path = new File(name);
        return !path.exists() && path.mkdirs();
    }

    //Метод возвращает путь к папке пользователя. Здесь будет папка сайта
    public static String getUserDir() {
        File dir = new File(System.getProperty("user.dir"));
        return dir.toString();
    }

    //Метод возвращает путь к папке сайта.
    public static String getPathProject(Site s) {
        String fileSeparator = new File(System.getProperty("file.separator"))
                .toString();
        return getUserDir() + fileSeparator + s.getDirName() + fileSeparator;
    }

    //Метод для сохранения файла с url в ФС пользователя.
    public static void saveFile(String pathToSave, String url) throws IOException {
        File file = new File(pathToSave);
        if (file.exists()) {
            Message.fileIsExists(url);
        } else {
            FileUtils.copyURLToFile(new URL(url), file);
            Message.fileDone(url);
        }
    }

    //Разбиваем url на массив.
    public static String[] mySlit(String url) {
        String tmp = null;
        if (url.contains("://")) {
            int index = url.lastIndexOf("//");
            tmp = url.substring(index + 2);
        }
        assert tmp != null;
        return tmp.split("/");
    }

    //Определяем является ли ссылка файлом или папкой.
    // -1 ссылка содержит в себе имя файла, но это папка нужно зайти в нее.
    // 0 - это файл его нужно сохранить
    // 1 - ссылка на папку
    public static int isFile(String url) {
        if (url.contains(".")) {
            int length = url.length();
            int index = url.indexOf('.');
            int result = length - index;
            if (result > 6) {
                return -1;
            } else {
                return 0;
            }
        } else {
            return 1;
        }
    }

    //Строем путь для сохранения файла в ФС пользователя.
    public static String recursionCreatePath(String[] arr, String pathSite, int i, String fileSeparator) {
        if (i == 0) {
            return pathSite;
        }
        if (i == 1) {
            return pathSite + arr[1] + fileSeparator;
        }
        return recursionCreatePath(arr, pathSite, i - 1, fileSeparator) + arr[i] + fileSeparator;

    }

    //Проверяем можно ли ссылку использовать для установления соеденения.
    public static boolean canToConnect(String url) {
        if (url.contains(".")) {
            Pattern pattern = Pattern.compile("\\.(html|xhtml" +
                    "|wml|php|jsp" +
                    "|asp|aspx|xml)");
            Matcher matcher = pattern.matcher(url);
            return matcher.matches();
        }
        return true;
    }

    //Проверка url на корректность.
    public static boolean isUrlCorrect(String url) {
        if (url.contains("#")) {
            return false;
        }
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(url);
    }

    public static boolean equalsUrl(String url1, String url2) throws MalformedURLException {
        URL url_1 = new URL(url1);
        URL url_2 = new URL(url2);
        return url_2.getHost().equals(url_1.getHost());
    }

    //Проверяем уровень вложености ссылки. Если допустимый возвращаем true;
    public static boolean checkLevel(int max, String now) {
        if (!isUrlCorrect(now)) {
            return false;
        }
        String[] strArr = mySlit(now);
        int level = strArr.length - 1;
        if (strArr.length == 0) {
            return true;
        }
        int whatIs = isFile(now);
        if (whatIs == 1 || whatIs == -1) {
            return level <= max;
        }
        return (level - 1) <= max;
    }
}
