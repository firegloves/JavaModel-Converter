package it.caneserpente.javamodelconverter;

public enum ESuperPower {

    FLY_DOWN("CAN FLY"), NO_POWER, STRENGTH("SUPER STRENGTH"), INVISIBILITY("BECAME TRANSPARENT");

    private String descrPower;

    ESuperPower() {

    }

    ESuperPower(String descrPower) {
        this.descrPower = descrPower;
    }

    public String getDescrPower() {
        return descrPower;
    }

    public void setDescrPower(String descrPower) {
        this.descrPower = descrPower;
    }
}
