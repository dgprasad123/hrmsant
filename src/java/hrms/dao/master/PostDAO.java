/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.model.master.GPost;
import java.util.ArrayList;

/**
 *
 * @author Manas Jena
 */
public interface PostDAO {

    public ArrayList getPostList(String departmentCode);

    public ArrayList getOfficeWisePostList(String offCode);

    public ArrayList getCadrewisePostList(String departmentCode, String cadreCode);

    public ArrayList getPostList(String departmentCode, String offcode);

    public ArrayList getPostListForAERMapping(String departmentCode);

    public String getPostName(String postcode);

    public String savePost(GPost post);

    public ArrayList getGenericPostList(String departmentCode);

    public GPost getPostDetail(String postcode);

    public ArrayList getCadreList(String departmentCode, String postCode);

    public void saveCadrePost(String postcode, String cadrecode, String mapAction);

    public ArrayList getModifiedPostList(String departmentCode);

    public void mergeDuplicatePost(GPost post);

    public ArrayList viewDuplicatePostList(String departmentCode);

    public void setPostAsAuthority(String postcode, String status);

    public ArrayList getForeignBodyPostList(String departmentCode);

    public void checkDuplicatePost(GPost post);
    
    public GPost getPostDetailsforMerging(String dupPost,String finPost);
    
    public ArrayList getTotalSPC(String dupPost,String finPost);
    
    public void getUpdateAndReplaceSPN(GPost post,String finalPostName, String postList);
    
    public ArrayList viewPreviousMergedPost(String finPost);
    
}
