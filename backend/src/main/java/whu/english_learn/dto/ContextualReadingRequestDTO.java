package whu.english_learn.dto;

import java.util.Map;

public class ContextualReadingRequestDTO {
    private Map<String, Object> inputs;
    private String response_mode;
    private String user;

    // Getters & Setters
    public Map<String, Object> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, Object> inputs) {
        this.inputs = inputs;
    }

    public String getResponse_mode() {
        return response_mode;
    }

    public void setResponse_mode(String response_mode) {
        this.response_mode = response_mode;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
