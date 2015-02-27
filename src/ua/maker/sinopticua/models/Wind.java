package ua.maker.sinopticua.models;

import java.io.Serializable;

/**
 * Created by Daniil on 02.02.2015.
 */
public class Wind implements Serializable {

    public String imgDirection;
    public String classNameImg;
    public String speed;

    public Wind(String img, String speed, String classNameImg) {
        this.imgDirection = img;
        this.speed = speed;
        this.classNameImg = classNameImg;
    }
}
