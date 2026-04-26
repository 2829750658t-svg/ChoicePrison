package logic;

public class Question {

    public int id;
    public String content;
    public String opt1;
    public String opt2;
    public String opt_mid;
    public String dims1;
    public String dims2;
    public String dim_mid;


    public Question(int id, String content, String opt1, String opt2, String opt_mid,
                    String dims1, String dims2, String dim_mid) {
        this.id = id;
        this.content = content;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.opt_mid = opt_mid;
        this.dims1 = dims1;
        this.dims2 = dims2;
        this.dim_mid = dim_mid;
    }
}