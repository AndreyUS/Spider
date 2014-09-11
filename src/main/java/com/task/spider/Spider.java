package com.task.spider;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.logging.Logger;


public class Spider {
    private static Logger log = Logger.getLogger(Spider.class.getName());

    private static Document getDoc(String url) throws IOException {
        return Jsoup
                .connect(url)
                .userAgent("Mozilla/5.0 (X11; Linux x86_64) "
                        + "AppleWebKit/537.36 (KHTML, like Gecko) "
                        + "Chrome/36.0.1985.125 Safari/537.36")
                .get();

    }

    //Метод предназначен для парсинга ссылок страницы.
    private static boolean parseLink(String url, Site s) {
        Document doc = null;
        try {
            //Проверяем введет ли ссылка на другой рессурс.
            if (!FileManager.equalsUrl(url, s.getUrl())) {
                log.info("It is url from other site" + url);
                return false;
            }
            //Получаем страницу
            doc = getDoc(url);
            log.info("Connection OK! : " + url);
        } catch (UnsupportedMimeTypeException e) {
            log.info("Unsupported Mime " + e.toString());
        } catch (HttpStatusException e) {
            log.info("Error: " + e.getStatusCode() + " " + url);
        } catch (IllegalArgumentException e) {
            log.info("Illegal Argument Exception " + url);
        } catch (SocketTimeoutException e) {
            log.info("Connect timed out" + url);
        } catch (Exception e) {
            log.info("Error. Maybe url is empty? " + url.isEmpty());
        }
        if (doc == null) {
            log.info("Doc is null");
            return false;
        }
        //Получаем полный список ссылок со страницы
        Elements links = doc.select("a[href]");
        //Сохраняем каждую ссылку с полученой страницы
        for (Element link : links) {
            //Проверяем нужно ли соблюдать уровень вложености
            if (s.getFlag()) {
                if (!FileManager.checkLevel(s.getDephth(), link.attr("abs:href"))) {
                    continue;
                }
            }
            //Добавление в Set что бы избежать повторной скачки файлов.
            if (s.addFiles(link.attr("abs:href"))) {
                downloadFile(link.absUrl("href"), s);
            }
        }
        //Получаем весь список ссылок с тегом src
        Elements imgs = doc.select("[src]");
        //Скачиваем изображения
        for (Element img : imgs) {
            if (img.tagName().equals("img")) {
                if (s.addFiles(img.attr("abs:src"))) {
                    downloadFile(img.attr("abs:src"), s);
                }
            }
        }
        //Заходим на каталог ниже
        for (Element link : links) {
            // Добавляем ссылку в Set что бы избежать повторного конекта и парсинга страницы.
            if (s.addLinks(link.absUrl("href"))) {
                //Проверяем нужно ли соблюдать уровень вложености. Если да то проверить уровни.
                if (s.getFlag()) {
                    if (!FileManager.checkLevel(s.getDephth(), link.attr("abs:href"))) {
                        continue;
                    }
                }
                //Проверяем ссылку. Если это ссылка на файл пропускаем его.
                if (FileManager.isFile(link.absUrl("href")) == 0
                        && !FileManager.canToConnect(link.absUrl("href"))) {
                    continue;
                }
                //Входим на уровень ниже и начинаем парсить ссылки со старници.
                parseLink(link.absUrl("href"), s);
            }
        }
        return true;
    }

    private static boolean downloadFile(String url, Site s) {

        // Проверяем верная ли ссылка для сохранения файла
        if (!FileManager.isUrlCorrect(url)) {
            return false;
        }
        //Удаляем запрещеный символ для названия папок в файловой системе.
        String url1 = url.replaceAll("\\?", "");
        String[] splitPath = FileManager.mySlit(url1);
        //Проверяем ссылка введет на этот рессурс или нет.
        try {
            if (FileManager.equalsUrl(url, s.getUrl())) {
                String path = FileManager.recursionCreatePath(splitPath,
                        FileManager.getPathProject(s),
                        splitPath.length - 1,
                        new File(System.getProperty("file.separator"))
                                .toString());
                //Проверяем это ссылка на главный каталог
                if (splitPath.length > 1) {
                    //Проверяем на что введет ссылка
                    switch (FileManager.isFile(splitPath[splitPath.length - 1])) {
                        case 0:
                            break;
                        case -1:
                        case 1:
                            path = path + "index.html";
                            break;
                    }

                } else {
                    path = path + "index.html";
                }
                // Сохраняем файл
                    Message.startDownload(url);
                    FileManager.saveFile(path, url);
            } else {
                log.info("File is not from this site: " + url);
                return false;
            }
        } catch (MalformedURLException e) {
            Message.malformedUrl(url);
        } catch (FileNotFoundException e) {
            Message.fileNotFound(url);
        } catch (IOException e) {
            Message.ioException(url);
        }
        return true;
    }

    //Метод создает объект скачиваемого сайта
    public void startWork(String url) {
        this.startWork(url, null);
    }

    //Метод создает объект скачиваемого сайта, главный каталог + уровень вложености.
    public void startWork(String url, Integer dephth) {
        Site site;
        try {
            if (dephth == null) {
                site = new Site(getDoc(url).location());
            } else {
                site = new Site(getDoc(url).location(), dephth);
            }
            FileManager.createDir(FileManager.getPathProject(site));
            downloadFile(site.getUrl(), site);
            parseLink(url, site);
        } catch (Exception e) {
            Message.errorUrl();
            log.info("Connection error: " + url);
        }
    }
}
