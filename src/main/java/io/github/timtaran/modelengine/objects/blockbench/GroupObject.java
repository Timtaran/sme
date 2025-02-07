package io.github.timtaran.modelengine.objects.blockbench;

import lombok.Getter;

/**
 * Custom class for storing outliner objects as groups, contains group_name and origin (both for
 * spawning models correctly). Results not necessary storing full bbmodel in ram.
 */
public record GroupObject(String name, double[] origin) {}
