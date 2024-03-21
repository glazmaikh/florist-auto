package entity.register;

import lombok.Data;

public @Data class UserWrapper {
    private User user;

    public UserWrapper(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
