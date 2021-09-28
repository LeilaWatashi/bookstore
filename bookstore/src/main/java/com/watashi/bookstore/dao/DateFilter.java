package com.watashi.bookstore.dao;

import com.watashi.bookstore.entity.Entity;

import java.util.List;

public interface DateFilter<T extends Entity> {
    List<T> findAllByOrderByDateDesc();
}
