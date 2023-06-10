package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @NotNull(groups = {Create.class})
    @Min(groups = {Update.class}, value = 1L)
    Long id;
    @EqualsAndHashCode.Include
    @NotBlank(groups = {Create.class})
    @Email(groups = {Create.class})
    @Column(name = "email")
    String email;
    @NotBlank(groups = {Create.class})
    @Column(name = "name")
    String name;
}
