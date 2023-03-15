package edu.skku.map.pa2;

import android.content.ClipData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Image{

    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<Item> items;

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String _String() {
        return "{" +
                "lastBuildDate='" + lastBuildDate + '\'' +
                ", total=" + total +
                ", start=" + start +
                ", display=" + display +
                '}';
    }

    public static class Item {
        private String title;
        private String link;
        private String thumbnail;
        private String sizeheight;
        private String sizewidth;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getSizeheight() {
            return sizeheight;
        }

        public void setSizeheight(String sizeheight) {
            this.sizeheight = sizeheight;
        }

        public String getSizewidth() {
            return sizewidth;
        }

        public void setSizewidth(String sizewidth) {
            this.sizewidth = sizewidth;
        }

        public String _String() {
            return "itmes{" +
                    "title='" + title + '\'' +
                    ", link='" + link + '\'' +
                    ", thumbnail='" + thumbnail + '\'' +
                    ", sizeheight='" + sizeheight + '\'' +
                    ", sizewidth='" + sizewidth + '\'' +
                    '}';
        }
    }

}
