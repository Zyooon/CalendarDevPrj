package com.calendardev.calendardevelop.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    public Comment() {
    }

    public Comment(String contents) {
        this.contents = contents;
    }

    public void setUserAndBoard(User user, Board board){
        this.user = user;
        this.board = board;
    }

    public void updateContents(String contents){
        this.contents = contents;
    }
}
