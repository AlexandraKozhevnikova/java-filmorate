package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.IdControl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class InMemoryStorage<T extends IdControl> implements Storage<T> {
    private final HashSet<T> itemList = new HashSet<>();
    private int currentItemId = 1;

    @Override
    public T add(T item) {
        item.setId(getIdForNewItem());
        itemList.add(item);
        return item;
    }

    @Override
    public T update(T newItem) {
        Optional<T> oldItem = itemList.stream()
                .filter(item -> item.getId() == newItem.getId())
                .findFirst();

        if (oldItem.isPresent()) {
            itemList.remove(oldItem.get());
        } else {
            throw new NoSuchElementException("Не найден элемент с id " + newItem.getId());
        }

        itemList.add(newItem);

        return itemList.stream()
                .filter(item -> item.getId() == newItem.getId())
                .findFirst().get();
    }

    @Override
    public List<T> getAllItems() {
        return new ArrayList<>(itemList);
    }

    @Override
    public T getItemById(int id) {
        Optional<T> targetItem = itemList.stream()
                .filter(it -> it.getId() == id)
                .findFirst();
        return targetItem.orElseThrow(() -> new NoSuchElementException("element with id = " + id + " not found"));
    }

    private int getIdForNewItem() {
        return currentItemId++;
    }
}
