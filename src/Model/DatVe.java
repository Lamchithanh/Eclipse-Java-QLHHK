package Model;

import java.time.LocalDateTime;
import java.util.List;
import java.sql.Date;

public class DatVe {
    // Core booking information
    private String maDatVe;
    private ChuyenBay chuyenBay;
    private int soLuong;
    private double tongGia;
    private String trangThai;
    private String phuongThucThanhToan;
    private String maGiamGia;
    private boolean xacNhanThanhToan;
    private List<VeMayBay> danhSachVe;
    @SuppressWarnings("unused")
    private String maKhachHang;
    
    // Personal information
    private String hoTen;
    private String cmnd;
    private Date ngaySinh;
    private String gioiTinh;
    private String quocTich;
    
    // Contact information
    private String soDienThoai;
    private String email;
    private String diaChi;
    
    // Flight details
    private String diemDi;
    private String diemDen;
    private Date ngayBay;
    private String hangVe;
    private String soGhe;


    
    // Special requests
    private boolean suatAnDacBiet;
    private boolean hoTroYTe;
    private boolean choNgoiUuTien;
    private boolean hanhLyDacBiet;
    
    // Emergency contact
    private String nguoiLienHe;
    private String sdtNguoiLienHe;
    
    // Timestamps
    private LocalDateTime ngayDat;
    private LocalDateTime ngayCapNhat;

    // Default constructor
    public DatVe() {
        this.ngayDat = LocalDateTime.now();
    }

