package nlu.fit.studyappr.model.grammar;

import java.io.Serializable;
import java.util.List;

public class ExerciseResult  implements Serializable {
    int correct;
    int total;
    List<InCorrectDetail> inCorrectDetailList;

    public ExerciseResult(int correct, int total, List<InCorrectDetail> inCorrectDetailList) {
        this.correct = correct;
        this.total = total;
        this.inCorrectDetailList = inCorrectDetailList;
    }

    public ExerciseResult() {
    }

    @Override
    public String toString() {
        return "ExerciseResult{" +
                "correct=" + correct +
                ", total=" + total +
                ", inCorrectDetailList=" + inCorrectDetailList +
                '}';
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<InCorrectDetail> getInCorrectDetailList() {
        return inCorrectDetailList;
    }

    public void setInCorrectDetailList(List<InCorrectDetail> inCorrectDetailList) {
        this.inCorrectDetailList = inCorrectDetailList;
    }
}
