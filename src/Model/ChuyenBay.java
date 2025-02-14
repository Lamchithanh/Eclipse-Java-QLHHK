package Model;

public class    ChuyenBay {
    private String maChuyenBay;
    private String sanBay;
    private String changBay;
    private String ngayBay;
    private String nhaGa;
    private int soGhe;
    private String tinhTrang;
    private String maMaybay;
    private String maHang;
    private double giaVe;
    private String diemDi;    // Thêm điểm đi
    private String diemDen;   // Thêm điểm đến

    // Constructor cập nhật
    public ChuyenBay(String maChuyenBay, String sanBay, String changBay, 
                     String ngayBay, String nhaGa, int soGhe, String tinhTrang, 
                     String maMaybay, String maHang, String diemDi, String diemDen) {
        this.maChuyenBay = maChuyenBay;
        this.sanBay = sanBay;
        this.changBay = changBay;
        this.ngayBay = ngayBay;
        this.nhaGa = nhaGa;
        this.soGhe = soGhe;
        this.tinhTrang = tinhTrang;
        this.maMaybay = maMaybay;
        this.maHang = maHang;
        this.diemDi = diemDi;
        this.diemDen = diemDen;
    }

    // Thêm getter và setter cho điểm đi
    public String getDiemDi() {
        return diemDi;
    }

    public void setDiemDi(String diemDi) {
        this.diemDi = diemDi;
    }

    // Thêm getter và setter cho điểm đến
    public String getDiemDen() {
        return diemDen;
    }

    public void setDiemDen(String diemDen) {
        this.diemDen = diemDen;
    }

    // Các getter và setter khác giữ nguyên
    public double getGiaVe() {
        return giaVe;
    }
    
    public void setGiaVe(double giaVe) {
        this.giaVe = giaVe;
    }

    public String getMaChuyenBay() {
        return maChuyenBay;
    }

    public void setMaChuyenBay(String maChuyenBay) {
        this.maChuyenBay = maChuyenBay;
    }

    public String getSanBay() {
        return sanBay;
    }

    public void setSanBay(String sanBay) {
        this.sanBay = sanBay;
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

    public String getNhaGa() {
        return nhaGa;
    }

    public void setNhaGa(String nhaGa) {
        this.nhaGa = nhaGa;
    }

    public int getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public String getMaMaybay() {
        return maMaybay;
    }

    public void setMaMaybay(String maMaybay) {
        this.maMaybay = maMaybay;
    }

    public String getMaHang() {
        return maHang;
    }

    public void setMaHang(String maHang) {
        this.maHang = maHang;
    }
}