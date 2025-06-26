package com.example;

public class Main {
    public static void main(String... args) {
        addCustomList();
    }

    public static void addCustomList() {
        CustomList<Object> list = new CustomList<>();
        list.add("Hello");
        list.add("World");
        list.add("!");

        System.out.printf("%s %s%s%n", list.get(0), list.get(1), list.get(2));
    }
}