package ru.practicum.shareit.client;

import ru.practicum.shareit.dto.BookingDto;
import ru.practicum.shareit.dto.BookingState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.exception.WrongDatesException;

import java.time.LocalDateTime;
import java.util.Map;


@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";


    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> saveBooking(BookingDto bookingDto, long userId) {
        if (!bookingDto.getEnd().isAfter(bookingDto.getStart()) ||
                bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new WrongDatesException("Дата начала бронирования должна быть раньше даты возврата");
        }
        return post("", userId, bookingDto);
    }

    public ResponseEntity<Object> approve(long bookingId, Boolean isApproved, long userId) {
        Map<String, Object> parameters = Map.of("approved", isApproved);
        return patch("/" + bookingId + "?approved={approved}", userId, parameters, null);
    }

    public ResponseEntity<Object> getBookingById(long bookingId, long userId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllByBooker(Integer from, Integer size, BookingState state, long userId) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getAllByOwner(Integer from, Integer size, BookingState state, long userId) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }
}