package com.android.be.mapper;

import com.android.be.dto.DanhMucMonAnDTO;
import com.android.be.model.DanhMucMonAn;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class DanhMucMonAnMapper {
    
    private final MonAnMapper monAnMapper;
    
    public DanhMucMonAnMapper(MonAnMapper monAnMapper) {
        this.monAnMapper = monAnMapper;
    }
    
    public DanhMucMonAnDTO toDTO(DanhMucMonAn danhMuc) {
        if (danhMuc == null) return null;
        
        DanhMucMonAnDTO dto = new DanhMucMonAnDTO();
        dto.setMaDanhMuc(danhMuc.getMaDanhMuc());
        dto.setTenDanhMuc(danhMuc.getTenDanhMuc());
        dto.setIconDanhMuc(danhMuc.getIconDanhMuc());
        dto.setThuTu(danhMuc.getThuTu());
        dto.setDanhSachMon(new ArrayList<>());
        
        return dto;
    }
}
