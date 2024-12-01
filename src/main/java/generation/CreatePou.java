package generation;

import devicesDB.*;
import enums.FilePath;
import enums.eDevices;
import enums.ePLC;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

import static enums.eDevices.*;

public class CreatePou {
    private String device = "";
    private String pouName = "";
    private String drvName = "";
    private String cntName = "";
    private String folder = "Local PRG";
    private ePLC protocol = ePLC.EMPTY;
    private File selectedFile = new File("");

    public CreatePou(File selectedFile, ePLC protocol) throws IOException {
        this.selectedFile = selectedFile;
        this.protocol = protocol;
    }

    public StringBuilder createOne(eDevices devType) throws IOException {
        //Загрузка данных
        StringBuilder data = getDeviceFromDB(devType);
        StringBuilder pou = new StringBuilder();
        loadTypeData(devType);

        // Настройка Velocity
        Properties properties = new Properties();
        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(properties);

        VelocityContext context = new VelocityContext();
        context.put("pouName",      pouName);
        context.put("drvName",      drvName);
        context.put("device",       device);
        context.put("cntName",      cntName);
        context.put("loader",       devType.getLoader());
        context.put("device",       devType.getValue());
        context.put("data",         data);

        pou.append(generateText(context, FilePath.FILEPATH_XML_POU));
        return pou;
    }

    public StringBuilder createData(eDevices devType) {
        //Загрузка данных
        StringBuilder data = new StringBuilder();
        // Настройка Velocity
        Properties properties = new Properties();
        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(properties);
        VelocityContext context = new VelocityContext();
        context.put("folder",      folder);
        context.put("name",        devType.getPou());
        data.append(generateText(context, FilePath.FILEPATH_XML_DATA));
        return data;
    }

    private StringBuilder getDeviceFromDB(eDevices devType) throws IOException {
        StringBuilder device = new StringBuilder();
        switch (devType){
            case MOTOR ->{
                MotorDatabase.getInstance().clear();
                DeviceCreator deviceCreator = new DeviceCreator(selectedFile, protocol);
                deviceCreator.reviewDevice(protocol, MOTOR);
                device.append(MotorDatabase.getInstance().getAllRecords());
            }
            case VALVE ->{
                ValveDatabase.getInstance().clear();
                DeviceCreator deviceCreator = new DeviceCreator(selectedFile, protocol);
                deviceCreator.reviewDevice(protocol, VALVE);
                device.append(ValveDatabase.getInstance().getAllRecords());
            }
            case AI ->{
                AnalogInputDatabase.getInstance().clear();
                DeviceCreator deviceCreator = new DeviceCreator(selectedFile, protocol);
                deviceCreator.reviewDevice(protocol, AI);
                device.append(AnalogInputDatabase.getInstance().getAllRecords());
            }
            case AO ->{
                AnalogOutputDatabase.getInstance().clear();
                DeviceCreator deviceCreator = new DeviceCreator(selectedFile, protocol);
                deviceCreator.reviewDevice(protocol, AO);
                device.append(AnalogOutputDatabase.getInstance().getAllRecords());
            }
            case DI ->{
                DiscreteInputDatabase.getInstance().clear();
                DeviceCreator deviceCreator = new DeviceCreator(selectedFile, protocol);
                deviceCreator.reviewDevice(protocol, DI);
                device.append(DiscreteInputDatabase.getInstance().getAllRecords());
            }
            case DO ->{
                DiscreteOutputDatabase.getInstance().clear();
                DeviceCreator deviceCreator = new DeviceCreator(selectedFile, protocol);
                deviceCreator.reviewDevice(protocol, DO);
                device.append(DiscreteOutputDatabase.getInstance().getAllRecords());
            }
            case PID ->{
                PidDatabase.getInstance().clear();
                DeviceCreator deviceCreator = new DeviceCreator(selectedFile, protocol);
                deviceCreator.reviewDevice(protocol, PID);
                //PidDatabase.getInstance().printAllRecords();
                device.append(PidDatabase.getInstance().getAllRecords());
            }
            case FLOW ->{
                FlowMetersDatabase.getInstance().clear();
                DeviceCreator deviceCreator = new DeviceCreator(selectedFile, protocol);
                deviceCreator.reviewDevice(protocol, FLOW);
                //FlowMetersDatabase.getInstance().printAllRecords();
                device.append("mt.sys.upTimeMs := TIME_TO_UDINT(mt.sys.UpTime);\n\n");
                device.append(FlowMetersDatabase.getInstance().getAllRecords());
            }
        }
        return device;
    }

    private void loadTypeData (eDevices devType) {
        device = devType.getName();
        pouName = devType.getPou();
        drvName = devType.getDrv();
        cntName = devType.getCnt();
    }

    // Метод для генерации текста на основе контекста и шаблона
    private static String generateText(VelocityContext context, String templatePath) {
        StringWriter writer = new StringWriter();
        Velocity.mergeTemplate(templatePath, "UTF-8", context, writer);
        return writer.toString(); // Возвращаем сгенерированный текст для данного контекста
    }
}
