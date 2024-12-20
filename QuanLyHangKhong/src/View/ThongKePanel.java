package View;

import Model.ThongKe;
import Model.UserAccount;
import Service.ThongKeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ThongKePanel extends JPanel {
	private DefaultTableModel tableModel;
	private ThongKeService thongKeService;
	private JTextField maChuyenBayField;
	private JTextField sanBayField;
	private JTextField changBayField;
	private JTextField ngayBayField;
	private JTextField soGheDaDatField;
	private JTextField tinhTrangField;
	private UserAccount currentUser; // Thêm thuộc tính người dùng hiện tại

	public ThongKePanel(UserAccount user) {
		this.currentUser = user;
		thongKeService = new ThongKeService();
		setLayout(new BorderLayout(10, 10));
		setBackground(new Color(245, 245, 250));

		// Header Panel
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(new Color(245, 245, 250));

		// Tạo tiêu đề chính
		JLabel headerLabel = new JLabel("Thống Kê Chuyến Bay", SwingConstants.CENTER);
		headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
		headerLabel.setForeground(new Color(40, 40, 90));
		headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		headerPanel.add(headerLabel, BorderLayout.CENTER);

		// User and Logout Panel
		JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		userPanel.setBackground(new Color(245, 245, 250));
		
		
		headerPanel.add(userPanel, BorderLayout.EAST);

		add(headerPanel, BorderLayout.NORTH);

		// Panel nhập liệu
		JPanel inputPanel = new JPanel(new GridBagLayout());
		inputPanel.setBackground(Color.WHITE);
		inputPanel
				.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Thông tin chuyến bay"),
						BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Các ô nhập liệu
		maChuyenBayField = createStyledTextField();
		sanBayField = createStyledTextField();
		changBayField = createStyledTextField();
		ngayBayField = createStyledTextField();
		soGheDaDatField = createStyledTextField();
		tinhTrangField = createStyledTextField();

		// Thêm nhãn và trường nhập liệu
		addLabelAndField(inputPanel, "Mã chuyến bay:", maChuyenBayField, gbc, 0);
		addLabelAndField(inputPanel, "Sân bay:", sanBayField, gbc, 1);
		addLabelAndField(inputPanel, "Chặng bay:", changBayField, gbc, 2);
		addLabelAndField(inputPanel, "Ngày bay:", ngayBayField, gbc, 3);
		addLabelAndField(inputPanel, "Số ghế đã đặt:", soGheDaDatField, gbc, 4);
		addLabelAndField(inputPanel, "Tình trạng:", tinhTrangField, gbc, 5);

		// Bảng hiển thị danh sách thống kê
		String[] columnNames = { "Mã chuyến bay", "Sân bay", "Chặng bay", "Ngày bay", "Số ghế đã đặt", "Tình trạng" };
		tableModel = new DefaultTableModel(columnNames, 0);
		JTable thongKeTable = new JTable(tableModel);

		// Customize table appearance
		thongKeTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		thongKeTable.setRowHeight(25);
		JTableHeader header = thongKeTable.getTableHeader();
		header.setFont(new Font("Segoe UI", Font.BOLD, 14));
		header.setBackground(new Color(230, 230, 250));

		thongKeTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int selectedRow = thongKeTable.getSelectedRow();
				if (selectedRow >= 0) {
					// Lấy dữ liệu từ bảng và đặt vào các trường nhập liệu
					maChuyenBayField.setText(tableModel.getValueAt(selectedRow, 0).toString());
					sanBayField.setText(tableModel.getValueAt(selectedRow, 1).toString());
					changBayField.setText(tableModel.getValueAt(selectedRow, 2).toString());
					ngayBayField.setText(tableModel.getValueAt(selectedRow, 3).toString());
					soGheDaDatField.setText(tableModel.getValueAt(selectedRow, 4).toString());
					tinhTrangField.setText(tableModel.getValueAt(selectedRow, 5).toString());
				}
			}
		});

		JScrollPane tableScrollPane = new JScrollPane(thongKeTable);
		tableScrollPane.setPreferredSize(new Dimension(tableScrollPane.getPreferredSize().width, 350));
		tableScrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách thống kê chuyến bay"));

		// Chia giao diện thành 2 phần
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, tableScrollPane);
		splitPane.setResizeWeight(0.4);
		splitPane.setPreferredSize(new Dimension(600, 600));
		add(splitPane, BorderLayout.CENTER);

		// Panel chứa các nút thao tác
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		buttonPanel.setBackground(new Color(245, 245, 250));

		JButton updateButton = createStyledButton("Cập nhật");
		JButton exportButton = createStyledButton("Xuất báo cáo");
		JTextField searchField = new JTextField(15);
		searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		JButton searchButton = createStyledButton("Tìm kiếm");
		JComboBox<String> filterBox = new JComboBox<>(
				new String[] { "Tất cả", "Sắp khởi hành", "Delay", "Đã khởi hành" });
		filterBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		JButton filterButton = createStyledButton("Lọc");

		buttonPanel.add(updateButton);
		buttonPanel.add(exportButton);
		buttonPanel.add(new JLabel("Tìm mã chuyến bay:"));
		buttonPanel.add(searchField);
		buttonPanel.add(searchButton);
		buttonPanel.add(new JLabel("Lọc theo tình trạng:"));
		buttonPanel.add(filterBox);
		buttonPanel.add(filterButton);

		add(buttonPanel, BorderLayout.SOUTH);

		// Sự kiện cho các nút
		updateButton.addActionListener(e -> loadDataFromDatabase());

		exportButton.addActionListener(e -> exportStatisticsToCSV(thongKeTable));

		searchButton.addActionListener(e -> {
			String searchCode = searchField.getText();
			if (!searchCode.isEmpty()) {
				ThongKe thongKe = thongKeService.findThongKeByCode(searchCode);
				if (thongKe != null) {
					// Clear the table and show the search result
					tableModel.setRowCount(0);
					tableModel.addRow(
							new Object[] { thongKe.getMaChuyenBay(), thongKe.getTenSanBay(), thongKe.getChangBay(),
									thongKe.getNgayBay(), thongKe.getSoVeDaDat(), thongKe.getTinhTrang() });
				} else {
					JOptionPane.showMessageDialog(this, "Không tìm thấy chuyến bay!");
				}
			}
		});

		filterButton.addActionListener(e -> {
			String filterValue = (String) filterBox.getSelectedItem();
			filterDataByStatus(filterValue);
		});

		// Tải dữ liệu ban đầu
		loadDataFromDatabase();
	}

	// Phương thức trợ giúp để tạo TextField có kiểu
	private JTextField createStyledTextField() {
		JTextField textField = new JTextField();
		textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		textField.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 220), 1, true),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		return textField;
	}

	// Phương thức trợ giúp để thêm nhãn và trường
	private void addLabelAndField(JPanel panel, String labelText, JTextField field, GridBagConstraints gbc, int row) {
		JLabel label = new JLabel(labelText);
		label.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.weightx = 0.3;
		panel.add(label, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.7;
		panel.add(field, gbc);
	}

	// Phương thức tạo nút có kiểu
	private JButton createStyledButton(String text) {
		JButton button = new JButton(text);
		styleButton(button);
		return button;
	}

	// Phương thức style nút
	private void styleButton(JButton button) {
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));
		button.setBackground(new Color(70, 130, 180));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		button.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setBackground(new Color(100, 160, 210));
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setBackground(new Color(70, 130, 180));
			}
		});
	}

	// Phương thức đăng xuất
	private void performLogout() {
		// Đóng cửa sổ hiện tại
		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		topFrame.dispose();

		// Mở lại màn hình đăng nhập
		JFrame loginFrame = new JFrame("Airline Management System - Login");
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setBounds(100, 100, 500, 400);

		// Tạo panel đăng nhập mới
		LoginPanel loginPanel = new LoginPanel(null, loginFrame);
		loginFrame.add(loginPanel);
		loginFrame.setVisible(true);
	}

	// Các phương thức còn lại giữ nguyên như trong code gốc
	private void loadDataFromDatabase() {
		// Giữ nguyên implementation
		List<ThongKe> thongKeList = thongKeService.getAllThongKe();
		tableModel.setRowCount(0); // Xóa dữ liệu cũ trong bảng

		for (ThongKe thongKe : thongKeList) {
			tableModel.addRow(new Object[] { thongKe.getMaChuyenBay(), thongKe.getTenSanBay(), thongKe.getChangBay(),
					thongKe.getNgayBay(), thongKe.getSoVeDaDat(), thongKe.getTinhTrang() });
		}
	}

	private void exportStatisticsToCSV(JTable table) {
		// Giữ nguyên implementation
		try (FileWriter writer = new FileWriter("thong_ke_chuyen_bay.csv")) {
			writer.append("Mã chuyến bay, Sân bay, Chặng bay, Ngày bay, Số ghế đã đặt, Tình trạng\n");

			for (int row = 0; row < table.getRowCount(); row++) {
				for (int col = 0; col < table.getColumnCount(); col++) {
					writer.append(table.getValueAt(row, col).toString());
					if (col < table.getColumnCount() - 1) {
						writer.append(",");
					}
				}
				writer.append("\n");
			}
			JOptionPane.showMessageDialog(null, "Đã xuất báo cáo ra file CSV!");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Lỗi khi xuất báo cáo!");
			e.printStackTrace();
		}
	}

	private void filterDataByStatus(String status) {
		List<ThongKe> thongKeList = thongKeService.getAllThongKe();

		// Lọc dữ liệu theo tình trạng
		if (!status.equals("Tất cả")) {
			thongKeList.removeIf(thongKe -> !thongKe.getTinhTrang().equals(status));
		}

		// Xóa dữ liệu cũ trong bảng
		tableModel.setRowCount(0);

		// Thêm dữ liệu mới vào bảng
		for (ThongKe thongKe : thongKeList) {
			tableModel.addRow(new Object[] { thongKe.getMaChuyenBay(), thongKe.getTenSanBay(), thongKe.getChangBay(),
					thongKe.getNgayBay(), thongKe.getSoVeDaDat(), thongKe.getTinhTrang() });
		}
	}
}