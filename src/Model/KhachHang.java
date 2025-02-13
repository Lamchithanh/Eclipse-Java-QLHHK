package Model;

import java.sql.Date;

public class KhachHang {
    private String maKhachHang;
    private String tenKhachHang;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private String cmnd;         // Thêm trường cmnd
    private Date ngaySinh;       // Thêm trường ngaySinh
    private String quocTich;

    // Constructors
    public KhachHang() {}

    public KhachHang(String maKhachHang, String tenKhachHang) {
        this.maKhachHang = maKhachHang;
        this.tenKhachHang = tenKhachHang;
    }

    public KhachHang(String maKhachHang, String tenKhachHang, 
                     String soDienThoai, String email, String diaChi) {
        this.maKhachHang = maKhachHang;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
    }

    // Getters and Setters
    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
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

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getQuocTich() {
        return quocTich;
    }

    public void setQuocTich(String quocTich) {
        this.quocTich = quocTich;
    }


    // Phương thức toString để hiển thị trong ComboBox
    @Override
    public String toString() {
        return tenKhachHang + " - " + maKhachHang;
    }

    // Để so sánh trong ComboBox
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        KhachHang khachHang = (KhachHang) obj;
        return maKhachHang.equals(khachHang.maKhachHang);
    }

    @Override
    public int hashCode() {
        return maKhachHang.hashCode();
    }

    public String validateCmnd(String cmnd) {
        if (cmnd == null || cmnd.trim().isEmpty()) {
            return "Chưa có CMND";
        }
        if (!cmnd.matches("\\d{9}|\\d{12}")) {
            return "CMND không hợp lệ";
        }
        return cmnd;
    }
    
    public Object getNgaySinh(Object ngaySinh) {
            if (ngaySinh == null) {
            // Trả về ngày hiện tại nếu không có ngày sinh
            return new Date(System.currentTimeMillis());
        }
        return ngaySinh;
    }
    
    public Object getQuocTich(Object quocTich) {
            if (quocTich == null || ((String) quocTich).trim().isEmpty()) {
            return "Việt Nam"; // Giá trị mặc định
        }
        return quocTich;
    }
}