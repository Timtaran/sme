package io.github.timtaran.modelengine.models.blockbench;

/**
 * Custom class for storing outliner objects as groups, contains group_name and origin (both for
 * spawning models correctly). Results not necessary storing full bbmodel in ram.
 */
public record ModelGroup(String name, double[] origin) {}
