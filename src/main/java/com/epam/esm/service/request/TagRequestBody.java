package com.epam.esm.service.request;

public class TagRequestBody {

    private int limit;
    private int offset;

    private static final int DEFAULT_LIMIT = 10;
    private static final int DEFAULT_OFFSET = 0;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public static TagRequestBody getDefault() {
        TagRequestBody tagRequestBody = new TagRequestBody();
        tagRequestBody.setLimit(DEFAULT_LIMIT);
        tagRequestBody.setOffset(DEFAULT_OFFSET);

        return tagRequestBody;
    }
}
