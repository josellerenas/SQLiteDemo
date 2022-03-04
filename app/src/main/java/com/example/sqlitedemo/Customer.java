package com.example.sqlitedemo;

public class Customer {
// Class that have all the characteristics of a customer

    // Defining the fields
    private int id;
    private String name;
    private int age;
    private boolean isActive;

    // Constructors
    public Customer(int id, String name, int age, boolean isActive) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.isActive = isActive;
    }

    public Customer() {
    }

    // To String Method. This is necessary for printing the contents of a class object
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", isActive=" + isActive +
                '}';
    }

    // Getters and Setters
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
