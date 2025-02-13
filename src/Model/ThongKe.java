package Model;

public class ThongKe {
    private String maChuyenBay;
    private String tenSanBay;
    private String changBay;
    private String ngayBay;
    private int soVeDaDat;
    private String tinhTrang;

    // Constructor
    public ThongKe(String maChuyenBay, String tenSanBay, String changBay, 
                   String ngayBay, int soVeDaDat, String tinhTrang) {
        this.maChuyenBay = maChuyenBay;
        this.tenSanBay = tenSanBay;
        this.changBay = changBay;
        this.ngayBay = ngayBay;
        this.soVeDaDat = soVeDaDat;
        this.tinhTrang = tinhTrang;
    }

    // Getters and Setters
    public String getMaChuyenBay() {
        return maChuyenBay;
    }

    public void setMaChuyenBay(String maChuyenBay) {
        this.maChuyenBay = maChuyenBay;
    }

    public String getTenSanBay() {
        return tenSanBay;
    }

    public void setTenSanBay(String tenSanBay) {
        this.tenSanBay = tenSanBay;
    }

    public String getChangBay() {
        return changBay;
    }

    public void setChangBay(String changBay) {
        this.changBay = changBay;
    }

    public String getNgayBay() {
        return ngayBay;
    }

    public void setNgayBay(String ngayBay) {
        this.ngayBay = ngayBay;
    }

    public int getSoVeDaDat() {
        return soVeDaDat;
    }

    public void setSoVeDaDat(int soVeDaDat) {
        this.soVeDaDat = soVeDaDat;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }
}