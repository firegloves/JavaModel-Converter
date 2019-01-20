package it.caneserpente.javamodelconverter;

import java.util.List;

public class Dog {

    private int id;
    private String name;
    private Kennel kennel;
    private List<Dog> puppyList;
    private Collar collar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Kennel getKennel() {
        return kennel;
    }

    public void setKennel(Kennel kennel) {
        this.kennel = kennel;
    }

    public List<Dog> getPuppyList() {
        return puppyList;
    }

    public void setPuppyList(List<Dog> puppyList) {
        this.puppyList = puppyList;
    }

    public Collar getCollar() {
        return collar;
    }

    public void setCollar(Collar collar) {
        this.collar = collar;
    }
}
