package com.snapquest.snapquest.repository;

import com.snapquest.snapquest.model.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestRepository
        extends JpaRepository<Quest, Long> {

    List<Quest> findByActiveTrue();

    List<Quest> findByFeaturedTrue();
}