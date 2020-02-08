package com.cqrs.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;

import java.util.Optional;

@Slf4j
public class MyCommandHandlerInterceptor implements MessageHandlerInterceptor<CommandMessage<?>> {

    @Override
    public Object handle(UnitOfWork<? extends CommandMessage<?>> unitOfWork, InterceptorChain interceptorChain) throws Exception {
        CommandMessage<?> command = unitOfWork.getMessage();
        log.info("MyCommandHandlerInterceptor => {}", command);
//        String userId = Optional.ofNullable(command.getMetaData().get("userId"))
//                .map(uId -> (String) uId)
//                .orElseThrow(IllegalArgumentException::new);
//        if ("axonUser".equals(userId)) {
//            return interceptorChain.proceed();
//        }
//        return null;
        return interceptorChain.proceed();
    }
}
