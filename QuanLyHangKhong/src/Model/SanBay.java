package Model;

public class SanBay {
    private String maSanBay;
    private String tenSanBay;

    // Constructor
    public SanBay() {}

    public SanBay(String maSanBay, String tenSanBay) {
        this.maSanBay = maSanBay;
        this.tenSanBay = tenSanBay;
    }

    // Getter for Mã Sân Bay
    public String getMaSanBay() {
        return maSanBay;
    }

    // Setter for Mã Sân Bay
    public void setMaSanBay(String maSanBay) {
        this.maSanBay = maSanBay;
    }

    // Getter for Tên Sân Bay
    public String getTenSanBay() {
        return tenSanBay;
    }

    // Setter for Tên Sân Bay
    public void setTenSanBay(String tenSanBay) {
        this.tenSanBay = tenSanBay;
    }

    // Optional: toString method for easier debugging
    @Override
    public String toString() {
        return "SanBay{" +
                "maSanBay='" + maSanBay + '\'' +
                ", tenSanBay='" + tenSanBay + '\'' +
                '}';
    }
}