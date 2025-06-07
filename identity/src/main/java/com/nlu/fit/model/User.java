package com.nlu.fit.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Table(name = "user")
@Entity()
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String uid;

    private String name;

    private String email;
}
