package ru.practicum.shareit.item.model;

import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validateInterfaces.Create;

import javax.persistence.*;
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
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @NotBlank(groups = {Create.class})
    @Column(name = "name")
    private String name;
    @NotBlank(groups = {Create.class})
    @Column(name = "description")
    private String description;
    @BooleanFlag
    @NotNull(groups = {Create.class})
    @Column(name = "available")
    private Boolean available;
    @Transient
    private List<Comment> comments;
}
