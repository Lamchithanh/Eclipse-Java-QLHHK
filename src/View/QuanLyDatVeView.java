package View;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import View.JDateChooser;

import Controller.DatVeController;
import Model.ChuyenBay;
import Model.DatVe;
import Model.KhachHang;
import Service.DatVeService;

@SuppressWarnings("unused")
public class QuanLyDatVeView extends JPanel {
	private DatVeController controller;
	private DatVeService service;

	// UI Components
	private DefaultTableModel tableModel;
	private JTable table;

	// Search Panel Components
	private JTextField txtSearch;
	private JDateChooser dateSearch;
	private JComboBox<String> cbFilterStatus;

	// Flight Selection
	private JComboBox<ChuyenBay> cbChuyenBay;
	private JLabel lblDiemDi;
	private JLabel lblDiemDen;
	private JLabel lblNgayBay;
	private JLabel lblGiaVe;
	private JLabel lblTinhTrang;
	private JLabel lblChangBay;
	private JLabel lblNhaGa;
	private JLabel lblGioKhoiHanh;
	private JLabel lblGioHaCanh;
	private JLabel lblThoiGianBay;
	private JLabel lblTenSanBay;
	

	// Booking Information
	private JTextField txtMaDatVe;
	private JTextField txtSoLuong;
	private JTextField txtTongGia;

	// Customer Information
	private JTextField txtHoTen;
	private JTextField txtCMND;
	private JDateChooser dateNgaySinh;
	private JComboBox<String> cbGioiTinh;
	private JTextField txtQuocTich;
	private JTextField txtSoDienThoai;
	private JTextField txtEmail;
	private JTextArea txtDiaChi;

	// Ticket Type and Payment
	private JComboBox<String> cbHangVe;
	private JComboBox<String> cbPhuongThucThanhToan;
	private JTextField txtMaGiamGia;
	private JCheckBox chkXacNhanThanhToan;

	// Special Requests
	private JCheckBox chkSuatAnDacBiet;
	private JCheckBox chkHoTroYTe;
	private JCheckBox chkChoNgoiUuTien;
	private JCheckBox chkHanhLyDacBiet;

	// Emergency Contact
	private JTextField txtNguoiLienHe;
	private JTextField txtSDTNguoiLienHe;

	// UI Colors
	private Color primaryColor = new Color(41, 128, 185);
	private Color backgroundColor = new Color(236, 240, 241);
	private Color textColor = new Color(44, 62, 80);
	private JTextField txtNgaySinh;
	
	private static final int ROWS_PER_PAGE = 100; // Số dòng load mỗi lần
	private int currentPage = 0;
	private List<DatVe> allBookings = new ArrayList<>();
	
	

	public QuanLyDatVeView(TrangChuPanel trangChuPanel) {
		service = new DatVeService();
		controller = new DatVeController(this);
		initializeComponents();
		setupUI();
		setupListeners();
		loadInitialData();
	}

	private void loadInitialData() {
		try {
			// Tải danh sách chuyến bay
			List<ChuyenBay> flights = service.getAllChuyenBays();
			updateFlightsList(flights);

			// Tải danh sách đặt vé
			List<DatVe> bookings = service.getAllDatVe();
			updateTable(bookings);
		} catch (Exception e) {
			showMessage("Lỗi tải dữ liệu ban đầu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void initializeComponents() {
		// Initialize Search Components
		txtSearch = createStyledTextField("Tìm kiếm...");
		dateSearch = new JDateChooser();
		cbFilterStatus = new JComboBox<>(new String[] { "Tất cả", "Sắp khởi hành", "Đã khởi hành", "Đã hủy" });

		// Initialize Flight Information Components
		cbChuyenBay = new JComboBox<>();
		lblDiemDi = new JLabel();
		lblDiemDen = new JLabel();
		lblNgayBay = new JLabel();
		lblGiaVe = new JLabel();
		lblTinhTrang = new JLabel();
		lblChangBay = new JLabel();
		lblNhaGa = new JLabel();
		lblGioKhoiHanh = new JLabel();
		lblGioHaCanh = new JLabel();
		lblThoiGianBay = new JLabel();
		lblTenSanBay = new JLabel();

		// Initialize Booking Components
		txtMaDatVe = createStyledTextField("");
		txtMaDatVe.setEditable(false);
		txtSoLuong = createStyledTextField("");
		txtTongGia = createStyledTextField("");
		txtTongGia.setEditable(false);

		// Initialize Customer Information Components
		txtHoTen = createStyledTextField("");
		txtCMND = createStyledTextField("");
		((PlainDocument) txtCMND.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
				if ((newText.length() <= 12) && text.matches("\\d*")) { // Chỉ cho phép số và độ dài tối đa 12
					super.replace(fb, offset, length, text, attrs);
				}
			}

			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
					throws BadLocationException {
				String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
				if ((newText.length() <= 12) && string.matches("\\d*")) { // Chỉ cho phép số và độ dài tối đa 12
					super.insertString(fb, offset, string, attr);
				}
			}
		});
		
		
		dateNgaySinh = new JDateChooser();
		txtNgaySinh = createStyledTextField("dd/MM/yyyy");
		txtNgaySinh.setToolTipText("Nhập ngày sinh (dd/MM/yyyy)");
		cbGioiTinh = new JComboBox<>(new String[] { "Nam", "Nữ" });
		txtQuocTich = createStyledTextField("");
		txtSoDienThoai = createStyledTextField("");
		txtEmail = createStyledTextField("");
		txtDiaChi = new JTextArea(3, 20);
		setupTextArea(txtDiaChi);

		// Initialize Ticket and Payment Components
		cbHangVe = new JComboBox<>(new String[] { "Phổ thông", "Thương gia", "Hạng nhất" });
		cbPhuongThucThanhToan = new JComboBox<>(new String[] { "Tiền mặt", "Chuyển khoản", "Thẻ tín dụng" });
		txtMaGiamGia = createStyledTextField("");
		chkXacNhanThanhToan = new JCheckBox("Xác nhận thanh toán");

		// Initialize Special Request Components
		chkSuatAnDacBiet = new JCheckBox("Suất ăn đặc biệt");
		chkHoTroYTe = new JCheckBox("Hỗ trợ y tế");
		chkChoNgoiUuTien = new JCheckBox("Chỗ ngồi ưu tiên");
		chkHanhLyDacBiet = new JCheckBox("Hành lý đặc biệt");

		// Initialize Emergency Contact Components
		txtNguoiLienHe = createStyledTextField("");
		txtSDTNguoiLienHe = createStyledTextField("");

		// Initialize Table
		initializeTable();
	}

