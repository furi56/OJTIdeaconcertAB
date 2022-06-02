package storybird.enums;

import lombok.Getter;

@Getter
public enum GlobalEnum {
    RENT_DAY(90, "java.lang.Integer"),
    IAMPORT_WEBHOOK_URL("183.111.234.51:9002/iamport-webhook", "java.lang.String"),
    REFUNDABLE_AMOUNT(10000, "java.lang.Integer")
    ;

    private Object value;
    private String type;

    private GlobalEnum(Object value, String type){
        this.value = value;
        this.type = type;
    }

    public static <T> T get(GlobalEnum e) {
        try{
            Class<?> theClass = Class.forName(e.type);
            return (T) theClass.cast(e.value);
        }catch(ClassNotFoundException ex){
            return null;
        }
    }
}
