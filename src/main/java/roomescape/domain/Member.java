package roomescape.domain;

public class Member {

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String MANAGER_ROLE = "MANAGER";

    private final Long id;
    private final String loginId;
    private final String password;
    private final String name;
    private final String role;
    private final Long storeId;

    private Member(Long id, String loginId, String password, String name, String role, Long storeId) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.role = role;
        this.storeId = storeId;
    }

    public static Member from(Long id, String loginId, String password, String name, String role, Long storeId) {
        return new Member(id, loginId, password, name, role, storeId);
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

    public Long getStoreId() {
        return storeId;
    }

    public boolean isAdmin() {
        return ADMIN_ROLE.equals(role);
    }

    public boolean isManager() {
        return MANAGER_ROLE.equals(role);
    }

    public boolean canManageStore(Long storeId) {
        return isAdmin() || (isManager() && this.storeId.equals(storeId));
    }
}