    // Full constructor
    @SuppressWarnings("exports")
    public DatVe(String maDatVe, ChuyenBay chuyenBay, String hoTen, String cmnd, 
                 Date ngaySinh, String gioiTinh, String quocTich, String soDienThoai,
                 String email, String diaChi, String diemDi, String diemDen,
                 Date ngayBay, String hangVe, int soLuong, double tongGia,
                 String trangThai, String phuongThucThanhToan, String maGiamGia,
                 boolean xacNhanThanhToan, boolean suatAnDacBiet, boolean hoTroYTe,
                 boolean choNgoiUuTien, boolean hanhLyDacBiet, String nguoiLienHe,
                 String sdtNguoiLienHe) {
        this.maDatVe = maDatVe;
        this.chuyenBay = chuyenBay;
        this.hoTen = hoTen;
        this.cmnd = cmnd;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.quocTich = quocTich;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
        this.diemDi = diemDi;
        this.diemDen = diemDen;
        this.ngayBay = ngayBay;
        this.hangVe = hangVe;
        this.soLuong = soLuong;
        this.tongGia = tongGia;
        this.trangThai = trangThai;
        this.phuongThucThanhToan = phuongThucThanhToan;
        this.maGiamGia = maGiamGia;
        this.xacNhanThanhToan = xacNhanThanhToan;
        this.suatAnDacBiet = suatAnDacBiet;
        this.hoTroYTe = hoTroYTe;
        this.choNgoiUuTien = choNgoiUuTien;
        this.hanhLyDacBiet = hanhLyDacBiet;
        this.nguoiLienHe = nguoiLienHe;
        this.sdtNguoiLienHe = sdtNguoiLienHe;
        this.ngayDat = LocalDateTime.now();
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    // Getters and Setters
    public String getMaDatVe() {
        return maDatVe;
    }

    public void setMaDatVe(String maDatVe) {
        this.maDatVe = maDatVe;
    }

    public ChuyenBay getChuyenBay() {
        return chuyenBay;
    }

    public void setChuyenBay(ChuyenBay chuyenBay) {
        this.chuyenBay = chuyenBay;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getCMND() {
        return cmnd;
    }

    public void setCMND(String cmnd) {
        this.cmnd = cmnd;
    }

    @SuppressWarnings("exports")
    public Date getNgaySinh() {
        return ngaySinh;
    }

    @SuppressWarnings("exports")
    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getQuocTich() {
        return quocTich;
    }

    public void setQuocTich(String quocTich) {
        this.quocTich = quocTich;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getDiemDi() {
        return diemDi;
    }

    public void setDiemDi(String diemDi) {
        this.diemDi = diemDi;
    }

    public String getDiemDen() {
        return diemDen;
    }

    public void setDiemDen(String diemDen) {
        this.diemDen = diemDen;
    }

    @SuppressWarnings("exports")
    public Date getNgayBay() {
        return ngayBay;
    }

    @SuppressWarnings("exports")
    public void setNgayBay(Date ngayBay) {
        this.ngayBay = ngayBay;
    }

    public String getSoGhe() {
        return soGhe;
    }
    
    public void setSoGhe(String soGhe) {
        this.soGhe = soGhe;
    }    

    public String getHangVe() {
        return hangVe;
    }

    public void setHangVe(String hangVe) {
        this.hangVe = hangVe;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getTongGia() {
        return tongGia;
    }

    public void setTongGia(double tongGia) {
        this.tongGia = tongGia;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public String getMaGiamGia() {
        return maGiamGia;
    }

    public void setMaGiamGia(String maGiamGia) {
        this.maGiamGia = maGiamGia;
    }

    public boolean isXacNhanThanhToan() {
        return xacNhanThanhToan;
    }

    public void setXacNhanThanhToan(boolean xacNhanThanhToan) {
        this.xacNhanThanhToan = xacNhanThanhToan;
    }

    public boolean isSuatAnDacBiet() {
        return suatAnDacBiet;
    }

    public void setSuatAnDacBiet(boolean suatAnDacBiet) {
        this.suatAnDacBiet = suatAnDacBiet;
    }

    public boolean isHoTroYTe() {
        return hoTroYTe;
    }

    public void setHoTroYTe(boolean hoTroYTe) {
        this.hoTroYTe = hoTroYTe;
    }

    public boolean isChoNgoiUuTien() {
        return choNgoiUuTien;
    }

    public void setChoNgoiUuTien(boolean choNgoiUuTien) {
        this.choNgoiUuTien = choNgoiUuTien;
    }

    public boolean isHanhLyDacBiet() {
        return hanhLyDacBiet;
    }

    public void setHanhLyDacBiet(boolean hanhLyDacBiet) {
        this.hanhLyDacBiet = hanhLyDacBiet;
    }

    public String getNguoiLienHe() {
        return nguoiLienHe;
    }

    public void setNguoiLienHe(String nguoiLienHe) {
        this.nguoiLienHe = nguoiLienHe;
    }

    public String getSDTNguoiLienHe() {
        return sdtNguoiLienHe;
    }

    public void setSDTNguoiLienHe(String sdtNguoiLienHe) {
        this.sdtNguoiLienHe = sdtNguoiLienHe;
    }

    public LocalDateTime getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(LocalDateTime ngayDat) {
        this.ngayDat = ngayDat;
    }

    public LocalDateTime getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(LocalDateTime ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    public List<VeMayBay> getDanhSachVe() {
        return danhSachVe;
    }

    public void setDanhSachVe(List<VeMayBay> danhSachVe) {
        this.danhSachVe = danhSachVe;
    }

    @Override
    public String toString() {
        return "DatVe{" +
                "maDatVe='" + maDatVe + '\'' +
                ", chuyenBay=" + chuyenBay +
                ", hoTen='" + hoTen + '\'' +
                ", ngayDat=" + ngayDat +
                ", tongGia=" + tongGia +
                ", trangThai='" + trangThai + '\'' +
                ", phuongThucThanhToan='" + phuongThucThanhToan + '\'' +
                '}';
    }

    // Helper methods for validation
    public boolean isValidBooking() {
        return maDatVe != null && !maDatVe.trim().isEmpty() &&
               chuyenBay != null &&
               hoTen != null && !hoTen.trim().isEmpty() &&
               cmnd != null && !cmnd.trim().isEmpty() &&
               ngaySinh != null &&
               soLuong > 0 &&
               tongGia > 0;
    }

    public boolean hasValidContactInfo() {
        return soDienThoai != null && !soDienThoai.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               diaChi != null && !diaChi.trim().isEmpty();
    }

    public boolean hasValidEmergencyContact() {
        return nguoiLienHe != null && !nguoiLienHe.trim().isEmpty() &&
               sdtNguoiLienHe != null && !sdtNguoiLienHe.trim().isEmpty();
    }

    public String getMaKhachHang() {
        // Format: KH + last 5 digits of CMND + first letter of name
        // Example: For CMND "123456789" and name "Nguyen Van A" -> KH56789N
        try {
            if (this.cmnd == null || this.cmnd.trim().isEmpty() || 
                this.hoTen == null || this.hoTen.trim().isEmpty()) {
                throw new IllegalStateException("CMND and HoTen must not be empty");
            }
    
            // Get last 5 digits of CMND
            String cmndSuffix = this.cmnd.length() > 5 ? 
                this.cmnd.substring(this.cmnd.length() - 5) : 
                this.cmnd;
    
            // Get first letter of name (after trimming and converting to uppercase)
            String firstLetter = this.hoTen.trim().substring(0, 1).toUpperCase();
    
            // Combine to create MaKhachHang
            return "KH" + cmndSuffix + firstLetter;
        } catch (Exception e) {
            // Log the error and return a default value or throw exception based on your needs
            System.err.println("Error generating MaKhachHang: " + e.getMessage());
            throw new RuntimeException("Unable to generate MaKhachHang", e);
        }
    }

    public String getViTriGhe() {
        if (danhSachVe != null && !danhSachVe.isEmpty()) {
            return danhSachVe.get(0).getSoGhe();
        }
        return null;  // Trả về null thay vì giá trị mặc định
    }
}