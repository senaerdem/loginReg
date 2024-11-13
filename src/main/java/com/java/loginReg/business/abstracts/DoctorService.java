package com.java.loginReg.business.abstracts;

import java.util.List;

import com.java.loginReg.entities.Doctor;

public interface DoctorService {

	List<Doctor> getAllDoctors();
	
	Doctor save(Doctor doctor);
	
	boolean updateDoctor(Long id, Doctor doctor);
	
	Doctor getDoctorById(Long id);

}
