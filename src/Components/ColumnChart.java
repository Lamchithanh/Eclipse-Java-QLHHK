package Components;


import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.Color;
import java.awt.Dimension;

public class ColumnChart extends JPanel {
    private static final long serialVersionUID = 1L;
    private int[] values;
    private String[] columnNames;
    private DefaultCategoryDataset dataset;

    private ChartPanel chartPanel;
    private String title;
    private String titleX;
    private String titleY;

    // Constructor to initialize the chart with default values
    public ColumnChart() {
        this.values = new int[] { 10, 30, 20 };
        this.columnNames = new String[] { "A", "B", "C"};
        this.title = "Biểu đồ cột";
        this.titleX = "Tháng";
        this.titleY = "Doanh thu (VND)";

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); // Set layout to display chart horizontally
        dataset = new DefaultCategoryDataset();
        chartPanel = createChartPanel(dataset); // Initialize chart panel
        add(chartPanel); // Add chart panel to the JPanel
        updateChart(); // Update the chart with initial data
    }

    // Method to create a ChartPanel using a dataset
    private ChartPanel createChartPanel(DefaultCategoryDataset data) {
        // Create the chart using the given dataset
        JFreeChart chart = ChartFactory.createBarChart(
                this.title,                 // Chart title
                this.titleX,                // X-axis label
                this.titleY,                // Y-axis label
                data,                       // Dataset to be plotted
                PlotOrientation.VERTICAL,   // Vertical bar chart
                true,                        // Show legend
                true,                        // Enable tooltips
                false                        // Disable URL linking
        );

        // Customize the ChartPanel for better responsiveness
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400)); // Set preferred size
        chartPanel.setMinimumSize(new Dimension(300, 300));   // Minimum size for resizing
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add margin around chart
        chartPanel.setBackground(Color.white); // Set chart background color

        return chartPanel;
    }

    public void updateChart() {
        // Clear the existing dataset
        dataset.clear();

        // Add the new data to the dataset
        for (int i = 0; i < columnNames.length; i++) {
            dataset.addValue(values[i], "Doanh thu", columnNames[i]); // Add new data to the dataset
        }

        // Create a new chart with updated dataset and titles
        JFreeChart chart = ChartFactory.createBarChart(
                this.title,                 // Chart title
                this.titleX,                // X-axis label
                this.titleY,                // Y-axis label
                dataset,                    // Dataset to be plotted
                PlotOrientation.VERTICAL,   // Vertical bar chart
                true,                        // Show legend
                true,                        // Enable tooltips
                false                        // Disable URL linking
        );

        // Remove the old chart panel (if any) and create a new one
        if (chartPanel != null) {
            remove(chartPanel); // Remove the previous chart panel from the JPanel
        }

        // Create a new ChartPanel with the updated chart
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400)); // Set preferred size
        chartPanel.setMinimumSize(new Dimension(300, 300));   // Minimum size for resizing
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add margin around chart
        chartPanel.setBackground(Color.white); // Set chart background color

        // Add the new chart panel to the JPanel
        add(chartPanel);
        revalidate(); // Revalidate the JPanel layout
        repaint(); // Repaint the JPanel to reflect changes
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
