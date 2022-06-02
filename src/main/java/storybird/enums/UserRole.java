package storybird.enums;

import java.util.Arrays;

public enum UserRole {
	ROLE_ADMIN10(10, "ADMIN10"),
	ROLE_ADMIN9(9, "ADMIN9"),
	ROLE_ADMIN8(8, "ADMIN8"),
	ROLE_ADMIN7(7, "ADMIN7"),
	ROLE_ADMIN6(6, "ADMIN6"),
	ROLE_ADMIN5(5, "ADMIN5"),
	ROLE_ADMIN4(4, "ADMIN4"),
	ROLE_ADMIN3(3, "ADMIN3"),
	ROLE_ADMIN2(2, "ADMIN2"),
	ROLE_ADMIN1(1, "ADMIN1"),
	ROLE_AUTHOR1(1, "AUTHOR1"),
	ROLE_AUTHOR2(2, "AUTHOR2"),
	ROLE_AUTHOR3(3, "AUTHOR3"),
	ROLE_PARTNER1(1, "PARTNER1"),
	ROLE_PARTNER2(2, "PARTNER2"),
	ROLE_PARTNER3(3, "PARTNER3"),
	ROLE_EMPLOYEE(2, "EMPLOYEE"),
	ROLE_BASIC(1, "BASIC");
	
	private int level;
	private String role;
	
	private UserRole(int level,String role) {
		this.level = level;
		this.role = role;
	}
	public boolean equals(int level) {
		return this.level == level;
	}
	public int getLevel() {
		return level;
	}
	public String getRole() {
		return role;
	}
	public static UserRole find(int level) {
		return Arrays.stream(UserRole.values())
			.filter(user -> user.equals(level) && !user.getRole().contains("ADMIN")
					&& !user.getRole().contains("AUTHOR") && !user.getRole().contains("PARTNER"))
			.findAny()
			.orElse(ROLE_BASIC);
	}

	public static UserRole findAuthor(int level) {
		return Arrays.stream(UserRole.values())
				.filter(user -> user.equals(level) && user.getRole().contains("AUTHOR"))
				.findAny()
				.orElse(ROLE_AUTHOR1);
	}

	public static UserRole findPartner(int level) {
		return Arrays.stream(UserRole.values())
				.filter(user -> user.equals(level) && user.getRole().contains("PARTNER"))
				.findAny()
				.orElse(ROLE_PARTNER1);
	}

	public static UserRole findAdmin(int level) {
		return Arrays.stream(UserRole.values())
			.filter(user -> user.equals(level))
			.findAny()
			.orElse(ROLE_ADMIN1);
	}

	public static String getTableNameByRole(String role) {
		role = role.replaceFirst("ROLE_","");
		switch(role){
			case "PARTNER":
				return "partner_company";
			case "EMPLOYEE":
				return "partner_employee";
			case "AUTHOR":
				return "author";
			default:
				return "";
		}
	}
}
