package com.snapquest.snapquest.repository;

import com.snapquest.snapquest.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByUserId(Long userId);
    List<Submission> findByQuestId(Long questId);
    boolean existsByUserIdAndQuestId(Long userId, Long questId);

    int countByUserId(
            Long userId
    );

    int countByUserIdAndVerifiedTrue(
            Long userId
    );
}