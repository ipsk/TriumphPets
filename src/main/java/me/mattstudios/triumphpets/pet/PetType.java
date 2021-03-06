package me.mattstudios.triumphpets.pet;

public enum PetType {

    PET_FOX_SNOW("Pet Fox", "eyJ0aW1lc3RhbXAiOjE1NjU0Nzc0NjQ2MzgsInByb2ZpbGVJZCI6IjdkYTJhYjNhOTNjYTQ4ZWU4MzA0OGFmYzNiODBlNjhlIiwicHJvZmlsZU5hbWUiOiJHb2xkYXBmZWwiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzYyNjlmMTBjNTBjZTdiZTBiZWYyYTI4N2MzNmM3NDA0MDEyZjI0OTg0MTU2NzU2M2M4NGRjNTcxYjEwNjU2ZDkifX19"),
    PET_FOX_RED("Pet Fox", "eyJ0aW1lc3RhbXAiOjE1NjU0NzgwODE4MDUsInByb2ZpbGVJZCI6IjJjMTA2NGZjZDkxNzQyODI4NGUzYmY3ZmFhN2UzZTFhIiwicHJvZmlsZU5hbWUiOiJOYWVtZSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTIwNzI1YzE1YTdiMDEwOWQ0MjVhYjA5MTg2OTYxNmFhYTY1MWVlNDMzODM4NjM5MDZiNDQxMGQxOGVhODZkZSJ9fX0=");

    private String defaultName;
    private String texture;

    PetType(String defaultName, String texture) {
        this.defaultName = defaultName;
        this.texture = texture;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public String getTexture() {
        return texture;
    }
}
