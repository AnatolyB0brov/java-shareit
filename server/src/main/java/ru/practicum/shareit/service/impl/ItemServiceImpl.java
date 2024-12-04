package ru.practicum.shareit.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.dto.CommentDto;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.model.*;
import ru.practicum.shareit.repository.*;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.NotBookerException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.mapper.CommentMapper;
import ru.practicum.shareit.dto.ItemDtoIn;
import ru.practicum.shareit.dto.ItemDtoOut;
import ru.practicum.shareit.service.ItemService;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Transactional
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository requestRepository;

    @Override
    public ItemDtoOut addItem(ItemDtoIn itemDtoIn, long userId) {
        User owner = getUser(userId);
        Item item = ItemMapper.toItem(itemDtoIn);
        item.setOwner(owner);
        Long requestId = itemDtoIn.getRequestId();
        if (requestId != null) {
            item.setRequest(requestRepository.findById(requestId).orElseThrow(() ->
                    new EntityNotFoundException(String.format("Объект класса %s не найден", ItemRequest.class))));
        }
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDtoOut updateItem(long itemId, ItemDtoIn itemDtoIn, long userId) {
        getUser(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", Item.class)));
        String name = itemDtoIn.getName();
        String description = itemDtoIn.getDescription();
        Boolean available = itemDtoIn.getAvailable();
        if (item.getOwner().getId() != userId) {
            throw new NotOwnerException(String.format("Пользователь с id %s не является владельцем %s",
                    userId, name));
        }
        if (name != null && !name.isBlank()) {
            item.setName(name);
        }
        if (description != null && !description.isBlank()) {
            item.setDescription(description);
        }
        if (available != null) {
            item.setAvailable(available);
        }
        return ItemMapper.toDto(item);
    }

    @Transactional(readOnly = true)
    @Override
    public ItemDtoOut getItemById(long itemId, long userId) {
        return itemRepository.findById(itemId).map(item -> addBookingsAndComments(item, userId)).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", Item.class)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDtoOut> getItemsByOwner(Integer from, Integer size, long userId) {
        getUser(userId);
        List<Item> items = itemRepository.findAllByOwnerId(userId, PageRequest.of(from / size, size,
                Sort.by("id").ascending()));
        return addBookingsAndCommentsForList(items);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDtoOut> getItemBySearch(Integer from, Integer size, String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text, PageRequest.of(from / size, size)).stream()
                .map(ItemMapper::toDto).collect(toList());
    }

    @Override
    public CommentDto addComment(long itemId, CommentDto commentDto, long userId) {
        User user = getUser(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", Item.class)));
        if (!bookingRepository.existsByBookerIdAndItemIdAndEndBefore(user.getId(), item.getId(), LocalDateTime.now())) {
            throw new NotBookerException("Сущность не пользовался вещью");
        }
        Comment comment = commentRepository.save(CommentMapper.toComment(commentDto, item, user));
        return CommentMapper.toCommentDto(comment);
    }

    private ItemDtoOut addBookingsAndComments(Item item, long userId) {
        ItemDtoOut itemDtoOut = ItemMapper.toDto(item);

        LocalDateTime thisMoment = LocalDateTime.now();
        if (itemDtoOut.getOwner().getId() == userId) {
            itemDtoOut.setLastBooking(bookingRepository
                    .findFirstByItemIdAndStartLessThanEqualAndStatus(itemDtoOut.getId(), thisMoment,
                            BookingStatus.APPROVED, Sort.by(DESC, "end"))
                    .map(BookingMapper::toBookingDtoShort)
                    .orElse(null));

            itemDtoOut.setNextBooking(bookingRepository
                    .findFirstByItemIdAndStartAfterAndStatus(itemDtoOut.getId(), thisMoment,
                            BookingStatus.APPROVED, Sort.by(ASC, "end"))
                    .map(BookingMapper::toBookingDtoShort)
                    .orElse(null));
        }

        itemDtoOut.setComments(commentRepository.findAllByItemId(itemDtoOut.getId())
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(toList()));

        return itemDtoOut;
    }

    private List<ItemDtoOut> addBookingsAndCommentsForList(List<Item> items) {
        LocalDateTime thisMoment = LocalDateTime.now();

        Map<Item, Booking> itemsWithLastBookings = bookingRepository
                .findByItemInAndStartLessThanEqualAndStatus(items, thisMoment,
                        BookingStatus.APPROVED, Sort.by(DESC, "end"))
                .stream()
                .collect(Collectors.toMap(Booking::getItem, Function.identity(), (o1, o2) -> o1));

        Map<Item, Booking> itemsWithNextBookings = bookingRepository
                .findByItemInAndStartAfterAndStatus(items, thisMoment,
                        BookingStatus.APPROVED, Sort.by(ASC, "end"))
                .stream()
                .collect(Collectors.toMap(Booking::getItem, Function.identity(), (o1, o2) -> o1));

        Map<Item, List<Comment>> itemsWithComments = commentRepository
                .findByItemIn(items, Sort.by(DESC, "created"))
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));

        List<ItemDtoOut> itemDtoOuts = new ArrayList<>();
        for (Item item : items) {
            ItemDtoOut itemDtoOut = ItemMapper.toDto(item);
            Booking lastBooking = itemsWithLastBookings.get(item);
            if (!itemsWithLastBookings.isEmpty() && lastBooking != null) {
                itemDtoOut.setLastBooking(BookingMapper.toBookingDtoShort(lastBooking));
            }
            Booking nextBooking = itemsWithNextBookings.get(item);
            if (!itemsWithNextBookings.isEmpty() && nextBooking != null) {
                itemDtoOut.setNextBooking(BookingMapper.toBookingDtoShort(nextBooking));
            }
            List<CommentDto> commentDtos = itemsWithComments.getOrDefault(item, Collections.emptyList())
                    .stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(toList());
            itemDtoOut.setComments(commentDtos);

            itemDtoOuts.add(itemDtoOut);
        }
        return itemDtoOuts;
    }

    private User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", User.class)));
    }
}
