package Model;

public class LichBay {
    private String maChuyenBay; // Flight Code
    private String gioKhoiHanh; // Departure Time
    private String gioHaCanh;   // Arrival Time
    private int thoiGianBay;    // Flight Duration (in minutes)

    // Constructor
    public LichBay(String maChuyenBay, String gioKhoiHanh, String gioHaCanh, int thoiGianBay) {
        this.maChuyenBay = maChuyenBay;
        this.gioKhoiHanh = gioKhoiHanh;
        this.gioHaCanh = gioHaCanh;
        this.thoiGianBay = thoiGianBay;
    }

    // Getter and Setter methods
    public String getMaChuyenBay() {
        return maChuyenBay;
    }

    public void setMaChuyenBay(String maChuyenBay) {
        this.maChuyenBay = maChuyenBay;
    }

    public String getGioKhoiHanh() {
        return gioKhoiHanh;
    }

    public void setGioKhoiHanh(String gioKhoiHanh) {
        this.gioKhoiHanh = gioKhoiHanh;
    }

    public String getGioHaCanh() {
        return gioHaCanh;
    }

    public void setGioHaCanh(String gioHaCanh) {
        this.gioHaCanh = gioHaCanh;
    }

    public int getThoiGianBay() {
        return thoiGianBay;
    }

    public void setThoiGianBay(int thoiGianBay) {
        this.thoiGianBay = thoiGianBay;
    }
}
