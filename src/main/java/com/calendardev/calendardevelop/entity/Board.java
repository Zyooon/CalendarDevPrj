package com.calendardev.calendardevelop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "board")
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Board() {
    }

    public Board(String title, String contents, User user) {
        this.title = title;
        this.contents = contents;
        this.user = user;
    }

    public void updateTitle(String title){
        this.title = title;
    }

    public void updateContents(String contents){
        this.contents = contents;
    }
}
