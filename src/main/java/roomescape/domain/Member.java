package roomescape.domain;

public class Member {

    private final Long id;
    private final String loginId;
    private final String password;
    private final String name;
    private final String role;

    private Member(Long id, String loginId, String password, String name, String role) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public static Member from(Long id, String loginId, String password, String name, String role) {
        return new Member(id, loginId, password, name, role);
    }

    public Long getId() {
        return id;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}
