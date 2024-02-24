package com.polak.githubrepofinder.dtos;

import lombok.Builder;

@Builder
public record ErrorResponse(String status, String message) {
}