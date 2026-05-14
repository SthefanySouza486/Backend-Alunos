package com.agrupa.tat_3ds.controller;


import com.agrupa.tat_3ds.dto.RelatorioTrabalhoDTO;
import com.agrupa.tat_3ds.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/finalizacao")
@CrossOrigin(origins = "http://172.100.120.103:4200")
public class RelatorioController {
    @Autowired
    private RelatorioService relatorioService;
    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> gerarRelatorioPdf(@PathVariable("id") Integer id) throws Exception {
        RelatorioTrabalhoDTO dados = relatorioService.buscarDadosConsolidados(id);

        Context context = new Context();
        context.setVariable("dados", dados);

        String htmlContent = templateEngine.process("relatorio", context);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);

        byte[] pdfBytes = outputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "relatorio-agrupa.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}