package university.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import university.model.Statistics;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class XlsWriter {
    private XlsWriter() {
        throw new IllegalStateException("Utility class - создание объектов класса запрещено");
    }

    private static CellStyle createHeaderStyle(Workbook workbook){
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setFontName("Arial");
        headerFont.setColor(IndexedColors.WHITE.getIndex());

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        return headerStyle;
    }
    private static CellStyle createDataStyle(Workbook workbook) {
        Font dataFont = workbook.createFont();
        dataFont.setFontHeightInPoints((short)12);
        dataFont.setFontName("Arial");

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setFont(dataFont);
        dataStyle.setAlignment(HorizontalAlignment.LEFT);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setWrapText(true);

        return dataStyle;
    }
    private static CellStyle createNumberStyle(Workbook workbook) {
        CellStyle numberCellStyle = workbook.createCellStyle();
        numberCellStyle.setFont(workbook.createFont());
        numberCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        numberCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        numberCellStyle.setBorderBottom(BorderStyle.THIN);
        numberCellStyle.setBorderTop(BorderStyle.THIN);
        numberCellStyle.setBorderLeft(BorderStyle.THIN);
        numberCellStyle.setBorderRight(BorderStyle.THIN);

        DataFormat format = workbook.createDataFormat();
        numberCellStyle.setDataFormat(format.getFormat("#,##0.00"));
        return numberCellStyle;
    }

    public static void generateStatisticsTable(List<Statistics> statisticsList, String filePath) {
        System.out.println("ГЕНЕРАЦИЯ EXEL-ФАЙЛА");
        System.out.println("Файл " + filePath);
        System.out.println("Собрано статистик " + statisticsList.size());

        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet();
            sheet.setColumnWidth(0,4000);
            sheet.setColumnWidth(1,2500);
            sheet.setColumnWidth(2,3000);
            sheet.setColumnWidth(3,3000);
            sheet.setColumnWidth(4,10000);

            CellStyle headStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle numberStyle = createNumberStyle(workbook);

            Row headerRow = sheet.createRow(0);
            String [] headers = {"Профиль обучения", "Средний балл за экзамен", "Количество студентов", "Количество университетов", "Названия университетов"};

            for(int i=0; i<headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headStyle);
            }
            int rowNumber = 1;
            for(Statistics statistics: statisticsList) {
                Row row = sheet.createRow(rowNumber++);

                Cell profileCell = row.createCell(0);
                profileCell.setCellValue(statistics.getMainProfile()!=null ? statistics.getMainProfile().getEnglishName():"Общий");
                profileCell.setCellStyle(dataStyle);

                Cell scoreCell = row.createCell(1);
                BigDecimal avgScore = statistics.getAvgExamScore();
                if(avgScore!=null) {
                    scoreCell.setCellValue(avgScore.doubleValue());
                }
                else {
                    scoreCell.setCellValue("Н/Д");
                }
                scoreCell.setCellStyle(numberStyle);

                Cell studentCell = row.createCell(2);
                studentCell.setCellValue(statistics.getStudentCount());
                studentCell.setCellStyle(numberStyle);

                Cell universityCell = row.createCell(3);
                universityCell.setCellValue(statistics.getUniversityCount());
                universityCell.setCellStyle(numberStyle);

                Cell namesCell = row.createCell(4);
                namesCell.setCellValue(statistics.getUniversityName());
                namesCell.setCellStyle(dataStyle);
            }
            addSummaryRow(sheet, statisticsList, rowNumber,numberStyle,dataStyle);
            headerRow.setHeightInPoints((short)25);

            try(FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
                System.out.println("\nФайл успешно создан " + filePath);
                System.out.println("Записано строк " + rowNumber);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при создании файла " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void addSummaryRow(Sheet sheet, List<Statistics> statisticsList, int rowNum, CellStyle numberStyle, CellStyle dataStyle) {
        Row summaryRow = sheet.createRow(rowNum);
        Cell labelCell = summaryRow.createCell(0);
        labelCell.setCellValue("ИТОГО ");
        labelCell.setCellStyle(dataStyle);

        BigDecimal totalWeightedScore = statisticsList.stream().filter(stat->stat.getAvgExamScore()!=null&&stat.getStudentCount()>0)
                .map(stat->stat.getAvgExamScore().multiply(BigDecimal.valueOf(stat.getStudentCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalStudent = statisticsList.stream().mapToInt(Statistics::getStudentCount).sum();

        Cell totalScoreCell = summaryRow.createCell(1);
        if(totalStudent>0) {
            BigDecimal overallAvg = totalWeightedScore.divide(BigDecimal.valueOf(totalStudent), 2, BigDecimal.ROUND_HALF_UP);
            totalScoreCell.setCellValue(overallAvg.doubleValue());
        }
        else {
            totalScoreCell.setCellValue("H/Д");
        }
        totalScoreCell.setCellStyle(numberStyle);

        Cell totalStudentCell = summaryRow.createCell(2);
        totalStudentCell.setCellValue(totalStudent);
        totalStudentCell.setCellStyle(numberStyle);

        int totalUniversities = statisticsList.stream().mapToInt(Statistics::getUniversityCount).sum();
        Cell totalUniversitiesCell = summaryRow.createCell(3);
        totalUniversitiesCell.setCellValue(totalUniversities);
        totalUniversitiesCell.setCellStyle(numberStyle);

        Cell emptyCell = summaryRow.createCell(4);
        emptyCell.setCellValue(" ");
        emptyCell.setCellStyle(dataStyle);
    }

    public static void generateDetailReport(List <Statistics> statisticsList, String filePath){

        try(Workbook workbook = new XSSFWorkbook ()){
            Sheet summarySheet = workbook.createSheet("Сводная статистика");
            createSummarySheet(summarySheet,statisticsList,workbook);

            Sheet detailsSheet = workbook.createSheet("Детали по профилям");
            createDetailSheet(detailsSheet, statisticsList,workbook);

            Sheet chartSheet = workbook.createSheet("Диаграмма");
            createChartSheet(chartSheet, statisticsList, workbook);

        }
        catch (Exception e) {
            System.err.println("Ошибка при создании детализированного отчета " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createSummarySheet(Sheet summarySheet, List <Statistics> statisticsList, Workbook workbook) {
        for(int i=0; i<6; i++) {
            summarySheet.setColumnWidth(1, 4000);
        }

        Row titleRow = summarySheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Статистика по профилям обучения");

        Row headerRow = summarySheet.createRow(1);
        String [] header={"Профиль ","Средний балл","Студентов","Университетов", "% от общего", "Рейтинг"};
        CellStyle headerStyle = createHeaderStyle(workbook);
        for(int i=0; i<header.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(header[i]);
            cell.setCellStyle(headerStyle);
        }

        CellStyle dataStyle = createDataStyle(workbook);
        CellStyle percentStyle = workbook.createCellStyle();
        percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));

        int totalStudents = statisticsList.stream().mapToInt(Statistics::getStudentCount).sum();
        int rowNum = 3;
        for(Statistics stat: statisticsList) {
            Row row = summarySheet.createRow(rowNum++);
            row.createCell(0).setCellValue(stat.getMainProfile().getEnglishName());

            if(stat.getAvgExamScore()!=null) {
                row.createCell(1).setCellValue(stat.getAvgExamScore().doubleValue());
            }

            row.createCell(2).setCellValue(stat.getStudentCount());
            row.createCell(3).setCellValue(stat.getUniversityCount());

            if(totalStudents>0) {
                double percent = (double) stat.getStudentCount()/totalStudents;
                Cell percentCell = row.createCell(4);
                percentCell.setCellValue(percent);
                percentCell.setCellStyle(percentStyle);
            }

            if(stat.getAvgExamScore()!=null) {
                row.createCell(5).setCellValue(stat.getAvgExamScore().doubleValue()>=4.5? "Высокий " : "Средний");
            }
        }

    }
    private static void createDetailSheet(Sheet detailsSheet, List <Statistics> statisticsList, Workbook workbook){
        Row headerRow = detailsSheet.createRow(0);
        headerRow.createCell(0).setCellValue("Профиль");
        headerRow.createCell(1).setCellValue("Детальная информация");

        int rowNum=1;
        for(Statistics stat: statisticsList) {
            Row row = detailsSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(stat.getMainProfile().getEnglishName());
            String details = String.format("Студентов: %d, средний балл: %s, Университеты: %s", stat.getStudentCount(), stat.getUniversityCount(),
            stat.getAvgExamScore()!=null? stat.getAvgExamScore().toString(): "Н/Д", stat.getUniversityName());
            row.createCell(1).setCellValue(details);
        }
    }

    private static void createChartSheet(Sheet chartSheet, List <Statistics> statisticsList, Workbook workbook) {

        Row headerRow = chartSheet.createRow(0);
        headerRow.createCell(0).setCellValue("Профиль");
        headerRow.createCell(1).setCellValue("Количество студентов");
        headerRow.createCell(2).setCellValue("Средний балл");

        int rowNum = 1;
        for (Statistics stat: statisticsList) {
            Row row= chartSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(stat.getMainProfile().getEnglishName());
            row.createCell(1).setCellValue(stat.getStudentCount());

            if(stat.getAvgExamScore()!=null) {
                row.createCell(2).setCellValue(stat.getAvgExamScore().doubleValue());
            }
        }
    }

}
