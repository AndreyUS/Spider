package com.task.spider;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

public class Site {
    private String url;
    private Set<String> links = new TreeSet<String>();
    private Set<String> files = new TreeSet<String>();
    private String dirName;
    private int dephth;
    private boolean flag;

    Site(String s) {
        this.url = s;
        this.setDirName(s);
        this.addLinks(s);
    }

    Site(String s, int i) {
        this.url = s;
        this.setDirName(s);
        this.addLinks(s);
        this.dephth = i;
        this.flag = true;
    }

    public String getUrl() {
        return url;
    }

    public boolean addLinks(String url) {
        return links.add(url);
    }

    public void setDirName(String url) {
        this.dirName = new File(url).getName();
    }

    public String getDirName() {
        return dirName;
    }

    public int getDephth() {
        return dephth;
    }

    public boolean getFlag() {
        return flag;
    }

    public boolean addFiles(String url) {
        return files.add(url);
    }
}