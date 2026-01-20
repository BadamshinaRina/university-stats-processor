package university.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import university.model.Statistics;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class XlsWriter {

    private static final Logger logger = LoggerUtil.getLogger(XlsWriter.class);

    private XlsWriter() {
        throw new IllegalStateException("Utility class - создание объектов класса запрещено");
    }

    private static CellStyle createHeaderStyle(Workbook workbook){
        logger.fine("Создание стиля для заголовка таблицы");
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
        logger.fine("Создание стиля для данных таблицы");
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
        logger.fine("Создание стиля для числовых данных");
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

        logger.info("Начало генерации Excel-файла "+ filePath);

        if(statisticsList==null||statisticsList.isEmpty()) {
            logger.warning("Передана пустая коллекция статистик");
        }

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
                logger.info("Файл успешно создан");
                            }
        } catch (Exception e) {
            LoggerUtil.logException(logger, "Ошибка при создании файла Excel", e);
                   }
    }

    private static void addSummaryRow(Sheet sheet, List<Statistics> statisticsList, int rowNum, CellStyle numberStyle, CellStyle dataStyle) {
        logger.fine("Добавление итоговой строки в таблицу");
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
            logger.fine("Средний балл не рассчитан, нет студентов");
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

       }


