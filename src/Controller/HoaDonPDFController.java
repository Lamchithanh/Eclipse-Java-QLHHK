package Controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.BaseColor;

import Model.DatVe;
import Model.ChuyenBay;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class HoaDonPDFController {
	private static BaseFont baseFont;
	static {
		try {
			// Sử dụng font đi kèm với thư viện hoặc font hệ thống
			baseFont = BaseFont.createFont("C:\\Windows\\Fonts\\arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Cập nhật lại các font sử dụng baseFont
	private static final Font TITLE_FONT = new Font(baseFont, 24, Font.BOLD);
	private static final Font HEADER_FONT = new Font(baseFont, 16, Font.BOLD);
	private static final Font NORMAL_FONT = new Font(baseFont, 12, Font.NORMAL);
	private static final Font BOLD_FONT = new Font(baseFont, 12, Font.BOLD);
	private static final Font ITALIC_FONT = new Font(baseFont, 10, Font.ITALIC);

	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	@SuppressWarnings("unused")
	private static final BaseColor LIGHT_GRAY = BaseColor.LIGHT_GRAY;

	public static void xuatHoaDonPDF(DatVe datVe, String filePath) throws Exception {
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, new FileOutputStream(filePath));
		document.open();

		// Thêm tiêu đề
		themTieuDe(document);

		// Thêm thông tin hóa đơn
		themThongTinHoaDon(document, datVe);

		// Thêm thông tin chuyến bay
		themThongTinChuyenBay(document, datVe.getChuyenBay());

		// Thêm thông tin hành khách
		themThongTinHanhKhach(document, datVe);

		// Thêm thông tin đặt vé
		themThongTinDatVe(document, datVe);

		// Thêm thông tin thanh toán
		themThongTinThanhToan(document, datVe);

		// Thêm yêu cầu đặc biệt nếu có
		if (coYeuCauDacBiet(datVe)) {
			themYeuCauDacBiet(document, datVe);
		}

		// Thêm điều khoản và điều kiện
		themDieuKhoanDieuKien(document);

		document.close();
	}

	private static void themTieuDe(Document document) throws DocumentException {
		Paragraph title = new Paragraph("HÓA ĐƠN VÉ MÁY BAY", TITLE_FONT);
		title.setAlignment(Element.ALIGN_CENTER);
		title.setSpacingAfter(20);
		document.add(title);
	}
	
	private static String formatDateTime(Object timeObj) {
	    if (timeObj == null) {
	        return "Chưa xác định";
	    }
	    
	    try {
	        if (timeObj instanceof String) {
	            // Nếu là String, kiểm tra format HH:mm:ss
	            String timeStr = (String) timeObj;
	            if (timeStr.matches("\\d{2}:\\d{2}:\\d{2}")) {
	                return timeStr.substring(0, 5); // Chỉ lấy HH:mm
	            }
	            return timeStr;
	        }
	        
	        if (timeObj instanceof java.sql.Time) {
	            return TIME_FORMAT.format((java.sql.Time) timeObj);
	        }
	        
	        if (timeObj instanceof java.util.Date) {
	            return TIME_FORMAT.format((java.util.Date) timeObj);
	        }
	        
	        if (timeObj instanceof java.time.LocalTime) {
	            return ((java.time.LocalTime) timeObj).format(DateTimeFormatter.ofPattern("HH:mm"));
	        }
	        
	        return timeObj.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Chưa xác định";
	    }
	}
	
	private static String formatDate(Object dateObj) {
	    if (dateObj == null) return "Chưa xác định";
	    
	    try {
	        // Xử lý java.sql.Date
	        if (dateObj instanceof java.sql.Date) {
	            return DATE_FORMAT.format(new Date(((java.sql.Date) dateObj).getTime()));
	        }
	        
	        // Xử lý java.util.Date
	        if (dateObj instanceof java.util.Date) {
	            return DATE_FORMAT.format((java.util.Date) dateObj);
	        }
	        
	        // Xử lý LocalDateTime
	        if (dateObj instanceof java.time.LocalDateTime) {
	            Date convertedDate = Date.from(
	                ((java.time.LocalDateTime) dateObj)
	                .atZone(java.time.ZoneId.systemDefault())
	                .toInstant()
	            );
	            return DATE_FORMAT.format(convertedDate);
	        }
	        
	        // Xử lý LocalDate
	        if (dateObj instanceof java.time.LocalDate) {
	            Date convertedDate = Date.from(
	                ((java.time.LocalDate) dateObj)
	                .atStartOfDay(java.time.ZoneId.systemDefault())
	                .toInstant()
	            );
	            return DATE_FORMAT.format(convertedDate);
	        }
	        
	        // Xử lý String
	        if (dateObj instanceof String) {
	            try {
	                Date parsedDate = DATE_FORMAT.parse((String) dateObj);
	                return DATE_FORMAT.format(parsedDate);
	            } catch (Exception e) {
	                try {
	                    Date parsedDate = DATE_TIME_FORMAT.parse((String) dateObj);
	                    return DATE_FORMAT.format(parsedDate);
	                } catch (Exception ex) {
	                    return (String) dateObj;
	                }
	            }
	        }
	        
	        // Nếu không phải các kiểu trên, chuyển về String
	        return dateObj.toString();
	        
	    } catch (Exception e) {
	        System.err.println("Lỗi format ngày: " + e.getMessage());
	        e.printStackTrace();
	        return "Chưa xác định";
	    }
	}

	
	private static void themThongTinHoaDon(Document document, DatVe datVe) throws DocumentException {
	    PdfPTable table = new PdfPTable(2);
	    table.setWidthPercentage(100);

	    themDongThongTin(table, "Mã Đặt Vé:", datVe.getMaDatVe());

	    // Log chi tiết thông tin ngày đặt
	    System.out.println("=== CHI TIẾT NGÀY ĐẶT ===");
	    System.out.println("Ngày Đặt (Raw): " + datVe.getNgayDat());
	    System.out.println("Kiểu Ngày Đặt: " + (datVe.getNgayDat() != null ? datVe.getNgayDat().getClass() : "null"));

	    // Xử lý ngày đặt an toàn
	    String ngayDatStr = "Chưa xác định";
	    try {
	        Object ngayDat = datVe.getNgayDat();
	        
	        if (ngayDat != null) {
	            if (ngayDat instanceof java.time.LocalDateTime) {
	                Date convertedDate = Date.from(
	                    ((java.time.LocalDateTime) ngayDat)
	                    .atZone(java.time.ZoneId.systemDefault())
	                    .toInstant()
	                );
	                ngayDatStr = DATE_TIME_FORMAT.format(convertedDate);
	                System.out.println("Ngày Đặt là LocalDateTime: " + ngayDatStr);
	            } 
	            else if (ngayDat instanceof Date) {
	                ngayDatStr = DATE_TIME_FORMAT.format((Date) ngayDat);
	                System.out.println("Ngày Đặt là Date: " + ngayDatStr);
	            } 
	            else if (ngayDat instanceof java.sql.Date) {
	                ngayDatStr = DATE_TIME_FORMAT.format(
	                    new Date(((java.sql.Date) ngayDat).getTime())
	                );
	                System.out.println("Ngày Đặt là java.sql.Date: " + ngayDatStr);
	            } 
	            else if (ngayDat instanceof String) {
	                ngayDatStr = ngayDat.toString();
	                System.out.println("Ngày Đặt là String: " + ngayDatStr);
	            } 
	            else {
	                System.err.println("Kiểu ngày không được hỗ trợ: " + ngayDat.getClass());
	                ngayDatStr = ngayDat.toString();
	            }
	        }
	    } catch (Exception e) {
	        System.err.println("LỖI định dạng ngày đặt: " + e.getMessage());
	        e.printStackTrace();
	    }

	    System.out.println("Đã format ngày đặt: " + ngayDatStr);
	    themDongThongTin(table, "Ngày Đặt:", ngayDatStr);
	    
	    String ngayBayStr = formatDate(datVe.getNgayBay());
	    System.out.println("Đã format ngày bay: " + ngayBayStr);
	    themDongThongTin(table, "Ngày Bay:", ngayBayStr);

	    document.add(table);
	    themDuongKe(document);

	    document.add(table);
	    themDuongKe(document);
	}

	private static void themThongTinChuyenBay(Document document, ChuyenBay chuyenBay) throws DocumentException {
	    // Thêm header
	    Paragraph header = new Paragraph("THÔNG TIN CHUYẾN BAY", HEADER_FONT);
	    header.setSpacingBefore(10);
	    header.setSpacingAfter(10);
	    document.add(header);

	    // Log thông tin chuyến bay để debug
	    System.out.println("=== CHI TIẾT CHUYẾN BAY ===");
	    System.out.println("Mã Chuyến Bay: " + chuyenBay.getMaChuyenBay());
	    System.out.println("Điểm Đi: " + chuyenBay.getDiemDi());
	    System.out.println("Điểm Đến: " + chuyenBay.getDiemDen());
	    System.out.println("Ngày Bay (Raw): " + chuyenBay.getNgayBay());
	    System.out.println("Giờ Khởi Hành (Raw): " + chuyenBay.getGioKhoiHanh());
	    System.out.println("Giờ Hạ Cánh (Raw): " + chuyenBay.getGioHaCanh());

	    // Tạo bảng thông tin
	    PdfPTable table = new PdfPTable(2);
	    table.setWidthPercentage(100);

	    // Thông tin cơ bản chuyến bay
	    themDongThongTin(table, "Mã Chuyến Bay:", chuyenBay.getMaChuyenBay());
	    themDongThongTin(table, "Điểm Khởi Hành:", chuyenBay.getDiemDi());
	    themDongThongTin(table, "Điểm Đến:", chuyenBay.getDiemDen());

	    // Xử lý ngày bay
	    String ngayBayStr = "Chưa xác định";
	    try {
	        Object ngayBay = chuyenBay.getNgayBay();
	        if (ngayBay != null) {
	            if (ngayBay instanceof String) {
	                ngayBayStr = (String) ngayBay;
	            } else if (ngayBay instanceof java.util.Date) {
	                ngayBayStr = DATE_FORMAT.format((java.util.Date) ngayBay);
	            } else if (ngayBay instanceof java.sql.Date) {
	                ngayBayStr = DATE_FORMAT.format(new java.util.Date(((java.sql.Date) ngayBay).getTime()));
	            } else {
	                ngayBayStr = ngayBay.toString();
	            }
	        }
	    } catch (Exception e) {
	        System.err.println("Lỗi định dạng ngày bay: " + e.getMessage());
	        e.printStackTrace();
	    }
	    themDongThongTin(table, "Ngày Bay:", ngayBayStr);

	    // Xử lý giờ khởi hành
	    String gioKhoiHanhStr = formatDateTime(chuyenBay.getGioKhoiHanh());
	    themDongThongTin(table, "Giờ Khởi Hành:", gioKhoiHanhStr);

	    // Xử lý giờ hạ cánh
	    String gioHaCanhStr = formatDateTime(chuyenBay.getGioHaCanh());
	    themDongThongTin(table, "Giờ Hạ Cánh:", gioHaCanhStr);

	    // Xử lý thời gian bay
	    String thoiGianBayStr = "Chưa xác định";
	    int thoiGianBay = chuyenBay.getThoiGianBay();
	    if (thoiGianBay > 0) {
	        thoiGianBayStr = thoiGianBay + " phút";
	    }
	    themDongThongTin(table, "Thời Gian Bay:", thoiGianBayStr);

	    // Thông tin bổ sung
	    themDongThongTin(table, "Chặng Bay:", chuyenBay.getChangBay());
	    themDongThongTin(table, "Nhà Ga:", chuyenBay.getNhaGa());
	    
	    if (chuyenBay.getTenSanBay() != null && !chuyenBay.getTenSanBay().trim().isEmpty()) {
	        themDongThongTin(table, "Sân Bay:", chuyenBay.getTenSanBay());
	    }

	    // Thêm bảng vào document
	    document.add(table);
	    
	    // Thêm đường kẻ phân cách
	    themDuongKe(document);
	    
	    // Log kết quả xử lý
	    System.out.println("=== KẾT QUẢ XỬ LÝ ===");
	    System.out.println("Ngày Bay đã format: " + ngayBayStr);
	    System.out.println("Giờ Khởi Hành đã format: " + gioKhoiHanhStr);
	    System.out.println("Giờ Hạ Cánh đã format: " + gioHaCanhStr);
	    System.out.println("Thời Gian Bay đã format: " + thoiGianBayStr);
	}

	private static void themThongTinHanhKhach(Document document, DatVe datVe) throws DocumentException {
	    Paragraph header = new Paragraph("THÔNG TIN HÀNH KHÁCH", HEADER_FONT);
	    header.setSpacingBefore(10);
	    header.setSpacingAfter(10);
	    document.add(header);

	    PdfPTable table = new PdfPTable(2);
	    table.setWidthPercentage(100);

	    themDongThongTin(table, "Họ và Tên:", datVe.getHoTen());
	    themDongThongTin(table, "CMND/CCCD:", datVe.getCMND());

	    // Xử lý ngày sinh an toàn
	    String ngaySinhStr = "Chưa xác định";
	    try {
	        // Lấy giá trị ngày sinh
	        Object ngaySinh = datVe.getNgaySinh();
	        
	        if (ngaySinh != null) {
	            // Nếu là chuỗi
	            if (ngaySinh instanceof String) {
	                ngaySinhStr = (String) ngaySinh;
	            } 
	            // Nếu là java.util.Date
	            else if (ngaySinh instanceof Date) {
	                ngaySinhStr = DATE_FORMAT.format((Date) ngaySinh);
	            } 
	            // Nếu là java.sql.Date
	            else if (ngaySinh instanceof java.sql.Date) {
	                ngaySinhStr = DATE_FORMAT.format(new Date(((java.sql.Date) ngaySinh).getTime()));
	            } 
	            // Thử parse từ các định dạng khác
	            else {
	                ngaySinhStr = ngaySinh.toString();
	            }
	        }
	    } catch (Exception e) {
	        System.err.println("Lỗi định dạng ngày sinh: " + e.getMessage());
	    }

	    System.out.println("Đã format ngày sinh: " + ngaySinhStr);
	    themDongThongTin(table, "Ngày Sinh:", ngaySinhStr);

	    themDongThongTin(table, "Giới Tính:", datVe.getGioiTinh());
	    themDongThongTin(table, "Quốc Tịch:", datVe.getQuocTich());
	    themDongThongTin(table, "Số Điện Thoại:", datVe.getSoDienThoai());

	    document.add(table);
	    themDuongKe(document);
	}

	private static void themThongTinDatVe(Document document, DatVe datVe) throws DocumentException {
		Paragraph header = new Paragraph("THÔNG TIN ĐẶT VÉ", HEADER_FONT);
		header.setSpacingBefore(10);
		header.setSpacingAfter(10);
		document.add(header);

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);

		themDongThongTin(table, "Hạng Vé:", datVe.getHangVe());
		themDongThongTin(table, "Số Lượng Vé:", String.valueOf(datVe.getSoLuong()));
		themDongThongTin(table, "Mã Giảm Giá:", datVe.getMaGiamGia() != null ? datVe.getMaGiamGia() : "Không có");

		document.add(table);
		themDuongKe(document);
	}

	private static void themThongTinThanhToan(Document document, DatVe datVe) throws DocumentException {
		Paragraph header = new Paragraph("THÔNG TIN THANH TOÁN", HEADER_FONT);
		header.setSpacingBefore(10);
		header.setSpacingAfter(10);
		document.add(header);

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);

		themDongThongTin(table, "Tổng Tiền:", String.format("%,.0f VND", datVe.getTongGia()));
		themDongThongTin(table, "Phương Thức Thanh Toán:", datVe.getPhuongThucThanhToan());
		themDongThongTin(table, "Trạng Thái Thanh Toán:",
				datVe.isXacNhanThanhToan() ? "Đã thanh toán" : "Chưa thanh toán");

		document.add(table);
		themDuongKe(document);
	}

	private static void themYeuCauDacBiet(Document document, DatVe datVe) throws DocumentException {
		Paragraph header = new Paragraph("YÊU CẦU ĐẶC BIỆT", HEADER_FONT);
		header.setSpacingBefore(10);
		header.setSpacingAfter(10);
		document.add(header);

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);

		if (datVe.isSuatAnDacBiet())
			themDongThongTin(table, "Suất Ăn Đặc Biệt:", "Có");
		if (datVe.isHoTroYTe())
			themDongThongTin(table, "Hỗ Trợ Y Tế:", "Có");
		if (datVe.isChoNgoiUuTien())
			themDongThongTin(table, "Chỗ Ngồi Ưu Tiên:", "Có");
		if (datVe.isHanhLyDacBiet())
			themDongThongTin(table, "Hành Lý Đặc Biệt:", "Có");

		document.add(table);
		themDuongKe(document);
	}

	private static void themDieuKhoanDieuKien(Document document) throws DocumentException {
		Paragraph header = new Paragraph("Điều khoản và điều kiện:", BOLD_FONT);
		header.setSpacingBefore(10);
		header.setSpacingAfter(5);
		document.add(header);

		Paragraph terms = new Paragraph();
		terms.setFont(ITALIC_FONT);
		terms.add("1. Vé đã xuất không được hoàn lại hoặc thay đổi thông tin.\n");
		terms.add("2. Hành khách cần có mặt tại sân bay ít nhất 2 tiếng trước giờ khởi hành.\n");
		terms.add("3. Hành lý xách tay không được vượt quá 7kg.\n");
		terms.add("4. Vui lòng mang theo giấy tờ tùy thân khi làm thủ tục.");

		document.add(terms);
	}

	private static void themDongThongTin(PdfPTable table, String label, String value) {
	    PdfPCell labelCell = new PdfPCell(new Phrase(label, BOLD_FONT));
	    PdfPCell valueCell = new PdfPCell(new Phrase(value, NORMAL_FONT));

	    labelCell.setBorder(Rectangle.NO_BORDER);
	    valueCell.setBorder(Rectangle.NO_BORDER);

	    labelCell.setPadding(5);
	    valueCell.setPadding(5);

	    table.addCell(labelCell);
	    table.addCell(valueCell);
	}

	private static void themDuongKe(Document document) throws DocumentException {
		LineSeparator line = new LineSeparator();
		line.setLineColor(BaseColor.LIGHT_GRAY);
		document.add(new Chunk(line));
	}

	private static boolean coYeuCauDacBiet(DatVe datVe) {
		return datVe.isSuatAnDacBiet() || datVe.isHoTroYTe() || datVe.isChoNgoiUuTien() || datVe.isHanhLyDacBiet();
	}
}