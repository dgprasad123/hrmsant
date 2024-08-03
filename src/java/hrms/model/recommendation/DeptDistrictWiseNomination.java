/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.recommendation;

import hrms.model.master.Department;

/**
 *
 * @author Manas
 */
public class DeptDistrictWiseNomination extends Department{
    private String distCode;
    private String distName;
    private int receivedatdepartment;
    private int pendingathodA;
    private int pendingathodB;
    private int pendingathodC;
    private int pendingathodD;
    private int pendingatcollectorateA;
    private int pendingatcollectorateB;
    private int pendingatcollectorateC;
    private int pendingatcollectorateD;

    public String getDistCode() {
        return distCode;
    }

    public void setDistCode(String distCode) {
        this.distCode = distCode;
    }

    public String getDistName() {
        return distName;
    }

    public void setDistName(String distName) {
        this.distName = distName;
    }

    public int getReceivedatdepartment() {
        return receivedatdepartment;
    }

    public void setReceivedatdepartment(int receivedatdepartment) {
        this.receivedatdepartment = receivedatdepartment;
    }

    public int getPendingathodA() {
        return pendingathodA;
    }

    public void setPendingathodA(int pendingathodA) {
        this.pendingathodA = pendingathodA;
    }

    public int getPendingathodB() {
        return pendingathodB;
    }

    public void setPendingathodB(int pendingathodB) {
        this.pendingathodB = pendingathodB;
    }

    public int getPendingathodC() {
        return pendingathodC;
    }

    public void setPendingathodC(int pendingathodC) {
        this.pendingathodC = pendingathodC;
    }

    public int getPendingathodD() {
        return pendingathodD;
    }

    public void setPendingathodD(int pendingathodD) {
        this.pendingathodD = pendingathodD;
    }

    public int getPendingatcollectorateA() {
        return pendingatcollectorateA;
    }

    public void setPendingatcollectorateA(int pendingatcollectorateA) {
        this.pendingatcollectorateA = pendingatcollectorateA;
    }

    public int getPendingatcollectorateB() {
        return pendingatcollectorateB;
    }

    public void setPendingatcollectorateB(int pendingatcollectorateB) {
        this.pendingatcollectorateB = pendingatcollectorateB;
    }

    public int getPendingatcollectorateC() {
        return pendingatcollectorateC;
    }

    public void setPendingatcollectorateC(int pendingatcollectorateC) {
        this.pendingatcollectorateC = pendingatcollectorateC;
    }

    public int getPendingatcollectorateD() {
        return pendingatcollectorateD;
    }

    public void setPendingatcollectorateD(int pendingatcollectorateD) {
        this.pendingatcollectorateD = pendingatcollectorateD;
    }

    
    
}
