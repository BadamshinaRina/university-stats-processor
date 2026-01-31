package university.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import university.model.Statistics;
import university.model.Student;
import university.model.University;
import university.model.xml.UniversityDataXml;
import university.util.*;


import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class ExportService {
    private static final Logger logger = LoggerUtil.getLogger(ExportService.class);

    public static ExportResult exportAllData (List<Student> students, List <University> universities, List <Statistics> statistics){
        logger.info("Экспорт данных во все форматы ");
        ExportResult exportResult = new ExportResult();
        exportResult.setExportTime(new Date());

        try {
            FileUtil.createDirectory("exports");
            FileUtil.createDirectory("exports/xml");
            FileUtil.createDirectory("exports/excel");
            FileUtil.createDirectory("exports/json");
            FileUtil.createDirectory("exports/archive");

            UniversityDataXml xmlData = DataConverter.createUniversityDataXml(students,universities,statistics);

            String timestamp = String.valueOf(System.currentTimeMillis());
            String xmlFileName = "university_data_" + timestamp + ".xml";
            String xmlPath = "exports/xml" + xmlFileName;

            XmlWriter.writeXmlFile(xmlData,xmlPath);
            exportResult.setXmlFilePath(xmlPath);

            String jsonFileName = "university_data_" + timestamp + ".json";
            String jsonPath = "exports/json" + jsonFileName;

            String jsonContent = JsonUtil.serialize(xmlData);
            FileUtil.writeFileContent(jsonPath, jsonContent);
            exportResult.setJsonFilePath(jsonPath);

            String excelFileName = "university_statistics_" +timestamp + "xlsx";
            String excelPath = "exports/excel" + excelFileName;

            exportToExcel(statistics, excelPath);
            exportResult.setExcelFilePath(excelPath);

            createArchive(exportResult);
            generateExportReport(exportResult, students.size(), universities.size(), statistics.size());

            logger.info("Экспорт удачно завершен");
            exportResult.setSuccess(true);

        } catch (Exception e) {
            LoggerUtil.logException(logger, "Ошибка при экспорте данных", e);
            exportResult.setErrorMessage(e.getMessage());
            exportResult.setSuccess(false);

        }
        return exportResult;


    }

    private static void exportToExcel(List <Statistics> statistics, String filePath) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Статистика");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Профиль обучения");
        headerRow.createCell(1).setCellValue("Средний балл");
        headerRow.createCell(2).setCellValue("Количество студентов");
        headerRow.createCell(3).setCellValue("Количество университетов");

        int rowNum = 1;
        for(Statistics stat: statistics) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(stat.getMainProfile()!=null? stat.getMainProfile().getEnglishName():"UNKNOWN");
            row.createCell(1).setCellValue(stat.getAvgExamScore()!=null?stat.getAvgExamScore().doubleValue():0.0);
            row.createCell(2).setCellValue(stat.getStudentCount()!=0? stat.getStudentCount():0);
            row.createCell(3).setCellValue(stat.getUniversityCount()!=0? stat.getUniversityCount():0);
        }

        for(int i=0; i<4; i++) {
            sheet.autoSizeColumn(i);
        }

        try(FileOutputStream stream = new FileOutputStream(filePath)) {
            workbook.write(stream);
        }

        workbook.close();
        LoggerUtil.logFileOperation(logger, "Excel файл создан", filePath);
    }
    private static void createArchive(ExportResult exportResult) throws IOException {
        String archiveDir = "exports/archive/export_" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        FileUtil.createDirectory(archiveDir);

        if(exportResult.getXmlFilePath()!=null) {
            String xmlArchivePath = archiveDir+"/"+ Paths.get(exportResult.getXmlFilePath()).getFileName();
            FileUtil.copyFile(exportResult.getXmlFilePath(), xmlArchivePath);
        }

        if(exportResult.getExcelFilePath()!=null) {
            String excelArchivePath = archiveDir + "/" + Paths.get(exportResult.getExcelFilePath()).getFileName();
            FileUtil.copyFile(exportResult.getExcelFilePath(), excelArchivePath);
        }

        if(exportResult.getJsonFilePath()!=null) {
            String jsonArchivePath = archiveDir+ "/" + Paths.get(exportResult.getJsonFilePath()).getFileName();
            FileUtil.copyFile(exportResult.getJsonFilePath(), jsonArchivePath);
        }

        exportResult.setArchivePath(archiveDir);

    }
    private static void generateExportReport(ExportResult exportResult, int studentCount, int universityCount, int statisticCount) throws IOException {
        String reportContent = String.format(
                "ОТЧЕТ ОБ ЭКСПОРТЕ ДАННЫХ\n" + "========================\n" + "Дата экспорта: %s\n" + "Статус: %s\n\n" +
                        "СТАТИСТИКА ДАННЫХ:\n" + "- Студентов: %d\n" + "- Университетов: %d\n" + "- Статистических записей: %d\n\n" +
                        "СОЗДАННЫЕ ФАЙЛЫ:\n" + "- XML: %s\n" + "- JSON: %s\n" + "- Excel: %s\n\n" + "АРХИВ: %s\n",
                new Date(), exportResult.isSuccess() ? "УСПЕШНО" : "ОШИБКА", studentCount, universityCount, statisticCount,
                exportResult.getXmlFilePath(), exportResult.getJsonFilePath(), exportResult.getExcelFilePath(), exportResult.getArchivePath());

        String reportPath = "exports/export_report_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt";

        FileUtil.writeFileContent(reportPath,reportContent);
        exportResult.setReportPath(reportPath);
        logger.info("Отчет экспорта создан " + reportPath);

    }

    public static class ExportResult{
        private String errorMessage;
        private String xmlFilePath;
        private String jsonFilePath;
        private String excelFilePath;
        private String archivePath;
        private String reportPath;
        private boolean success;
        private Date exportTime;

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getXmlFilePath() {
            return xmlFilePath;
        }

        public void setXmlFilePath(String xmlFilePath) {
            this.xmlFilePath = xmlFilePath;
        }

        public String getJsonFilePath() {
            return jsonFilePath;
        }

        public void setJsonFilePath(String jsonFilePath) {
            this.jsonFilePath = jsonFilePath;
        }

        public String getExcelFilePath() {
            return excelFilePath;
        }

        public void setExcelFilePath(String excelFilePath) {
            this.excelFilePath = excelFilePath;
        }

        public String getArchivePath() {
            return archivePath;
        }

        public void setArchivePath(String archivePath) {
            this.archivePath = archivePath;
        }

        public String getReportPath() {
            return reportPath;
        }

        public void setReportPath(String reportPath) {
            this.reportPath = reportPath;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public Date getExportTime() {
            return exportTime;
        }

        public void setExportTime(Date exportTime) {
            this.exportTime = exportTime;
        }

        @Override
        public String toString() {
            return "ExportResult{" +
                    "xml='" + xmlFilePath + '\'' +
                    ", json='" + jsonFilePath + '\'' +
                    ", excel='" + excelFilePath + '\'' +
                    ", success=" + success +
                    '}';
        }
    }
}
