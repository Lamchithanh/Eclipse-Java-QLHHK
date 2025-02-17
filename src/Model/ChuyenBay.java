package Model;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ChuyenBay {
    private String maChuyenBay;
    private String sanBay;
    private String changBay;
    private Date ngayBay;  // Thay đổi kiểu dữ liệu thành java.sql.Date
    private String nhaGa;
    private int soGhe;
    private String tinhTrang;
    private String maMaybay;
    private String maHang;
    private double giaVe;
    private String diemDi;    
    private String diemDen;   
    private String gioKhoiHanh;  
    private String gioHaCanh;    
    private int thoiGianBay;
    private String tenSanBay; 

    // Format chuẩn cho ngày
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    // Constructor cập nhật
    public ChuyenBay(String maChuyenBay, String sanBay, String changBay, 
                    String ngayBayStr, String nhaGa, int soGhe, String tinhTrang, 
                    String maMaybay, String maHang, String diemDi, String diemDen, 
                    String tenSanBay) {
        this.maChuyenBay = maChuyenBay;
        this.sanBay = sanBay;
        this.changBay = changBay;
        setNgayBayFromString(ngayBayStr);
        this.nhaGa = nhaGa;
        this.soGhe = soGhe;
        this.tinhTrang = tinhTrang;
        this.maMaybay = maMaybay;
        this.maHang = maHang;
        this.diemDi = diemDi;
        this.diemDen = diemDen;
        this.tenSanBay = tenSanBay;
    }

    // Thêm phương thức hỗ trợ chuyển đổi String sang java.sql.Date
    private void setNgayBayFromString(String ngayBayStr) {
        if (ngayBayStr == null || ngayBayStr.trim().isEmpty()) {
            this.ngayBay = null;
            return;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsed = sdf.parse(ngayBayStr);
            this.ngayBay = new java.sql.Date(parsed.getTime());
        } catch (ParseException e) {
            this.ngayBay = null;
        }
    }

    public boolean hasValidNgayBay() {
        return ngayBay != null;
    }
    
    
    // Getter và setter cho ngayBay
    @SuppressWarnings("exports")
    public Date getNgayBay() {
        return ngayBay;
    }

    @SuppressWarnings("exports")
    public void setNgayBay(Date ngayBay) {
        this.ngayBay = ngayBay;
    }

    // Phương thức format ngày bay để hiển thị
    public String getNgayBayFormatted() {
        if (ngayBay == null) {
            return "Chưa xác định";
        }
        try {
            return dateFormat.format(ngayBay);
        } catch (Exception e) {
            return "Lỗi định dạng";
        }
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
 // Thêm getter và setter cho các trường mới
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
    
    @Override
    public String toString() {
        return this.diemDi + " - " + this.diemDen;
    }

    public String getTenSanBay() {
        return tenSanBay != null ? tenSanBay : sanBay;
    }

    public void setTenSanBay(String tenSanBay) {
        this.tenSanBay = tenSanBay;
    }

    public String getMaSanBay() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMaSanBay'");
    }
}