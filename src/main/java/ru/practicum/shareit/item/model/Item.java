package ru.practicum.shareit.item.model;

import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = {Create.class})
    @Min(groups = {Update.class}, value = 1L)
    Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @NotBlank(groups = {Create.class})
    @Column(name = "name")
    String name;
    @NotBlank(groups = {Create.class})
    @Column(name = "description")
    String description;
    @BooleanFlag
    @NotNull(groups = {Create.class})
    @Column(name = "available")
    Boolean available;
    @Transient
    List<Comment> comments;
}
