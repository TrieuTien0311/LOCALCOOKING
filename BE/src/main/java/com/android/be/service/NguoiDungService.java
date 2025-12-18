package com.android.be.service;

import com.android.be.dto.LoginRequest;
import com.android.be.dto.LoginResponse;
import com.android.be.model.NguoiDung;
import com.android.be.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NguoiDungService {
    
    private final NguoiDungRepository nguoiDungRepository;
    
    public List<NguoiDung> getAllNguoiDung() {
        return nguoiDungRepository.findAll();
    }
    
    public Optional<NguoiDung> getNguoiDungById(Integer id) {
        return nguoiDungRepository.findById(id);
    }
    
    public NguoiDung createNguoiDung(NguoiDung nguoiDung) {
        return nguoiDungRepository.save(nguoiDung);
    }
    
    public NguoiDung updateNguoiDung(Integer id, NguoiDung nguoiDung) {
        nguoiDung.setMaNguoiDung(id);
        return nguoiDungRepository.save(nguoiDung);
    }
    
    public void deleteNguoiDung(Integer id) {
        nguoiDungRepository.deleteById(id);
    }
    
    public LoginResponse login(LoginRequest request) {
        Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository.findByEmailAndMatKhau(
            request.getEmail(), 
            request.getMatKhau()
        );
        
        if (nguoiDungOpt.isPresent()) {
            NguoiDung nguoiDung = nguoiDungOpt.get();
            
            // Kiểm tra trạng thái tài khoản
            if (!"HoatDong".equals(nguoiDung.getTrangThai())) {
                return new LoginResponse(
                    false, 
                    "Tài khoản đã bị khóa", 
                    null, null, null, null, null
                );
            }
            
            return new LoginResponse(
                true,
                "Đăng nhập thành công",
                nguoiDung.getMaNguoiDung(),
                nguoiDung.getTenDangNhap(),
                nguoiDung.getHoTen(),
                nguoiDung.getEmail(),
                nguoiDung.getVaiTro()
            );
        }
        
        return new LoginResponse(
            false, 
            "Email hoặc mật khẩu không đúng", 
            null, null, null, null, null
        );
    }
}
