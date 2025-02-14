package vn.DA_KNNN.Components;

import javax.swing.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.*;

public class PieChart extends JPanel {
	private static final long serialVersionUID = 1L;
	private int[] values;
	private String[] columnNames;
	private String title;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int[] getValues() {
		return values;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setValues(int[] values) {
		this.values = values;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public PieChart() {
		this.values = new int[] { 10, 30, 50, 40 };
		this.columnNames = new String[] { "A", "B", "C", "D" };

		// Cập nhật layout để PieChart có thể co giãn
		setLayout(new BorderLayout());

		// Tạo biểu đồ và thêm vào panel
		add(createChartPanel(values, columnNames), BorderLayout.CENTER);
	}

	public PieChart(String[] columns,int[] values) {
		this.values = values;
		this.columnNames = columns;
		this.title = "Biểu đồ tròn";
		// Cập nhật layout để PieChart có thể co giãn
		setLayout(new BorderLayout());

		// Tạo biểu đồ và thêm vào panel
		add(createChartPanel(values, columnNames), BorderLayout.CENTER);
		updateChart();
	}
	
	private ChartPanel createChartPanel(int[] values, String[] columnNames) {
		// Tạo dataset cho Pie Chart
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
		for (int i = 0; i < values.length; i++) {
			dataset.setValue(columnNames[i], values[i]);
		}

		// Tạo Pie Chart từ dataset
		JFreeChart chart = ChartFactory.createPieChart(this.title, // Tên biểu đồ
				dataset, // Dữ liệu
				true, // Chú thích
				true, // Tooltip
				false // Không có URL
		);

		// Tùy chỉnh Pie Chart
		PiePlot<?> plot = (PiePlot<?>) chart.getPlot();
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})"));

		// Biểu đồ co giãn tốt hơn
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setMinimumSize(new Dimension(300, 300)); // Giữ nhỏ nhất 300x300
		chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		return chartPanel;
	}
	
	public void updateChart() {
	    removeAll(); // Xóa biểu đồ cũ
	    add(createChartPanel(values, columnNames), BorderLayout.CENTER); // Thêm biểu đồ mới
	    revalidate();
	    repaint();
	}
}
