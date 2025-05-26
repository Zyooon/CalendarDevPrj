package com.calendardev.calendardevelop.repository;

import com.calendardev.calendardevelop.dto.board.BoardResponseDto;
import com.calendardev.calendardevelop.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByUserId(Long userId);

    @Query("""
        SELECT new com.calendardev.calendardevelop.dto.board.BoardResponseDto(
            b.id,
            b.title,
            b.contents,
            u.username,
            COUNT(c.id) as commentCount,
            b.createdAt,
            b.modifiedAt
        )
        FROM Board b
        JOIN b.user u
        LEFT JOIN Comment c ON c.board = b
        GROUP BY b.id, b.title, b.contents, u.username, b.createdAt, b.modifiedAt
        ORDER BY b.createdAt DESC
    """)
    Page<BoardResponseDto> findAllWithCommentCount(Pageable pageable);
}
