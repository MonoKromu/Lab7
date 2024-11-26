package ru.mono;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();
        List<Visitor> visitors;
        Type visitorList = new TypeToken<List<Visitor>>(){}.getType();
        try(FileReader reader = new FileReader("src/books.json")){
            visitors = gson.fromJson(reader, visitorList);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        long vCount = visitors.stream()
                .count();
        System.out.println("Visitor count: "+vCount+"\nVisitors:");
        visitors.stream()
                .forEach(e -> System.out.println(e.getName()+" "+e.getSurname()));

        long fbCount = visitors.stream()
                .flatMap(e -> e.getFavoriteBooks()
                .stream())
                .distinct()
                .count();
        System.out.println("\nFavorite books count: "+fbCount+"\nFavorite books:");
        visitors.stream()
                .flatMap(e -> e.getFavoriteBooks()
                .stream())
                .distinct()
                .forEach(e -> System.out.println(e.getName()));

        System.out.println("\nBooks sorted by publishing year:");
        visitors.stream()
                .flatMap(e -> e.getFavoriteBooks()
                .stream())
                .distinct()
                .sorted(new Comparator<Book>() {
                    @Override
                    public int compare(Book o1, Book o2) {
                        return Integer.compare(o1.getPublishingYear(), o2.getPublishingYear());
                    }
                })
                .forEach(e -> System.out.println(e.getPublishingYear()+" - "+e.getName()));

        System.out.print("\nAre there books by Jane Austen on favorite books list? ");
        boolean jane = visitors.stream()
                .flatMap(e -> e.getFavoriteBooks()
                .stream())
                .anyMatch(b -> b.getAuthor().equals("Jane Austen"));
        System.out.println(jane);

        long maxBookCount = visitors.stream()
                .map(Visitor::getFavoriteBooks)
                .map(List::size)
                .max(Integer::compare)
                .get();
        System.out.println("\nMaximum favorite books count: "+maxBookCount);

        double averageBookCount = (double) visitors.stream()
                .map(Visitor::getFavoriteBooks)
                .map(List::size)
                .mapToInt(Integer::intValue)
                .sum() /
                visitors.stream()
                .count();
        ArrayList<Message> messages = new ArrayList<>();
        visitors.stream()
                .filter(Visitor::isSubscribed)
                .forEach(e -> {
                    int books = e.getFavoriteBooks().size();
                    String text = books < averageBookCount ? "read more" :
                            (books == averageBookCount ? "fine" : "you are a bookworm");
                    Message message = new Message();
                    message.setText(text);
                    message.setNumber(e.getPhone());
                    messages.add(message);
                });
        System.out.println("\nMessages:");
        messages.stream().forEach(System.out::println);
    }
}