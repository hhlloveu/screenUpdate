package com.wiwj.screen.update.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class CommandUtil {
    public static boolean execute(String commandStr) {
        Log.log("commandStr = " + commandStr);
        try {
            Runtime.getRuntime().exec(commandStr);
            return true;
        } catch (IOException e) {
            Log.log("Command.execute:" + e.getMessage());
            return false;
        }
    }

    public static String run(String commandStr) {
        Runtime runtime = Runtime.getRuntime();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    runtime.exec(commandStr).getInputStream(), Charset.forName("GBK")));
            String line = null;
            StringBuffer b = new StringBuffer();
            while ((line = br.readLine()) != null) {
                b.append(line + "<br/>");
            }
            return b.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
