/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.TransferProposal;

/**
 *
 * @author Tushar
 */
public class TransferProposalDetail {

    public int detailId;
    private int detailPostingId;
    public String newpost;
    public String newspc;
    public String isadditional;
    public String letterno;

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getDetailPostingId() {
        return detailPostingId;
    }

    public void setDetailPostingId(int detailPostingId) {
        this.detailPostingId = detailPostingId;
    }

    public String getNewpost() {
        return newpost;
    }

    public void setNewpost(String newpost) {
        this.newpost = newpost;
    }

    public String getNewspc() {
        return newspc;
    }

    public void setNewspc(String newspc) {
        this.newspc = newspc;
    }

    public String getIsadditional() {
        return isadditional;
    }

    public void setIsadditional(String isadditional) {
        this.isadditional = isadditional;
    }

    public String getLetterno() {
        return letterno;
    }

    public void setLetterno(String letterno) {
        this.letterno = letterno;
    }

}
