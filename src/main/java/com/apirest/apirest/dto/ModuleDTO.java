package com.apirest.apirest.dto;

import com.apirest.apirest.Entities.Language;
import com.apirest.apirest.Entities.Module;
import com.apirest.apirest.dto.LanguajeDTO;

public class ModuleDTO {
    private Long id;
    private String title;
    private String description;
    private Integer order;
    private LanguajeDTO language;

    public ModuleDTO(Long id, String title, String description, Integer order, LanguajeDTO language) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.order = order;
        this.language = language;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public LanguajeDTO getLanguage() {
        return language;
    }

    public void setLanguage(LanguajeDTO language) {
        this.language = language;
    }
}
