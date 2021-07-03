package ru.maximivanov.yourvocabular.models;

import java.util.ArrayList;

public class User {
    private static String fromLang = "en";
    private static String toLang = "ru";

    private static final ArrayList<Element> elements = new ArrayList<>();

    public static Element getElement(int index) {
        return elements.get(index);
    }

    public static void addElement(Element element) {
        elements.add(element);
    }

    public static String getFromLang() {
        return fromLang;
    }

    public static void setFromLang(String fromLang) {
        User.fromLang = fromLang;
    }

    public static String getToLang() {
        return toLang;
    }

    public static void setToLang(String toLang) {
        User.toLang = toLang;
    }
}
