package it.caneserpente.javamodelconverter;

import java.math.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PojoTest {

    private int id;
    private String name;
    private BigDecimal money;
    private Date dataCom;
    private String[] friends;
    private List<Integer> parents;
    private List rawList;
    private List<Date> dateList;
    private Map rawMap;
    private Map<String, Long> paramMap;
    private Map<Long, String> paramReverseMap;

    private char letter;
    private Character letter2;
    private double amount;
    private Double amount2;
    private double[] amountArray;
    private BigInteger sum;
    private byte b;
    private Byte bigByte;
    private short little;
    private Short little2;
    private List<Short> littleList;

    private ESuperHeroe superHero;
    private ESuperPower superPow;

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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Date getDataCom() {
        return dataCom;
    }

    public void setDataCom(Date dataCom) {
        this.dataCom = dataCom;
    }

    public String[] getFriends() {
        return friends;
    }

    public void setFriends(String[] friends) {
        this.friends = friends;
    }

    public List<Integer> getParents() {
        return parents;
    }

    public void setParents(List<Integer> parents) {
        this.parents = parents;
    }

    public List getRawList() {
        return rawList;
    }

    public void setRawList(List rawList) {
        this.rawList = rawList;
    }
}