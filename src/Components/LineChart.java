package Components;

import javax.swing.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("unused")
public class LineChart extends JPanel {
    private static final long serialVersionUID = 1L;
    private int[] values;
    private String[] columnNames;
    private String series;
    private String title;
    
    // Modern color schemes
    private final Color MAIN_LINE_COLOR = new Color(45, 152, 218);
    private final Color GRADIENT_START = new Color(45, 152, 218, 100);
    private final Color GRADIENT_END = new Color(45, 152, 218, 10);
    private final Color GRID_COLOR = new Color(230, 230, 230);
    private final Color AXIS_COLOR = new Color(51, 51, 51);
    private final Color TITLE_COLOR = new Color(51, 51, 51);
    private final Color POINT_COLOR = new Color(255, 255, 255);
    private final Color POINT_BORDER_COLOR = MAIN_LINE_COLOR;
    
    // Font settings
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private final Font AXIS_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font LEGEND_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    
    public LineChart() {
        initializeDefaultValues();
        setupPanel();
    }

    private void initializeDefaultValues() {
        this.values = new int[]{10, 30, 20, 40};
        this.columnNames = new String[]{"A", "B", "C", "D"};
        this.series = "Series 1";
        this.title = "Doanh thu từ 5 năm trước đến nay";
    }

    private void setupPanel() {
        setLayout(new BorderLayout());
        JPanel chartContainer = createModernChartPanel(values, series, columnNames);
        add(chartContainer, BorderLayout.CENTER);
    }

    private JPanel createModernChartPanel(int[] values, String series, String[] columnNames) {
        // Create dataset
        DefaultCategoryDataset dataset = createDataset(values, series, columnNames);
        
        // Create and customize chart
        JFreeChart chart = createModernChart(dataset);
        ChartPanel chartPanel = createStyledChartPanel(chart);
        
        // Create container panel
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        container.add(chartPanel, BorderLayout.CENTER);
        
        return container;
    }

    private DefaultCategoryDataset createDataset(int[] values, String series, String[] columnNames) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // Đảm bảo độ dài của mảng values và columnNames bằng nhau
        int minLength = Math.min(values.length, columnNames.length);
        for (int i = 0; i < minLength; i++) {
            dataset.addValue(values[i], series, columnNames[i]);
        }
        return dataset;
    }

    // Thêm kiểm tra trong setValues và setColumnNames
    public void setValues(int[] values) {
        if (values == null) {
            this.values = new int[0];
        } else {
            this.values = values.clone(); // Tạo bản sao để tránh tham chiếu trực tiếp
        }
        updateChart();
    }

    public void setColumnNames(String[] columnNames) {
        if (columnNames == null) {
            this.columnNames = new String[0];
        } else {
            this.columnNames = columnNames.clone(); // Tạo bản sao để tránh tham chiếu trực tiếp
        }
        updateChart();
    }

    private JFreeChart createModernChart(DefaultCategoryDataset dataset) {
        // Create base chart
        JFreeChart chart = ChartFactory.createLineChart(
            title,          // Chart title
            "Thời gian",    // X-axis label
            "Giá trị",      // Y-axis label
            dataset,        // Dataset
            PlotOrientation.VERTICAL,
            true,           // Include legend
            true,           // Include tooltips
            false          // URLs not required
        );

        // Customize chart appearance
        customizeChartAppearance(chart);
        
        return chart;
    }

    private void customizeChartAppearance(JFreeChart chart) {
        // Basic chart styling
        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(TITLE_FONT);
        chart.getTitle().setPaint(TITLE_COLOR);

        // Get the plot and customize it
        CategoryPlot plot = chart.getCategoryPlot();
        customizePlot(plot);
        
        // Customize renderer
        LineAndShapeRenderer renderer = createCustomRenderer();
        plot.setRenderer(renderer);
        
        // Customize axes
        customizeAxes(plot);
        
        // Customize legend
        customizeLegend(chart);
    }

    private void customizePlot(CategoryPlot plot) {
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(GRID_COLOR);
        plot.setDomainGridlinePaint(GRID_COLOR);
        plot.setOutlinePaint(null);
        plot.setRangeGridlineStroke(new BasicStroke(1.0f));
        plot.setDomainGridlineStroke(new BasicStroke(1.0f));
    }

    private LineAndShapeRenderer createCustomRenderer() {
        LineAndShapeRenderer renderer = new LineAndShapeRenderer() {
            @Override
            public Paint getItemPaint(int row, int col) {
                return MAIN_LINE_COLOR;
            }
        };
        
        // Line styling
        renderer.setSeriesStroke(0, new BasicStroke(2.5f));
        renderer.setSeriesShape(0, new Ellipse2D.Double(-4, -4, 8, 8));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setDrawOutlines(true);
        renderer.setUseFillPaint(true);
        renderer.setSeriesOutlinePaint(0, POINT_BORDER_COLOR);
        renderer.setSeriesFillPaint(0, POINT_COLOR);
        
        return renderer;
    }

    private void customizeAxes(CategoryPlot plot) {
        CategoryAxis domainAxis = plot.getDomainAxis();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        
        // Domain axis (X-axis)
        domainAxis.setTickLabelFont(AXIS_FONT);
        domainAxis.setLabelFont(AXIS_FONT);
        domainAxis.setTickLabelPaint(AXIS_COLOR);
        domainAxis.setAxisLinePaint(AXIS_COLOR);
        
        // Range axis (Y-axis)
        rangeAxis.setTickLabelFont(AXIS_FONT);
        rangeAxis.setLabelFont(AXIS_FONT);
        rangeAxis.setTickLabelPaint(AXIS_COLOR);
        rangeAxis.setAxisLinePaint(AXIS_COLOR);
    }

    private void customizeLegend(JFreeChart chart) {
        chart.getLegend().setFrame(org.jfree.chart.block.BlockBorder.NONE);
        chart.getLegend().setBackgroundPaint(null);
        chart.getLegend().setItemFont(LEGEND_FONT);
    }

    private ChartPanel createStyledChartPanel(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 400);
            }
        };
        
        // Configure chart panel
        chartPanel.setMinimumDrawWidth(0);
        chartPanel.setMinimumDrawHeight(0);
        chartPanel.setMaximumDrawWidth(Integer.MAX_VALUE);
        chartPanel.setMaximumDrawHeight(Integer.MAX_VALUE);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setDomainZoomable(true);
        chartPanel.setRangeZoomable(true);
        
        return chartPanel;
    }

    public void updateChart() {
        removeAll();
        add(createModernChartPanel(values, series, columnNames), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        updateChart();
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

    public void setSeries(String series) {
        this.series = series;
        updateChart();
    }
}