package map.domain;

public enum FriendshipStatus {
    DONE("DONE"),
    PENDING("PENDING"),
    REJECTED("REJECTED");

    FriendshipStatus(String done) {
    }

    public String toString() {
        return this.name();
    }
}
