package com.calendardev.calendardevelop.repository;

import com.calendardev.calendardevelop.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByUserId(Long userId);

    Page<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
