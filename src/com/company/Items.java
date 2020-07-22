package com.company;

public class Items {
    int id;
    String name;
    int price;
    int qty;
    public Items() {
    }

    public Items(int id, String name, int price,int qty) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.qty=qty;
    }

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public int setQty(int qty) {
        this.qty = qty;
        return qty;
    }

    @Override
    public String toString() {
        return "Items{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", qty=" + qty +
                '}';
    }
}

