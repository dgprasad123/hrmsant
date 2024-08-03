/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.common;

/**
 *
 * @author Manas
 */
public class ParGrade {
    public static String getGradingName(int gradeId) {
        String gradingName = "";
        switch (gradeId) {
            case 1:
                gradingName = "Below Average";
                break;
            case 2:
                gradingName = "Average";
                break;
            case 3:
                gradingName = "Good";
                break;
            case 4:
                gradingName = "Very Good";
                break;
            case 5:
                gradingName = "Outstanding";
                break;
            default:
        }
        return gradingName;
    }
}
