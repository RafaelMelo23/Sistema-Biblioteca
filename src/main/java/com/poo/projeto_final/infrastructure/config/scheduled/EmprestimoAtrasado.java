package com.poo.projeto_final.infrastructure.config.scheduled;

import com.poo.projeto_final.domain.service.EmprestimoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmprestimoAtrasado {

    private final EmprestimoService emprestimoService;

    public EmprestimoAtrasado(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void executarMeiaNoite() {

        emprestimoService.aplicarStatusAtrasadoAoEmprestimo();
    }
}
