package com.android.be.mapper;

import com.android.be.dto.MonAnDTO;
import com.android.be.model.MonAn;
import org.springframework.stereotype.Component;

@Component
public class MonAnMapper {
    
    public MonAnDTO toDTO(MonAn monAn) {
        if (monAn == null) return null;
        
        MonAnDTO dto = new MonAnDTO();
        dto.setMaMonAn(monAn.getMaMonAn());
        dto.setTenMon(monAn.getTenMon());
        dto.setGioiThieu(monAn.getGioiThieu());
        dto.setNguyenLieu(monAn.getNguyenLieu());
        dto.setHinhAnh(monAn.getHinhAnh());
        
        return dto;
    }
}
