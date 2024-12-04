package ru.practicum.shareit.mapper;

import ru.practicum.shareit.dto.CommentDto;
import ru.practicum.shareit.model.Comment;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.model.User;

import java.time.LocalDateTime;

public final class CommentMapper {

    private CommentMapper() {
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getContent(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }

    public static Comment toComment(CommentDto commentDto, Item item, User author) {
        Comment comment = new Comment();
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setContent(commentDto.getText());
        comment.setCreated(LocalDateTime.now());
        return comment;
    }
}
