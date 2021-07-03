package ru.maximivanov.yourvocabular.models;

public class Element {
//    private final String fromLang;
//    private final String toLang;
    private final String fromText;
    private final String toText;

    public Element(String from, String to) {
        fromText = from;
        toText = to;
//        fromLang = User.getFromLang();
//        toLang = User.getToLang();
    }

    public String getFromText() {
        return fromText;
    }

    public String getToText() {
        return toText;
    }

//    public String getFromLang() {
//        return fromLang;
//    }
//
//    public String getToLang() {
//        return toLang;
//    }
}