package com.esame.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 * Paged Response Wrapper
 * Wrapper per Risposta Paginata
 * 
 * Standardized response structure for paginated API endpoints
 * Struttura di risposta standardizzata per endpoint API paginati
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagedResponse<T> extends ApiResponse<List<T>> {
    
    private PagingInfo paging;
    
    // Constructors / Costruttori
    public PagedResponse() {
        super();
    }
    
    public PagedResponse(boolean success, String message, List<T> data, PagingInfo paging) {
        super(success, message, data);
        this.paging = paging;
    }
    
    // Static factory methods / Metodi factory statici
    public static <T> PagedResponse<T> success(List<T> data, PagingInfo paging) {
        return new PagedResponse<>(true, "Data retrieved successfully / Dati recuperati con successo", data, paging);
    }
    
    public static <T> PagedResponse<T> success(String message, List<T> data, PagingInfo paging) {
        return new PagedResponse<>(true, message, data, paging);
    }
    
    // Getters and Setters / Getter e Setter
    public PagingInfo getPaging() {
        return paging;
    }
    
    public void setPaging(PagingInfo paging) {
        this.paging = paging;
    }
    
    /**
     * Paging Information
     * Informazioni di Paginazione
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PagingInfo {
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean first;
        private boolean last;
        private boolean hasNext;
        private boolean hasPrevious;
        
        // Constructors / Costruttori
        public PagingInfo() {}
        
        public PagingInfo(int page, int size, long totalElements) {
            this.page = page;
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = (int) Math.ceil((double) totalElements / size);
            this.first = page == 0;
            this.last = page >= totalPages - 1;
            this.hasNext = !last;
            this.hasPrevious = !first;
        }
        
        // Getters and Setters / Getter e Setter
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
        
        public boolean isFirst() {
            return first;
        }
        
        public void setFirst(boolean first) {
            this.first = first;
        }
        
        public boolean isLast() {
            return last;
        }
        
        public void setLast(boolean last) {
            this.last = last;
        }
        
        public boolean isHasNext() {
            return hasNext;
        }
        
        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }
        
        public boolean isHasPrevious() {
            return hasPrevious;
        }
        
        public void setHasPrevious(boolean hasPrevious) {
            this.hasPrevious = hasPrevious;
        }
    }
}
