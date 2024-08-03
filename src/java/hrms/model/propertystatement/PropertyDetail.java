/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.propertystatement;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Manas Jena
 */
public class PropertyDetail {
    private int slno;
    private BigDecimal yearlyPropId;
    private BigDecimal propertyDtlsId;
    private String propertyType;
    private Integer propertyTypeId;
    private Integer propertyId;
    private String descofothitem;
    private String propertyName;
    private String propertyLocation;
    private Integer propertyOwner;
    private String propertyOwnerDtl;
    private String otherPropertyOwner;
    private BigDecimal propertyArea;
    private String areaunit;
    private BigDecimal propertyValue;
    private BigDecimal loan;
    private String interest;
    private String propertyNature;
    private Date dateOfAcq;
    private String remark;
    private String manner;
    
    public int getSlno() {
        return slno;
    }

    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public BigDecimal getYearlyPropId() {
        return yearlyPropId;
    }

    public void setYearlyPropId(BigDecimal yearlyPropId) {
        this.yearlyPropId = yearlyPropId;
    }
    
    public String getPropertyLocation() {
        return propertyLocation;
    }

    public void setPropertyLocation(String propertyLocation) {
        this.propertyLocation = propertyLocation;
    }

    public BigDecimal getLoan() {
        return loan;
    }

    public void setLoan(BigDecimal loan) {
        this.loan = loan;
    }    

    public String getPropertyOwnerDtl() {
        return propertyOwnerDtl;
    }

    public void setPropertyOwnerDtl(String propertyOwnerDtl) {
        this.propertyOwnerDtl = propertyOwnerDtl;
    }

    public String getOtherPropertyOwner() {
        return otherPropertyOwner;
    }

    public void setOtherPropertyOwner(String otherPropertyOwner) {
        this.otherPropertyOwner = otherPropertyOwner;
    }
        
    public BigDecimal getPropertyDtlsId() {
        return propertyDtlsId;
    }

    public void setPropertyDtlsId(BigDecimal propertyDtlsId) {
        this.propertyDtlsId = propertyDtlsId;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public Integer getPropertyTypeId() {
        return propertyTypeId;
    }

    public void setPropertyTypeId(Integer propertyTypeId) {
        this.propertyTypeId = propertyTypeId;
    }

    public Integer getPropertyOwner() {
        return propertyOwner;
    }

    public void setPropertyOwner(Integer propertyOwner) {
        this.propertyOwner = propertyOwner;
    }    

    public String getDescofothitem() {
        return descofothitem;
    }

    public void setDescofothitem(String descofothitem) {
        this.descofothitem = descofothitem;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }   

    public BigDecimal getPropertyArea() {
        return propertyArea;
    }

    public void setPropertyArea(BigDecimal propertyArea) {
        this.propertyArea = propertyArea;
    }

    public String getAreaunit() {
        return areaunit;
    }

    public void setAreaunit(String areaunit) {
        this.areaunit = areaunit;
    }

    public BigDecimal getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(BigDecimal propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getPropertyNature() {
        return propertyNature;
    }

    public void setPropertyNature(String propertyNature) {
        this.propertyNature = propertyNature;
    }

    public Date getDateOfAcq() {        
        return dateOfAcq;
    }

    public void setDateOfAcq(Date dateOfAcq) {        
        this.dateOfAcq = dateOfAcq;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getManner() {
        return manner;
    }

    public void setManner(String manner) {
        this.manner = manner;
    }
    
    
}
