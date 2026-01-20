package university.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import university.model.Student;
import university.model.StudyProfile;
import university.model.University;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class XlsxReader {

    private static final Logger logger = LoggerUtil.getLogger(XlsxReader.class);

    private XlsxReader() {
        throw new IllegalStateException("Utility class");
    }

    public static List<Student> readStudents(String filePath) {
        logger.info("Начинаем чтение студентов");


        List<Student> students = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            logger.fine("Workbook создан успешно");

            Sheet sheet = workbook.getSheet("Студенты");

            if (sheet == null) {
               logger.warning("Лист 'Студенты' не найден в файле");
                workbook.close();
                return students;
            }

            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
                }

            int processedRows=0;
            int successfulRows =0;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                processedRows++;

                if (isEmptyRow(row)) {
                    LoggerUtil.fine(logger, "Пропущена пустая строка" + row.getRowNum());
                    continue;
                }

                try {
                    Student student = createStudentFromRow(row);
                    if(student!=null) {
                        students.add(student);
                        successfulRows++;
                    }

                } catch (Exception e) {
                   logger.warning(" Ошибка в строке " + row.getRowNum() + ": " + e.getMessage());
                }
            }

            workbook.close();
            logger.info("Чтение студентов завершено успешно");

        } catch (IOException e) {
            LoggerUtil.logException(logger, " Ошибка чтения файла: ", e);
        } catch (Exception e) {
            LoggerUtil.logException(logger, "Неожиданная ошибка: ",e);
               }
        return students;
    }

    public static List<University> readUniversities(String filePath) {
        logger.info("Начинаем чтение университетов");

        List<University> universities = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            logger.fine("Workbook создан успешно");
            Sheet sheet = workbook.getSheet("Университеты");

            if (sheet == null) {
                logger.warning("Лист Университеты не найден");
                workbook.close();
                return universities;
            }

            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
            rowIterator.next();
            }

            int processedRow = 0;
            int successfulRow = 0;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                processedRow++;

                if (isEmptyRow(row)) {
                    LoggerUtil.fine(logger, "Пропущена пустая строка");
                    continue;
                }

                try {
                    University university = createUniversityFromRow(row);
                    if (university != null) {
                        universities.add(university);
                        successfulRow++;
                    }
                } catch (Exception ex) {
                    logger.warning("Ошибка при обработке строки");
                }
            }
            workbook.close();
            logger.info("Чтение университетов завершено успешно");

    } catch (IOException e) {
            LoggerUtil.logException(logger, " Ошибка чтения файла: ", e);
        } catch (Exception e) {
            LoggerUtil.logException(logger, "Неожиданная ошибка: ",e);
        }
        return universities;
    }

    private static String getCellStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:

                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {

                    double value = cell.getNumericCellValue();
                    if (value == Math.floor(value)) {
                        return String.valueOf((int) value);
                    } else {
                        return String.valueOf(value);
                    }
                }

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    return cell.getCellFormula();
                }

            case BLANK:
                return "";

            default:
                return "";
        }
    }


    private static boolean isEmptyRow(Row row) {
        if (row == null) {
            return true;
        }

        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = getCellStringValue(cell);
                if (!value.trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private static Student createStudentFromRow(Row row) {
        try {
            Cell fullNameCell = row.createCell(0);
            Cell universityIdCell = row.createCell(1);
            Cell courseNumberCell = row.createCell(2);
            Cell avgScoreCell = row.createCell(3);

            String fullName = getCellStringValue(fullNameCell);
            String universityId = getCellStringValue(universityIdCell);

            if (fullName.isEmpty() || universityId.isEmpty()) {
                LoggerUtil.fine(logger, "Пропущена строка с пустыми обязательными полями");

            }
            int currentCourse = 1;
            if (courseNumberCell != null && courseNumberCell.getCellType() == CellType.NUMERIC) {
                currentCourse = (int) courseNumberCell.getNumericCellValue();
            }

            float avgExamScore = 0.0f;
            if (avgScoreCell != null && avgScoreCell.getCellType() == CellType.NUMERIC) {
                avgExamScore = (float) avgScoreCell.getNumericCellValue();
            }

            Student student = new Student().setFullName(fullName)
                    .setUniversityId(universityId)
                    .setCurrentCourseNumber(currentCourse)
                    .setAvgExamScore(avgExamScore);

            LoggerUtil.fine(logger,"Создан студент "+ student.getFullName());

            return student;
        } catch (Exception e) {
           logger.warning("Ошибка создания студента");
           return null;
        }
    }
    private static University createUniversityFromRow(Row row) {
        try{
            Cell idCell = row.getCell(0);
            Cell fullNameCell = row.getCell(1);
            Cell shortNameCell = row.getCell(2);
            Cell yearCell = row.getCell(3);
            Cell profileCell = row.getCell(4);

            String id = getCellStringValue(idCell);
            String fullName = getCellStringValue(fullNameCell);
            String shortName = getCellStringValue(shortNameCell);

            if (id.isEmpty() || fullName.isEmpty()) {
               LoggerUtil.fine(logger, "Пропущена строка с пустыми обязательными полями");
            }

            int yearOfFoundation = 0;
            if (yearCell != null && yearCell.getCellType() == CellType.NUMERIC) {
                yearOfFoundation = (int) yearCell.getNumericCellValue();
            }
            String profileText = getCellStringValue(profileCell);
            StudyProfile profile = StudyProfile.fromString(profileText);

            University university = new University()
                    .setId(id)
                    .setFullName(fullName)
                    .setShortName(shortName)
                    .setYearsOfFoundation(yearOfFoundation)
                    .setMainProfile(profile);

            LoggerUtil.fine(logger, "Создан университет " + university.getShortName());
            return university;
        } catch (Exception e) {
           logger.warning("Ошибка создания университета");
           return null;
        }
    }

}