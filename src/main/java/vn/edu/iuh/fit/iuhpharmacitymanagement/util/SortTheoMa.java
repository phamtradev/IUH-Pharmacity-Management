/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.function.Function;

/**
 *
 * @author dohoainho
 */
public class SortTheoMa {

    public static <T> Comparator<T> sortTheoMa(Function<T, String> extractor) {
        return (a, b) -> {
            String maA = extractor.apply(a).substring(2); // ddMMyyyyNNNN
            String maB = extractor.apply(b).substring(2);

            int ngayA = Integer.parseInt(maA.substring(0, 2));
            int thangA = Integer.parseInt(maA.substring(2, 4));
            int namA = Integer.parseInt(maA.substring(4, 8));
            int soCuoiA = Integer.parseInt(maA.substring(8));

            int ngayB = Integer.parseInt(maB.substring(0, 2));
            int thangB = Integer.parseInt(maB.substring(2, 4));
            int namB = Integer.parseInt(maB.substring(4, 8));
            int soCuoiB = Integer.parseInt(maB.substring(8));

            int cmp = Integer.compare(namB, namA);
            if (cmp != 0) {
                return cmp;
            }

            cmp = Integer.compare(thangB, thangA);
            if (cmp != 0) {
                return cmp;
            }

            cmp = Integer.compare(ngayB, ngayA);
            if (cmp != 0) {
                return cmp;
            }

            return Integer.compare(soCuoiB, soCuoiA);
        };
    }

    public static <T> Comparator<T> sortTheoMaTienTo3(Function<T, String> extractor) {
        return (a, b) -> {
            String maA = extractor.apply(a).substring(3); // ddMMyyyyNNNN
            String maB = extractor.apply(b).substring(3);

            int ngayA = Integer.parseInt(maA.substring(0, 2));
            int thangA = Integer.parseInt(maA.substring(2, 4));
            int namA = Integer.parseInt(maA.substring(4, 8));
            int soCuoiA = Integer.parseInt(maA.substring(8));

            int ngayB = Integer.parseInt(maB.substring(0, 2));
            int thangB = Integer.parseInt(maB.substring(2, 4));
            int namB = Integer.parseInt(maB.substring(4, 8));
            int soCuoiB = Integer.parseInt(maB.substring(8));

            int cmp = Integer.compare(namB, namA);
            if (cmp != 0) {
                return cmp;
            }

            cmp = Integer.compare(thangB, thangA);
            if (cmp != 0) {
                return cmp;
            }

            cmp = Integer.compare(ngayB, ngayA);
            if (cmp != 0) {
                return cmp;
            }

            return Integer.compare(soCuoiB, soCuoiA);
        };
    }

}
