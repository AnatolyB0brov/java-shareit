package ru.practicum.shareit.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.dto.BookingItemDto;
import ru.practicum.shareit.enums.BookingState;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.model.Booking;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.repository.BookingRepository;
import ru.practicum.shareit.repository.ItemRepository;
import ru.practicum.shareit.repository.UserRepository;
import ru.practicum.shareit.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Transactional
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Booking addBooking(BookingItemDto bookingItemDtoIn, long userId) {
        User booker = getUser(userId);
        Item item = getItem(bookingItemDtoIn.getItemId());
        if (!item.getAvailable()) {
            throw new ItemNotAvailableForBookingException("Вещь недоступна для брони");
        }
        if (booker.getId() == item.getOwner().getId()) {
            throw new BookOwnItemsException("Нельзя забронировать свою вещь");
        }
        if (!bookingItemDtoIn.getEnd().isAfter(bookingItemDtoIn.getStart()) ||
                bookingItemDtoIn.getStart().isBefore(LocalDateTime.now())) {
            throw new WrongDatesException("Неверная дата бронирования");
        }
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        bookingRepository.save(BookingMapper.toBooking(bookingItemDtoIn, booking));
        return booking;
    }

    @Override
    public Booking approve(long bookingId, Boolean isApproved, long userId) {
        Booking booking = getById(bookingId);
        getItem(booking.getItem().getId());

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ItemNotAvailableForBookingException("Вещь уже забронирована");
        }
        if (booking.getItem().getOwner().getId() != userId) {
            throw new IllegalViewAndUpdateException("Подтвердить бронирование может только владцелец");
        }
        BookingStatus newBookingStatus = isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(newBookingStatus);
        return booking;

    }

    @Transactional(readOnly = true)
    @Override
    public Booking getBookingById(long bookingId, long userId) {
        Booking booking = getById(bookingId);
        User booker = booking.getBooker();
        User owner = getUser(booking.getItem().getOwner().getId());
        if (booker.getId() != userId && owner.getId() != userId) {
            throw new IllegalViewAndUpdateException("Только автор или владелец может просматривать бронирование");
        }
        return booking;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Booking> getAllByBooker(String state, long bookerId) {
        User booker = getUser(bookerId);
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("UnsupportedStatusException");
        }
        return switch (bookingState) {
            case ALL -> bookingRepository.findAllByBookerId(booker.getId(), Sort.by(DESC, "start"));
            case CURRENT -> bookingRepository.findAllByBookerIdAndStateCurrent(booker.getId(),
                    Sort.by(Sort.Direction.DESC, "start"));
            case PAST -> bookingRepository.findAllByBookerIdAndStatePast(booker.getId(),
                    Sort.by(Sort.Direction.DESC, "start"));
            case FUTURE -> bookingRepository.findAllByBookerIdAndStateFuture(booker.getId(),
                    Sort.by(Sort.Direction.DESC, "start"));
            case WAITING -> bookingRepository.findAllByBookerIdAndStatus(booker.getId(),
                    BookingStatus.WAITING, Sort.by(Sort.Direction.DESC, "start"));
            case REJECTED -> bookingRepository.findAllByBookerIdAndStatus(booker.getId(),
                    BookingStatus.REJECTED, Sort.by(DESC, "end"));
        };
    }

    @Transactional(readOnly = true)
    @Override
    public List<Booking> getAllByOwner(long ownerId, String state) {
        User owner = getUser(ownerId);
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("UnsupportedStatusException");
        }
        return switch (bookingState) {
            case ALL -> bookingRepository.findAllByOwnerId(owner.getId(),
                    Sort.by(Sort.Direction.DESC, "start"));
            case CURRENT -> bookingRepository.findAllByOwnerIdAndStateCurrent(owner.getId(),
                    Sort.by(Sort.Direction.DESC, "start"));
            case PAST -> bookingRepository.findAllByOwnerIdAndStatePast(owner.getId(),
                    Sort.by(Sort.Direction.DESC, "start"));
            case FUTURE -> bookingRepository.findAllByOwnerIdAndStateFuture(owner.getId(),
                    Sort.by(Sort.Direction.DESC, "start"));
            case WAITING -> bookingRepository.findAllByOwnerIdAndStatus(owner.getId(),
                    BookingStatus.WAITING, Sort.by(Sort.Direction.DESC, "start"));
            case REJECTED -> bookingRepository.findAllByOwnerIdAndStatus(owner.getId(),
                    BookingStatus.REJECTED, Sort.by(Sort.Direction.DESC, "start"));
        };
    }

    @Transactional(readOnly = true)
    public Booking getById(long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", Booking.class)));
    }

    private User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", User.class)));
    }

    private Item getItem(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", Item.class)));
    }
}
