package com.hospital.patientmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@SpringBootApplication
public class Server {
    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}

// ------------------- ENTITY MODEL -------------------
@Entity
@Table(name = "patients")
class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank private String name;
    @NotNull private Integer age;
    @NotBlank private String gender;
    @NotBlank private String phone;
    @NotBlank private String department;
    @NotBlank private String disease;
    @NotBlank private String doctor;
    @NotBlank private String admissionDate;
    @NotBlank private String status;

    public Patient() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }
    public String getDoctor() { return doctor; }
    public void setDoctor(String doctor) { this.doctor = doctor; }
    public String getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(String admissionDate) { this.admissionDate = admissionDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

// ------------------- REPOSITORY -------------------
@Repository
interface PatientRepository extends JpaRepository<Patient, Long> {}

// ------------------- SERVICE -------------------
@Service
class PatientService {
    @Autowired private PatientRepository repo;

    public List<Patient> getAll() { return repo.findAll(); }
    public Patient getById(Long id) { return repo.findById(id).orElse(null); }
    public Patient save(Patient p) { return repo.save(p); }
    public Patient update(Long id, Patient p) {
        Patient existing = repo.findById(id).orElseThrow();
        existing.setName(p.getName());
        existing.setAge(p.getAge());
        existing.setGender(p.getGender());
        existing.setPhone(p.getPhone());
        existing.setDepartment(p.getDepartment());
        existing.setDisease(p.getDisease());
        existing.setDoctor(p.getDoctor());
        existing.setAdmissionDate(p.getAdmissionDate());
        existing.setStatus(p.getStatus());
        return repo.save(existing);
    }
    public void delete(Long id) { repo.deleteById(id); }
}

// ------------------- REST CONTROLLER -------------------
@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
class PatientController {
    @Autowired private PatientService service;

    @GetMapping
    public List<Patient> getAllPatients() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Patient p = service.getById(id);
        return p != null ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Patient createPatient(@Valid @RequestBody Patient patient) {
        return service.save(patient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @Valid @RequestBody Patient patient) {
        try {
            return ResponseEntity.ok(service.update(id, patient));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}