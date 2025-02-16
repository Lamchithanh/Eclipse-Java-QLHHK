package Components;

import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.title.TextTitle;

import java.awt.*;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("unused")
public class ColumnChart extends JPanel {
    private static final long serialVersionUID = 1L;
    private int[] values;
    private String[] columnNames;
    private DefaultCategoryDataset dataset;
    private ChartPanel chartPanel;
    private String title;
    private String titleX;
    private String titleY;

    // Màu sắc hiện đại
    private final Color[] MODERN_COLORS = {
        new Color(55, 126, 184),  // Xanh dương
        new Color(77, 175, 74),   // Xanh lá
        new Color(152, 78, 163),  // Tím
        new Color(255, 127, 0),   // Cam
        new Color(228, 26, 28),   // Đỏ
        new Color(255, 187, 120)  // Cam nhạt
    };

    public ColumnChart() {
        initializeDefaultValues();
        setupPanel();
    }

    private void initializeDefaultValues() {
        this.values = new int[]{10, 30, 20};
        this.columnNames = new String[]{"A", "B", "C"};
        this.title = "Biểu đồ cột";
        this.titleX = "Tháng";
        this.titleY = "Doanh thu (VND)";
        this.dataset = new DefaultCategoryDataset();
    }

    private void setupPanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        chartPanel = createModernChartPanel(dataset);
        add(chartPanel);
        updateChart();
    }

    private ChartPanel createModernChartPanel(DefaultCategoryDataset data) {
        JFreeChart chart = createBaseChart(data);
        styleChart(chart);
        return createStyledChartPanel(chart);
    }

    private JFreeChart createBaseChart(DefaultCategoryDataset data) {
        JFreeChart chart = ChartFactory.createBarChart(
            this.title,
            this.titleX,
            this.titleY,
            data,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        return chart;
    }

    private void styleChart(JFreeChart chart) {
        // Phông chữ hiện đại
        Font titleFont = new Font("Segoe UI", Font.BOLD, 18);
        Font axisFont = new Font("Segoe UI", Font.PLAIN, 12);
        Font legendFont = new Font("Segoe UI", Font.PLAIN, 11);

        // Style tổng thể
        chart.setBackgroundPaint(new Color(255, 255, 255, 240));
        chart.getTitle().setFont(titleFont);
        chart.getTitle().setPaint(new Color(51, 51, 51));

        // Style plot
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(new Color(230, 230, 230));
        plot.setOutlinePaint(null);
        plot.setDomainGridlinesVisible(false);

        // Style renderer
        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                return MODERN_COLORS[column % MODERN_COLORS.length];
            }
        };
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);
        renderer.setItemMargin(0.1);
        plot.setRenderer(renderer);

        // Style axes
        CategoryAxis domainAxis = plot.getDomainAxis();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        
        domainAxis.setTickLabelFont(axisFont);
        domainAxis.setLabelFont(axisFont);
        rangeAxis.setTickLabelFont(axisFont);
        rangeAxis.setLabelFont(axisFont);

        // Style legend
        chart.getLegend().setFrame(org.jfree.chart.block.BlockBorder.NONE);
        chart.getLegend().setBackgroundPaint(null);
        chart.getLegend().setItemFont(legendFont);
    }

    private ChartPanel createStyledChartPanel(JFreeChart chart) {
        ChartPanel panel = new ChartPanel(chart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 400);
            }
        };
        panel.setMinimumDrawWidth(0);
        panel.setMinimumDrawHeight(0);
        panel.setMaximumDrawWidth(Integer.MAX_VALUE);
        panel.setMaximumDrawHeight(Integer.MAX_VALUE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setMouseWheelEnabled(true);
        panel.setDomainZoomable(true);
        panel.setRangeZoomable(true);
        return panel;
    }

    public void updateChart() {
        dataset.clear();
        for (int i = 0; i < columnNames.length; i++) {
            dataset.addValue(values[i], "Doanh thu", columnNames[i]);
        }

        JFreeChart chart = createBaseChart(dataset);
        styleChart(chart);

        if (chartPanel != null) {
            remove(chartPanel);
        }

        chartPanel = createStyledChartPanel(chart);
        add(chartPanel);
        revalidate();
        repaint();
    }


    // Setter for chart values
    public void setValues(int[] values) {
        this.values = values;
        updateChart(); // Update chart after setting new values
    }

    // Setter for column names
    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
        updateChart(); // Update chart after setting new column names
    }

    // Getter for values
    public int[] getValues() {
        return values;
    }

    // Getter for column names
    public String[] getColumnNames() {
        return columnNames;
    }

    // Getter for X-axis title
    public String getTitleX() {
        return titleX;
    }

    // Setter for X-axis title
    public void setTitleX(String titleX) {
        this.titleX = titleX;
        updateChart(); // Update chart after setting new X-axis title
    }

    // Getter for Y-axis title
    public String getTitleY() {
        return titleY;
    }

    // Setter for Y-axis title
    public void setTitleY(String titleY) {
        this.titleY = titleY;
        updateChart(); // Update chart after setting new Y-axis title
    }

    // Getter for chart title
    public String getTitle() {
        return title;
    }

    // Setter for chart title
    public void setTitle(String title) {
        this.title = title;
        updateChart(); // Update chart after setting new title
    }
}
