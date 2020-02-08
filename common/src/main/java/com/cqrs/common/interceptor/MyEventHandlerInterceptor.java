package com.cqrs.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;

import java.util.Optional;

@Slf4j
public class MyEventHandlerInterceptor implements MessageHandlerInterceptor<EventMessage<?>> {

    @Override
    public Object handle(UnitOfWork<? extends EventMessage<?>> unitOfWork,
                         InterceptorChain interceptorChain) throws Exception {
        EventMessage<?> event = unitOfWork.getMessage();
//        String userId = Optional.ofNullable(event.getMetaData().get("userId"))
//                .map(uId -> (String) uId)
//                .orElseThrow(Ill::new);
//        if ("axonUser".equals(userId)) {
//            return interceptorChain.proceed();
//        }
        log.info("MyEventHandlerInterceptor event => {}", event);
        return interceptorChain.proceed();
//        return null;
    }
}
