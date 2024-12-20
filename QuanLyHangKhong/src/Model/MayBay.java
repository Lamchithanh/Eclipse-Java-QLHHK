package Model;

public class MayBay {
    private String maMayBay;
    private String loaiMayBay;
    private int sucChua;
    private String maHang;

    // Constructor
    public MayBay(String maMayBay, String loaiMayBay, int sucChua, String maHang) {
        this.maMayBay = maMayBay;
        this.loaiMayBay = loaiMayBay;
        this.sucChua = sucChua;
        this.maHang = maHang;
    }

    // Getters and setters
    public String getMaMayBay() {
        return maMayBay;
    }

    public void setMaMayBay(String maMayBay) {
        this.maMayBay = maMayBay;
    }

    public String getLoaiMayBay() {
        return loaiMayBay;
    }

    public void setLoaiMayBay(String loaiMayBay) {
        this.loaiMayBay = loaiMayBay;
    }

    public int getSucChua() {
        return sucChua;
    }

    public void setSucChua(int sucChua) {
        this.sucChua = sucChua;
    }

    public String getMaHang() {
        return maHang;
    }

    public void setMaHang(String maHang) {
        this.maHang = maHang;
    }
}