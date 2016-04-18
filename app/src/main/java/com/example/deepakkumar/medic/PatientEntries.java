/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medic_hospital;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Deepak Kumar
 */
public class WeightTrend {

	Connection con;
	ResultSet rs;
	PreparedStatement pst;
	JFrame f = new JFrame();

	static int patientId;

	public WeightTrend(int pId) {

		patientId = pId;

	}

	public static void main(String[] args) {

	}

	public void populateChart(int id) {

		int[] weightList;
		double[] dateList;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/medic", "root", "");

			//To get the number of rows in the result
			String sql = "Select Count(*) from patient_statistics where patient_id = ?";
			pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			rs = pst.executeQuery();

			if (rs.next()) {
				weightList = new int[rs.getInt("Count(*)")];
				dateList = new double[rs.getInt("Count(*)")];
			} else {
				weightList = new int[1];
				dateList = new double[1];
			}

			//To get the statistic
			sql = "Select weight, date_of_submission from patient_statistics where patient_id = ?";
			pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			rs = pst.executeQuery();

			int li_item = 0;
			String date = "";
			while (rs.next()) {
				date = "";

				weightList[li_item] = rs.getInt("weight");

				date = rs.getString("date_of_submission");
				date = date.replaceAll("/", "");
				date = date.replaceAll(":", "");
				date = date.replaceAll(" ", "");

				dateList[li_item] = Double.parseDouble(date);

				li_item++;
			}

			JFreeChart chart;
			chart = ChartFactory.createLineChart(
					"Weight Over Time",
					"Time", "Weight(kgs)",
					createDataset(weightList, dateList),
					PlotOrientation.VERTICAL,
					true, true, false);

			//Setting the window contents of the JFrame
			ChartPanel chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
			f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			f.setLayout(new BorderLayout(0, 5));
			f.add(chartPanel, BorderLayout.CENTER);
			f.pack();
			f.setVisible(true);

		} catch (SQLException | ClassNotFoundException ex) {
			Logger.getLogger(GlucoseTrend.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	//populate Chart
	private DefaultCategoryDataset createDataset(int[] glucoseList, double[] dateList) {
		int i;
		int size = glucoseList.length;
		DefaultCategoryDataset data = new DefaultCategoryDataset();

		for (i = 0; i < size; i++) {
			data.addValue(glucoseList[i], "Weight Level", "" + dateList[i]);

		}
		return data;
	}

}
