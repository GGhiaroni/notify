package com.GabrielTiziano.Notify.client;

import com.GabrielTiziano.Notify.dto.MailtrapRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="mailtrap-client", url="${api.external.mailtrap.url}")
public interface MailtrapClient {
    @PostMapping("/api/send/{inbox_id}")
    public void sendEmail(
            @RequestHeader("Authorization") String token,
            @PathVariable("inbox_id") String inboxId,
            @RequestBody MailtrapRequestDTO request
            );
}
