package com.calendardev.calendardevelop.repository;

import com.calendardev.calendardevelop.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardId(Long boardId);

    Optional<Comment> findByIdAndBoardId(Long id, Long boardId);
}
