package com.java.loginReg.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.loginReg.business.abstracts.DoctorService;
import com.java.loginReg.dataAccess.DoctorDao;
import com.java.loginReg.dataAccess.UserDao;
import com.java.loginReg.entities.Doctor;
import com.java.loginReg.entities.DoctorDto;
import com.java.loginReg.entities.Role;
import com.java.loginReg.entities.User;

@RestController
@RequestMapping("/doctors")
@CrossOrigin(origins = "http://localhost:3000")
public class DoctorController {
	@Autowired
    private DoctorService doctorService;
	
	@Autowired
    private DoctorDao doctorDao;

    @Autowired
    private UserDao userDao;
	
	@GetMapping("/all")
    public ResponseEntity<List<Doctor>> getAllDoctors(@RequestParam Role role) {
		if (role != Role.DOCTOR) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }
	
	@PostMapping("/add")
	public ResponseEntity<Doctor> addDoctor(@RequestBody Doctor doctor) {
		Doctor savedDoctor = doctorService.save(doctor);
		return ResponseEntity.ok(savedDoctor);
	}

	// Doktor bilgilerini almak için
    @GetMapping("/{id}")
    public DoctorDto getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorDao.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
        User user = doctor.getUser(); // Doktorun kullanıcı bilgilerini alıyoruz
        return new DoctorDto(
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPassword(),
            user.getRole(),
            doctor.getWorkingDays(),
            doctor.getWorkingHours(),
            doctor.getHospital(),
            doctor.getSpecialization()
        );
    }
    
    // Doktor bilgisini güncelleme
    @PutMapping("/{id}")
    public DoctorDto updateDoctor(@PathVariable Long id, @RequestBody DoctorDto doctorDto) {
        Doctor doctor = doctorDao.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
        User user = doctor.getUser();
        
        // Kullanıcı bilgilerini güncelle
        user.setFirstName(doctorDto.getFirstName());
        user.setLastName(doctorDto.getLastName());
        user.setEmail(doctorDto.getEmail());
        user.setPassword(doctorDto.getPassword());
        
        // Doktor bilgilerini güncelle
        doctor.setWorkingDays(doctorDto.getWorkingDays());
        doctor.setWorkingHours(doctorDto.getWorkingHours());
        doctor.setHospital(doctorDto.getHospital());
        doctor.setSpecialization(doctorDto.getSpecialty());
        
        // Veritabanına kaydet
        userDao.save(user);
        doctorDao.save(doctor);

        return new DoctorDto(
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPassword(),
            user.getRole(),
            doctor.getWorkingDays(),
            doctor.getWorkingHours(),
            doctor.getHospital(),
            doctor.getSpecialization()
        );
    }

    // Uzmanlık alanına göre doktorları getiren endpoint
    @GetMapping("/doctors")
    public List<Doctor> getDoctorsBySpecialization(@RequestParam String specialization) {
        return doctorService.findBySpecialization(specialization); // DoctorService'den gelen listi döndürüyoruz
    }


}
