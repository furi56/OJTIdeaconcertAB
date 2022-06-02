package storybird.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum FileType {
    IMAGE("image", "WT", "IMG"),
    VIDEO("video", "WM", "MOV"),
    EPUB("epub", "WB", "EPB"),
    AUDIO("audio","AB","AUD"),
    CALL_EPISODE("episode","E","EPISODE"),
    CALL_ARTWORK("image","A","ARTWORK"),
    NULL(null,null, null)
    ;

    private String type;
    private String kind;
    private String viewKind;

    private FileType(String type, String kind, String viewKind){
        this.type = type;
        this.kind = kind;
        this.viewKind = viewKind;
    }

    public static FileType of(String kindValue){
        return Arrays.stream(FileType.values())
                .filter(type -> type.getViewKind().equals(kindValue))
                .findAny()
                .orElse(NULL);
    }
}
