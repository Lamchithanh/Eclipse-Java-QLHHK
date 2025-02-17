package Model;

import java.sql.Date;

public class VeMayBay {
    private String maVe;
    private String maChuyenBay;
    private String maKhachHang;
    private Date ngayDat;
    private String tenKhachHang;
    private String cmnd;
    private double giaVe;
    private String trangThai;
    private String soGhe;        // Thêm trường số ghế
    private String hangVe;

    // Thêm các trường thông tin khách hàng
    private String soDienThoai;
    private String diaChi;
    private String email;
    private Date ngaySinh;
    private String quocTich;

    // Constructor đầy đủ với thông tin vé và khách hàng
    @SuppressWarnings("exports")
    public VeMayBay(String maVe, String maChuyenBay, String maKhachHang, 
                    Date ngayDat, String tenKhachHang, String cmnd, 
                    double giaVe, String trangThai,
                    String soDienThoai, String diaChi, String email, 
                    Date ngaySinh, String quocTich,
                    String soGhe, String hangVe) {  // Thêm 2 tham số mới
        this.maVe = maVe;
        this.maChuyenBay = maChuyenBay;
        this.maKhachHang = maKhachHang;
        this.ngayDat = ngayDat;
        this.tenKhachHang = tenKhachHang;
        this.cmnd = cmnd;
        this.giaVe = giaVe;
        this.trangThai = trangThai;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.email = email;
        this.ngaySinh = ngaySinh;
        this.quocTich = quocTich;
        this.soGhe = soGhe;
        this.hangVe = hangVe;
    }

    // Constructor với thông tin cơ bản
    // Constructor đầy đủ với thông tin vé và khách hàng
    @SuppressWarnings("exports")
    public VeMayBay(String maVe, String maChuyenBay, String maKhachHang, 
                    Date ngayDat, String tenKhachHang, String cmnd, 
                    double giaVe, String trangThai,
                    String soDienThoai, String diaChi, String email, 
                    Date ngaySinh, String quocTich) {
        this(maVe, maChuyenBay, maKhachHang, ngayDat, tenKhachHang, cmnd,
            giaVe, trangThai, soDienThoai, diaChi, email, ngaySinh, quocTich,
            null, null); // Thêm soGhe và hangVe là null
    }

    // Constructor với thông tin cơ bản
    @SuppressWarnings("exports")
    public VeMayBay(String maVe, String maChuyenBay, String maKhachHang, 
                    Date ngayDat, String tenKhachHang, String cmnd) {
        this(maVe, maChuyenBay, maKhachHang, ngayDat, tenKhachHang, cmnd,
            0.0, "Đã đặt", null, null, null, null, null, null, null);
    }

    // Constructor với thông tin vé đầy đủ
    @SuppressWarnings("exports")
    public VeMayBay(String maVe, String maChuyenBay, String maKhachHang, 
                    Date ngayDat, String tenKhachHang, String cmnd,
                    double giaVe, String trangThai) {
        this(maVe, maChuyenBay, maKhachHang, ngayDat, tenKhachHang, cmnd,
            giaVe, trangThai, null, null, null, null, null, null, null);
    }

    public String getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(String soGhe) {
        this.soGhe = soGhe;
    }

    // Thêm getter và setter cho hạng vé
    public String getHangVe() {
        return hangVe;
    }

    public void setHangVe(String hangVe) {
        this.hangVe = hangVe;
    }

    // Getter và setter cho các trường thông tin vé
    public String getMaVe() { return maVe; }
    public void setMaVe(String maVe) { this.maVe = maVe; }

    public String getMaChuyenBay() { return maChuyenBay; }
    public void setMaChuyenBay(String maChuyenBay) { this.maChuyenBay = maChuyenBay; }

    public String getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(String maKhachHang) { this.maKhachHang = maKhachHang; }

    @SuppressWarnings("exports")
    public Date getNgayDat() { return ngayDat; }
    @SuppressWarnings("exports")
    public void setNgayDat(Date ngayDat) { this.ngayDat = ngayDat; }

    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }

    public String getCmnd() { return cmnd; }
    public void setCmnd(String cmnd) { this.cmnd = cmnd; }

    public double getGiaVe() { return giaVe; }
    public void setGiaVe(double giaVe) { this.giaVe = giaVe; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    // Getter và setter cho các trường thông tin khách hàng bổ sung
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @SuppressWarnings("exports")
    public Date getNgaySinh() { return ngaySinh; }
    @SuppressWarnings("exports")
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getQuocTich() { return quocTich; }
    public void setQuocTich(String quocTich) { this.quocTich = quocTich; }

    @Override
    public String toString() {
        return "VeMayBay{" +
               "maVe='" + maVe + '\'' +
               ", maChuyenBay='" + maChuyenBay + '\'' +
               ", maKhachHang='" + maKhachHang + '\'' +
               ", ngayDat=" + ngayDat +
               ", tenKhachHang='" + tenKhachHang + '\'' +
               ", cmnd='" + cmnd + '\'' +
               ", giaVe=" + giaVe +
               ", trangThai='" + trangThai + '\'' +
               ", soDienThoai='" + soDienThoai + '\'' +
               ", diaChi='" + diaChi + '\'' +
               ", email='" + email + '\'' +
               ", ngaySinh=" + ngaySinh +
               ", quocTich='" + quocTich + '\'' +
               ", soGhe='" + soGhe + '\'' +    // Thêm số ghế
               ", hangVe='" + hangVe + '\'' +  // Thêm hạng vé
               '}';
    }

    public boolean isValidSeatNumber() {
        if (soGhe == null || soGhe.trim().isEmpty()) {
            return false;
        }

        // Kiểm tra định dạng: [A-C][0-9]{2}
        return soGhe.matches("^[A-C]\\d{2}$");
    }
}