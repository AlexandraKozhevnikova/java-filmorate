package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.IdControl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class InMemoryStorage<T extends IdControl> {
    private final HashSet<T> itemList = new HashSet<>();
    private int currentItemId = 1;

    public int add(T item) {
        int id = getIdForNewItem();
        item.setId(id);
        itemList.add(item);
        return item.getId();
    }

    public void update(T newItem) {
        Optional<T> oldItem = itemList.stream()
                .filter(item -> item.getId() == newItem.getId())
                .findFirst();

        if (oldItem.isPresent()) {
            itemList.remove(oldItem.get());
        } else {
            throw new NoSuchElementException("Не найден элемент с id " + newItem.getId());
        }

        itemList.add(newItem);
    }

    public List<T> getAllItems() {
        return new ArrayList<>(itemList);
    }

    public Optional<T> getItemById(int id) {
        Optional<T> targetItem = itemList.stream()
                .filter(it -> it.getId() == id)
                .findFirst();
        return targetItem;
    }

    private int getIdForNewItem() {
        return currentItemId++;
    }
}
