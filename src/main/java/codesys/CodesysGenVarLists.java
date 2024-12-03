package codesys;

import databases.DatabaseRegistry;
import databases.GData;
import dev.*;
import enums.eActions;
import enums.eDevType;

public class CodesysGenVarLists extends CodesysGenAbstract{

    CodesysGenVarTags codesysVarTags = new CodesysGenVarTags();

    public CodesysGenVarLists() {
        super();
    }

    public StringBuilder genVarLists() {
        StringBuilder iol;
        StringBuilder nvl;
        StringBuilder dataIol = null;
        StringBuilder dataNvl = null;
        StringBuilder data = new StringBuilder();
        if (GData.getActions().contains(eActions.IOL)) {
            StringBuilder listIol = generateIol();
            StringBuilder addDataIol = generateAddData();
            listIol.append(addDataIol);
            iol = generateTagGlobalVars("IOL", listIol);
            dataIol = generateTagData("globalvars", "implementation", iol);
        }
        if (GData.getActions().contains(eActions.MBS)) {
            StringBuilder listNvl = generateNvl();
            StringBuilder addDataNvl = generateAddData();
            listNvl.append(addDataNvl);
            nvl = generateTagGlobalVars("NVL", listNvl);
            dataNvl = generateTagData("globalvars", "implementation", nvl);
        }
        data.append(dataIol);
        data.append(dataNvl);
        return data;
    }


    private StringBuilder generateAddData () {
        StringBuilder attribute = generateTagAttribute("qualified_only", "");
        StringBuilder attributes = generateTagAttributes(attribute);
        StringBuilder dataAttributes = generateTagData("attributes", "implementation", attributes);
        StringBuilder objectId = generateTagObjectId("");
        StringBuilder dataObjectId = generateTagData("objectid", "discard", objectId);
        dataAttributes.append(dataObjectId);
        return generateTagAddData(dataAttributes);
    }

    private StringBuilder generateIol () {
        StringBuilder globalVar = new StringBuilder();
        GData.getDevices().forEach(device -> globalVar.append(genOneIolDevVar(device)));
        return globalVar;
    }

    private StringBuilder generateNvl () {
        StringBuilder globalVar = new StringBuilder();
        GData.getDevices().forEach(device -> globalVar.append(genOneNvlDevVar(device)));
        return globalVar;
    }

    private StringBuilder generateTagGlobalVars (String varList, StringBuilder content) {
        StringBuilder tag = new StringBuilder();
        tag.append("<globalVars name=\"" + varList +"\">\n");
        tag.append(content);
        tag.append("</globalVars>\n");
        tag = addPrefix(tag, GData.tab);
        return tag;
    }

    private StringBuilder genOneIolDevVar (eDevType devType) {
        StringBuilder devVar = new StringBuilder();
        switch (devType) {
            case DI -> {
                for (DevDI devDI : DatabaseRegistry.getInstance(DevDI.class).getRecords()) {
                    devVar.append(codesysVarTags.genIol(devDI)).append(codesysVarTags.genId(devDI));
                }
            }
            case DO -> {
                for (DevDO devDO : DatabaseRegistry.getInstance(DevDO.class).getRecords()) {
                    devVar.append(codesysVarTags.genIol(devDO)).append(codesysVarTags.genId(devDO));
                }
            }
            case AI -> {
                for (DevAI devAI : DatabaseRegistry.getInstance(DevAI.class).getRecords()) {
                    devVar.append(codesysVarTags.genIol(devAI)).append(codesysVarTags.genId(devAI));
                }
            }
            case AO -> {
                for (DevAO devAO : DatabaseRegistry.getInstance(DevAO.class).getRecords()) {
                    devVar.append(codesysVarTags.genIol(devAO)).append(codesysVarTags.genId(devAO));
                }
            }
            case MOTOR -> {
                for (DevMotor devMotor : DatabaseRegistry.getInstance(DevMotor.class).getRecords()) {
                    devVar.append(codesysVarTags.genIol(devMotor)).append(codesysVarTags.genId(devMotor));
                }
            }
            case VALVE -> {
                for (DevValve devValve : DatabaseRegistry.getInstance(DevValve.class).getRecords()) {
                    devVar.append(codesysVarTags.genIol(devValve)).append(codesysVarTags.genId(devValve));
                }
            }
        }
        devVar = addPrefix(devVar, GData.tab);
        return devVar;
    }

    private StringBuilder genOneNvlDevVar (eDevType devType) {
        StringBuilder devVar = new StringBuilder();
        switch (devType) {
            case DI -> {
                for (DevDI devDI : DatabaseRegistry.getInstance(DevDI.class).getRecords()) {
                    devVar.append(codesysVarTags.genNvl(devDI));
                }
            }
            case DO -> {
                for (DevDO devDO : DatabaseRegistry.getInstance(DevDO.class).getRecords()) {
                    devVar.append(codesysVarTags.genNvl(devDO));
                }
            }
            case AI -> {
                for (DevAI devAI : DatabaseRegistry.getInstance(DevAI.class).getRecords()) {
                    devVar.append(codesysVarTags.genNvl(devAI));
                }
            }
            case AO -> {
                for (DevAO devAO : DatabaseRegistry.getInstance(DevAO.class).getRecords()) {
                    devVar.append(codesysVarTags.genNvl(devAO));
                }
            }
            case MOTOR -> {
                for (DevMotor devMotor : DatabaseRegistry.getInstance(DevMotor.class).getRecords()) {
                    devVar.append(codesysVarTags.genNvl(devMotor));
                }
            }
            case VALVE -> {
                for (DevValve devValve : DatabaseRegistry.getInstance(DevValve.class).getRecords()) {
                    devVar.append(codesysVarTags.genNvl(devValve));
                }
            }
        }
        devVar = addPrefix(devVar, GData.tab);
        return devVar;
    }


}
