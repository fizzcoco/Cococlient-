package com.example.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public void register(Module module) { modules.add(module); }

    public List<Module> getModules() { return modules; }

    public Optional<Module> get(String name) {
        return modules.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public void tick() {
        for (Module m : modules) {
            if (m.isEnabled()) m.onTick();
        }
    }
}
