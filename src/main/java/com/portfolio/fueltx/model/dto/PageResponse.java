package com.portfolio.fueltx.model.dto;

import java.util.List;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.model.dto
 * @since 2026-09-10T12:15:00
 */
public class PageResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public PageResponse() {
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public static final class Builder<T> {
        private final PageResponse<T> target = new PageResponse<>();

        public Builder<T> content(List<T> content) {
            target.content = content;
            return this;
        }

        public Builder<T> page(int page) {
            target.page = page;
            return this;
        }

        public Builder<T> size(int size) {
            target.size = size;
            return this;
        }

        public Builder<T> totalElements(long totalElements) {
            target.totalElements = totalElements;
            return this;
        }

        public Builder<T> totalPages(int totalPages) {
            target.totalPages = totalPages;
            return this;
        }

        public PageResponse<T> build() {
            return target;
        }
    }
}
