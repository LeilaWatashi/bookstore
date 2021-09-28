package com.watashi.bookstore.exception.helper;

import com.watashi.bookstore.entity.Entity;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

import java.util.Optional;

public class ProxyHelper {

    @SuppressWarnings({"unchecked"})
    private static <T extends Entity> T getTargetObject(final Object proxyObject)  {
        if (AopUtils.isCglibProxy(proxyObject)) {
            try {
                return getTargetObject(((Advised) proxyObject).getTargetSource().getTarget());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return (T) proxyObject;
    }

    public static <T extends Entity> Optional<T> recoveryEntityFromProxy(final Object proxyObject) {
        T entity = getTargetObject(proxyObject);

        return Optional.ofNullable(entity);
    }
}
