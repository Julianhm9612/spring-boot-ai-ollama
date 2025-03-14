package com.example.spring_boot_ai_ollama.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import com.example.spring_boot_ai_ollama.dto.PoetryDto;

@Service
public class PoetryService {
    public static final String WRITE_ME_HAIKU_ABOUT_CAT = """
        Write me Haiku about cat,
        haiku should start with the word cat obligatory""";

    private final ChatModel aiClient;

    public PoetryService(ChatModel aiClient) {
        this.aiClient = aiClient;
    }

    public String getCatHaiku() {
        return aiClient.call(WRITE_ME_HAIKU_ABOUT_CAT);
    }

    public PoetryDto getPoetryByGenreAndTheme(String genre, String theme) {
        BeanOutputConverter<PoetryDto> outputConverter = new BeanOutputConverter<>(PoetryDto.class);

        String promptString = """
            Write me {genre} poetry about {theme}
            {format}
            """;

        PromptTemplate promptTemplate = new PromptTemplate(promptString);
        promptTemplate.add("genre", genre);
        promptTemplate.add("theme", theme);
        promptTemplate.add("format", outputConverter.getFormat());

        ChatResponse response = aiClient.call(promptTemplate.create());
        return outputConverter.convert(response.getResult().getOutput().getContent());
    }
}
