package com.calendardev.calendardevelop.repository;

import com.calendardev.calendardevelop.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardId(Long boardId);

    List<Comment> findAllByUserId(Long userId);

    Optional<Comment> findByIdAndBoardId(Long id, Long boardId);

    Page<Comment> findAllByBoardIdOrderByCreatedAtDesc(Long boardId, Pageable pageable);

}
