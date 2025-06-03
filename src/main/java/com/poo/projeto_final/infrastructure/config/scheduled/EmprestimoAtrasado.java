package com.poo.projeto_final.infrastructure.config.scheduled;

import com.poo.projeto_final.application.dto.DTOEmprestimoAtrasado;
import com.poo.projeto_final.domain.enums.StatusEmprestimo;
import com.poo.projeto_final.domain.enums.StatusExemplar;
import com.poo.projeto_final.domain.repository.DAOEmprestimo;
import com.poo.projeto_final.domain.repository.DAOExemplarLivro;
import com.poo.projeto_final.domain.service.EmprestimoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class EmprestimoAtrasado {

    private final EmprestimoService emprestimoService;

    public EmprestimoAtrasado(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void executarMeiaNoite() {

        emprestimoService.executarEmprestimoAtrasado();
    }
}
