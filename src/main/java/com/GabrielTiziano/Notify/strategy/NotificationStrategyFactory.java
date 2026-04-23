package com.GabrielTiziano.Notify.strategy;

import com.GabrielTiziano.Notify.exception.BusinessRuleException;
import com.GabrielTiziano.Notify.model.enums.NotificationChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationStrategyFactory {
    private final List<NotificationStrategy> strategies;

    public NotificationStrategy getStrategy(NotificationChannel notificationChannel){
        return strategies.stream()
                .filter(strategy -> strategy.suports(notificationChannel))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleException("Nenhum provedor configurado para o canal " + notificationChannel + "."));
    }
}
