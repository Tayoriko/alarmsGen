package devHMI;

public class AlarmWeintek extends AlarmMessage {
    private String category = "0: Category 0";
    private String priority = "Low";
    private String addressType = "Bit";
    private String plcNameRead = "CODESYS V3 (Ethernet)";
    private String dataTypeRead = "BOOL";
    private boolean systemTagRead = false;
    private boolean userDefinedTagRead = false;
    private String addressRead = "";
    private String indexRead = "null";
    private boolean enableNotification = false;
    private String condition = "bt: 1";
    private String triggerValue = "0";
    private String alarmMessage = "текст аварии";
    private boolean useLabelLibrary = false;
    private String labelName = "";
    private String font = "Droid Sans Fallback";
    private String color = "0:0:0";
    private int acknowledgeValue = 11;
    private boolean enableSound = true;

    public AlarmWeintek(String address, String message) {
        super(address, message);
        this.addressRead = address;
        this.alarmMessage = message; // Устанавливаем текст аварии
    }

    // Метод для получения строки значений переменных в строгом порядке для Excel
    public String toExcelRow() {
        return String.join("\t",
                category,
                priority,
                addressType,
                plcNameRead,
                dataTypeRead,
                Boolean.toString(systemTagRead),
                Boolean.toString(userDefinedTagRead),
                addressRead != null ? addressRead : "",
                indexRead,
                "", // Замените или обновите, если нужно
                Boolean.toString(enableNotification),
                "", // Placeholder для Set ON (Notification)
                "", // Placeholder для PLC Name (Notification)
                "", // Placeholder для Device Type (Notification)
                "", // Placeholder для System Tag (Notification)
                "", // Placeholder для User-defined Tag (Notification)
                "", // Placeholder для Address (Notification)
                "", // Placeholder для Index (Notification)
                condition,
                triggerValue,
                alarmMessage,
                Boolean.toString(useLabelLibrary),
                labelName,
                font,
                color,
                Integer.toString(acknowledgeValue),
                Boolean.toString(enableSound)
        );
    }

}