package Components;

import javax.swing.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.title.TextTitle;
import java.awt.*;
import java.text.DecimalFormat;

public class PieChart extends JPanel {
    private static final long serialVersionUID = 1L;
    private int[] values;
    private String[] columnNames;
    private String title;
    
    // Modern color scheme
    private final Color[] MODERN_COLORS = {
        new Color(52, 152, 219),   // Blue
        new Color(46, 204, 113),   // Green
        new Color(155, 89, 182),   // Purple
        new Color(231, 76, 60),    // Red
        new Color(241, 196, 15),   // Yellow
        new Color(230, 126, 34),   // Orange
        new Color(149, 165, 166),  // Gray
        new Color(211, 84, 0)      // Dark Orange
    };

    public PieChart() {
        initializeDefaultValues();
        setupPanel();
    }

    public PieChart(String[] columns, int[] values) {
        this.values = values;
        this.columnNames = columns;
        this.title = "Biểu đồ tròn";
        setupPanel();
        updateChart();
    }

    private void initializeDefaultValues() {
        this.values = new int[]{10, 30, 50, 40};
        this.columnNames = new String[]{"A", "B", "C", "D"};
        this.title = "Biểu đồ tròn";
    }

    private void setupPanel() {
        setLayout(new BorderLayout());
        add(createModernChartPanel(), BorderLayout.CENTER);
    }

    private ChartPanel createModernChartPanel() {
        JFreeChart chart = createModernChart();
        return createStyledChartPanel(chart);
    }

    private JFreeChart createModernChart() {
        DefaultPieDataset dataset = createDataset();
        JFreeChart chart = ChartFactory.createPieChart(
            title,
            dataset,
            true,  // legend
            true,  // tooltips
            false  // urls
        );

        customizeChartStyle(chart);
        return chart;
    }

    private DefaultPieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (int i = 0; i < values.length; i++) {
            dataset.setValue(columnNames[i], values[i]);
        }
        return dataset;
    }

    private void customizeChartStyle(JFreeChart chart) {
        // Font styles
        Font titleFont = new Font("Segoe UI", Font.BOLD, 18);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 12);
        Font legendFont = new Font("Segoe UI", Font.PLAIN, 11);

        // Title style
        chart.setBackgroundPaint(new Color(255, 255, 255, 240));
        TextTitle title = chart.getTitle();
        title.setFont(titleFont);
        title.setPaint(new Color(51, 51, 51));

        // Plot customization
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        plot.setLabelFont(labelFont);
        plot.setLabelBackgroundPaint(new Color(255, 255, 255, 200));
        plot.setLabelOutlinePaint(null);
        plot.setLabelShadowPaint(null);
        plot.setShadowPaint(null);
        plot.setInteriorGap(0.04);

        // Custom label generator
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
            "{0}: {1} ({2})",
            new DecimalFormat("0"),
            new DecimalFormat("0.0%")
        ));

        // Set modern colors for sections
        for (int i = 0; i < values.length; i++) {
            plot.setSectionPaint(columnNames[i], MODERN_COLORS[i % MODERN_COLORS.length]);
        }

        // Legend style
        chart.getLegend().setFrame(org.jfree.chart.block.BlockBorder.NONE);
        chart.getLegend().setBackgroundPaint(null);
        chart.getLegend().setItemFont(legendFont);

        // Add subtle shadow effect
        plot.setShadowXOffset(4.0);
        plot.setShadowYOffset(4.0);
        plot.setShadowPaint(new Color(0, 0, 0, 50));
    }

    private ChartPanel createStyledChartPanel(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 400);
            }
        };
        
        chartPanel.setMinimumDrawWidth(0);
        chartPanel.setMinimumDrawHeight(0);
        chartPanel.setMaximumDrawWidth(Integer.MAX_VALUE);
        chartPanel.setMaximumDrawHeight(Integer.MAX_VALUE);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setMouseWheelEnabled(true);
        
        return chartPanel;
    }

    public void updateChart() {
        removeAll();
        add(createModernChartPanel(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // Getters và setters giữ nguyên
    public String getTitle() { return title; }
    public void setTitle(String title) { 
        this.title = title; 
        updateChart();
    }
    public int[] getValues() { return values; }
    public String[] getColumnNames() { return columnNames; }
    public void setValues(int[] values) { 
        this.values = values; 
        updateChart();
    }
    public void setColumnNames(String[] columnNames) { 
        this.columnNames = columnNames; 
        updateChart();
    }
}