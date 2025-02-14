package vn.DA_KNNN.Components;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BoxLayout;

public class LineChart extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int[] values;
	private String[] columnNames;
	private String series;
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

	
	public String getSeries() {
		return series;
	}

	public void setValues(int[] values) {
		this.values = values;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public LineChart() {
		this.values = new int[] { 10, 30, 20, 40 };
		this.columnNames = new String[] { "A", "B", "C", "D" };
		this.title = "Doanh thu từ 5 năm trước đến nay";
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(createChartPanel(values, "Series 1", columnNames));
	}

	public JPanel createChartPanel(int[] values, String series, String[] columnNames) {
		// Tạo dataset cho Line Chart
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int i = 0; i < values.length; i++) {
			dataset.addValue(values[i], series, columnNames[i]);
		}

		// Tạo biểu đồ đường từ dataset
		JFreeChart chart = ChartFactory.createLineChart(this.title, // Tiêu đề biểu đồ
				"Category", // Tên trục X
				"Value", // Tên trục Y
				dataset, // Dataset
				org.jfree.chart.plot.PlotOrientation.VERTICAL, // Định hướng dọc
				true, // Hiển thị chú thích (legend)
				true, // Hiển thị tooltips
				false // Không có URL
		);

		// Tùy chỉnh giao diện biểu đồ
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		LineAndShapeRenderer renderer = new LineAndShapeRenderer();
		renderer.setSeriesPaint(0, new Color(0, 0, 255)); // Màu xanh cho đường
		renderer.setSeriesShapesVisible(0, true); // Hiển thị dấu tròn trên đường
		plot.setRenderer(renderer);

		// Tạo một ChartPanel chứa biểu đồ và trả về JPanel
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));

		return chartPanel;
	}
	
	public void updateChart() {
	    removeAll(); // Xóa biểu đồ cũ
	    add(createChartPanel(values,series, columnNames), BorderLayout.CENTER); // Thêm biểu đồ mới
	    revalidate();
	    repaint();
	}
}
