def prepareConfig() {
    def yamlConfig = readYaml test: $YAML_CONFIG
    yamlConfig.each(k, v -> System.setProperty(v))
}

this
