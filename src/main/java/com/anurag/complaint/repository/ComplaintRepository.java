package com.anurag.complaint.repository;

import com.anurag.complaint.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByUserNameIgnoreCase(String userName);
}

