package Reqres.colors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ColorsData {
    public String name;
    public Integer year;
    public String color;
    public String pantone_value;

    public ColorsData() {}
    public ColorsData(String name, Integer year, String color, String pantone_value) {
        this.name = name;
        this.year = year;
        this.color = color;
        this.pantone_value = pantone_value;
    }

    public String getName() {
        return name;
    }

    public Integer getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public String getPantone_value() {
        return pantone_value;
    }
}
