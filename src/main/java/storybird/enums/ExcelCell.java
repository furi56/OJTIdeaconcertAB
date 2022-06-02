package storybird.enums;

public enum ExcelCell {
    OPUS_CONT_CODE(1),
    OPUS_TITLE(2),
    OPUS_CATEGORY(3),
    OPUS_GENRE(4),
    OPUS_INTRO(5),
    OPUS_COMP_YN(6),
    OPUS_WEEK_DAY(7),
    OPUS_AGE(8),
    OPUS_CYCLE(9),
    OPUS_START_DATE(10),
    OPUS_END_DATE(11),
    OPUS_AUTHOR_CODE(12),
    OPUS_COMPANY_CODE(13),
    OPUS_TAX_FREE_YN(14),
    OPUS_TAX_TAG(15),
    OPUS_TAX_CODE(16),
    OPUS_RENT_PRICE(17),
    OPUS_PRODUCT_PRICE(18),
    OPUS_TAG(19),
    OPUS_NATION(20),
    OPUS_LANG(21),
    OPUS_OPEN_YN(22),

    EPI_OPUS_CODE(0),
    EPI_NUM(1),
    EPI_TITLE(2),
    EPI_VIEW_KIND(3),
    EPI_TAX_FREE_YN(4),
    EPI_TAX_TAG(5),
    EPI_TAX_CODE(6),
    EPI_RENT_PRICE(7),
    EPI_PRODUCT_PRICE(8)
    ;



    private int index;

    ExcelCell(int index){
        this.index = index;
    }
    public int get(){
        return index;
    }

    @Override
    public String toString() {
        return Integer.toString(index);
    }
}
