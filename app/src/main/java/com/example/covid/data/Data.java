package com.example.covid.data;

abstract public class Data {

    public static final int LOW = 5;
    public static final int MED = 10;
    public static final int HIGH = 15;

    abstract int calculateScore();

    abstract int getId();

    abstract int getMin();

    abstract int getMax();

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        if (getId() != ((Data)obj).getId())
            return false;
        return true;
    }

}
