package lab1.chem.demo.entities;

public class AsyncAnswer {
    private String answer;
    private Integer idAnswer;

    public AsyncAnswer() {}
    public AsyncAnswer(String answer, Integer idAnswer) {
        this.answer = answer;
        this.idAnswer = idAnswer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getIdAnswer() {
        return idAnswer;
    }

    public void setIdAnswer(Integer idAnswer) {
        this.idAnswer = idAnswer;
    }
}
