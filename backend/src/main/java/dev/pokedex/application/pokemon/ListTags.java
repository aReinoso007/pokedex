package dev.pokedex.application.pokemon;

import dev.pokedex.domain.port.TagRepository;
import java.util.List;

/** Tags used before, offered for reuse when customising a Pokemon. */
public class ListTags {

    private final TagRepository tags;

    public ListTags(TagRepository tags) {
        this.tags = tags;
    }

    public List<String> execute() {
        return tags.findAll();
    }
}
