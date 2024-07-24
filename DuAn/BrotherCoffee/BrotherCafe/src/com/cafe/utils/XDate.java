/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author NGHIA
 */
public class XDate {
    static SimpleDateFormat formater = new SimpleDateFormat();
    public static Date toDate(String date,String parent){
        try {
            formater.applyPattern(parent);
            return formater.parse(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String toString(Date date,String pattern){
        formater.applyPattern(pattern);
        return formater.format(date);
    }
    public static Date addDays(Date date, long days){
        date.setTime(date.getTime()+days*24*60*60*1000);
        return date;
    }
    
    public static boolean checkDateValid(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false); // Không cho phép chấp nhận các giá trị ngày tháng không hợp lệ
        try {
            Date date = sdf.parse(dateStr);
            if (date != null) {
                return true; // Đã phân tích thành công và là định dạng hợp lệ
            }
        } catch (ParseException e) {
            // Ngày tháng không phù hợp với định dạng
        }
        return false; // Định dạng không hợp lệ
    }
}
