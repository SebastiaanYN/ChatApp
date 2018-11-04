package com.sebastiaanyn.requesthandler;

class Utils {

    static String escapeHtml(String string) {
        return string
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
