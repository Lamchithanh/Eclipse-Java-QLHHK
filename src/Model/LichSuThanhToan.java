package Model;

import java.time.LocalDateTime;

public class LichSuThanhToan {
    private Integer maLichSu;
    private String maVe;
    private LocalDateTime ngayThanhToan;
    private double soTien;
    private String phuongThucThanhToan;
    
    // Constructor đầy đủ
    public LichSuThanhToan(Integer maLichSu, String maVe, LocalDateTime ngayThanhToan, 
                          double soTien, String phuongThucThanhToan) {
        this.maLichSu = maLichSu;
        this.maVe = maVe;
        this.ngayThanhToan = ngayThanhToan;
        this.soTien = soTien;
        this.phuongThucThanhToan = phuongThucThanhToan;
    }
    
    // Constructor không có mã (cho thêm mới)
    public LichSuThanhToan(String maVe, LocalDateTime ngayThanhToan, 
                          double soTien, String phuongThucThanhToan) {
        this(null, maVe, ngayThanhToan, soTien, phuongThucThanhToan);
    }
    
    // Constructor mặc định
    public LichSuThanhToan() {
    }
    
    // Getters and Setters
    public Integer getMaLichSu() {
        return maLichSu;
    }
    
    public void setMaLichSu(Integer maLichSu) {
        this.maLichSu = maLichSu;
    }
    
    public String getMaVe() {
        return maVe;
    }
    
    public void setMaVe(String maVe) {
        this.maVe = maVe;
    }
    
    public LocalDateTime getNgayThanhToan() {
        return ngayThanhToan;
    }
    
    public void setNgayThanhToan(LocalDateTime ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }
    
    public double getSoTien() {
        return soTien;
    }
    
    public void setSoTien(double soTien) {
        this.soTien = soTien;
    }
    
    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }
    
    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }
    
    @Override
    public String toString() {
        return "LichSuThanhToan{" +
                "maLichSu=" + maLichSu +
                ", maVe='" + maVe + '\'' +
                ", ngayThanhToan=" + ngayThanhToan +
                ", soTien=" + soTien +
                ", phuongThucThanhToan='" + phuongThucThanhToan + '\'' +
                '}';
    }
}