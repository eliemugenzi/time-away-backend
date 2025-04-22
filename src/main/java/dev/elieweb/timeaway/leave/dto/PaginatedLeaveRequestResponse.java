package dev.elieweb.timeaway.leave.dto;

import java.util.List;

public class PaginatedLeaveRequestResponse {
    private List<LeaveRequestResponseDTO> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

    // Default constructor
    public PaginatedLeaveRequestResponse() {
    }

    // All-args constructor
    public PaginatedLeaveRequestResponse(List<LeaveRequestResponseDTO> content, int pageNo,
                                       int pageSize, long totalElements, int totalPages,
                                       boolean last) {
        this.content = content;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    // Getters and setters
    public List<LeaveRequestResponseDTO> getContent() {
        return content;
    }

    public void setContent(List<LeaveRequestResponseDTO> content) {
        this.content = content;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    // Builder pattern
    public static PaginatedLeaveRequestResponseBuilder builder() {
        return new PaginatedLeaveRequestResponseBuilder();
    }

    public static class PaginatedLeaveRequestResponseBuilder {
        private List<LeaveRequestResponseDTO> content;
        private int pageNo;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean last;

        PaginatedLeaveRequestResponseBuilder() {
        }

        public PaginatedLeaveRequestResponseBuilder content(List<LeaveRequestResponseDTO> content) {
            this.content = content;
            return this;
        }

        public PaginatedLeaveRequestResponseBuilder pageNo(int pageNo) {
            this.pageNo = pageNo;
            return this;
        }

        public PaginatedLeaveRequestResponseBuilder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public PaginatedLeaveRequestResponseBuilder totalElements(long totalElements) {
            this.totalElements = totalElements;
            return this;
        }

        public PaginatedLeaveRequestResponseBuilder totalPages(int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public PaginatedLeaveRequestResponseBuilder last(boolean last) {
            this.last = last;
            return this;
        }

        public PaginatedLeaveRequestResponse build() {
            return new PaginatedLeaveRequestResponse(content, pageNo, pageSize,
                                                   totalElements, totalPages, last);
        }
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaginatedLeaveRequestResponse that = (PaginatedLeaveRequestResponse) o;

        if (pageNo != that.pageNo) return false;
        if (pageSize != that.pageSize) return false;
        if (totalElements != that.totalElements) return false;
        if (totalPages != that.totalPages) return false;
        if (last != that.last) return false;
        return content != null ? content.equals(that.content) : that.content == null;
    }

    @Override
    public int hashCode() {
        int result = content != null ? content.hashCode() : 0;
        result = 31 * result + pageNo;
        result = 31 * result + pageSize;
        result = 31 * result + (int) (totalElements ^ (totalElements >>> 32));
        result = 31 * result + totalPages;
        result = 31 * result + (last ? 1 : 0);
        return result;
    }

    // toString
    @Override
    public String toString() {
        return "PaginatedLeaveRequestResponse{" +
                "content=" + content +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                ", last=" + last +
                '}';
    }
} 