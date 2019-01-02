package com.example.zhepingjiang.navigation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerRequestHandler {

    public static String serverRequestHelper(String url_str) {
        StringBuilder result = new StringBuilder();
        url_str = url_str.replaceAll(" ", "%20");

        try {
            URL url = new URL(url_str);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public static String getAllFood() {
        String url_str = "http://ece496puts.ddns.net:59496/raw_sql/use putsDB;select std_name from purchase_history;";
        String result_str = serverRequestHelper(url_str);

        //Extract from the html text
        result_str = result_str.replace("<!DOCTYPE html    PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"     \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en-US\" xml:lang=\"en-US\"><head><title>sql response</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" /></head><body><h1>",
                "");
        result_str = result_str.replace("</h1>", "");


        return result_str;
    }

    public static String getAlltemp() {
        try {

            String url_str = "http://ece496puts.ddns.net:59496/raw_sql/use putsDB;select std_name from purchase_history;";
            url_str = url_str.replaceAll(" ", "%20");
            System.out.println("url_str is " + url_str);


            URL url = new URL(url_str);


            StringBuilder result = new StringBuilder();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            System.out.println("\n>>>>Start printing result>>>>\n");
            String result_str = result.toString();

            result_str = result_str.replace("<!DOCTYPE html    PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"     \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en-US\" xml:lang=\"en-US\"><head><title>sql response</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" /></head><body><h1>",
                    "");
            result_str = result_str.replace("</h1>", "");
            System.out.println(result_str);


            System.out.println("\n>>>>End printing result>>>>\n");
            return result_str;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
