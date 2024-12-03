package ru.practicum.shareit.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.model.Comment;
import ru.practicum.shareit.model.Item;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByItemId(long itemId);

    List<Comment> findByItemIn(List<Item> allByOwnerId, Sort created);
}