	private void initializeTable() {
	    String[] columnNames = {"Mã Đặt Vé", "Mã Chuyến Bay", "Họ Tên", "Ngày Đặt", 
	                           "Số Lượng", "Tổng Giá", "Trạng Thái"};

	    tableModel = new DefaultTableModel(columnNames, 0) {
	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return false;
	        }
	        
	        // Định nghĩa kiểu dữ liệu cho các cột
	        @Override
	        public Class<?> getColumnClass(int columnIndex) {
	            switch (columnIndex) {
	                case 4: return Integer.class;  // Số lượng
	                case 5: return String.class;   // Tổng giá - để là String vì đã format với "VND"
	                default: return String.class;
	            }
	        }
	    };

	    table = new JTable(tableModel);
	    table.setRowHeight(35);
	    table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    
	    // Custom renderer cho từng cột
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	    
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    
	    // Renderer cho cột tổng giá
	    DefaultTableCellRenderer moneyRenderer = new DefaultTableCellRenderer() {
	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value,
	                boolean isSelected, boolean hasFocus, int row, int column) {
	            
	            Component c = super.getTableCellRendererComponent(table, value, 
	                    isSelected, hasFocus, row, column);
	            
	            // Set alignment cho giá trị tiền
	            setHorizontalAlignment(JLabel.RIGHT);
	            
	            // Set màu nền cho row
	            if (!isSelected) {
	                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
	            }
	            
	            return c;
	        }
	    };

	    // Áp dụng renderers cho các cột
	    table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);  // Số lượng
	    table.getColumnModel().getColumn(5).setCellRenderer(moneyRenderer);   // Tổng giá
	    
	    // Style header
	    JTableHeader header = table.getTableHeader();
	    header.setDefaultRenderer(new DefaultTableCellRenderer() {
	        {
	            setHorizontalAlignment(JLabel.CENTER);
	            setBackground(primaryColor.brighter());
	            setForeground(Color.WHITE);
	            setFont(new Font("Segoe UI", Font.BOLD, 14));
	            setBorder(BorderFactory.createCompoundBorder(
	                BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(200, 200, 200)),
	                BorderFactory.createEmptyBorder(8, 10, 8, 10)
	            ));
	        }
	    });
	    header.setReorderingAllowed(false);

	    // Tối ưu hiệu suất
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	    table.setUpdateSelectionOnSort(true);
	    table.getTableHeader().setResizingAllowed(true);
	    table.setFillsViewportHeight(true);
	    table.setShowGrid(false);
	    table.setIntercellSpacing(new Dimension(0, 0));
	}

	private void loadMoreRows() {
	    int start = currentPage * ROWS_PER_PAGE;
	    if (start >= allBookings.size()) return;
	    
	    int end = Math.min(start + ROWS_PER_PAGE, allBookings.size());
	    List<DatVe> nextPage = allBookings.subList(start, end);
	    
	    SwingUtilities.invokeLater(() -> {
	        for (DatVe booking : nextPage) {
	            tableModel.addRow(new Object[]{
	                booking.getMaDatVe(),
	                booking.getChuyenBay().getMaChuyenBay(),
	                booking.getHoTen(),
	                booking.getNgayDat(),
	                booking.getSoLuong(),
	                String.format("%,.0f VND", booking.getTongGia()),
	                booking.getTrangThai()
	            });
	        }
	    });
	    
	    currentPage++;
	}

	private JTextField createStyledTextField(String placeholder) {
		JTextField textField = new JTextField(20);
		textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		textField.setPreferredSize(new Dimension(200, 35));
		textField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(224, 224, 224)),
				BorderFactory.createEmptyBorder(5, 8, 5, 8)));

		// Thêm placeholder text
		if (!placeholder.isEmpty()) {
			textField.setForeground(Color.GRAY);
			textField.setText(placeholder);
			textField.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					if (textField.getText().equals(placeholder)) {
						textField.setText("");
						textField.setForeground(Color.BLACK);
					}
				}

				@Override
				public void focusLost(FocusEvent e) {
					if (textField.getText().isEmpty()) {
						textField.setForeground(Color.GRAY);
						textField.setText(placeholder);
					}
				}
			});
		}

		return textField;
	}

	private void setupTextArea(JTextArea textArea) {
		textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(224, 224, 224)),
				BorderFactory.createEmptyBorder(5, 8, 5, 8)));
	}

	private void setupUI() {
		setLayout(new BorderLayout(10, 10));
		setBackground(backgroundColor);

		// Add Header
		add(createHeaderPanel(), BorderLayout.NORTH);

		// Create Main Content Panel
		JPanel mainContent = new JPanel(new BorderLayout(10, 10));
		mainContent.setBackground(backgroundColor);

		// Add Search Panel
		mainContent.add(createSearchPanel(), BorderLayout.NORTH);

		// Create Center Panel with Form and Table
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createFormPanel(), createTablePanel());
		splitPane.setResizeWeight(0.6);
		mainContent.add(splitPane, BorderLayout.CENTER);

		// Add Button Panel
		mainContent.add(createButtonPanel(), BorderLayout.SOUTH);

		add(mainContent, BorderLayout.CENTER);
	}

	private Component createTablePanel() {
		// Khởi tạo table nếu chưa được khởi tạo
		if (table == null) {
			initializeTable();
		}

		// Tạo JScrollPane để chứa bảng
		JScrollPane scrollPane = new JScrollPane(table);

		// Tùy chỉnh giao diện ScrollPane
		scrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10),
				BorderFactory.createLineBorder(new Color(224, 224, 224))));
		scrollPane.setBackground(Color.WHITE);
		scrollPane.getViewport().setBackground(Color.WHITE);

		// Tạo panel chứa bảng với border layout
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setBackground(Color.WHITE);

		// Thêm tiêu đề cho bảng
		JLabel tableTitle = new JLabel("Danh Sách Đặt Vé", SwingConstants.LEFT);
		tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
		tableTitle.setForeground(primaryColor);
		tableTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Thêm các thành phần vào panel
		tablePanel.add(tableTitle, BorderLayout.NORTH);
		tablePanel.add(scrollPane, BorderLayout.CENTER);

		return tablePanel;
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
		panel.setBackground(backgroundColor);

		// Tạo các nút với style thống nhất
		JButton btnAdd = createStyledButton("Thêm", new Color(46, 204, 113));
		JButton btnEdit = createStyledButton("Sửa", new Color(52, 152, 219));
		JButton btnDelete = createStyledButton("Xóa", new Color(231, 76, 60));
		JButton btnClear = createStyledButton("Làm mới", new Color(241, 196, 15));
		JButton btnInvoice = createStyledButton("Xuất Hóa Đơn", new Color(142, 68, 173));

		// Thêm xử lý sự kiện cho các nút
		btnAdd.addActionListener(e -> handleBooking());
		btnEdit.addActionListener(e -> {
			if (validateInput()) {
				try {
					DatVe booking = createBookingFromFields();
					service.updateDatVe(booking);
					refreshTable();
					clearFields();
					showMessage("Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception ex) {
					showMessage("Lỗi khi cập nhật: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnDelete.addActionListener(e -> {
			try {
				String maDatVe = txtMaDatVe.getText().trim();
				if (maDatVe.isEmpty()) {
					showMessage("Vui lòng chọn vé cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
					return;
				}
				int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa vé này?", "Xác nhận xóa",
						JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					service.deleteDatVe(maDatVe);
					refreshTable();
					clearFields();
					showMessage("Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (Exception ex) {
				showMessage("Lỗi khi xóa: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		});
		btnClear.addActionListener(e -> clearFields());
		btnInvoice.addActionListener(e -> handleInvoice());

		// Thêm các nút vào panel
		panel.add(btnAdd);
		panel.add(btnEdit);
		panel.add(btnDelete);
		panel.add(btnClear);
		panel.add(btnInvoice);

		return panel;
	}

	private void handleInvoice() {
		// Lấy dòng được chọn trong bảng
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			showMessage("Vui lòng chọn một đặt vé để xuất hóa đơn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			// Lấy mã đặt vé từ dòng được chọn
			String maDatVe = table.getValueAt(selectedRow, 0).toString();
			// Lấy thông tin đặt vé từ database
			DatVe datVe = service.getDatVeByMa(maDatVe);

			if (datVe != null) {
				// Hiển thị form hóa đơn
				HoaDonVeMayBayView.showInvoice(SwingUtilities.getWindowAncestor(this), datVe);
			} else {
				showMessage("Không tìm thấy thông tin đặt vé!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception ex) {
			showMessage("Lỗi khi xuất hóa đơn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	@SuppressWarnings("exports")
	public static void showInvoice(Window parent, DatVe datVe) {
		// Kiểm tra và chuyển đổi parent thành JFrame
		JFrame parentFrame;
		if (parent instanceof JFrame) {
			parentFrame = (JFrame) parent;
		} else {
			parentFrame = (JFrame) SwingUtilities.getWindowAncestor(parent);
		}

		HoaDonVeMayBayView dialog = new HoaDonVeMayBayView(parentFrame, datVe);
		dialog.setVisible(true);
	}

	public void clearFields() {
		// Xóa thông tin đặt vé
		txtMaDatVe.setText("");
		cbChuyenBay.setSelectedIndex(-1);
		txtSoLuong.setText("");
		txtTongGia.setText("");

		// Xóa thông tin khách hàng
		txtHoTen.setText("");
		txtCMND.setText("");
		dateNgaySinh.setDate(null);
		cbGioiTinh.setSelectedIndex(0);
		txtQuocTich.setText("");

		// Xóa thông tin liên hệ
		txtSoDienThoai.setText("");
		txtEmail.setText("");
		txtDiaChi.setText("");

		// Xóa thông tin vé và thanh toán
		cbHangVe.setSelectedIndex(0);
		cbPhuongThucThanhToan.setSelectedIndex(0);
		txtMaGiamGia.setText("");
		chkXacNhanThanhToan.setSelected(false);

		// Xóa yêu cầu đặc biệt
		chkSuatAnDacBiet.setSelected(false);
		chkHoTroYTe.setSelected(false);
		chkChoNgoiUuTien.setSelected(false);
		chkHanhLyDacBiet.setSelected(false);

		// Xóa thông tin liên hệ khẩn cấp
		txtNguoiLienHe.setText("");
		txtSDTNguoiLienHe.setText("");

		// Xóa thông tin chuyến bay
		lblDiemDi.setText("");
		lblDiemDen.setText("");
		lblNgayBay.setText("");
		lblGiaVe.setText("");
		lblTinhTrang.setText("");
		lblChangBay.setText("");
		lblNhaGa.setText("");
	}

	private JButton createStyledButton(String text, Color color) {
		JButton button = new JButton(text);
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));
		button.setForeground(Color.WHITE);
		button.setBackground(color);
		button.setOpaque(true);
		button.setBorderPainted(false);
		button.setPreferredSize(new Dimension(120, 40));
		button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
		button.setFocusPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// Thêm hiệu ứng hover
		button.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				button.setBackground(color.brighter());
			}

			public void mouseExited(MouseEvent e) {
				button.setBackground(color);
			}
		});

		return button;
	}

	private JPanel createHeaderPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(primaryColor);
		panel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

		JLabel headerLabel = new JLabel("Quản Lý Đặt Vé");
		headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
		headerLabel.setForeground(Color.WHITE);
		panel.add(headerLabel, BorderLayout.WEST);

		return panel;
	}

	private JPanel createSearchPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		panel.add(new JLabel("Tìm kiếm:"));
		panel.add(txtSearch);
		panel.add(new JLabel("Ngày:"));
		panel.add(dateSearch);
		panel.add(new JLabel("Trạng thái:"));
		panel.add(cbFilterStatus);

		JButton btnSearch = new JButton("Tìm kiếm");
		btnSearch.addActionListener(e -> performSearch());
		panel.add(btnSearch);

		return panel;
	}

	private JScrollPane createFormPanel() {
	    JPanel formPanel = new JPanel(new GridBagLayout());
	    formPanel.setBackground(Color.WHITE);
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5);
	    gbc.fill = GridBagConstraints.HORIZONTAL;

	    // Thêm các section
	    addSection(formPanel, createFlightSelectionPanel(), "Thông tin chuyến bay", gbc, 0);
	    addSection(formPanel, createCustomerInfoPanel(), "Thông tin khách hàng", gbc, 1);
	    addSection(formPanel, createTicketPaymentPanel(), "Thông tin vé và thanh toán", gbc, 2);
	    addSection(formPanel, createSpecialRequestsPanel(), "Yêu cầu đặc biệt", gbc, 3);
	    addSection(formPanel, createEmergencyContactPanel(), "Thông tin liên hệ khẩn cấp", gbc, 4);

	    JScrollPane scrollPane = new JScrollPane(formPanel);
	    scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Tăng tốc độ cuộn
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    
	    // Tối ưu render khi cuộn
	    scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
	    
	    return scrollPane;
	}

	private JPanel createFlightSelectionPanel() {
	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.setBackground(Color.WHITE);
	    GridBagConstraints gbc = createStandardGBC();

	    addFormField(panel, "Chuyến bay:", cbChuyenBay, gbc, 0);
	    addFormField(panel, "Điểm đi:", lblDiemDi, gbc, 1);
	    addFormField(panel, "Điểm đến:", lblDiemDen, gbc, 2);
	    addFormField(panel, "Ngày bay:", lblNgayBay, gbc, 3);
	    addFormField(panel, "Giờ khởi hành:", lblGioKhoiHanh, gbc, 4); // Thêm trường này
	    addFormField(panel, "Giờ hạ cánh:", lblGioHaCanh, gbc, 5); // Thêm trường này
	    addFormField(panel, "Thời gian bay (phút):", lblThoiGianBay, gbc, 6); // Thêm trường này
	    addFormField(panel, "Giá vé:", lblGiaVe, gbc, 7);
	    addFormField(panel, "Trạng thái:", lblTinhTrang, gbc, 8);
	    addFormField(panel, "Sân bay:", lblTenSanBay, gbc, 9);
	    addFormField(panel, "Nhà ga:", lblNhaGa, gbc, 10);

	    return panel;
	}


	private JPanel createCustomerInfoPanel() {
	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.setBackground(Color.WHITE);
	    GridBagConstraints gbc = createStandardGBC();

	    addFormField(panel, "Họ và tên:", txtHoTen, gbc, 0);
	    addFormField(panel, "CMND/CCCD:", txtCMND, gbc, 1);
	    
	    // Tạo panel để chứa cả JDateChooser và TextField
	    JPanel ngaySinhPanel = new JPanel(new GridBagLayout());
	    ngaySinhPanel.setBackground(Color.WHITE);
	    
	    GridBagConstraints ngaySinhGbc = new GridBagConstraints();
	    ngaySinhGbc.fill = GridBagConstraints.HORIZONTAL;
	    ngaySinhGbc.weightx = 0.7; // Ưu tiên cho JDateChooser
	    ngaySinhGbc.gridx = 0;
	    ngaySinhGbc.gridy = 0;
	    ngaySinhPanel.add(dateNgaySinh, ngaySinhGbc);
	    
	    ngaySinhGbc.weightx = 0.3; // Phần nhỏ hơn cho TextField
	    ngaySinhGbc.gridx = 1;
	    ngaySinhGbc.insets = new Insets(0, 5, 0, 0); // Thêm khoảng cách giữa 2 components
	    ngaySinhPanel.add(txtNgaySinh, ngaySinhGbc);
	    
	    addFormField(panel, "Ngày sinh:", ngaySinhPanel, gbc, 2);
	    addFormField(panel, "Giới tính:", cbGioiTinh, gbc, 3);
	    addFormField(panel, "Quốc tịch:", txtQuocTich, gbc, 4);
	    addFormField(panel, "Số điện thoại:", txtSoDienThoai, gbc, 5);
	    addFormField(panel, "Email:", txtEmail, gbc, 6);
	    addFormField(panel, "Địa chỉ:", new JScrollPane(txtDiaChi), gbc, 7);

	    return panel;
	}

	private void addSection(JPanel container, JPanel section, String title, GridBagConstraints gbc, int gridy) {
		section.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(primaryColor), title,
				TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), primaryColor));

		gbc.gridy = gridy;
		gbc.gridx = 0;
		gbc.weightx = 1.0;
		container.add(section, gbc);
	}

	private JPanel createTicketPaymentPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		GridBagConstraints gbc = createStandardGBC();

		addFormField(panel, "Hạng vé:", cbHangVe, gbc, 0);
		addFormField(panel, "Số lượng:", txtSoLuong, gbc, 1);
		addFormField(panel, "Tổng giá:", txtTongGia, gbc, 2);
		addFormField(panel, "Phương thức thanh toán:", cbPhuongThucThanhToan, gbc, 3);
		addFormField(panel, "Mã giảm giá:", txtMaGiamGia, gbc, 4);
		addFormField(panel, "", chkXacNhanThanhToan, gbc, 5);

		return panel;
	}

	private JPanel createSpecialRequestsPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		panel.setBackground(Color.WHITE);

		panel.add(chkSuatAnDacBiet);
		panel.add(chkHoTroYTe);
		panel.add(chkChoNgoiUuTien);
		panel.add(chkHanhLyDacBiet);

		// Style cho các checkbox
		Font checkboxFont = new Font("Segoe UI", Font.PLAIN, 14);
		chkSuatAnDacBiet.setFont(checkboxFont);
		chkHoTroYTe.setFont(checkboxFont);
		chkChoNgoiUuTien.setFont(checkboxFont);
		chkHanhLyDacBiet.setFont(checkboxFont);

		return panel;
	}

	private JPanel createEmergencyContactPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		GridBagConstraints gbc = createStandardGBC();

		addFormField(panel, "Tên người liên hệ:", txtNguoiLienHe, gbc, 0);
		addFormField(panel, "Số điện thoại:", txtSDTNguoiLienHe, gbc, 1);

		return panel;
	}

	private GridBagConstraints createStandardGBC() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 10, 5, 10);
		gbc.anchor = GridBagConstraints.WEST;
		return gbc;
	}

	private void addFormField(JPanel panel, String labelText, Component component, GridBagConstraints gbc, int gridy) {
		JLabel label = new JLabel(labelText);
		label.setFont(new Font("Segoe UI", Font.BOLD, 14));
		label.setForeground(textColor);

		gbc.gridy = gridy;
		gbc.gridx = 0;
		gbc.weightx = 0.3;
		panel.add(label, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		panel.add(component, gbc);
	}

	// Thay đổi cách xử lý trong setupListeners()
	private void setupListeners() {
		// Flight Selection Listener
		cbChuyenBay.addActionListener(e -> {
			ChuyenBay selectedFlight = (ChuyenBay) cbChuyenBay.getSelectedItem();
			if (selectedFlight != null) {
				updateFlightDetails(selectedFlight);
			}
		});

		// Quantity Change Listener
		txtSoLuong.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				SwingUtilities.invokeLater(() -> updateTotalPrice());
			}

			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				SwingUtilities.invokeLater(() -> updateTotalPrice());
			}

			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				SwingUtilities.invokeLater(() -> updateTotalPrice());
			}
		});

		// CMND Change Listener
		txtCMND.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			private Timer timer = new Timer(300, e -> processCMND());

			private void processCMND() {
				SwingUtilities.invokeLater(() -> {
					try {
						String cmnd = txtCMND.getText().trim();
						if (cmnd.length() >= 9 && cmnd.length() <= 12) {
							String validatedCmnd = new KhachHang().validateCmnd(cmnd);
							if (!validatedCmnd.equals("CMND không hợp lệ")) {
								// Tìm khách hàng bằng CMND
								KhachHang khachHang = service.findKhachHangByCMND(cmnd);
								if (khachHang != null) {
									// Chỉ cập nhật thông tin nếu tìm thấy khách hàng
									txtHoTen.setText(khachHang.getTenKhachHang());
									if (khachHang.getNgaySinh() != null) {
										dateNgaySinh.setDate(khachHang.getNgaySinh());
									}
									txtQuocTich.setText((String) khachHang.getQuocTich(khachHang.getQuocTich()));
									txtSoDienThoai.setText(khachHang.getSoDienThoai());
									txtEmail.setText(khachHang.getEmail());
									txtDiaChi.setText(khachHang.getDiaChi());
								}
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						showMessage("Lỗi khi tải thông tin khách hàng: " + ex.getMessage(), "Lỗi",
								JOptionPane.ERROR_MESSAGE);
					}
				});
			}

			private void updateCustomerFields(KhachHang khachHang) {
				if (khachHang != null) {
					txtHoTen.setText(khachHang.getTenKhachHang());
					dateNgaySinh.setDate(khachHang.getNgaySinh());
					txtQuocTich.setText((String) khachHang.getQuocTich(khachHang.getQuocTich()));
					txtSoDienThoai.setText(khachHang.getSoDienThoai());
					txtEmail.setText(khachHang.getEmail());
					txtDiaChi.setText(khachHang.getDiaChi());

					// Validate CMND
					String validatedCmnd = khachHang.validateCmnd(khachHang.getCmnd());
					if (!validatedCmnd.equals("Chưa có CMND") && !validatedCmnd.equals("CMND không hợp lệ")) {
						txtCMND.setText(validatedCmnd);
					}
				}
			}

			private void loadCustomerInfo(String cmnd) {
				if (cmnd == null || cmnd.trim().isEmpty()) {
					return;
				}

				SwingUtilities.invokeLater(() -> {
					try {
						String validatedCmnd = new KhachHang().validateCmnd(cmnd);
						if (validatedCmnd.equals("CMND không hợp lệ")) {
							clearCustomerFields();
							return;
						}

						KhachHang khachHang = service.findKhachHangByCMND(cmnd);
						if (khachHang != null) {
							updateCustomerFields(khachHang);
						} else {
							clearCustomerFields();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						showMessage("Lỗi khi tải thông tin khách hàng: " + ex.getMessage(), "Lỗi",
								JOptionPane.ERROR_MESSAGE);
					}
				});
			}

			private boolean isValidCMND(String cmnd) {
				return cmnd != null && cmnd.matches("\\d{9,12}");
			}

			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				timer.restart();
			}

			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				timer.restart();
			}

			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				timer.restart();
			}
		});

		// Ticket Class Change Listener
		cbHangVe.addActionListener(e -> SwingUtilities.invokeLater(() -> updateTotalPrice()));
	}

	private void loadCustomerInfo() {
		String cmnd = txtCMND.getText().trim();
		if (!cmnd.equals(lastProcessedCMND) && cmnd.length() >= 9) {
			lastProcessedCMND = cmnd;
			try {
				// Fetch customer info from database
				// If customer found, update fields
				// If not found, clear fields
				SwingUtilities.invokeLater(() -> clearCustomerFields());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private String lastProcessedCMND = "";

	private void clearCustomerFields() {
		SwingUtilities.invokeLater(() -> {
			try {
				// Xử lý các trường text thông thường
				txtHoTen.setText("");
				cbGioiTinh.setSelectedIndex(0);
				txtQuocTich.setText("");
				txtSoDienThoai.setText("");
				txtEmail.setText("");
				txtDiaChi.setText("");

				// Xử lý JDateChooser an toàn
				if (dateNgaySinh != null) {
					dateNgaySinh.setDate(null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private void updateTotalPrice() {
		try {
			ChuyenBay selectedFlight = (ChuyenBay) cbChuyenBay.getSelectedItem();
			String soLuongText = txtSoLuong.getText().trim();

			if (selectedFlight != null && !soLuongText.isEmpty()) {
				try {
					int quantity = Integer.parseInt(soLuongText);
					if (quantity > 0) {
						double basePrice = selectedFlight.getGiaVe();
						double totalPrice = calculateTotalPrice(basePrice, quantity);
						final String formattedPrice = String.format("%,.0f VND", totalPrice);
						SwingUtilities.invokeLater(() -> txtTongGia.setText(formattedPrice));
					}
				} catch (NumberFormatException ex) {
					SwingUtilities.invokeLater(() -> txtTongGia.setText(""));
				}
			} else {
				SwingUtilities.invokeLater(() -> txtTongGia.setText(""));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateFlightDetails(ChuyenBay selectedFlight) {
	    if (selectedFlight != null) {
	        lblDiemDi.setText(selectedFlight.getDiemDi());
	        lblDiemDen.setText(selectedFlight.getDiemDen());
	        lblNgayBay.setText(selectedFlight.getNgayBayFormatted());
	        lblGioKhoiHanh.setText(selectedFlight.getGioKhoiHanh()); // Cập nhật giá trị
	        lblGioHaCanh.setText(selectedFlight.getGioHaCanh()); // Cập nhật giá trị
	        lblThoiGianBay.setText(String.valueOf(selectedFlight.getThoiGianBay())); // Cập nhật giá trị
	        lblGiaVe.setText(String.format("%,.0f VND", selectedFlight.getGiaVe()));
	        lblTinhTrang.setText(selectedFlight.getTinhTrang());
	        lblTenSanBay.setText(selectedFlight.getTenSanBay());
	        lblNhaGa.setText(selectedFlight.getNhaGa());
	        updateTotalPrice();
	    }
	}

	private double calculateTotalPrice(double basePrice, int quantity) {
		double total = basePrice * quantity;

		// Apply class multiplier
		String selectedClass = cbHangVe.getSelectedItem().toString();
		switch (selectedClass) {
		case "Thương gia":
			total *= 1.5;
			break;
		case "Hạng nhất":
			total *= 2.0;
			break;
		}

		// Apply discount if available
		if (!txtMaGiamGia.getText().trim().isEmpty()) {
			// Here you would typically validate the discount code
			// For now, we'll apply a simple 10% discount
			total *= 0.9;
		}

		return total;
	}

	private void performSearch() {
		try {
			String searchTerm = txtSearch.getText().trim();
			Date searchDate = dateSearch.getDate() != null ? new Date(dateSearch.getDate().getTime()) : null;
			String status = cbFilterStatus.getSelectedItem().toString();

			List<DatVe> searchResults = service.searchDatVe(searchTerm);
			updateTable(searchResults);
		} catch (Exception e) {
			showMessage("Lỗi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void handleBooking() {
	    try {
	        if (!validateInput()) {
	            return;
	        }

	        DatVe booking = createBookingFromFields();

	        try {
	            if (txtMaDatVe.getText().trim().isEmpty()) {
	                service.addDatVe(booking);
	                showMessage("Đặt vé thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	            } else {
	                service.updateDatVe(booking);
	                showMessage("Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	            }

	            refreshTable();
	            clearFields();

	        } catch (SQLIntegrityConstraintViolationException ex) {
	            showMessage("Mã đặt vé có thể đã tồn tại. Vui lòng thử lại.", 
	                        "Lỗi", JOptionPane.ERROR_MESSAGE);
	        } catch (SQLException ex) {
	            showMessage("Lỗi kết nối cơ sở dữ liệu: " + ex.getMessage(), 
	                        "Lỗi", JOptionPane.ERROR_MESSAGE);
	        }

	    } catch (Exception e) {
	        showMessage("Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	    }
	}

	private boolean validateInput() {
		// Kiểm tra chuyến bay
		if (cbChuyenBay.getSelectedItem() == null) {
			showMessage("Vui lòng chọn chuyến bay!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// Kiểm tra thông tin khách hàng
		String hoTen = txtHoTen.getText();
		if (hoTen == null || hoTen.trim().isEmpty()) {
			showMessage("Vui lòng nhập họ tên khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			txtHoTen.requestFocus();
			return false;
		}

		String cmnd = txtCMND.getText();
		if (cmnd == null || cmnd.trim().isEmpty()) {
			showMessage("Vui lòng nhập CMND/CCCD!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			txtCMND.requestFocus();
			return false;
		}

		// Kiểm tra ngày sinh
		 if (dateNgaySinh.getDate() == null && txtNgaySinh.getText().trim().isEmpty()) {
		        showMessage("Vui lòng nhập hoặc chọn ngày sinh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		        return false;
		    }
		 
		// Kiểm tra định dạng ngày sinh nếu nhập text
		    if (!txtNgaySinh.getText().trim().isEmpty()) {
		        try {
		            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		            sdf.setLenient(false); // Chặt chẽ hơn trong việc kiểm tra ngày
		            sdf.parse(txtNgaySinh.getText().trim());
		        } catch (ParseException e) {
		            showMessage("Định dạng ngày sinh không hợp lệ. Vui lòng nhập theo định dạng dd/MM/yyyy", 
		                        "Lỗi", JOptionPane.ERROR_MESSAGE);
		            return false;
		        }
		    }

		// Kiểm tra số điện thoại
		String sdt = txtSoDienThoai.getText();
		if (sdt == null || sdt.trim().isEmpty()) {
			showMessage("Vui lòng nhập số điện thoại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			txtSoDienThoai.requestFocus();
			return false;
		}

		// Kiểm tra số lượng vé
		try {
			String soLuongText = txtSoLuong.getText().trim();
			if (soLuongText.isEmpty()) {
				showMessage("Vui lòng nhập số lượng vé!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				txtSoLuong.requestFocus();
				return false;
			}

			int soLuong = Integer.parseInt(soLuongText);
			if (soLuong <= 0) {
				showMessage("Số lượng vé phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				txtSoLuong.requestFocus();
				return false;
			}
		} catch (NumberFormatException e) {
			showMessage("Vui lòng nhập số lượng vé hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			txtSoLuong.requestFocus();
			return false;
		}

		return true;
	}

	private DatVe createBookingFromFields() throws Exception {
	    DatVe booking = new DatVe();
	    
	    // Thông tin ngày sinh
	    Date ngaySinh = null;
	    if (dateNgaySinh.getDate() != null) {
	        ngaySinh = new Date(dateNgaySinh.getDate().getTime());
	    } else {
	        String ngaySinhText = txtNgaySinh.getText().trim();
	        if (!ngaySinhText.isEmpty() && !ngaySinhText.equals("dd/MM/yyyy")) {
	            try {
	                SimpleDateFormat[] formats = {
	                    new SimpleDateFormat("dd/MM/yyyy"),
	                    new SimpleDateFormat("dd-MM-yyyy"),
	                    new SimpleDateFormat("yyyy-MM-dd")
	                };
	                
	                for (SimpleDateFormat format : formats) {
	                    try {
	                        java.util.Date parsedDate = format.parse(ngaySinhText);
	                        ngaySinh = new Date(parsedDate.getTime());
	                        break;
	                    } catch (ParseException e) {
	                        continue;
	                    }
	                }
	                
	                if (ngaySinh == null) {
	                    throw new ParseException("Không thể parse ngày sinh", 0);
	                }
	            } catch (Exception e) {
	                throw new Exception("Định dạng ngày sinh không hợp lệ. Vui lòng nhập theo định dạng dd/MM/yyyy");
	            }
	        }
	    }
	    booking.setNgaySinh(ngaySinh);

	    // Nếu là đặt vé mới, tạo mã đặt vé mới
	    if (txtMaDatVe.getText().trim().isEmpty()) {
	        booking.setMaDatVe(service.generateMaDatVe());
	    } else {
	        booking.setMaDatVe(txtMaDatVe.getText().trim());
	    }

	    // Thông tin chuyến bay
	    ChuyenBay selectedFlight = (ChuyenBay) cbChuyenBay.getSelectedItem();
	    if (selectedFlight != null) {
	        booking.setChuyenBay(selectedFlight);
	        // Nếu ngày bay của chuyến bay là null thì dùng ngày hiện tại
	        java.sql.Date ngayBay = selectedFlight.getNgayBay() != null ? 
	            selectedFlight.getNgayBay() : 
	            new java.sql.Date(System.currentTimeMillis());
	        booking.setNgayBay(ngayBay);
	    } else {
	        throw new Exception("Vui lòng chọn chuyến bay!");
	    }

	    // KHÔNG gán mã khách hàng ở đây - để cho service xử lý

	    // Thông tin khách hàng
	    booking.setHoTen(txtHoTen.getText().trim());
	    booking.setCMND(txtCMND.getText().trim());
	    booking.setGioiTinh(cbGioiTinh.getSelectedItem().toString());
	    booking.setQuocTich(txtQuocTich.getText().trim());

	    // Thông tin liên hệ
	    booking.setSoDienThoai(txtSoDienThoai.getText().trim());
	    booking.setEmail(txtEmail.getText().trim());
	    booking.setDiaChi(txtDiaChi.getText().trim());

	    // Thông tin vé
	    booking.setHangVe(cbHangVe.getSelectedItem().toString());
	    booking.setSoLuong(Integer.parseInt(txtSoLuong.getText().trim()));
	    booking.setTongGia(Double.parseDouble(txtTongGia.getText().trim().replace("VND", "").replace(",", "").trim()));

	    // Thông tin thanh toán
	    booking.setPhuongThucThanhToan(cbPhuongThucThanhToan.getSelectedItem().toString());
	    booking.setMaGiamGia(txtMaGiamGia.getText().trim());
	    booking.setXacNhanThanhToan(chkXacNhanThanhToan.isSelected());

	    // Yêu cầu đặc biệt
	    booking.setSuatAnDacBiet(chkSuatAnDacBiet.isSelected());
	    booking.setHoTroYTe(chkHoTroYTe.isSelected());
	    booking.setChoNgoiUuTien(chkChoNgoiUuTien.isSelected());
	    booking.setHanhLyDacBiet(chkHanhLyDacBiet.isSelected());

	    // Thông tin liên hệ khẩn cấp
	    booking.setNguoiLienHe(txtNguoiLienHe.getText().trim());
	    booking.setSDTNguoiLienHe(txtSDTNguoiLienHe.getText().trim());

	    // Đặt trạng thái mặc định
	    booking.setTrangThai("Đã đặt");

	    return booking;
	}
	
	public class DateUtils {
	    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	    @SuppressWarnings("exports")
		public static java.sql.Date stringToSqlDate(String dateStr) {
	        if (dateStr == null || dateStr.trim().isEmpty()) {
	            return null;
	        }
	        try {
	            java.util.Date parsed = dateFormat.parse(dateStr);
	            return new java.sql.Date(parsed.getTime());
	        } catch (ParseException e) {
	            return null;
	        }
	    }

	    @SuppressWarnings("exports")
		public static String dateToString(java.sql.Date date) {
	        if (date == null) {
	            return "Chưa xác định";
	        }
	        return dateFormat.format(date);
	    }

	    @SuppressWarnings("exports")
		public static java.sql.Date utilDateToSqlDate(java.util.Date date) {
	        if (date == null) {
	            return null;
	        }
	        return new java.sql.Date(date.getTime());
	    }
	}

	private void refreshTable() {
		try {
			List<DatVe> bookings = service.getAllDatVe();
			updateTable(bookings);
		} catch (Exception e) {
			showMessage("Lỗi khi tải danh sách đặt vé: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void updateTable(List<DatVe> bookings) {
	    SwingUtilities.invokeLater(() -> {
	        tableModel.setRowCount(0);
	        for (DatVe booking : bookings) {
	            // Format giá trị tiền thành String trước khi thêm vào table
	            String formattedPrice = String.format("%,.0f VND", booking.getTongGia());
	            
	            tableModel.addRow(new Object[]{
	                booking.getMaDatVe(),
	                booking.getChuyenBay().getMaChuyenBay(),
	                booking.getHoTen(),
	                booking.getNgayDat(),
	                booking.getSoLuong(),
	                formattedPrice,  // Đã format sẵn thành String
	                booking.getTrangThai()
	            });
	        }
	    });
	}

	public void showMessage(String message, String title, int messageType) {
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}

	// Các phương thức getter cho controller
	@SuppressWarnings("exports")
	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public void loadBookingData(DatVe booking) {
		if (booking != null) {
			txtMaDatVe.setText(booking.getMaDatVe());
			cbChuyenBay.setSelectedItem(booking.getChuyenBay());
			txtHoTen.setText(booking.getHoTen());
			txtCMND.setText(booking.getCMND());
			dateNgaySinh.setDate(booking.getNgaySinh());
			cbGioiTinh.setSelectedItem(booking.getGioiTinh());
			txtQuocTich.setText(booking.getQuocTich());
			txtSoDienThoai.setText(booking.getSoDienThoai());
			txtEmail.setText(booking.getEmail());
			txtDiaChi.setText(booking.getDiaChi());
			txtSoLuong.setText(String.valueOf(booking.getSoLuong()));
			txtTongGia.setText(String.format("%,.0f VND", booking.getTongGia()));
			cbHangVe.setSelectedItem(booking.getHangVe());
			cbPhuongThucThanhToan.setSelectedItem(booking.getPhuongThucThanhToan());
			txtMaGiamGia.setText(booking.getMaGiamGia());
			chkXacNhanThanhToan.setSelected(booking.isXacNhanThanhToan());
			txtNguoiLienHe.setText(booking.getNguoiLienHe());
			txtSDTNguoiLienHe.setText(booking.getSDTNguoiLienHe());
			chkSuatAnDacBiet.setSelected(booking.isSuatAnDacBiet());
			chkHoTroYTe.setSelected(booking.isHoTroYTe());
			chkChoNgoiUuTien.setSelected(booking.isChoNgoiUuTien());
			chkHanhLyDacBiet.setSelected(booking.isHanhLyDacBiet());
		}
	}

	public void updateFlightsList(List<ChuyenBay> flights) {
		cbChuyenBay.removeAllItems();
		for (ChuyenBay flight : flights) {
			cbChuyenBay.addItem(flight);
		}
	}

	public String getMaDatVeText() {
		return txtMaDatVe.getText().trim();
	}

	public boolean isImmediatePayment() {
		return chkXacNhanThanhToan.isSelected();
	}

	// Thông tin chuyến bay
	public ChuyenBay getSelectedChuyenBay() {
		return (ChuyenBay) cbChuyenBay.getSelectedItem();
	}

	public int getSoLuongValue() {
		try {
			return Integer.parseInt(txtSoLuong.getText().trim());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public String getHangVeText() {
		return (String) cbHangVe.getSelectedItem();
	}

	public String getPhuongThucThanhToanText() {
		return (String) cbPhuongThucThanhToan.getSelectedItem();
	}

	public String getSelectedDiscountType() {
		return txtMaGiamGia.getText().trim();
	}

	// Thông tin khách hàng
	public String getHoTenText() {
		return txtHoTen.getText().trim();
	}

	public String getCMNDText() {
		return txtCMND.getText().trim();
	}

	@SuppressWarnings("exports")
    public Date getNgaySinhDate() {
		return dateNgaySinh.getDate() != null ? new Date(dateNgaySinh.getDate().getTime()) : null;
	}

	public String getGioiTinhText() {
		return (String) cbGioiTinh.getSelectedItem();
	}

	public String getQuocTichText() {
		return txtQuocTich.getText().trim();
	}

	// Thông tin liên hệ
	public String getSoDienThoaiText() {
		return txtSoDienThoai.getText().trim();
	}

	public String getEmailText() {
		return txtEmail.getText().trim();
	}

	public String getDiaChiText() {
		return txtDiaChi.getText().trim();
	}

	// Yêu cầu đặc biệt
	public boolean isSuatAnDacBiet() {
		return chkSuatAnDacBiet.isSelected();
	}

	public boolean isHoTroYTe() {
		return chkHoTroYTe.isSelected();
	}

	public boolean isChoNgoiUuTien() {
		return chkChoNgoiUuTien.isSelected();
	}

	public boolean isHanhLyDacBiet() {
		return chkHanhLyDacBiet.isSelected();
	}

	// Thông tin liên hệ khẩn cấp
	public String getNguoiLienHeText() {
		return txtNguoiLienHe.getText().trim();
	}

	public String getSDTNguoiLienHeText() {
		return txtSDTNguoiLienHe.getText().trim();
	}

	public boolean isValidInput() {
		// Kiểm tra chuyến bay
		if (cbChuyenBay.getSelectedItem() == null) {
			showMessage("Vui lòng chọn chuyến bay!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// Kiểm tra thông tin khách hàng
		if (txtHoTen.getText().trim().isEmpty()) {
			showMessage("Vui lòng nhập họ tên khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (txtCMND.getText().trim().isEmpty()) {
			showMessage("Vui lòng nhập CMND/CCCD!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (dateNgaySinh.getDate() == null) {
			showMessage("Vui lòng chọn ngày sinh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// Kiểm tra thông tin liên hệ
		if (txtSoDienThoai.getText().trim().isEmpty()) {
			showMessage("Vui lòng nhập số điện thoại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// Kiểm tra số lượng vé
		try {
			int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
			if (soLuong <= 0) {
				showMessage("Số lượng vé phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		} catch (NumberFormatException e) {
			showMessage("Vui lòng nhập số lượng vé hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}
}