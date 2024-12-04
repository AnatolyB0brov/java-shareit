package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.dto.ItemDtoIn;
import ru.practicum.shareit.dto.UserDto;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.service.ItemService;
import ru.practicum.shareit.service.UserService;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "spring.datasource.username=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ItemServiceIntegrationTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;

    @Test
    void saveNewItem() {
        UserDto userDto = new UserDto(1L, "User", "user@mail.ru");
        ItemDtoIn itemDtoIn = ItemDtoIn.builder().name("item").description("cool item").available(true).build();

        UserDto user = userService.addUser(userDto);
        itemService.addItem(itemDtoIn, user.getId());

        TypedQuery<Item> queryItem = em.createQuery("Select i from Item i where i.name like :item", Item.class);
        Item item = queryItem.setParameter("item", itemDtoIn.getName()).getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo(itemDtoIn.getName()));
        assertThat(item.getDescription(), equalTo(itemDtoIn.getDescription()));
    }
}
