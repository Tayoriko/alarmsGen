package devHMI;

import databases.DatabaseRegistry;
import databases.GData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Weintek {

    public Weintek(){

    }

    // Метод для экспорта базы данных в Excel файл
    public void exportToExcelWeintek(String filePath) {
        // Создаем новый workbook и лист для записи данных
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Alarms");

            // Создаем новую строку в начале файла
            Row systemRow = sheet.createRow(0);

            // Устанавливаем значения в ячейки
            systemRow.createCell(0).setCellValue("VERSION");
            systemRow.createCell(1).setCellValue("2");
            systemRow.createCell(2).setCellValue("HARDWARE_VERSION");
            systemRow.createCell(3).setCellValue("41");

            // Создаем заголовок
            Row headerRow = sheet.createRow(1);
            String[] headers = {
                    "Category", "Priority", "Address Type", "PLC Name (Read)", "Device Type (Read)",
                    "System Tag (Read)", "User-defined Tag (Read)", "Address (Read)", "Index (Read)",
                    "Data Format (Read)", "Enable Notification", "Set ON (Notification)",
                    "PLC Name (Notification)", "Device Type (Notification)", "System Tag (Notification)",
                    "User-defined Tag (Notification)", "Address (Notification)", "Index (Notification)",
                    "Condition", "Trigger Value", "Content", "Use Label Library", "Label Name",
                    "Font", "Color", "Acknowledge Value", "Enable Sound"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Получаем список всех AlarmMessage из базы данных
            List<AlarmMessage> alarms = DatabaseRegistry.getInstance(AlarmMessage.class).getRecords();

            // Добавляем каждое сообщение в виде строки в Excel, начиная со второй строки
            int rowIndex = 2;
            for (AlarmMessage alarm : alarms) {
                // Преобразуем AlarmMessage в AlarmWeintek
                AlarmWeintek weintekAlarm = convertToWeintek(alarm);

                // Записываем строку в Excel
                Row row = sheet.createRow(rowIndex++);
                String[] values = weintekAlarm.toExcelRow().split("\t");

                for (int i = 0; i < values.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(values[i]);
                }
            }

            // Записываем workbook в файл
            String fullFilePath =  filePath + "/" + GData.getProjectName() + "_Weintek_alarms.xlsx";
            try (FileOutputStream fileOut = new FileOutputStream(fullFilePath)) {
                workbook.write(fileOut);
                workbook.close();
            }
            System.out.println("Excel file created successfully at " + fullFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Преобразование AlarmMessage в AlarmWeintek
    private static AlarmWeintek convertToWeintek(AlarmMessage alarm) {
        // Преобразуем AlarmMessage в AlarmWeintek, используя его адрес и сообщение
        return new AlarmWeintek(alarm.getAddress(), alarm.getMessage());
    }


}
