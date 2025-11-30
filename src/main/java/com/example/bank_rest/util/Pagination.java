package com.example.bank_rest.util;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Pagination {

    public <T> List<T> getPagination(List<T> a, int page, int size){
        int fromIndex = (page - 1) * size;
        if (fromIndex >= a.size()) {
            return List.of();
        }

        int toIndex = Math.min(fromIndex + size, a.size());
        return a.subList(fromIndex, toIndex);
    }


}
