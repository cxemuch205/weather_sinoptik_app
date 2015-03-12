package ua.maker.sinopticua.models;

import java.io.Serializable;

/**
 * Created by Daniil on 11-Mar-15.
 */
public class PageHTML implements Serializable {

    public long idDB;
    public long timeInsertMillis;
    public String html;

    public PageHTML(){}

    public PageHTML(String html) {
        this.html = html;
    }
}
