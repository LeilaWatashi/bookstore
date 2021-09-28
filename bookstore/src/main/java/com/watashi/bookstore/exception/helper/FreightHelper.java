package com.watashi.bookstore.exception.helper;

import com.watashi.bookstore.entity.shop.Freight;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FreightHelper {

    private ObjectProvider<Freight> freightProvider;

    @Autowired
    public FreightHelper(ObjectProvider<Freight> freightProvider) {
        this.freightProvider = freightProvider;
    }

    public Freight build() {
        return freightProvider.getObject();
    }
}
