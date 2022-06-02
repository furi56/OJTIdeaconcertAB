package storybird.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum NationCode {
    KR("KR"),
    BR("BR"),
    EN("EN"),;

    private String code;

    private NationCode(String code) {
        this.code = code;
    }
    public static NationCode find(String code) {
        return Arrays.stream(NationCode.values())
                .filter(nationCode -> nationCode.equals(code))
                .findAny()
                .orElse(KR);
    }
}
