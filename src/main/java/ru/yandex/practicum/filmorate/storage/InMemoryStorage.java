package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.IdControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class InMemoryStorage<T extends IdControl> {
    private final Map<Integer, T> itemList = new HashMap<>();
    private int currentItemId = 1;

    public int add(T item) {
        int id = getIdForNewItem();
        item.setId(id);
        itemList.put(id, item);
        return item.getId();
    }

    public void update(T newItem) {
        itemList.put(newItem.getId(), newItem);
    }

    public List<T> getAllItems() {
        return new ArrayList<>(itemList.values());
    }

    public T getItemById(int id) {
        T item = itemList.get(id);
        if (item == null) {
            throw new NoSuchElementException("item with id = " + id + " not found");
        }
        return item;
    }

    private int getIdForNewItem() {
        return currentItemId++;
    }
}
