package com.chatbot.util.rag;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class DocxTextExtractor implements TextExtractor {

    @Override
    public boolean supports(String contentType) {

        return "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                .equals(contentType);
    }

    @Override
    public String extract(MultipartFile file) throws IOException {

        try (XWPFDocument document =
                     new XWPFDocument(file.getInputStream())) {

            return new XWPFWordExtractor(document).getText();
        }
    }
}