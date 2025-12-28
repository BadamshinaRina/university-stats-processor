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

public class XlsxReader {

    private XlsxReader() {
        throw new IllegalStateException("Utility class");
    }

    public static List<Student> readStudents(String filePath) {
        System.out.println("\n=== НАЧАЛО readStudents() ===");
//        System.out.println("Файл: " + filePath);

        List<Student> students = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

//            System.out.println("1. Workbook создан");

            Sheet sheet = workbook.getSheet("Студенты");
//            System.out.println("2. Лист 'Студенты' найден? " + (sheet != null));

//            if (sheet == null) {
//               System.out.println("   Доступные листы:");
//                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
//                    System.out.println("   - " + workbook.getSheetName(i));
//                }
//                workbook.close();
//                return students;
//            }

//            System.out.println("3. Всего строк в листе: " + sheet.getPhysicalNumberOfRows());

            Iterator<Row> rowIterator = sheet.iterator();

                        if (rowIterator.hasNext()) {
                Row headerRow = rowIterator.next();
//                System.out.println("4. Заголовок (строка 0):");
                for (Cell cell : headerRow) {
//                    System.out.println("   Колонка " + cell.getColumnIndex() +
//                            ": '" + getCellStringValue(cell) + "'");
                }
            }

//            System.out.println("5. Начинаем чтение данных...");
            int rowCount = 0;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                rowCount++;

//                System.out.println("   Строка " + row.getRowNum() + ":");

                if (isEmptyRow(row)) {
//                    System.out.println("     [ПУСТАЯ СТРОКА - пропускаем]");
                    continue;
                }

                try {

                    Cell fullNameCell = row.getCell(0);
                    Cell universityIdCell = row.getCell(1);
                    Cell courseCell = row.getCell(2);
                    Cell avgScoreCell = row.getCell(3);
//
//                    System.out.println("     Колонка 0 (ФИО): " + getCellStringValue(fullNameCell));
//                    System.out.println("     Колонка 1 (ID универа): " + getCellStringValue(universityIdCell));
//                    System.out.println("     Колонка 2 (Курс): " + getCellStringValue(courseCell));
//                    System.out.println("     Колонка 3 (Балл): " + getCellStringValue(avgScoreCell));


                    String fullName = getCellStringValue(fullNameCell);
                    String universityId = getCellStringValue(universityIdCell);

                    int currentCourse = 1;
                    if (courseCell != null && courseCell.getCellType() == CellType.NUMERIC) {
                        currentCourse = (int) courseCell.getNumericCellValue();
                    }

                    float avgExamScore = 0.0f;
                    if (avgScoreCell != null && avgScoreCell.getCellType() == CellType.NUMERIC) {
                        avgExamScore = (float) avgScoreCell.getNumericCellValue();
                    }


                    if (fullName.isEmpty() || universityId.isEmpty()) {
//                        System.out.println("     [ПРОПУСКАЕМ - нет обязательных данных]");
                        continue;
                    }

                    Student student = new Student()
                            .setFullName(fullName)
                            .setUniversityId(universityId)
                            .setCurrentCourseNumber(currentCourse)
                            .setAvgExamScore(avgExamScore);

                    students.add(student);
//                    System.out.println(" Студент создан: " + student.getFullName());

                } catch (Exception e) {
                    System.err.println(" Ошибка в строке " + row.getRowNum() + ": " + e.getMessage());
                }
            }

            workbook.close();
//            System.out.println("6. Чтение завершено. Прочитано строк: " + rowCount);
//            System.out.println("Прочитано студентов: " + students.size());

        } catch (IOException e) {
            System.err.println(" Ошибка чтения файла: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Неожиданная ошибка: " + e.getMessage());
            e.printStackTrace();
        }

        return students;
    }

    public static List<University> readUniversities(String filePath) {
        System.out.println("\n=== НАЧАЛО readUniversities() ===");
//        System.out.println("Файл: " + filePath);

        List<University> universities = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

//            System.out.println("1. Workbook создан");

            Sheet sheet = workbook.getSheet("Университеты");
//            System.out.println("2. Лист 'Университеты' найден? " + (sheet != null));

//            if (sheet == null) {
//                System.out.println("   Доступные листы:");
//                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
//                    System.out.println("   - " + workbook.getSheetName(i));
//                }
//                workbook.close();
//                return universities;
//            }

//            System.out.println("3. Всего строк в листе: " + sheet.getPhysicalNumberOfRows());

            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                Row headerRow = rowIterator.next();
//                System.out.println("4. Заголовок (строка 0):");
                for (Cell cell : headerRow) {
//                    System.out.println("   Колонка " + cell.getColumnIndex() +
//                            ": '" + getCellStringValue(cell) + "'");
                }
            }

//            System.out.println("5. Начинаем чтение данных...");
            int rowCount = 0;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                rowCount++;

//                System.out.println("   Строка " + row.getRowNum() + ":");

                if (isEmptyRow(row)) {
                    System.out.println("     [ПУСТАЯ СТРОКА - пропускаем]");
                    continue;
                }

                try {
                    // Читаем все 5 колонок для университетов
                    Cell idCell = row.getCell(0);
                    Cell fullNameCell = row.getCell(1);
                    Cell shortNameCell = row.getCell(2);
                    Cell yearCell = row.getCell(3);
                    Cell profileCell = row.getCell(4);

//                    System.out.println("     Колонка 0 (ID): " + getCellStringValue(idCell));
//                    System.out.println("     Колонка 1 (Полное название): " + getCellStringValue(fullNameCell));
//                    System.out.println("     Колонка 2 (Короткое название): " + getCellStringValue(shortNameCell));
//                    System.out.println("     Колонка 3 (Год): " + getCellStringValue(yearCell));
//                    System.out.println("     Колонка 4 (Профиль): " + getCellStringValue(profileCell));
//

                    String id = getCellStringValue(idCell);
                    String fullName = getCellStringValue(fullNameCell);
                    String shortName = getCellStringValue(shortNameCell);

                    int yearOfFoundation = 0;
                    if (yearCell != null && yearCell.getCellType() == CellType.NUMERIC) {
                        yearOfFoundation = (int) yearCell.getNumericCellValue();
                    }

                    String profileText = getCellStringValue(profileCell);
                    StudyProfile profile = StudyProfile.fromString(profileText);


                    if (id.isEmpty() || fullName.isEmpty()) {
                        System.out.println("     [ПРОПУСКАЕМ - нет обязательных данных]");
                        continue;
                    }


                    University university = new University()
                            .setId(id)
                            .setFullName(fullName)
                            .setShortName(shortName)
                            .setYearsOfFoundation(yearOfFoundation)
                            .setMainProfile(profile);

                    universities.add(university);
//                    System.out.println("Университет создан: " + university.getShortName());

                } catch (Exception e) {
                    System.err.println("Ошибка в строке " + row.getRowNum() + ": " + e.getMessage());
                }
            }

            workbook.close();
//            System.out.println("6. Чтение завершено. Прочитано строк: " + rowCount);
//            System.out.println(" Прочитано университетов: " + universities.size());

        } catch (IOException e) {
            System.err.println(" Ошибка чтения файла: " + e.getMessage());
        } catch (Exception e) {
            System.err.println(" Неожиданная ошибка: " + e.getMessage());
            e.printStackTrace();
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

}