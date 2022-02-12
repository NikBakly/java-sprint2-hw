package controller;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int SIZE_HISTORY = 10;
    private final MyLinkedList<Task> myLinkedList = new MyLinkedList<>();

    //для добавления нового просмотра задачи
    @Override
    public void add(Task task) {
        if (myLinkedList.map.containsKey(task.getId()))
            remove(task.getId());
        myLinkedList.linkLast(task);

    }

    //для удаления просмотра из истории
    @Override
    public void remove(int id) {
        if (!myLinkedList.map.isEmpty()) {
            //удаляем узел
            myLinkedList.removeNode(myLinkedList.map.get(id));
            //Удаляем пару ключ-значение из HashMap
            myLinkedList.map.remove(id);
        }
    }

    //для получения истории последних просмотров
    @Override
    public List<Task> getHistory() {
        return myLinkedList.getTasks();
    }

    private static class MyLinkedList<E extends Task> {
        private Node<E> head = null;
        private Node<E> tail = null;
        private int size = 0;
        private final Map<Integer, Node<E>> map = new HashMap<>();

        //будет добавлять задачу в конец этого списка
        private void linkLast(E value) {
            if (size + 1 <= SIZE_HISTORY) {
                if (size == 0) {
                    Node<E> newNode = new Node<>(value);
                    map.put(value.getId(), newNode);
                    head = newNode;
                } else if (size == 1) {
                    Node<E> newNode = new Node<>(value);
                    head.setNext(newNode);
                    map.put(value.getId(), newNode);
                    newNode.setPrev(head);
                    tail = newNode;
                } else {
                    Node<E> newNode = new Node<>(value);
                    newNode.setPrev(tail);
                    map.put(value.getId(), newNode);
                    tail.setNext(newNode);
                    tail = newNode;
                }
                ++size;
            } else {
                removeNode(head);
                linkLast(value);
            }
        }

        //удаляет узел(Node) O(1)
        private void removeNode(Node<E> value) {
            if (size == 1) {
                head = null;
            } else if (value == head) {
                if (size == 2) {
                    tail.setPrev(null);
                    head.setNext(null);
                    head = tail;
                    tail = null;
                } else {
                    head = head.getNext();
                    head.getPrev().setNext(null);
                    head.setPrev(null);

                }
            } else if (value == tail) {
                if (size == 2) {
                    tail.setPrev(null);
                    tail = null;
                    head.setNext(null);
                } else {
                    tail = tail.getPrev();
                    tail.getNext().setPrev(null);
                    tail.setNext(null);
                }
            } else {
                value.getPrev().setNext(value.getNext());
                value.getNext().setPrev(value.getPrev());
                value.setNext(null);
                value.setPrev(null);
            }
            --size;
        }

        //собирает все задачи из MyLinkedList в обычный ArrayList
        private List<E> getTasks() {
            if (head != null) {
                List<E> tasks = new ArrayList<>();
                Node<E> iter = head; // начальный элемент
                //Пока следующий элемент существует
                while (true) {
                    tasks.add(iter.getValue());
                    //проверка на след элемент
                    if (iter.getNext() == null)
                        break;
                    //переходим на след. элемент
                    iter = iter.getNext();
                }
                return tasks;
            } else
                return null;
        }

    }

    private static class Node<E> {
        private final E value;
        private Node<E> next;
        private Node<E> prev;

        private Node(E task) {
            this.value = task;
        }

        public E getValue() {
            return value;
        }

        private Node<E> getNext() {
            return next;
        }

        private Node<E> getPrev() {
            return prev;
        }

        private void setNext(Node<E> next) {
            this.next = next;
        }

        private void setPrev(Node<E> prev) {
            this.prev = prev;
        }
    }
}
