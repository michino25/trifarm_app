package michittio.ueh.trifarm_app.data;

public class User {
    private String email;
    private String password;
    private String rule;

    public User() {}

    public User(String email, String password, String rule) {
        this.email = email;
        this.password = password;
        this.rule = rule;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}

