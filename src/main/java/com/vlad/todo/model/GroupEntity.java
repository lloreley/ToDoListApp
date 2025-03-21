package com.vlad.todo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 200)
    private String description;
    private LocalDate createdDate = LocalDate.now();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonBackReference
    private List<UserEntity> users = new ArrayList<>();

    public void addUser(UserEntity userEntity) {
        if (!users.contains(userEntity)) {
            users.add(userEntity);
            userEntity.getGroups().add(this);
        }
    }

    public void removeUser(UserEntity userEntity) {
        users.remove(userEntity);
        userEntity.getGroups().remove(this);
    }

}
