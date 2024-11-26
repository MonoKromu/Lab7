package ru.mono;
import lombok.Data;
import java.util.List;

@Data
public class Visitor {
    private String name;
    private String surname;
    private String phone;
    private boolean subscribed;
    private List<Book> favoriteBooks;
}
