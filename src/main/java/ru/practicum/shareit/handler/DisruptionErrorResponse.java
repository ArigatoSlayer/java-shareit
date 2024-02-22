package ru.practicum.shareit.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class DisruptionErrorResponse {
    private final List<Disruption> disruptions;
}