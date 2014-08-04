require.config({
    baseUrl: EnvJasmine.rootDir,
    paths: {
        mGen: 'src/main/javascript/mgen-lib',
        standard_blueprint: 'src_generated/test/javascript/standard_blueprint',
        default_blueprint: 'src_generated/test/javascript/default_blueprint',
        default_required_blueprint: 'src_generated/test/javascript/default_required_blueprint',
        transient_blueprint: 'src_generated/test/javascript/transient_blueprint',
        mocks: EnvJasmine.mocksDir,
        specs: EnvJasmine.specsDir
    },
    shim: {
        "specs/standard.spec" : { deps: ["mGen", "standard_blueprint"]},
        "specs/default.spec": { deps: ["mGen", "default_blueprint"]}
    }
});