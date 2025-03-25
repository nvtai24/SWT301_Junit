package addtocart;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.io.File;

public class ReportExporter {

    private static final String OUTPUT_DIR = "cart_test_output/";

    // Hàm xóa toàn bộ file trong thư mục
    private static void deleteExistingReports() {
        File outputDir = new File(OUTPUT_DIR);
        if (outputDir.exists() && outputDir.isDirectory()) {
            File[] existingFiles = outputDir.listFiles();
            if (existingFiles != null) {
                for (File file : existingFiles) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
        } else {
            outputDir.mkdir(); // Tạo thư mục nếu chưa tồn tại
        }
    }

    // Hàm export báo cáo ra CSV
    public static void exportToCSV(List<TestResult> testResults) {
        deleteExistingReports(); // Xóa các báo cáo cũ trước khi tạo mới

        String reportPath = OUTPUT_DIR + "test_report_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportPath))) {
            writer.write("Test Case,Status,Message,Time (ms)");
            writer.newLine();

            for (TestResult result : testResults) {
                writer.write(result.testCaseName + ",");
                writer.write(result.status + ",");
                writer.write(result.message + ",");
                writer.write(result.executionTime + "");
                writer.newLine();
            }

            System.out.println("CSV report exported to: " + reportPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Hàm export báo cáo ra HTML
    public static void exportToHTML(List<TestResult> testResults) {
        deleteExistingReports(); // Xóa các báo cáo cũ trước khi tạo mới

        String reportPath = OUTPUT_DIR + "test_report_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".html";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportPath))) {
            writer.write("<!DOCTYPE html><html lang='en'><head>");
            writer.write("<meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            writer.write("<title>Test Results</title>");
            writer.write("<style>");
            writer.write("body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }");
            writer.write("h1 { text-align: center; color: #333; }");
            writer.write("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
            writer.write("th, td { padding: 12px 15px; text-align: left; border: 1px solid #ddd; }");
            writer.write("th { background-color: #4CAF50; color: white; }");
            writer.write("tr:nth-child(even) { background-color: #f2f2f2; }");
            writer.write("tr:hover { background-color: #ddd; }");
            writer.write(".status-passed { color: green; font-weight: bold; }");
            writer.write(".status-failed { color: red; font-weight: bold; }");
            writer.write(".status-error { color: orange; font-weight: bold; }");
            writer.write("</style>");
            writer.write("</head><body>");
            writer.write("<h1>Test Results</h1>");
            writer.write("<table>");
            writer.write("<tr><th>Test Case</th><th>Status</th><th>Message</th><th>Time (ms)</th></tr>");

            for (TestResult result : testResults) {
                String statusClass = "";
                switch (result.status) {
                    case "PASSED":
                        statusClass = "status-passed";
                        break;
                    case "FAILED":
                        statusClass = "status-failed";
                        break;
                    case "ERROR":
                        statusClass = "status-error";
                        break;
                }

                writer.write("<tr>");
                writer.write("<td>" + result.testCaseName + "</td>");
                writer.write("<td class='" + statusClass + "'>" + result.status + "</td>");
                writer.write("<td>" + result.message + "</td>");
                writer.write("<td>" + result.executionTime + "</td>");
                writer.write("</tr>");
            }

            writer.write("</table></body></html>");
            System.out.println("HTML report exported to: " + reportPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}