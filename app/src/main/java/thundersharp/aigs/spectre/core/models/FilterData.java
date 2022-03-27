package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;
import java.util.List;

public class FilterData implements Serializable{
    public FilterData() {}

    public List<String> tags,difficulty,language;

    public FilterData(List<String> tags, List<String> difficulty, List<String> language) {
        this.tags = tags;
        this.difficulty = difficulty;
        this.language = language;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setDifficulty(List<String> difficulty) {
        this.difficulty = difficulty;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getDifficulty() {
        return difficulty;
    }

    public List<String> getLanguage() {
        return language;
    }
}
