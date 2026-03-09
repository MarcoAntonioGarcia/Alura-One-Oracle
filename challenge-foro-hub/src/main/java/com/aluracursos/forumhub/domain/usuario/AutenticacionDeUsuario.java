package com.aluracursos.forumhub.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record AutenticacionDeUsuario(
        @NotBlank String login,
        @NotBlank String clave) {
}
