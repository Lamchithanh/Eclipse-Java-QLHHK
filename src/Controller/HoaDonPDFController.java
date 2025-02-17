package Controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.qrcode.EncodeHintType;
import com.itextpdf.text.pdf.qrcode.ErrorCorrectionLevel;

import Database.MYSQLDB;
import Model.DatVe;
import Model.ChuyenBay;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;


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

	// Updated color palette similar to Swing view
    private static final BaseColor PRIMARY_COLOR = new BaseColor(25, 118, 210);
    private static final BaseColor SECONDARY_COLOR = new BaseColor(41, 128, 185);
    private static final BaseColor TEXT_COLOR = new BaseColor(33, 33, 33);
    private static final BaseColor LABEL_COLOR = new BaseColor(100, 100, 100);

    // Updated fonts with more style
	private static final Font TITLE_FONT = new Font(baseFont, 20, Font.BOLD, PRIMARY_COLOR);
	private static final Font HEADER_FONT = new Font(baseFont, 14, Font.BOLD, PRIMARY_COLOR);
	private static final Font SECTION_HEADER_FONT = HEADER_FONT;
	private static final Font LABEL_FONT = new Font(baseFont, 9, Font.BOLD, LABEL_COLOR);
	private static final Font BOLD_FONT = LABEL_FONT;
	private static final Font NORMAL_FONT = new Font(baseFont, 9, Font.NORMAL, TEXT_COLOR);
	private static final Font ITALIC_FONT = new Font(baseFont, 8, Font.ITALIC, SECONDARY_COLOR);
	private static final Font BILINGUAL_LABEL_FONT = ITALIC_FONT;

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    // Bilingual labels method
    private static String getBilingualLabel(String key) {
        switch (key) {
            // Flight Information
            case "Flight No": return "Số Hiệu (Flight No):";
            case "Airline": return "Hãng Bay (Airline):";
            case "Aircraft": return "Máy Bay (Aircraft):";
            case "Route": return "Hành Trình (Route):";
            case "Date": return "Ngày Bay (Date):";
            case "Departure Time": return "Giờ Khởi Hành (Departure):";
            case "Arrival Time": return "Giờ Hạ Cánh (Arrival):";
            case "Flight Duration": return "Thời Gian Bay (Duration):";
            case "Airport": return "Sân Bay (Airport):";
            case "Terminal": return "Nhà Ga (Terminal):";
            case "Flight Status": return "Trạng Thái (Status):";
            case "Seat Class": return "Hạng Ghế (Class):";

            // Passenger Information
            case "Name": return "Họ Tên (Name):";
            case "ID": return "CMND/CCCD (ID):";
            case "Date of Birth": return "Ngày Sinh (Date of Birth):";
            case "Gender": return "Giới Tính (Gender):";
            case "Nationality": return "Quốc Tịch (Nationality):";
            case "Phone": return "Điện Thoại (Phone):";
            case "Email": return "Email (Email):";

            // Payment Details
            case "Booking Date": return "Ngày Đặt (Booking Date):";
            case "Payment Method": return "Phương Thức Thanh Toán (Payment):";
            case "Amount": return "Tổng Tiền (Amount):";
            case "Ticket Price": return "Giá Vé (Ticket Price):";
            case "Quantity": return "Số Lượng (Quantity):";
            case "Discount Code": return "Mã Giảm Giá (Discount):";
            case "Total Amount": return "Tổng Cộng (Total):";
            case "Payment Status": return "Trạng Thái (Status):";

            // Special Services
            case "Special Meal": return "Suất Ăn Đặc Biệt (Special Meal):";
            case "Medical Assistance": return "Hỗ Trợ Y Tế (Medical):";
            case "Priority Seating": return "Chỗ Ngồi Ưu Tiên (Priority):";
            case "Special Baggage": return "Hành Lý Đặc Biệt (Special Baggage):";

            // Emergency Contact
            case "Contact Name": return "Người Liên Hệ (Contact):";
            case "Contact Phone": return "SĐT Liên Hệ (Phone):";

            default: return key + ":";
        }
    }

	@SuppressWarnings("unused")
	private static final BaseColor LIGHT_GRAY = BaseColor.LIGHT_GRAY;

	public static void xuatHoaDonPDF(DatVe datVe, String filePath) throws Exception {
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
		document.open();
	
		// Tạo màu sắc và font
		BaseColor primaryColor = new BaseColor(25, 118, 210); // Xanh dương chủ đạo
		BaseColor secondaryColor = new BaseColor(41, 128, 185); // Xanh dương phụ
	
		// Header với logo và thông tin hãng bay
		PdfPTable headerTable = new PdfPTable(2);
		headerTable.setWidthPercentage(100);
		headerTable.setWidths(new float[]{2, 3});
	
		// Đọc logo từ resources
		InputStream inputStream = HoaDonPDFController.class.getResourceAsStream("/image/airplane11.png");
		if (inputStream != null) {
			byte[] bytes = inputStream.readAllBytes();
			Image logo = Image.getInstance(bytes);
			logo.scaleToFit(100, 100);
			PdfPCell logoCell = new PdfPCell(logo);
			logoCell.setBorder(Rectangle.NO_BORDER);
			headerTable.addCell(logoCell);
		} else {
			// Nếu không tìm thấy logo, thêm một cell trống hoặc cell có text thay thế
			PdfPCell logoCell = new PdfPCell(new Phrase("NTN AIRLINES", new Font(baseFont, 20, Font.BOLD, primaryColor)));
			logoCell.setBorder(Rectangle.NO_BORDER);
			headerTable.addCell(logoCell);
			
		}
	
		// Thông tin hãng bay
		PdfPCell airlineInfoCell = new PdfPCell();
		airlineInfoCell.setBorder(Rectangle.NO_BORDER);
		Paragraph airlineInfo = new Paragraph();
		airlineInfo.add(new Chunk("NTN AIRLINES\n", new Font(baseFont, 20, Font.BOLD, primaryColor)));
		airlineInfo.add(new Chunk("International Flights\n", new Font(baseFont, 12, Font.ITALIC, secondaryColor)));
		airlineInfo.add(new Chunk("Booking Reference: " + datVe.getMaDatVe(), new Font(baseFont, 10, Font.NORMAL)));
		airlineInfoCell.addElement(airlineInfo);
		headerTable.addCell(airlineInfoCell);
	
		document.add(headerTable);
		document.add(Chunk.NEWLINE);
	
		// Thêm đường kẻ
		LineSeparator separator = new LineSeparator();
		separator.setLineColor(primaryColor);
		document.add(new Chunk(separator));
		document.add(Chunk.NEWLINE);
	
		// QR Code với thông tin chi tiết
		BarcodeQRCode qrCode = new BarcodeQRCode(
			formatDetailedQRData(datVe), 
			100, 
			100, 
			new HashMap<EncodeHintType, Object>() {{
				put(EncodeHintType.CHARACTER_SET, "UTF-8");
				put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			}}
		);																				
		Image qrImage = qrCode.getImage();
		qrImage.setAbsolutePosition(document.right() - 100, document.top() - 100);
		document.add(qrImage);
	
		// Thông tin hành khách
		PdfPTable passengerTable = createPassengerInfoTable(datVe);
		document.add(passengerTable);
	
		// Thông tin chuyến bay
		PdfPTable flightTable = createFlightInfoTable(datVe);
		document.add(flightTable);
	
		// Thông tin thanh toán
		PdfPTable paymentTable = createPaymentInfoTable(datVe);
		document.add(paymentTable);
	
		// Điều khoản
		document.add(createTermsSection());
	
		document.close();
	}
	
	private static PdfPTable createPassengerInfoTable(DatVe datVe) throws DocumentException {
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setSpacingBefore(5);
		table.setSpacingAfter(5);
	
		// Tiêu đề phần với phong cách song ngữ và màu sắc nhất quán
		PdfPCell headerCell = new PdfPCell(new Phrase("THÔNG TIN HÀNH KHÁCH - PASSENGER INFORMATION", SECTION_HEADER_FONT));
		headerCell.setColspan(2);
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerCell.setBorder(Rectangle.BOTTOM);
		table.addCell(headerCell);
	
		// Các thông tin chi tiết
		addDetailRow(table, "Name", datVe.getHoTen());
		addDetailRow(table, "ID", datVe.getCMND());
		addDetailRow(table, "Date of Birth", formatDate(datVe.getNgaySinh()));
		addDetailRow(table, "Gender", datVe.getGioiTinh());
		addDetailRow(table, "Nationality", datVe.getQuocTich());
		addDetailRow(table, "Phone", datVe.getSoDienThoai());
		
		// Thêm email nếu có
		if (datVe.getEmail() != null && !datVe.getEmail().isEmpty()) {
			addDetailRow(table, "Email", datVe.getEmail());
		}
	
		return table;
	}
	
	private static String getTenSanBay(String maSanBay) {
        try {
            Connection conn = MYSQLDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT TenSanBay FROM SanBay WHERE MaSanBay = ?"
            );
            stmt.setString(1, maSanBay);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("TenSanBay");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maSanBay; // Return code if name not found
    }

    private static String getTenMayBay(String maMayBay) {
        try {
            Connection conn = MYSQLDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT LoaiMayBay FROM MayBay WHERE MaMayBay = ?"
            );
            stmt.setString(1, maMayBay);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("LoaiMayBay");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maMayBay; // Return code if name not found
    }

    private static String getTenHangBay(String maHang) {
        try {
            Connection conn = MYSQLDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT TenHang FROM HangHangKhong WHERE MaHang = ?"
            );
            stmt.setString(1, maHang);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("TenHang");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maHang; // Return code if name not found
    }

	private static PdfPTable createFlightInfoTable(DatVe datVe) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);

        // Section header with bilingual title
        PdfPCell headerCell = new PdfPCell(new Phrase("THÔNG TIN CHUYẾN BAY - FLIGHT INFORMATION", SECTION_HEADER_FONT));
        headerCell.setColspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBorder(Rectangle.BOTTOM);
        table.addCell(headerCell);

        ChuyenBay flight = datVe.getChuyenBay();

        // Get detailed names from database
        String tenMayBay = getTenMayBay(flight.getMaMaybay());
        String tenHangBay = getTenHangBay(flight.getMaHang());
        String tenSanBay = getTenSanBay(flight.getTenSanBay());

        // Use addDetailRow with bilingual labels
        addDetailRow(table, "Flight No", flight.getMaChuyenBay());
        addDetailRow(table, "Airline", tenHangBay);
        addDetailRow(table, "Aircraft", tenMayBay);
        addDetailRow(table, "Route", flight.getChangBay());
        addDetailRow(table, "Date", formatDate(flight.getNgayBay()));
        addDetailRow(table, "Departure Time", flight.getGioKhoiHanh());
        addDetailRow(table, "Arrival Time", flight.getGioHaCanh());
        addDetailRow(table, "Flight Duration", flight.getThoiGianBay() + " minutes");
        addDetailRow(table, "Airport", tenSanBay);
        addDetailRow(table, "Terminal", flight.getNhaGa());

        return table;
    }

	private static PdfPTable createPaymentInfoTable(DatVe datVe) throws DocumentException {
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setSpacingBefore(5);
		table.setSpacingAfter(5);
	
		// Tiêu đề phần với phong cách song ngữ và màu sắc nhất quán
		PdfPCell headerCell = new PdfPCell(new Phrase("THÔNG TIN THANH TOÁN - PAYMENT DETAILS", SECTION_HEADER_FONT));
		headerCell.setColspan(2);
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerCell.setBorder(Rectangle.BOTTOM);
		table.addCell(headerCell);
	
		// Các thông tin thanh toán
		addDetailRow(table, "Total Amount", String.format("%,.0f VND", datVe.getTongGia()));
		addDetailRow(table, "Payment Method", datVe.getPhuongThucThanhToan());
		addDetailRow(table, "Payment Status", 
			datVe.isXacNhanThanhToan() ? "Đã thanh toán (Paid)" : "Chưa thanh toán (Pending)");
		
		if (datVe.getMaGiamGia() != null && !datVe.getMaGiamGia().isEmpty()) {
			addDetailRow(table, "Discount Code", datVe.getMaGiamGia());
		}
	
		return table;
	}

	private static Paragraph createTermsSection() {
		Paragraph termsSection = new Paragraph();
		termsSection.setSpacingBefore(5);  // Giảm khoảng cách trước
		termsSection.setSpacingAfter(5);   // Giảm khoảng cách sau
	
		// Tiêu đề song ngữ
		Chunk termsTitle = new Chunk("ĐIỀU KHOẢN VÀ ĐIỀU KIỆN - TERMS AND CONDITIONS", SECTION_HEADER_FONT);
		termsSection.add(termsTitle);
		termsSection.add(Chunk.NEWLINE);
	
		// Các điều khoản
		Font termsFont = new Font(baseFont, 10, Font.NORMAL);
		String[] terms = {
			"1. Vé không được hoàn lại hoặc chuyển nhượng. (Ticket is non-refundable and non-transferable.)",
			"2. Hành khách phải đến sân bay ít nhất 2 giờ trước giờ khởi hành. (Passenger must arrive at the airport at least 2 hours before departure.)",
			"3. Hành lý xách tay tối đa 7kg. (Maximum carry-on baggage weight is 7kg.)",
			"4. Yêu cầu giấy tờ tùy thân hợp lệ để làm thủ tục. (Valid ID is required for check-in.)",
			"5. Lịch bay và giá vé có thể thay đổi mà không cần thông báo trước. (Flight schedule and prices are subject to change without prior notice.)"
		};
	
		for (String term : terms) {
			Paragraph termParagraph = new Paragraph(term, termsFont);
			termParagraph.setSpacingAfter(2);  // Giảm khoảng cách giữa các điều khoản còn rất nhỏ
			termsSection.add(termParagraph);
		}
	
		return termsSection;
	}
	
	 private static void addDetailRow(PdfPTable table, String label, String value) {
        String bilingualLabel = getBilingualLabel(label);
        
        PdfPCell labelCell = new PdfPCell(new Phrase(bilingualLabel, LABEL_FONT));
        PdfPCell valueCell = new PdfPCell(new Phrase(value, NORMAL_FONT));

        labelCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setBorder(Rectangle.NO_BORDER);

        labelCell.setPadding(5);
        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
	
	private static String formatDetailedQRData(DatVe datVe) {
		ChuyenBay flight = datVe.getChuyenBay();
		return String.format(
			"🎫 BOARDING PASS 🎫\n" +
			"-------------------\n" +
			"Passenger: %s\n" +
			"Flight: %s (%s)\n" +
			"From: %s → To: %s\n" +
			"Date: %s\n" +
			"Departure: %s\n" +
			"Seat: %s\n" +
			"Price: %,.0f VND\n" +
			"Booking Ref: %s\n" +
			"-------------------",
			datVe.getHoTen(),
			flight.getMaChuyenBay(), 
			getTenHangBay(flight.getMaHang()),
			flight.getDiemDi(),
			flight.getDiemDen(),
			formatDate(flight.getNgayBay()),
			flight.getGioKhoiHanh(),
			datVe.getViTriGhe(),
			datVe.getTongGia(),
			datVe.getMaDatVe()
		);
	}

	private static void themQRCodeVaThongTinLuotDi(Document document, DatVe datVe, PdfWriter writer) throws DocumentException {
		Paragraph header = new Paragraph("THÔNG TIN LƯỢT ĐI", HEADER_FONT);
		header.setSpacingBefore(10);
		header.setSpacingAfter(10);
		document.add(header);

		// Tạo bảng chứa QR và thông tin cơ bản
		PdfPTable mainTable = new PdfPTable(2);
		mainTable.setWidthPercentage(100);
		mainTable.setWidths(new float[]{1, 2});

		// Cột bên trái chứa QR Code
		PdfPCell qrCell = new PdfPCell();
		qrCell.setBorder(Rectangle.BOX);
		qrCell.setPadding(10);
		
		try {
			// Tạo QR Code
			BarcodeQRCode qrCode = new BarcodeQRCode(datVe.getMaDatVe(), 100, 100, null);
			Image qrImage = qrCode.getImage();
			qrImage.scalePercent(200);
			
			// Thêm thông tin dưới QR
			PdfPTable qrInfo = new PdfPTable(1);
			qrInfo.setWidthPercentage(100);
			
			// Thêm QR Image
			PdfPCell imageCell = new PdfPCell(qrImage);
			imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			imageCell.setBorder(Rectangle.NO_BORDER);
			qrInfo.addCell(imageCell);
			
			// Thêm thông tin ghế và giá
			PdfPCell seatCell = new PdfPCell(new Phrase("Ghế: " + datVe.getViTriGhe(), BOLD_FONT));
			seatCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			seatCell.setBorder(Rectangle.NO_BORDER);
			qrInfo.addCell(seatCell);
			
			PdfPCell priceCell = new PdfPCell(new Phrase("Giá: " + String.format("%,.0f", datVe.getTongGia()) + "đ", BOLD_FONT));
			priceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			priceCell.setBorder(Rectangle.NO_BORDER);
			qrInfo.addCell(priceCell);
			
			qrCell.addElement(qrInfo);
		} catch (BadElementException e) {
			qrCell.addElement(new Phrase("Không thể tạo mã QR", NORMAL_FONT));
		}

		// Cột bên phải chứa thông tin chi tiết
		PdfPCell infoCell = new PdfPCell();
		infoCell.setBorder(Rectangle.NO_BORDER);
		PdfPTable infoTable = new PdfPTable(2);
		infoTable.setWidthPercentage(100);

		// Thêm thông tin chi tiết
		themDongThongTin(infoTable, "Trạng thái:", "Thanh toán thành công");
		themDongThongTin(infoTable, "Tuyến xe:", datVe.getChuyenBay().getChangBay());
		themDongThongTin(infoTable, "Thời gian khởi hành:", 
			formatDateTime(datVe.getChuyenBay().getGioKhoiHanh()) + " " + 
			formatDate(datVe.getChuyenBay().getNgayBay()));
		themDongThongTin(infoTable, "Số lượng vé:", datVe.getSoLuong() + " vé");
		themDongThongTin(infoTable, "Vị trí ghế:", datVe.getViTriGhe());

		// Thêm thông tin điểm lên/xuống xe
		PdfPTable locationTable = new PdfPTable(1);
		locationTable.setWidthPercentage(100);
		
		// Điểm lên xe
		Paragraph pickup = new Paragraph();
		pickup.add(new Chunk("Điểm lên xe: ", BOLD_FONT));
		pickup.add(new Chunk(datVe.getChuyenBay().getDiemDi() + "\n", NORMAL_FONT));
		pickup.add(new Chunk(datVe.getChuyenBay().getNhaGa(), ITALIC_FONT));
		
		PdfPCell pickupCell = new PdfPCell(pickup);
		pickupCell.setBorder(Rectangle.NO_BORDER);
		pickupCell.setPaddingTop(10);
		locationTable.addCell(pickupCell);
		
		// Điểm xuống xe
		Paragraph dropoff = new Paragraph();
		dropoff.add(new Chunk("Điểm xuống xe: ", BOLD_FONT));
		dropoff.add(new Chunk(datVe.getChuyenBay().getDiemDen() + "\n", NORMAL_FONT));
		dropoff.add(new Chunk(datVe.getChuyenBay().getNhaGa(), ITALIC_FONT));
		
		PdfPCell dropoffCell = new PdfPCell(dropoff);
		dropoffCell.setBorder(Rectangle.NO_BORDER);
		dropoffCell.setPaddingTop(10);
		locationTable.addCell(dropoffCell);

		infoCell.addElement(infoTable);
		infoCell.addElement(locationTable);

		mainTable.addCell(qrCell);
		mainTable.addCell(infoCell);

		document.add(mainTable);
		themDuongKe(document);
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

	
	@SuppressWarnings("unused")
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
	}

	@SuppressWarnings("unused")
	private static void themThongTinChuyenBay(Document document, ChuyenBay chuyenBay) throws DocumentException {
	    // Thêm header
	    Paragraph header = new Paragraph("THÔNG TIN CHUYẾN BAY", HEADER_FONT);
	    header.setSpacingBefore(5);
	    header.setSpacingAfter(5);
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

	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
	private static boolean coYeuCauDacBiet(DatVe datVe) {
		return datVe.isSuatAnDacBiet() || datVe.isHoTroYTe() || datVe.isChoNgoiUuTien() || datVe.isHanhLyDacBiet();
	}
}