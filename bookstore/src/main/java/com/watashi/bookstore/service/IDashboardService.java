package com.watashi.bookstore.service;

import com.watashi.bookstore.entity.Entity;
import org.springframework.stereotype.Component;

@Component
public interface IDashboardService<T extends Entity> extends Service<T> {

}
