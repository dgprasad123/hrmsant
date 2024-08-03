package hrms.model.master;

public class University {

    private String boardCode = null;
    private String boardName = null;
    private String hidboardCode;
    private int boardserialNumber;

    
    public String getBoardCode() {
        return boardCode;
    }

    public void setBoardCode(String boardCode) {
        this.boardCode = boardCode;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getHidboardCode() {
        return hidboardCode;
    }

    public void setHidboardCode(String hidboardCode) {
        this.hidboardCode = hidboardCode;
    }

    public int getBoardserialNumber() {
        return boardserialNumber;
    }

    public void setBoardserialNumber(int boardserialNumber) {
        this.boardserialNumber = boardserialNumber;
    }
    

}
