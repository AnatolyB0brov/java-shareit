package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.dto.BookingItemDto;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.exception.ItemNotAvailableForBookingException;
import ru.practicum.shareit.model.Booking;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.repository.BookingRepository;
import ru.practicum.shareit.repository.ItemRepository;
import ru.practicum.shareit.repository.UserRepository;
import ru.practicum.shareit.service.BookingService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "spring.datasource.username=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BookingServiceIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User owner;
    private User booker;
    private Item availableItem;
    private Item unavailableItem;
    private Booking booking;

    @BeforeEach
    public void setUp() {
        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        userRepository.save(owner);

        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@example.com");
        userRepository.save(booker);

        availableItem = new Item();
        availableItem.setName("Available Item");
        availableItem.setDescription("This item is available for booking.");
        availableItem.setOwner(owner);
        availableItem.setAvailable(true);
        itemRepository.save(availableItem);

        unavailableItem = new Item();
        unavailableItem.setName("Unavailable Item");
        unavailableItem.setDescription("This item is not available for booking.");
        unavailableItem.setOwner(owner);
        unavailableItem.setAvailable(false);
        itemRepository.save(unavailableItem);

        booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setItem(availableItem);
        booking.setBooker(owner);
        booking.setStatus(BookingStatus.WAITING);
    }

    @Test
    public void testSaveBookingSuccessfully() {
        BookingItemDto bookingDtoIn = new BookingItemDto(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), availableItem.getId());

        Booking savedBooking = bookingService.addBooking(bookingDtoIn, booker.getId());

        assertThat(savedBooking).isNotNull();
        assertThat(savedBooking.getItem().getId()).isEqualTo(availableItem.getId());
        assertThat(savedBooking.getBooker().getId()).isEqualTo(booker.getId());
    }


    @Test
    public void testSaveBookingThrowsExceptionWhenItemNotAvailable() {
        BookingItemDto bookingDtoIn = new BookingItemDto(LocalDateTime.now().plusDays(1),LocalDateTime.now().plusDays(2),unavailableItem.getId());

        assertThatThrownBy(() -> bookingService.addBooking(bookingDtoIn, booker.getId()))
                .isInstanceOf(ItemNotAvailableForBookingException.class)
                .hasMessage("Вещь недоступна для брони");
    }


    @Test
    void testFindBookingById() {
        Booking savedBooking = bookingRepository.save(booking);
        Optional<Booking> foundBooking = bookingRepository.findById(savedBooking.getId());
        assertTrue(foundBooking.isPresent());
        assertEquals(savedBooking.getId(), foundBooking.get().getId());
    }

    @Test
    @Rollback(false)
    void testDeleteBooking() {
        Booking savedBooking = bookingRepository.save(booking);
        bookingRepository.deleteById(savedBooking.getId());
        Optional<Booking> foundBooking = bookingRepository.findById(savedBooking.getId());
        assertFalse(foundBooking.isPresent());
    }

    @Test
    void testUpdateBooking() {
        Booking savedBooking = bookingRepository.save(booking);
        savedBooking.setStatus(BookingStatus.APPROVED);
        Booking updatedBooking = bookingRepository.save(savedBooking);
        assertEquals(BookingStatus.APPROVED, updatedBooking.getStatus());
    }
}