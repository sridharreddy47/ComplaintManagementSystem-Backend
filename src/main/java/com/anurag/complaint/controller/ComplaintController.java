package com.anurag.complaint.controller;

import com.anurag.complaint.model.Complaint;
import com.anurag.complaint.model.User;
import com.anurag.complaint.repository.ComplaintRepository;
import com.anurag.complaint.repository.UserRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/complaints")
@CrossOrigin(origins = "http://localhost:5173")
public class ComplaintController {

    private final ComplaintRepository repository;
    private final UserRepository userRepository;

    public ComplaintController(
            ComplaintRepository repository,
            UserRepository userRepository) {

        this.repository = repository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public Complaint createComplaint(@RequestBody Complaint complaint) {
        return repository.save(complaint);
    }

    @GetMapping("/my")
    public List<Complaint> getMyComplaints(
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return repository.findByUserNameIgnoreCase(user.getName());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Complaint> getAllComplaints() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Complaint getComplaint(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Complaint not found"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Complaint updateComplaint(
            @PathVariable Long id,
            @RequestBody Complaint updatedComplaint) {

        Complaint complaint = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Complaint not found"));

        complaint.setTitle(updatedComplaint.getTitle());
        complaint.setDescription(updatedComplaint.getDescription());
        complaint.setCategory(updatedComplaint.getCategory());
        complaint.setPriority(updatedComplaint.getPriority());
        complaint.setStatus(updatedComplaint.getStatus());
        complaint.setLocation(updatedComplaint.getLocation());
        complaint.setUserName(updatedComplaint.getUserName());

        return repository.save(complaint);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteComplaint(@PathVariable Long id) {

        repository.deleteById(id);

        return "Complaint deleted successfully";
    }
}

