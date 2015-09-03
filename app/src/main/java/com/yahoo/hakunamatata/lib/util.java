package com.yahoo.hakunamatata.lib;

import android.content.Context;
import android.media.MediaPlayer;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.yahoo.hakunamatata.R;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jonaswu on 2015/8/30.
 */
public class util {
    public static void startMyAudioFile(Context context) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.laugh);
        mediaPlayer.start();
    }

    public static Map<String, List<String>> splitQuery(URL url) throws UnsupportedEncodingException {
        final Map<String, List<String>> query_pairs = new LinkedHashMap<String, List<String>>();
        final String[] pairs = url.getQuery().split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!query_pairs.containsKey(key)) {
                query_pairs.put(key, new LinkedList<String>());
            }
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            query_pairs.get(key).add(value);
        }
        return query_pairs;
    }

    public static String expandUrl(String url) {
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url); //url is youtube url for which you want to extract the id.
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }


    public static String displayTimeToTweet(Date date) {
        return new SimpleDateFormat("hh:mm a dd MMM yy").format(date);
    }

    public static String getBestTimeDiff(Date targetTime) {
        Date currentTime = new Date();
        String display;
        long day = TimeUnit.MILLISECONDS.toHours(currentTime.getTime() - targetTime.getTime());
        long hour = TimeUnit.MILLISECONDS.toHours(currentTime.getTime() - targetTime.getTime());
        long minute = TimeUnit.MILLISECONDS.toMinutes(currentTime.getTime() - targetTime.getTime());
        long seconds = TimeUnit.MILLISECONDS.toSeconds(currentTime.getTime() - targetTime.getTime());
        display = String.valueOf(day) + "d";
        if (hour < 24) {
            display = String.valueOf(hour) + "h";
        }
        if (minute < 60) {
            display = String.valueOf(minute) + "m";
        }
        if (seconds < 60) {
            display = String.valueOf(seconds) + "s";
        }
        return display;
    }

    public static Date getDateFromString(String date) throws ParseException {
        // String date example: "2015-09-02T11:41:30+0000"
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSSS", Locale.ENGLISH);
        Date result = df.parse(date);
        return result;
    }

    public static void showToast(Context ctx, String message) {
        startMyAudioFile(ctx);
        Style style = Style.getStyle(Style.GREEN, SuperToast.Animations.FLYIN);
        style.textColor = ctx.getResources().getColor(R.color.brown);
        SuperToast.create(ctx, message, SuperToast.Duration.LONG,
                Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
    }
}
