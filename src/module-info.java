/**
 * 
 */
/**
 * 
 */
module Quizapp {
    requires java.base; // Mặc định có thể bỏ qua
    requires java.sql; // Nếu bạn sử dụng MySQL Connector
    requires java.desktop; // Nếu bạn sử dụng Swing, AWT
    requires itextpdf; // Nếu dùng itextpdf
    requires jcalendar; // Nếu dùng jcalendar

    // Nếu có package nào bạn muốn mở ra cho module khác, thêm như sau:
    // exports com.yourpackage;
}
