module QuanLyHangKhong {
    requires java.desktop;
    requires java.sql;
	requires jfreechart;
	requires java.xml;
	requires itextpdf;
    
    exports View;
    exports Controller;
    exports Model;
    exports Database;
}