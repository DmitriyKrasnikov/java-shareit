package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotNull(groups = {Create.class})
    @Min(groups = {Update.class}, value = 1L)
    Long id;
    @NotBlank(groups = {Create.class})
    @Email(groups = {Create.class})
    String email;
    @NotBlank(groups = {Create.class})
    String name;
    HashSet<Long> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
