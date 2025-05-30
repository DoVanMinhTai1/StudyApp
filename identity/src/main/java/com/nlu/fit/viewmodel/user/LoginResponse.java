package com.nlu.fit.viewmodel.user;

import lombok.Builder;

@Builder
public record LoginResponse(String token) {
}
