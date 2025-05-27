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

    //JPQL 에서는 import 구문이 없기 때문에, SELECT new 에서 클래스 사용 시에는 항상 패키지명을 포함한 전체 경로를 적어야 한다.
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
