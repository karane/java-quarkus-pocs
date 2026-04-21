package org.karane.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.karane.model.Book;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

// @ApplicationScoped = one instance for the app lifetime, shared across all injection points.
// @RequestScoped would create a fresh empty store per HTTP request — all data lost between calls.
@ApplicationScoped
public class BookService {
    private final Map<Long, Book> store = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(1);

    public Book create(Book book) {
        long id = counter.getAndIncrement();
        book.setId(id);
        store.put(id, book);
        return book;
    }

    public List<Book> findAll() {
        return List.copyOf(store.values());
    }

    public List<Book> findByGenre(String genre) {
        return store.values().stream()
                .filter(b -> genre.equalsIgnoreCase(b.getGenre()))
                .collect(Collectors.toList());
    }

    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Optional<Book> update(Long id, Book updated) {
        if (!store.containsKey(id)) return Optional.empty();
        updated.setId(id);
        store.put(id, updated);
        return Optional.of(updated);
    }

    public boolean delete(Long id) {
        return store.remove(id) != null;
    }

    public void deleteAll() {
        store.clear();
        counter.set(1);
    }
}
