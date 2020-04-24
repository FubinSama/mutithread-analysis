package com.wfb.mutithreadanalysis.util;

import java.util.ResourceBundle;

public class Paramters {
    public static String CALFUZZER_PATH;

    static {
        ResourceBundle config = ResourceBundle.getBundle("config");
        CALFUZZER_PATH = config.getString("calfuzzerPath");
    }
}
