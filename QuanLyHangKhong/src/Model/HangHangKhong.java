package Model;

public class HangHangKhong {
    private String maHang;
    private String tenHang;
    private String diaChi;
    private String soDienThoai;
    private String email;

    public HangHangKhong() {}

    public HangHangKhong(String maHang, String tenHang, String diaChi, String soDienThoai, String email) {
        this.maHang = maHang;
        this.tenHang = tenHang;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.email = email;
    }

    // Getters and setters
    public String getMaHang() { return maHang; }
    public void setMaHang(String maHang) { this.maHang = maHang; }

    public String getTenHang() { return tenHang; }
    public void setTenHang(String tenHang) { this.tenHang = tenHang; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}