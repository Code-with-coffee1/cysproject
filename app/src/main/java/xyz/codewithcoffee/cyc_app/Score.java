package xyz.codewithcoffee.cyc_app;

public class Score {
    String userid,username;
    int correct,wrong,total;

    Score(String userid,String username,int correct, int wrong, int total) {
        this.userid = userid;
        this.username = username;
        this.correct = correct;
        this.wrong = wrong;
        this.total = total;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
