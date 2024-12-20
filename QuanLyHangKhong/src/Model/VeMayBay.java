package Model;

import java.sql.Date;

public class VeMayBay {
    private String maVe;
    private String maChuyenBay;
    private String maKhachHang;
    private Date ngayDat;
    private String tenKhachHang;
    private String cmnd;

    // Constructor có 5 tham số: maVe, maChuyenBay, maKhachHang, ngayDat, tenKhachHang
    public VeMayBay(String maVe, String maChuyenBay, String maKhachHang, Date ngayDat, String tenKhachHang, String cmnd) {
        this.maVe = maVe;
        this.maChuyenBay = maChuyenBay;
        this.maKhachHang = maKhachHang;
        this.ngayDat = ngayDat;
        this.tenKhachHang = tenKhachHang;
        this.cmnd = cmnd;
    }

    // Getter and Setter methods
    public String getMaVe() {
        return maVe;
    }

    public void setMaVe(String maVe) {
        this.maVe = maVe;
    }

    public String getMaChuyenBay() {
        return maChuyenBay;
    }

    public void setMaChuyenBay(String maChuyenBay) {
        this.maChuyenBay = maChuyenBay;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public Date getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(Date ngayDat) {
        this.ngayDat = ngayDat;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }


    @Override
    public String toString() {
        return "VeMayBay{" +
               "maVe='" + maVe + '\'' +
               ", maChuyenBay='" + maChuyenBay + '\'' +
               ", maKhachHang='" + maKhachHang + '\'' +
               ", ngayDat=" + ngayDat +
               ", cmnd='" + cmnd + '\'' +
               '}';
    }
}
